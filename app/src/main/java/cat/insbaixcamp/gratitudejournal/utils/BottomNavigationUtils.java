package cat.insbaixcamp.gratitudejournal.utils;

import android.content.Context;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import cat.insbaixcamp.gratitudejournal.R;

public class BottomNavigationUtils {
    private final BottomNavigationView bottomNavigationView;

    public BottomNavigationUtils(Context context) {
        FragmentActivity activity = (FragmentActivity) context;
        this.bottomNavigationView = activity.findViewById(R.id.bottom_navigation);
    }

    public void show() {
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }

    public void hide() {
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(View.GONE);
        }
    }
}
