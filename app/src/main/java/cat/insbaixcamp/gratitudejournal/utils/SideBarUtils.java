package cat.insbaixcamp.gratitudejournal.utils;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import cat.insbaixcamp.gratitudejournal.R;

public class SideBarUtils implements NavigationView.OnNavigationItemSelectedListener {

    private final Context context;
    private final DrawerLayout drawerLayout;

    public SideBarUtils(Context context) {
        this.context = context;
        drawerLayout = ((AppCompatActivity) context).findViewById(R.id.drawer_layout);
    }

    public void setup() {
        AppCompatActivity activity = (AppCompatActivity) context;

        MaterialToolbar toolBar = activity.findViewById(R.id.topAppBar);
        activity.setSupportActionBar(toolBar);

        if (drawerLayout != null && toolBar != null) {
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    activity,
                    drawerLayout,
                    toolBar,
                    R.string.open_nav,
                    R.string.close_nav
            );

            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            // Set up navigation view listener
            ((NavigationView) activity.findViewById(R.id.sidebar_view)).setNavigationItemSelectedListener(this);
        } else {
            Toast.makeText(context, "Drawer setup failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void enable() {
        AppCompatActivity activity = (AppCompatActivity) context;

        Objects.requireNonNull(activity.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        activity.findViewById(R.id.topAppBar).setVisibility(View.VISIBLE);

        DrawerLayout drawerLayout = activity.findViewById(R.id.drawer_layout);
        if (drawerLayout != null) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

    public void disable() {
        AppCompatActivity activity = (AppCompatActivity) context;

        Objects.requireNonNull(activity.getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        activity.findViewById(R.id.topAppBar).setVisibility(View.GONE);

        DrawerLayout drawerLayout = activity.findViewById(R.id.drawer_layout);
        if (drawerLayout != null) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_rm_account) {
            Toast.makeText(context, "Removed Account", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.nav_logout) {
            Toast.makeText(context, "Logout!", Toast.LENGTH_SHORT).show();
        }

        drawerLayout.closeDrawers();
        return true;
    }
}
