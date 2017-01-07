package com.forloop.canopy;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import data.Api;
import data.DataManager;
import data.EventCategory;
import fragments.EventsFragment;
import io.paperdb.Paper;
import utils.Util;

public class MainContentActivity extends AppCompatActivity {


    ViewPager mViewPager;
    ViewPagerAdapter viewPagerAdapter;
    TabLayout tabLayout;
    List<EventCategory> eventCategoryList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), CreateEventActivity.class);
                startActivity(intent);
            }
        });

        mViewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(mViewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        eventCategoryList = DataManager.getEventCategoryList();

        if (eventCategoryList == null || eventCategoryList.size() < 1)
            new EventCategoryTask().execute();
    }

    private void setupViewPager(ViewPager viewPager) {
        try {
            viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            viewPagerAdapter.addFragment(new EventsFragment().newInstance(""), "ALL");
            viewPager.setAdapter(viewPagerAdapter);

            List<EventCategory> eventCategoryList = DataManager.getEventCategoryList();
            if (eventCategoryList == null)
                return;

            for (EventCategory eventCategory : eventCategoryList) {
                viewPagerAdapter.addFragment(new EventsFragment().newInstance(eventCategory.name), eventCategory.name.toUpperCase());
            }
            viewPagerAdapter.notifyDataSetChanged();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private class EventCategoryTask extends AsyncTask<String, String, String> {

        private EventCategoryTask() {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            InputStream in = null;
            HttpURLConnection urlConnection = null;
            String output = "";

            try {
                url = new URL(Api.GET_ALL_EVENT_CATEGORIES);
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
                EventCategory[] eventCategories = new Gson().fromJson(result, EventCategory[].class);
                List<EventCategory> eventCategoryList = Arrays.asList(eventCategories);

                for (EventCategory eventCategory : eventCategories) {
                    if (viewPagerAdapter != null)
                        viewPagerAdapter.addFragment(new EventsFragment(), eventCategory.name.toUpperCase());
                }
                viewPagerAdapter.notifyDataSetChanged();

                //Save event categories
                Paper.book().write(DataManager.EVENT_CATEGORY, eventCategoryList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
