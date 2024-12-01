package cat.insbaixcamp.gratitudejournal;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import cat.insbaixcamp.gratitudejournal.fragments.CalendarFragment;
import cat.insbaixcamp.gratitudejournal.fragments.InsightsFragment;
import cat.insbaixcamp.gratitudejournal.fragments.UserFragment;
import cat.insbaixcamp.gratitudejournal.fragments.LoginFragment;
import cat.insbaixcamp.gratitudejournal.middlewares.AuthMiddleware;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AuthMiddleware authMiddleware = new AuthMiddleware();
        BottomNavigationView bottomNavigationView;
        if (!authMiddleware.isAuthenticated(this)) {
            setContentView(R.layout.activity_main);
            loadFragment(new LoginFragment());
            bottomNavigationView = findViewById(R.id.bottom_navigation);
            bottomNavigationView.setVisibility(View.GONE);
            return;
        }

        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);

        if (savedInstanceState == null) {
            loadFragment(new InsightsFragment());
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            if (item.getItemId() == R.id.entries) {
                selectedFragment = new InsightsFragment();
            } else if (item.getItemId() == R.id.user) {
                selectedFragment = new UserFragment();
            } else if (item.getItemId() == R.id.calendar) {
                selectedFragment = new CalendarFragment();
            }
            return loadFragment(selectedFragment);
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
