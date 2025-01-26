package cat.insbaixcamp.gratitudejournal.utils;

import android.content.Context;

public class InitUtils {

    private final SharedPrefsUtils sharedPrefsUtils;

    public InitUtils(Context context) {
        sharedPrefsUtils = new SharedPrefsUtils(context, "onboarding");
    }

    public boolean isOnboardingCompleted() {
        return sharedPrefsUtils.get("isCompleted", false);
    }

    public void setOnboardingAsCompleted() {
        sharedPrefsUtils.createOrUpdate("isCompleted", true);
    }
}
