package cat.insbaixcamp.gratitudejournal.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import cat.insbaixcamp.gratitudejournal.models.CalendarItem;

public class SharedPrefsUtils {

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    private static final String PREFS_NAME = "calendar_items_prefs";
    private static final String ITEMS_KEY = "calendar_items";

    // Constructor
    public SharedPrefsUtils(Context context, String prefsName) {
        this.sharedPreferences = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    /**
     * Creates or updates a key-value pair in SharedPreferences.
     *
     * @param key   The key to store the value under.
     * @param value The value to store (can be of any supported type).
     */
    public void createOrUpdate(String key, Object value) {
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else {
            throw new IllegalArgumentException("Unsupported data type for SharedPreferences");
        }
        editor.apply();
    }

    /**
     * Removes a key-value pair from SharedPreferences.
     *
     * @param key The key to remove.
     */
    public void remove(String key) {
        editor.remove(key).apply();
    }

    /**
     * Clears all data in the SharedPreferences.
     */
    public void clearAll() {
        editor.clear().apply();
    }

    /**
     * Retrieves a value from SharedPreferences.
     *
     * @param key          The key of the value to retrieve.
     * @param defaultValue The default value to return if the key doesn't exist.
     * @param <T>          The expected type of the value.
     * @return The value stored in SharedPreferences, or the defaultValue if not found.
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, T defaultValue) {
        Object result;
        if (defaultValue instanceof String) {
            result = sharedPreferences.getString(key, (String) defaultValue);
        } else if (defaultValue instanceof Integer) {
            result = sharedPreferences.getInt(key, (Integer) defaultValue);
        } else if (defaultValue instanceof Boolean) {
            result = sharedPreferences.getBoolean(key, (Boolean) defaultValue);
        } else if (defaultValue instanceof Float) {
            result = sharedPreferences.getFloat(key, (Float) defaultValue);
        } else if (defaultValue instanceof Long) {
            result = sharedPreferences.getLong(key, (Long) defaultValue);
        } else {
            throw new IllegalArgumentException("Unsupported data type for SharedPreferences");
        }
        return (T) result;
    }

    /**
     * Saves a list of CalendarItem in SharedPreferences.
     *
     * @param context The application context.
     * @param items   The list of CalendarItem to save.
     */
    public static void saveCalendarItems(Context context, List<CalendarItem> items) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(items);
            oos.close();
            String serializedItems = android.util.Base64.encodeToString(baos.toByteArray(), android.util.Base64.DEFAULT);
            sharedPreferences.edit().putString(ITEMS_KEY, serializedItems).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the list of CalendarItem from SharedPreferences.
     *
     * @param context The application context.
     * @return A list of CalendarItem.
     */
    public static List<CalendarItem> getCalendarItems(Context context) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String serializedItems = sharedPreferences.getString(ITEMS_KEY, "");
            if (serializedItems.isEmpty()) {
                return new ArrayList<>();
            }
            byte[] data = android.util.Base64.decode(serializedItems, android.util.Base64.DEFAULT);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
            List<CalendarItem> items = (List<CalendarItem>) ois.readObject();
            ois.close();

            items.sort(Comparator.comparing(CalendarItem::getDate));

            return items;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Adds a CalendarItem to the existing list and saves it in SharedPreferences.
     *
     * @param context The application context.
     * @param item    The CalendarItem to add.
     */
    public static void addCalendarItem(Context context, CalendarItem item) {
        List<CalendarItem> items = getCalendarItems(context);
        items.add(item);
        saveCalendarItems(context, items);
    }

    /**
     * Clears all CalendarItem saved in SharedPreferences.
     *
     * @param context The application context.
     */
    public static void clearCalendarItems(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(ITEMS_KEY).apply();
    }

}
