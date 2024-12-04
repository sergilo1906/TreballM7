package cat.insbaixcamp.gratitudejournal.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsUtils {

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

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
}
