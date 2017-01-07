package controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.forloop.canopy.R;

import java.util.List;

import data.Event;


public class EventItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    List allItems = null;

    public EventItemAdapter(Context c, List allItems) {
        mContext = c;
        this.allItems = allItems;
    }

    @Override
    public int getItemCount() {
        if (allItems != null)
            return allItems.size();
        else
            return 0;
    }

    public Object getItemObject(int position) {
        if (allItems != null) {
            return allItems.get(position);
        }

        return null;
    }

    public List getItemList() {
        return allItems;
    }

    @Override
    public long getItemId(int index) {
        Event event = null;
        if (allItems != null && allItems.size() > index) {
            event = (Event) allItems.get(index);
            return Long.valueOf(event.id);
        } else
            return 0;
    }

    public void clear() {
        if (allItems != null)
            allItems.clear();

        notifyDataSetChanged();
    }

    public void addItem(Event event) {
        if (!allItems.contains(event)) {
            allItems.add(event);
            notifyItemInserted(allItems.size() - 1);
        }
    }

    public void removeItem(int position) {
        if (allItems.size() > position) {
            allItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void deleteViewItem(int position) {
        allItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_view, parent, false);
        viewHolder = new EventItemViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        try {
            StaggeredGridLayoutManager.LayoutParams layoutParamsFull = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParamsFull.setFullSpan(true);

            EventItemViewHolder eventItemViewHolder = (EventItemViewHolder) holder;
            configureContentItemViewHolder(eventItemViewHolder, position);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void configureContentItemViewHolder(final EventItemViewHolder holder, int position) {
        final Event event = (Event) allItems.get(position);

        if (event != null) {
            holder.bind(event);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
