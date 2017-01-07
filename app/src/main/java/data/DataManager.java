package data;

import java.util.List;

import io.paperdb.Paper;

/**
 * Created by Jibola on 1/6/2017.
 */

public class DataManager {

    public static final String EVENT_DATA = "events_data";
    public static final String EVENT_CATEGORY = "event_categories";

    private static List<EventCategory> eventCategoryList;


    public static final List<EventCategory> getEventCategoryList() {
        if (eventCategoryList == null) {
            eventCategoryList = Paper.book().read(DataManager.EVENT_CATEGORY);
        }

        return eventCategoryList;
    }
}
