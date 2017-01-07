package data;

import java.util.List;

/**
 * Created by Jibola on 1/5/2017.
 */

public class Event implements Comparable<Event> {

    public String id;
    public String name;
    public String description;
    public String category;
    public String duration;
    public String startDate;
    public String time;
    public String city;
    public String address;
    public String country;
    public List<Photo> photos;

    public class Photo {
        public String name;
        public String value;
    }

    @Override
    public boolean equals(Object o) {
        if ((!(o instanceof Event)) || this.id == null || o == null) return false;

        return (this.id.equals(((Event) o).id));
    }

    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public int compareTo(Event contentItem) {
        return startDate.compareTo(contentItem.startDate);
    }
}
