package cat.insbaixcamp.gratitudejournal.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cat.insbaixcamp.gratitudejournal.adapters.UserAdapter;
import cat.insbaixcamp.gratitudejournal.R;

public class UserFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        // Initialize UserAdapter with views and context
        new UserAdapter(
            view.findViewById(R.id.tv_user_points),
            view.findViewById(R.id.btn_add_point),
            view.findViewById(R.id.btn_show_buy_menu),
            requireContext()
        );

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
