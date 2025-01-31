package cat.insbaixcamp.gratitudejournal.fragments;

import static cat.insbaixcamp.gratitudejournal.utils.DateUtils.isSameDay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static cat.insbaixcamp.gratitudejournal.MainActivity.MainActivity;
import cat.insbaixcamp.gratitudejournal.R;
import cat.insbaixcamp.gratitudejournal.models.CalendarItem;
import cat.insbaixcamp.gratitudejournal.utils.DateUtils;
import cat.insbaixcamp.gratitudejournal.utils.FragmentUtils;
import cat.insbaixcamp.gratitudejournal.utils.SharedPrefsUtils;
import cat.insbaixcamp.gratitudejournal.utils.UserUtils;

public class AddFragment extends Fragment {

    CalendarItem calendarItemToEdit = null;

    public AddFragment() {}

    public AddFragment(CalendarItem calendarItem) {
        calendarItemToEdit = calendarItem;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        EditText etDate = view.findViewById(R.id.et_date);
        EditText etTitle = view.findViewById(R.id.et_title);
        EditText etDescription = view.findViewById(R.id.et_description);
        Button btSave = view.findViewById(R.id.bt_save);

        if (calendarItemToEdit != null) {
            etDate.setText(DateUtils.formatDateToString(calendarItemToEdit.getDate()));
            etTitle.setText(calendarItemToEdit.getTitle());
            etDescription.setText(calendarItemToEdit.getDescription());
            btSave.setText("Update Note");
        } else {
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            etDate.setText(currentDate);
        }

        btSave.setOnClickListener(v -> {
            if (DateUtils.parseDate(etDate.getText().toString()) == null) {
                Toast.makeText(getContext(), "Write a valid date", Toast.LENGTH_SHORT).show();
                return;
            }
            if (etTitle.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Write a title", Toast.LENGTH_SHORT).show();
                return;
            }
            if (etDescription.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Write a description", Toast.LENGTH_SHORT).show();
                return;
            }


            CalendarItem createdCalendarItem = new CalendarItem(etTitle.getText().toString(), etDescription.getText().toString(), DateUtils.parseDate(etDate.getText().toString()));
            if (calendarItemToEdit != null) {
                SharedPrefsUtils.updateCalendarItem(getContext(), calendarItemToEdit, createdCalendarItem);

                Toast.makeText(getContext(), "Note updated successfully", Toast.LENGTH_SHORT).show();
                FragmentUtils.navigateTo(getContext(), new CalendarFragment(), true);
            } else {
                SharedPrefsUtils.addCalendarItem(getContext(), createdCalendarItem);

                Toast.makeText(getContext(), "Note added successfully", Toast.LENGTH_SHORT).show();
                String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                etDate.setText(currentDate);;
                etTitle.setText("");
                etDescription.setText("");


                UserUtils userUtils = new UserUtils(MainActivity);
                userUtils.getUserAttribute("lastTimeNoteCreated", new UserUtils.OnFetchCallback<Long>() {
                    @Override
                    public void onSuccess(Long lastTimeNoteCreatedDate) {
                        userUtils.updateUserAttribute("lastTimeNoteCreated", System.currentTimeMillis(), () -> {}, () -> {
                            Toast.makeText(MainActivity.getApplicationContext(), "An error ocurred while updating your last connection", Toast.LENGTH_LONG).show();
                        });

                        Calendar today = Calendar.getInstance();

                        Calendar dateFromTimestamp = Calendar.getInstance();
                        dateFromTimestamp.setTime(new Date(lastTimeNoteCreatedDate));
                        if (isSameDay(today, dateFromTimestamp)) return;

                        Calendar yesterday = (Calendar) today.clone();
                        yesterday.add(Calendar.DAY_OF_YEAR, -1);
                        if (isSameDay(yesterday, dateFromTimestamp)) {
                            userUtils.getUserAttribute("dayStreak", new UserUtils.OnFetchCallback<Long>() {
                                @Override
                                public void onSuccess(Long value) {
                                    value = value + 1;
                                    Toast.makeText(MainActivity.getApplicationContext(), "Your day streak is " + value + "!", Toast.LENGTH_LONG).show();
                                    userUtils.updateUserAttribute("dayStreak", value, () -> {}, () -> {
                                        Toast.makeText(MainActivity.getApplicationContext(), "An error ocurred while updating your day streak", Toast.LENGTH_LONG).show();
                                    });
                                    userUtils.increaseUserPoints(((value.intValue()-1) / 7) + 1);
                                }

                                @Override
                                public void onFailure(String errorMessage) {
                                    Toast.makeText(MainActivity.getApplicationContext(), "Error fetching your day streak", Toast.LENGTH_LONG).show();
                                }
                            });
                            return;
                        }

                        Toast.makeText(MainActivity.getApplicationContext(), "Your day streak is 1!", Toast.LENGTH_LONG).show();
                        userUtils.updateUserAttribute("dayStreak", 1, () -> {}, () -> {
                            Toast.makeText(MainActivity.getApplicationContext(), "An error ocurred while updating your day streak", Toast.LENGTH_LONG).show();
                        });
                        userUtils.increaseUserPoints(1);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(MainActivity.getApplicationContext(), "Error fetching last connection date: " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                });

                FragmentUtils.navigateTo(getContext(), new CalendarFragment(), true);

            }


        });

        return view;
    }
}
