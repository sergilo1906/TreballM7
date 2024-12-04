package cat.insbaixcamp.gratitudejournal.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import cat.insbaixcamp.gratitudejournal.adapters.RegisterAdapter;
import cat.insbaixcamp.gratitudejournal.R;

public class RegisterFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        RegisterAdapter registerAdapter = new RegisterAdapter(view.findViewById(R.id.progress_bar), getContext());
        registerAdapter.setup(view);

        return view;
    }
}
