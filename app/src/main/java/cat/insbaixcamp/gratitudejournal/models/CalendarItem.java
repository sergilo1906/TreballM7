package cat.insbaixcamp.gratitudejournal.models;

import java.util.Date;

public class CalendarItem {
    private final String title;
    private final String description;
    private final Date date;

    public CalendarItem(String title, String description, Date date) {
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }
}
