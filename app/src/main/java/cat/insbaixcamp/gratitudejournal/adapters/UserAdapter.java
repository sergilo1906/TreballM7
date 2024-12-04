package cat.insbaixcamp.gratitudejournal.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cat.insbaixcamp.gratitudejournal.fragments.LoginFragment;
import cat.insbaixcamp.gratitudejournal.utils.AuthUtils;
import cat.insbaixcamp.gratitudejournal.utils.BottomNavUtils;
import cat.insbaixcamp.gratitudejournal.utils.FragmentUtils;
import cat.insbaixcamp.gratitudejournal.utils.SharedPrefsUtils;
import cat.insbaixcamp.gratitudejournal.utils.SideBarUtils;
import cat.insbaixcamp.gratitudejournal.utils.UserUtils;
import cat.insbaixcamp.gratitudejournal.R;

public class UserAdapter {

    private final TextView tvPoints;
    private final Context context;
    private final AuthUtils authUtils;
    private final UserUtils userUtils;
    private final String userId;

    public UserAdapter(TextView tvPoints, Button btnAddPoint, Button btnLogOut, Button btnRemoveAccount, Context context) {
        this.tvPoints = tvPoints;
        this.context = context;
        this.authUtils = new AuthUtils(context);
        this.userUtils = new UserUtils();
        this.userId = userUtils.getUserId();

        setUpAddPointButton(btnAddPoint);
        setUpLogoutButton(btnLogOut);
        setUpRemoveAccountButton(btnRemoveAccount);
        fetchUserPoints();
    }

    private void navigateToLoginFragment() {
        new SharedPrefsUtils(context, "account").clearAll();
        new BottomNavUtils(context).hide();
        new SideBarUtils(context).disable();
        FragmentUtils.navigateTo(context, new LoginFragment(), true);
    }

    private void setUpAddPointButton(View view) {
        Button btnAddPoint = view.findViewById(R.id.btn_add_point);
        btnAddPoint.setOnClickListener(v -> addPoints());
    }

    private void addPoints() {
        userUtils.getUserPoints(new UserUtils.OnFetchCallback<>() {
            @Override
            public void onSuccess(Integer points) {
                int newPoints = points + 1;
                userUtils.updateUserPoints(newPoints,
                        () -> {
                            String formattedPoints = context.getString(R.string.current_points, newPoints);
                            tvPoints.setText(formattedPoints);
                            Toast.makeText(context, "Points added!", Toast.LENGTH_SHORT).show();
                        },
                        () -> Toast.makeText(context, "Error updating points.", Toast.LENGTH_LONG).show()
                );
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(context, "Error fetching points: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setUpLogoutButton(View view) {
        Button btnLogout = view.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(v -> authUtils.logout(() -> {
            Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show();
            navigateToLoginFragment();
        }));
    }

    public void setUpRemoveAccountButton(View view) {
        Button btnRemoveAccount = view.findViewById(R.id.btn_remove_account);
        btnRemoveAccount.setOnClickListener(v -> new AlertDialog.Builder(context)
                .setTitle("Remove Account")
                .setMessage("Are you sure you want to permanently remove your account? This action cannot be undone.")
                .setPositiveButton("Yes", (dialog, which) -> removeUserAccount())
                .setNegativeButton("No", null)
                .show());
    }

    private void removeUserAccount() {
        userUtils.deleteUserDataFromFirestore(userId, () -> authUtils.removeAccount(() -> {
            Toast.makeText(context, "Account successfully removed.", Toast.LENGTH_SHORT).show();
            navigateToLoginFragment();
        }));
    }

    private void fetchUserPoints() {
        userUtils.getUserPoints(new UserUtils.OnFetchCallback<>() {
            @Override
            public void onSuccess(Integer points) {
                String formattedPoints = context.getString(R.string.current_points, points);
                tvPoints.setText(formattedPoints);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(context, "Error fetching points: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
}
