package cat.insbaixcamp.gratitudejournal.utils;

import android.content.Context;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import cat.insbaixcamp.gratitudejournal.R;
import cat.insbaixcamp.gratitudejournal.fragments.AddFragment;
import cat.insbaixcamp.gratitudejournal.fragments.CalendarFragment;
import cat.insbaixcamp.gratitudejournal.fragments.UserFragment;

public class BottomNavUtils {

    private final AppCompatActivity activity;
    private final BottomNavigationView bottomNavigationView;

    public BottomNavUtils(Context context) {
        this.activity = ((AppCompatActivity) context);
        this.bottomNavigationView = activity.findViewById(R.id.bottom_navigation);
    }

    public void setup() {
        ((BottomNavigationView) activity.findViewById(R.id.bottom_navigation)).setOnItemSelectedListener(item -> {
            Fragment fragment;

            if (item.getItemId() == R.id.calendar) {
                fragment = new CalendarFragment();
            } else if (item.getItemId() == R.id.add) {
                fragment = new AddFragment();
            } else if (item.getItemId() == R.id.user) {
                fragment = new UserFragment();
            } else {
                return false;
            }

            FragmentUtils.navigateTo(activity, fragment, false);
            return true;
        });
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
