package cat.insbaixcamp.gratitudejournal.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cat.insbaixcamp.gratitudejournal.R;
import cat.insbaixcamp.gratitudejournal.adapters.TextBoxAdapter;
import cat.insbaixcamp.gratitudejournal.models.TextItem;

public class InsightsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entries, container, false);

        RecyclerView rv = view.findViewById(R.id.recycler_view_entries);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        List<TextItem> textItemList = new ArrayList<>();
        textItemList.add(new TextItem("Item 1"));
        textItemList.add(new TextItem("Item 2"));
        textItemList.add(new TextItem("Item 3"));

        rv.setAdapter(new TextBoxAdapter(textItemList));

        return view;
    }
}