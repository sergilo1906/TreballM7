package cat.insbaixcamp.gratitudejournal.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import cat.insbaixcamp.gratitudejournal.R;
import cat.insbaixcamp.gratitudejournal.fragments.LoginFragment;

public class SideBarUtils implements NavigationView.OnNavigationItemSelectedListener {

    private final AppCompatActivity activity;
    private final NavigationView navigationView;
    private final DrawerLayout drawerLayout;
    private final MaterialToolbar toolBar;
    private final UserUtils userUtils;
    private final AuthUtils authUtils;

    public SideBarUtils(Context context) {
        this.activity = (AppCompatActivity) context;
        navigationView = activity.findViewById(R.id.sidebar_view);
        drawerLayout = activity.findViewById(R.id.drawer_layout);
        toolBar = activity.findViewById(R.id.toolbar);
        userUtils = new UserUtils(activity);
        authUtils = new AuthUtils(activity);
    }

    // Setup navigation drawer and toolbar
    public void setup() {
        if (drawerLayout == null || toolBar == null) return;

        activity.setSupportActionBar(toolBar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                activity,
                drawerLayout,
                toolBar,
                R.string.open_nav,
                R.string.close_nav
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this); // Set listener
    }

    // Fetch user data for sidebar header
    public void fetchData() {
        View headerView = navigationView.getHeaderView(0);
        ((TextView) headerView.findViewById(R.id.nav_email)).setText(authUtils.getEmail());

        userUtils.getUserPoints(new UserUtils.OnFetchCallback<>() {
            @Override
            public void onSuccess(Integer points) {
                String formattedPoints = activity.getString(R.string.current_points, points);
                ((TextView) headerView.findViewById(R.id.nav_coins)).setText(formattedPoints);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(activity, "Error fetching points: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    // Enable the sidebar
    public void enable() {
        if (toolBar != null) toolBar.setVisibility(View.VISIBLE);
        if (drawerLayout != null) drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    // Disable the sidebar
    public void disable() {
        if (toolBar != null) toolBar.setVisibility(View.GONE);
        if (drawerLayout != null)
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    // Navigate to the login fragment after removing account or logout
    private void navigateToLoginFragment() {
        new SharedPrefsUtils(activity, "account").clearAll();
        new BottomNavUtils(activity).hide();
        new SideBarUtils(activity).disable();
        FragmentUtils.navigateTo(activity, new LoginFragment(), true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_rm_account) {
            new AlertDialog.Builder(activity)
                    .setTitle("Remove Account")
                    .setMessage("Are you sure you want to permanently remove your account? This action cannot be undone.")
                    .setPositiveButton("Yes", (dialog, which) -> userUtils.deleteUserDataFromFirestore(() -> authUtils.removeAccount(this::navigateToLoginFragment)))
                    .setNegativeButton("No", null)
                    .show();
        } else if (item.getItemId() == R.id.nav_logout) {
            authUtils.logout(this::navigateToLoginFragment);
        } else {
            return false;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
