package cat.insbaixcamp.gratitudejournal.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import cat.insbaixcamp.gratitudejournal.adapters.UserAdapter;
import cat.insbaixcamp.gratitudejournal.R;

public class UserFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        TextView tvPoints = view.findViewById(R.id.tv_user_points);
        Button btnAddPoint = view.findViewById(R.id.btn_add_point);
        Button btnLogOut = view.findViewById(R.id.btn_logout);
        Button btnRemoveAccount = view.findViewById(R.id.btn_remove_account);

        new UserAdapter(tvPoints, btnAddPoint, btnLogOut, btnRemoveAccount, getContext());

        return view;
    }
}
