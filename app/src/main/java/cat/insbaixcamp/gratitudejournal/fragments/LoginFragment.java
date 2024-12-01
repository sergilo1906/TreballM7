package cat.insbaixcamp.gratitudejournal.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import android.widget.ProgressBar;

import cat.insbaixcamp.gratitudejournal.adapters.LoginAdapter;
import cat.insbaixcamp.gratitudejournal.R;

public class LoginFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        ProgressBar progressBar = view.findViewById(R.id.progress_bar);

        LoginAdapter loginAdapter = new LoginAdapter(progressBar, getContext());
        loginAdapter.setUpLogin(view);

        return view;
    }
}
