package fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.forloop.canopy.R;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import controller.EventItemAdapter;
import data.Api;
import data.DataManager;
import data.Event;
import io.paperdb.Paper;
import utils.Util;


/**
 * Created by Jibola on 3/27/2015.
 */
public class EventsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeLayout = null;
    StaggeredGridLayoutManager layoutManager = null;

    EventItemAdapter eventItemAdapter;
    List<Event> eventList = null;
    String categoryId = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
        swipeLayout.setOnRefreshListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(layoutManager);

        int spacingInPixels = (int) Util.convertDpToPixel(16, getContext());
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels, spacingInPixels, spacingInPixels, spacingInPixels));
        recyclerView.setHasFixedSize(true);

        eventItemAdapter = new EventItemAdapter(getContext(), new ArrayList<Event>(2));
        recyclerView.setAdapter(eventItemAdapter);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        //refresh events
        new EventsTask().execute(categoryId);

        return view;
    }

    private class EventsTask extends AsyncTask<String, String, String> {

        private EventsTask() {
        }

        @Override
        protected void onPreExecute() {
            swipeLayout.setRefreshing(true);
        }

        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            InputStream in = null;
            HttpURLConnection urlConnection = null;
            String output = "";

            try {
                url = new URL(Api.GET_EVENTS);

                if (categoryId != null && categoryId.length() > 0) {
                    url = new URL(Api.GET_EVENTS + "?category=" + categoryId);
                }

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setDoInput(true);

                in = new BufferedInputStream(urlConnection.getInputStream());
                output = Util.streamToString(in);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }

            return output;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                Event[] events = new Gson().fromJson(result, Event[].class);
                eventList = Arrays.asList(events);

                //Save event categories
                Paper.book().write(DataManager.EVENT_DATA, eventList);
                loadEvents();
            } catch (Exception e) {
                e.printStackTrace();
            }

            swipeLayout.setRefreshing(false);
        }
    }

    public void loadEvents() {
        try {
            if (eventList.size() > 0) {
                int oldSize = eventItemAdapter.getItemList().size();
                eventItemAdapter.getItemList().clear();
                eventItemAdapter.getItemList().addAll(eventList);
                eventItemAdapter.notifyDataSetChanged();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onRefresh() {
        new EventsTask().execute(categoryId);
    }

    public static EventsFragment newInstance(String categoryID) {
        EventsFragment tempFrag = new EventsFragment();
        tempFrag.categoryId = categoryID;
        return tempFrag;
    }
}
