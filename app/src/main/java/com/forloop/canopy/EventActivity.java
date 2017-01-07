package com.forloop.canopy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.net.URL;
import java.util.List;

import data.DataManager;
import data.Event;
import io.paperdb.Paper;

public class EventActivity extends AppCompatActivity implements OnMapReadyCallback {

    TextView txtEventCategory;
    TextView txtEventTitle;
    ImageView imgShare;
    ImageView imgMain;
    TextView txtEventSummary;
    TextView txtStartTime;
    TextView txtEndTime;
    private GoogleMap mMap;

    public final static String EVENT_ID_EXTRA = "event_id_extra";
    public final static String EVENT_CATEGORY_ID_EXTRA = "event_category_id_extra";

    String eventId = null;
    String eventCategory = null;
    Event event = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                finish();
            }
        });

        txtEventCategory = (TextView) findViewById(R.id.txtEventCategory);
        txtEventTitle = (TextView) findViewById(R.id.txtEventTitle);
        imgShare = (ImageView) findViewById(R.id.imgShare);
        imgMain = (ImageView) findViewById(R.id.imgMain);
        txtEventSummary = (TextView) findViewById(R.id.txtEventSummary);
        txtStartTime = (TextView) findViewById(R.id.txtStartTime);
        txtEndTime = (TextView) findViewById(R.id.txtEndTime);

        try {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                eventId = extras.getString(EVENT_ID_EXTRA);
                eventCategory = extras.getString(EVENT_CATEGORY_ID_EXTRA);

                Event tempEvent = new Event();
                tempEvent.id = eventId;

                List<Event> eventList = Paper.book().read(DataManager.EVENT_DATA);
                int eventIndex = eventList.indexOf(tempEvent);
                event = eventList.get(eventIndex);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (event != null) {
            txtEventCategory.setText(event.category);
            txtEventTitle.setText(event.name);
            txtEventSummary.setText(event.description);
            txtStartTime.setText(event.startDate);
            txtEndTime.setText(event.time);

            if (event.photos != null && event.photos.size() > 0) {
                final Runnable runnable = new Runnable() {
                    public void run() {
                        URL url = null;
                        try {
                            url = new URL(event.photos.get(0).value);
                            final Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                            imgMain.post(new Runnable() {
                                @Override
                                public void run() {
                                    imgMain.setImageBitmap(bmp);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                new Thread(runnable).start();
            }
        }

        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, event.name + " " + event.description);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng eventLocation = new LatLng(6.6740, 3.1976);
        mMap.addMarker(new MarkerOptions().position(eventLocation).title("Event Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eventLocation, 15.0f));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
