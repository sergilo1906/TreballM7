package cat.insbaixcamp.gratitudejournal.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import cat.insbaixcamp.gratitudejournal.adapters.UserAdapter;
import cat.insbaixcamp.gratitudejournal.R;

public class UserFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        new UserAdapter(
                view.findViewById(R.id.tv_user_points),
                view.findViewById(R.id.btn_add_point),
                view.findViewById(R.id.btn_logout),
                view.findViewById(R.id.btn_remove_account),
                getContext()
        );

        return view;
    }
}
