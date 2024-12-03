package cat.insbaixcamp.gratitudejournal.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import cat.insbaixcamp.gratitudejournal.adapters.LoginAdapter;
import cat.insbaixcamp.gratitudejournal.R;

public class LoginFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        LoginAdapter loginAdapter = new LoginAdapter(view.findViewById(R.id.progress_bar), getContext());
        loginAdapter.setUpLogin(view);

        return view;
    }
}
