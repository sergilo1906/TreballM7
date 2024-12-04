package cat.insbaixcamp.gratitudejournal;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import cat.insbaixcamp.gratitudejournal.fragments.CalendarFragment;
import cat.insbaixcamp.gratitudejournal.fragments.LoginFragment;
import cat.insbaixcamp.gratitudejournal.middlewares.AuthMiddleware;
import cat.insbaixcamp.gratitudejournal.utils.BottomNavUtils;
import cat.insbaixcamp.gratitudejournal.utils.FragmentUtils;
import cat.insbaixcamp.gratitudejournal.utils.SideBarUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up Sidebar and Tool Bar
        SideBarUtils sbUtils = new SideBarUtils(this);
        sbUtils.setup();

        // Set up bottom navigation view
        BottomNavUtils bnUtils = new BottomNavUtils(this);
        bnUtils.setup();

        // Show or hide navigation bar / sidebar
        AuthMiddleware authMiddleware = new AuthMiddleware(this);
        if (authMiddleware.isAuthenticated()) {
            sbUtils.enable();
            sbUtils.fetchData();
            bnUtils.show();
            FragmentUtils.navigateTo(this, new CalendarFragment(), true);
        } else {
            sbUtils.disable();
            bnUtils.hide();
            FragmentUtils.navigateTo(this, new LoginFragment(), true);
        }
    }
}
