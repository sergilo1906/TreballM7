package cat.insbaixcamp.gratitudejournal;

import static cat.insbaixcamp.gratitudejournal.utils.DateUtils.isSameDay;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;

import cat.insbaixcamp.gratitudejournal.fragments.CalendarFragment;
import cat.insbaixcamp.gratitudejournal.fragments.LoginFragment;
import cat.insbaixcamp.gratitudejournal.middlewares.AuthMiddleware;
import cat.insbaixcamp.gratitudejournal.utils.BottomNavUtils;
import cat.insbaixcamp.gratitudejournal.utils.FragmentUtils;
import cat.insbaixcamp.gratitudejournal.utils.SideBarUtils;
import cat.insbaixcamp.gratitudejournal.utils.UserUtils;

public class MainActivity extends AppCompatActivity {

    public static MainActivity MainActivity;
    FrameLayout frameLayout;
    static UserUtils userUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActivity = this;

        frameLayout = findViewById(R.id.fragment_container);

        // Set up Sidebar and Tool Bar
        SideBarUtils sbUtils = new SideBarUtils(this);
        sbUtils.setup();

        // Set up bottom navigation view
        BottomNavUtils bnUtils = new BottomNavUtils(this);
        bnUtils.setup();

        // Show or hide navigation bar / sidebar
        if (AuthMiddleware.isAuthenticated()) {
            sbUtils.enable();
            sbUtils.fetchData();
            bnUtils.show();

            userUtils = new UserUtils(this);
            userUtils.getUserAttribute("lastTimeNoteCreated", new UserUtils.OnFetchCallback<Long>() {
                @Override
                public void onSuccess(Long lastTimeNoteCreatedDate) {
                    Calendar today = Calendar.getInstance();

                    Calendar dateFromTimestamp = Calendar.getInstance();
                    dateFromTimestamp.setTime(new Date(lastTimeNoteCreatedDate));

                    Calendar yesterday = (Calendar) today.clone();
                    yesterday.add(Calendar.DAY_OF_YEAR, -1);

                    if (!isSameDay(today, dateFromTimestamp) && !isSameDay(yesterday, dateFromTimestamp)) {
                        Toast.makeText(getApplicationContext(), "You lost your day streak", Toast.LENGTH_LONG).show();
                        userUtils.updateUserAttribute("dayStreak", 0, () -> {}, () -> {
                            Toast.makeText(getApplicationContext(), "An error ocurred while updating your day streak", Toast.LENGTH_LONG).show();
                        });
                    }
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(getApplicationContext(), "Error fetching last connection date: " + errorMessage, Toast.LENGTH_LONG).show();
                }
            });
            userUtils.getUserAttribute("currentBackground", new UserUtils.OnFetchCallback<Long>() {
                @Override
                public void onSuccess(Long currentBackground) {
                    Integer[] backgrounds = new Integer[]{
                            R.drawable.background_1,
                            R.drawable.background_2,
                            R.drawable.background_3,
                            R.drawable.background_4,
                            R.drawable.background_5,
                            R.drawable.background_6
                    };
                    changeBackground(backgrounds[currentBackground.intValue()]);
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(getApplicationContext(), "Error fetching current background: " + errorMessage, Toast.LENGTH_LONG).show();
                }
            });

            FragmentUtils.navigateTo(this, new CalendarFragment(), true);
        } else {
            sbUtils.disable();
            bnUtils.hide();
            FragmentUtils.navigateTo(this, new LoginFragment(), true);
        }
    }

    public static void changeBackground(Integer backgroundId) {
        MainActivity.frameLayout.setBackground(MainActivity.getApplicationContext().getDrawable(backgroundId));
    }
}
