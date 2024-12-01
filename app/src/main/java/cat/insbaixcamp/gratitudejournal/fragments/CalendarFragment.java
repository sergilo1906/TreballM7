package cat.insbaixcamp.gratitudejournal.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import cat.insbaixcamp.gratitudejournal.R;
import cat.insbaixcamp.gratitudejournal.adapters.CalendarAdapter;
import cat.insbaixcamp.gratitudejournal.models.CalendarItem;
import cat.insbaixcamp.gratitudejournal.utils.DateUtils;

public class CalendarFragment extends Fragment {
    private final List<String> months = new ArrayList<>();
    private final Map<String, List<CalendarItem>> calendarItemsByMonth = new TreeMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_calendar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        prepareCalendarData();

        CalendarAdapter calendarAdapter = new CalendarAdapter(calendarItemsByMonth, months);
        recyclerView.setAdapter(calendarAdapter);

        return view;
    }

    private void prepareCalendarData() {
        List<CalendarItem> allCalendarItems;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            allCalendarItems = getSampleCalendarItems().stream()
                    .sorted(Comparator.comparing(CalendarItem::getDate).reversed())
                    .toList();
        } else {
            allCalendarItems = getSampleCalendarItems();
            allCalendarItems.sort((item1, item2) -> item2.getDate().compareTo(item1.getDate()));
        }

        assert allCalendarItems != null;
        allCalendarItems.forEach(item -> {
            String monthYear = DateUtils.formatMonthYear(item.getDate());
            calendarItemsByMonth
                    .computeIfAbsent(monthYear, k -> {
                        months.add(k);
                        return new ArrayList<>();
                    })
                    .add(item);
        });
    }

    private List<CalendarItem> getSampleCalendarItems() {
        List<CalendarItem> items = new ArrayList<>();

        items.add(new CalendarItem("Meeting", "Discuss project status", DateUtils.parseDate("2024-01-15")));
        items.add(new CalendarItem("Doctor Appointment", "Annual check-up", DateUtils.parseDate("2024-05-10")));
        items.add(new CalendarItem("Conference", "Tech conference in San Francisco", DateUtils.parseDate("2024-02-25")));
        items.add(new CalendarItem("Workshop", "Android development workshop", DateUtils.parseDate("2024-09-05")));
        items.add(new CalendarItem("Vacation", "Beach trip", DateUtils.parseDate("2024-07-20")));
        items.add(new CalendarItem("Team Outing", "Company picnic", DateUtils.parseDate("2024-12-15")));
        items.add(new CalendarItem("Client Presentation", "Present new proposal", DateUtils.parseDate("2024-12-01")));

        items.sort(Comparator.comparing(CalendarItem::getDate));
        return items;
    }
}
