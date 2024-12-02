package cat.insbaixcamp.gratitudejournal;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

import cat.insbaixcamp.gratitudejournal.fragments.InsightsFragment;
import cat.insbaixcamp.gratitudejournal.fragments.LoginFragment;
import cat.insbaixcamp.gratitudejournal.middlewares.AuthMiddleware;
import cat.insbaixcamp.gratitudejournal.utils.BottomNavigationUtils;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AuthMiddleware authMiddleware = new AuthMiddleware();
        setContentView(R.layout.activity_main);

        // Setup Toolbar and Drawer Layout
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Initialize ActionBarDrawerToggle for the hamburger menu
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Handle authentication check and fragment loading
        BottomNavigationUtils bottomNavigationUtils = new BottomNavigationUtils(this);
        if (!authMiddleware.isAuthenticated(this)) {
            bottomNavigationUtils.hide();
            loadFragment(new LoginFragment());
            return;
        }

        bottomNavigationUtils.show();

        // Load default fragment if no saved instance state
        if (savedInstanceState == null) {
            loadFragment(new InsightsFragment());
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_coins) {
            Toast.makeText(this, "Coins", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.nav_rm_account) {
            Toast.makeText(this, "Removed Account", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.nav_logout) {
            Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    // Method to load fragments dynamically
    private void loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
