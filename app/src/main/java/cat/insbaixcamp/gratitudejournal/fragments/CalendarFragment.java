package cat.insbaixcamp.gratitudejournal.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cat.insbaixcamp.gratitudejournal.R;
import cat.insbaixcamp.gratitudejournal.adapters.CalendarAdapter;
import cat.insbaixcamp.gratitudejournal.models.CalendarItem;
import cat.insbaixcamp.gratitudejournal.utils.DateUtils;

public class CalendarFragment extends Fragment {

    private final List<String> months = new ArrayList<>();
    private final Map<String, List<CalendarItem>> calendarItemsByMonth = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        // Initialize RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_calendar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Prepare data
        prepareCalendarData();

        // Set adapter
        CalendarAdapter calendarAdapter = new CalendarAdapter(calendarItemsByMonth);
        recyclerView.setAdapter(calendarAdapter);

        return view;
    }

    private void prepareCalendarData() {
        // Sample calendar items for demonstration
        List<CalendarItem> allCalendarItems = new ArrayList<>();

        // Add sample calendar items
        allCalendarItems.add(new CalendarItem("Meeting", "Discuss project status", DateUtils.parseDate("2024-01-15"))); // January
        allCalendarItems.add(new CalendarItem("Doctor Appointment", "Annual check-up", DateUtils.parseDate("2024-05-10"))); // May
        allCalendarItems.add(new CalendarItem("Conference", "Tech conference in San Francisco", DateUtils.parseDate("2024-02-25"))); // February
        allCalendarItems.add(new CalendarItem("Workshop", "Android development workshop", DateUtils.parseDate("2024-09-05"))); // September
        allCalendarItems.add(new CalendarItem("Vacation", "Beach trip", DateUtils.parseDate("2024-07-20"))); // July
        allCalendarItems.add(new CalendarItem("Team Outing", "Company picnic", DateUtils.parseDate("2024-12-15"))); // December
        allCalendarItems.add(new CalendarItem("Client Presentation", "Present new proposal", DateUtils.parseDate("2024-12-01"))); // December

        // Sort the items by date
        allCalendarItems.sort(Comparator.comparing(CalendarItem::getDate));

        // Group items by month-year
        for (CalendarItem item : allCalendarItems) {
            String monthYear = DateUtils.formatMonthYear(item.getDate()); // Format date as "Month Year"

            // Add month if not already in the list
            if (!calendarItemsByMonth.containsKey(monthYear)) {
                calendarItemsByMonth.put(monthYear, new ArrayList<>());
                months.add(monthYear);
            }

            // Add the event to the respective month-year group
            Objects.requireNonNull(calendarItemsByMonth.get(monthYear)).add(item);
        }
    }
}
