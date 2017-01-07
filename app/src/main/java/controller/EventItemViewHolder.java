package controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.forloop.canopy.CreateEventActivity;
import com.forloop.canopy.EventActivity;
import com.forloop.canopy.R;

import java.lang.reflect.Field;
import java.net.URL;

import data.Event;

/**
 * Created by Jibola on 7/18/2016.
 */
public class EventItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView txtTitle;
    private TextView txtSummary;
    private TextView txtEventCategory;
    private ImageView imgEvent;

    Event event;

    /**
     * Default constructor.
     *
     * @param itemView The {@link View} being hosted in this ViewHolder
     */
    public EventItemViewHolder(View itemView) {
        super(itemView);

        try {
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtSummary = (TextView) itemView.findViewById(R.id.txtSummary);
            txtEventCategory = (TextView) itemView.findViewById(R.id.txtEventCategory);
            imgEvent = (ImageView) itemView.findViewById(R.id.imgEvent);

            itemView.setOnClickListener(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void bind(final Event event) {
        try {
            this.event = event;

            txtTitle.setText(event.name);
            txtEventCategory.setText(event.category.toUpperCase());
            txtSummary.setText(event.description + " // " + event.time + " // " + event.city);

            if (event.photos != null && event.photos.size() > 0) {
                final Runnable runnable = new Runnable() {
                    public void run() {
                        URL url = null;
                        try {
                            url = new URL(event.photos.get(0).value);
                            final Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                            imgEvent.post(new Runnable() {
                                @Override
                                public void run() {
                                    imgEvent.setImageBitmap(bmp);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                new Thread(runnable).start();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(txtTitle.getContext(), EventActivity.class);
        intent.putExtra(EventActivity.EVENT_ID_EXTRA, event.id);
        intent.putExtra(EventActivity.EVENT_CATEGORY_ID_EXTRA, event.category);
        txtTitle.getContext().startActivity(intent);
    }
}
