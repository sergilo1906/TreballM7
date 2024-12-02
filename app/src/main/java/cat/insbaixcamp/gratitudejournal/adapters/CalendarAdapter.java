package cat.insbaixcamp.gratitudejournal.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cat.insbaixcamp.gratitudejournal.R;
import cat.insbaixcamp.gratitudejournal.models.CalendarItem;
import cat.insbaixcamp.gratitudejournal.utils.DateUtils;

public class CalendarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_MONTH_HEADER = 0;
    private static final int TYPE_EVENT_ITEM = 1;

    private final List<Object> items = new ArrayList<>();

    public CalendarAdapter(Map<String, List<CalendarItem>> calendarItemsByMonth, List<String> months) {
        for (String month : months) {
            items.add(month);
            List<CalendarItem> events = calendarItemsByMonth.getOrDefault(month, new ArrayList<>());
            assert events != null;
            items.addAll(events);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) instanceof String ? TYPE_MONTH_HEADER : TYPE_EVENT_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_MONTH_HEADER) {
            View view = inflater.inflate(R.layout.item_month_header, parent, false);
            return new MonthHeaderViewHolder(view);
        }
        View view = inflater.inflate(R.layout.item_calendar_event, parent, false);
        return new CalendarEventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = items.get(position);
        if (holder instanceof MonthHeaderViewHolder) {
            ((MonthHeaderViewHolder) holder).bind((String) item);
        } else if (holder instanceof CalendarEventViewHolder) {
            ((CalendarEventViewHolder) holder).bind((CalendarItem) item);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MonthHeaderViewHolder extends RecyclerView.ViewHolder {
        private final TextView monthTextView;

        public MonthHeaderViewHolder(View itemView) {
            super(itemView);
            monthTextView = itemView.findViewById(R.id.tv_month_header);
        }

        public void bind(String month) {
            monthTextView.setText(month);
        }
    }

    public static class CalendarEventViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView description;
        private final TextView date;

        public CalendarEventViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_event_title);
            description = itemView.findViewById(R.id.tv_event_description);
            date = itemView.findViewById(R.id.tv_event_date);
        }

        public void bind(CalendarItem event) {
            title.setText(event.getTitle());
            description.setText(event.getDescription());
            date.setText(DateUtils.formatDateToString(event.getDate()));
        }
    }
}