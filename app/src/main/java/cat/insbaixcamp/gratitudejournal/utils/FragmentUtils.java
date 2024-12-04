package cat.insbaixcamp.gratitudejournal.utils;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import cat.insbaixcamp.gratitudejournal.R;

public class FragmentUtils {
    /**
     * Navigates to the specified fragment and clears the back stack if needed.
     *
     * @param fragment The fragment to navigate to.
     */
    public static void navigateTo(Context context, Fragment fragment, boolean clearBackStack) {
        if (context == null || fragment == null) return;

        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();

        if (clearBackStack) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);

        if (!clearBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
}
