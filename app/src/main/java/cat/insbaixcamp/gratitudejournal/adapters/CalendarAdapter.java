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
    private final List<Object> items;

    public CalendarAdapter(Map<String, List<CalendarItem>> calendarItemsByMonth) {
        items = new ArrayList<>();

        for (String month : calendarItemsByMonth.keySet()) {
            items.add(month);
            List<CalendarItem> events = calendarItemsByMonth.get(month);
            if (events != null) {
                items.addAll(events);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof String) {
            return TYPE_MONTH_HEADER;
        }

        return TYPE_EVENT_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_MONTH_HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_month_header, parent, false);
            return new MonthHeaderViewHolder(view);
        }

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_calendar_event, parent, false);
        return new CalendarEventViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = items.get(position);

        if (holder instanceof MonthHeaderViewHolder) {
            ((MonthHeaderViewHolder) holder).monthTextView.setText((String) item); // Header
        } else if (holder instanceof CalendarEventViewHolder) {
            CalendarItem event = (CalendarItem) item;
            CalendarEventViewHolder eventViewHolder = (CalendarEventViewHolder) holder;

            eventViewHolder.setTitle(event.getTitle());
            eventViewHolder.setDescription(event.getDescription());
            eventViewHolder.setDate(DateUtils.formatDateToString(event.getDate()));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MonthHeaderViewHolder extends RecyclerView.ViewHolder {
        TextView monthTextView;

        public MonthHeaderViewHolder(View itemView) {
            super(itemView);
            monthTextView = itemView.findViewById(R.id.tv_month_header);
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

        public void setTitle(String text) {
            title.setText(text);
        }

        public void setDescription(String text) {
            description.setText(text);
        }

        public void setDate(String text) {
            date.setText(text);
        }
    }
}
