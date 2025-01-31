package cat.insbaixcamp.gratitudejournal.fragments;

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
import java.util.Date;
import java.util.Locale;

import cat.insbaixcamp.gratitudejournal.R;
import cat.insbaixcamp.gratitudejournal.models.CalendarItem;
import cat.insbaixcamp.gratitudejournal.utils.DateUtils;
import cat.insbaixcamp.gratitudejournal.utils.FragmentUtils;
import cat.insbaixcamp.gratitudejournal.utils.SharedPrefsUtils;

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
            }
        });

        return view;
    }
}
