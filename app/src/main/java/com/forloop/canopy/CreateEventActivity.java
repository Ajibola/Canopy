package com.forloop.canopy;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import data.DataManager;
import data.EventCategory;

public class CreateEventActivity extends AppCompatActivity {

    LinearLayout createEventForm1;
    LinearLayout createEventForm2;
    LinearLayout createEventForm3;
    LinearLayout createEventForm4;
    FloatingActionButton fab;

    TextView txtEventName;
    Spinner spnEventCategory;

    TextView txtLocation;
    TextView txtAddress;

    Spinner spnEventDuration;
    TextView txtStartDate;
    TextView txtStartTime;

    TextView txtDescription;
    ImageView imgEventMain;

    int currForm = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        createEventForm1 = (LinearLayout) findViewById(R.id.createEventForm1);
        createEventForm2 = (LinearLayout) findViewById(R.id.createEventForm2);
        createEventForm3 = (LinearLayout) findViewById(R.id.createEventForm3);
        createEventForm4 = (LinearLayout) findViewById(R.id.createEventForm4);

        txtEventName = (TextView) findViewById(R.id.txtEventName);
        txtLocation = (TextView) findViewById(R.id.txtLocation);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        txtStartDate = (TextView) findViewById(R.id.txtStartDate);
        txtStartTime = (TextView) findViewById(R.id.txtStartTime);
        txtDescription = (TextView) findViewById(R.id.txtDescription);

        spnEventCategory = (Spinner) findViewById(R.id.spnEventCategory);
        spnEventDuration = (Spinner) findViewById(R.id.spnEventDuration);

        ArrayAdapter<CharSequence> durationAdapter = ArrayAdapter.createFromResource(this,
                R.array.duration_type_array, android.R.layout.simple_spinner_item);
        durationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnEventDuration.setAdapter(durationAdapter);

        List<EventCategory> categoryList = DataManager.getEventCategoryList();
        ArrayAdapter<EventCategory> categoryAdapter = new ArrayAdapter<EventCategory>(this, android.R.layout.simple_spinner_dropdown_item, (EventCategory[]) categoryList.toArray());
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnEventCategory.setAdapter(categoryAdapter);

        imgEventMain = (ImageView) findViewById(R.id.imgEventMain);
        imgEventMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currForm) {
                    case 1:
                        currForm = currForm + 1;
                        createEventForm2.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        currForm = currForm + 1;
                        createEventForm3.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        currForm = currForm + 1;
                        createEventForm4.setVisibility(View.VISIBLE);
                        Drawable drawable = getResources().getDrawable(R.drawable.tick);
                        fab.setImageDrawable(drawable);
                        break;
                    case 4:
                        createEvent();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    Uri imageURI;

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            default:
                if (resultCode == RESULT_OK) {
                    imageURI = imageReturnedIntent.getData();

                    imgEventMain.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                InputStream inputStream = getContentResolver().openInputStream(imageURI);
                                Drawable drawable = Drawable.createFromStream(inputStream, imageURI.toString());
                                imgEventMain.setImageDrawable(drawable);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                break;
        }
    }

    public void createEvent() {

        finish();
    }
}
