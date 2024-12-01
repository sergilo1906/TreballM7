package cat.insbaixcamp.gratitudejournal.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    // Format the given date into a string with the format "MMMM yyyy" (e.g., "December 2024").
    public static String formatMonthYear(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        return format.format(date);
    }

    // Convert a Date object to a formatted string with the format "yyyy-MM-dd".
    public static String formatDateToString(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
    }

    // Convert a String date to a formatted string with the format "yyyy-MM-dd".
    public static Date parseDate(String dateStr) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }
}
