package cat.insbaixcamp.gratitudejournal.utils;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import cat.insbaixcamp.gratitudejournal.R;
import cat.insbaixcamp.gratitudejournal.fragments.CalendarFragment;
import cat.insbaixcamp.gratitudejournal.fragments.InsightsFragment;
import cat.insbaixcamp.gratitudejournal.fragments.UserFragment;

public class BottomNavigationUtils {

    private final Context context;
    private final BottomNavigationView bottomNavigationView;

    public BottomNavigationUtils(Context context) {
        this.context = context;
        this.bottomNavigationView = ((AppCompatActivity) context).findViewById(R.id.bottom_navigation);
    }

    public void setup() {
        AppCompatActivity activity = (AppCompatActivity) context;

        ((BottomNavigationView) activity.findViewById(R.id.bottom_navigation)).setOnItemSelectedListener(item -> {
            Fragment fragment;

            if (item.getItemId() == R.id.entries) {
                fragment = new InsightsFragment();
            } else if (item.getItemId() == R.id.user) {
                fragment = new UserFragment();
            } else if (item.getItemId() == R.id.calendar) {
                fragment = new CalendarFragment();
            } else {
                return false;
            }

            FragmentUtils.navigateTo(((FragmentActivity) context), fragment, false);
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
