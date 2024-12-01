package cat.insbaixcamp.gratitudejournal.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cat.insbaixcamp.gratitudejournal.R;
import cat.insbaixcamp.gratitudejournal.models.TextItem;

public class TextBoxAdapter extends RecyclerView.Adapter<TextBoxAdapter.TextBoxViewHolder> {
    private final List<TextItem> items;

    public TextBoxAdapter(List<TextItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public TextBoxViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_textbox, parent, false);

        return new TextBoxViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TextBoxViewHolder holder, int position) {
        TextItem item = items.get(position);
        holder.textItem.setText(item.getText());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class TextBoxViewHolder extends RecyclerView.ViewHolder {
        public TextView textItem;

        public TextBoxViewHolder(View itemView) {
            super(itemView);
            textItem = itemView.findViewById(R.id.text_item);
        }
    }
}