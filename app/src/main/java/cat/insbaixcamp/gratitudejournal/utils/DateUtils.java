package cat.insbaixcamp.gratitudejournal.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    public static String checkDay(long timestamp) {
        // Obtenim el calendari actual
        Calendar today = Calendar.getInstance();

        // Configurem el calendari per al timestamp donat
        Calendar dateFromTimestamp = Calendar.getInstance();
        dateFromTimestamp.setTime(new Date(timestamp));

        // Comprovem si correspon al dia actual
        if (isSameDay(today, dateFromTimestamp)) {
            return "Dia actual";
        }

        // Comprovem si correspon al dia anterior
        Calendar yesterday = (Calendar) today.clone();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);
        if (isSameDay(yesterday, dateFromTimestamp)) {
            return "Dia anterior";
        }

        return "Un dia anterior a l'anterior";
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }
}
