package cat.insbaixcamp.gratitudejournal.utils;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import cat.insbaixcamp.gratitudejournal.R;


public class SideBarUtils implements NavigationView.OnNavigationItemSelectedListener {

    private final AppCompatActivity activity;
    private final NavigationView navigationView;
    private final DrawerLayout drawerLayout;
    private final MaterialToolbar toolBar;

    public SideBarUtils(Context context) {
        this.activity = (AppCompatActivity) context;
        navigationView = activity.findViewById(R.id.sidebar_view);
        drawerLayout = activity.findViewById(R.id.drawer_layout);
        toolBar = activity.findViewById(R.id.toolbar);
    }

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
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void fetchData() {
        UserUtils userUtils = new UserUtils();
        View headerView = navigationView.getHeaderView(0);
        ((TextView) headerView.findViewById(R.id.nav_email)).setText(new AuthUtils(activity).getEmail());
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

    public void enable() {
        if (toolBar != null) toolBar.setVisibility(View.VISIBLE);
        if (drawerLayout != null) drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    public void disable() {
        if (toolBar != null) toolBar.setVisibility(View.GONE);
        if (drawerLayout != null)
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_rm_account) {
            Toast.makeText(activity, "Removed Account", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.nav_logout) {
            Toast.makeText(activity, "Logout!", Toast.LENGTH_SHORT).show();
        }

        drawerLayout.closeDrawers();
        return true;
    }

}
