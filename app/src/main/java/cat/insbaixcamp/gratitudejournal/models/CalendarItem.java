package cat.insbaixcamp.gratitudejournal.models;

import java.io.Serializable;
import java.util.Date;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class CalendarItem implements Serializable {
    private static final long serialVersionUID = 1L;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;  // Same reference
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;  // Not the same class or obj is null
        }
        CalendarItem that = (CalendarItem) obj;
        return title.equals(that.title) &&
                description.equals(that.description) &&
                date.equals(that.date);  // Compare the attributes
    }

    @Override
    public int hashCode() {
        // Generate a unique hash code based on the attributes
        return Objects.hash(title, description, date);
    }
}
