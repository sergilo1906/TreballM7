package cat.insbaixcamp.gratitudejournal.models;

import java.util.Date;
import cat.insbaixcamp.gratitudejournal.utils.DateUtils;

public class CalendarItem {
    private final String title;
    private final String description;
    private final Date date;
    private final String monthYear;

    public CalendarItem(String title, String description, Date date) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.monthYear = getMonthYear(date);
    }

    private String getMonthYear(Date date) {
        return DateUtils.formatMonthYear(date);
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
