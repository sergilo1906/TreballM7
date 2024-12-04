package cat.insbaixcamp.gratitudejournal.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import cat.insbaixcamp.gratitudejournal.R;
import cat.insbaixcamp.gratitudejournal.adapters.AddAdapter;

public class AddFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        TextView tvSample = view.findViewById(R.id.add_text);
        tvSample.setText("TODO: Edit Add page");

        AddAdapter addAdapter = new AddAdapter();

        return view;
    }
}
