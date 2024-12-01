package cat.insbaixcamp.gratitudejournal.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import cat.insbaixcamp.gratitudejournal.fragments.LoginFragment;
import cat.insbaixcamp.gratitudejournal.utils.AuthUtils;
import cat.insbaixcamp.gratitudejournal.utils.UserUtils;
import cat.insbaixcamp.gratitudejournal.R;

public class UserAdapter {

    private final TextView tvPoints;
    private final Context context;
    private final AuthUtils authUtils;
    private final UserUtils userUtils;

    public UserAdapter(TextView tvPoints, Button btnAddPoint, Button btnLogOut, Button btnRemoveAccount, Context context) {
        this.tvPoints = tvPoints;
        this.context = context;
        this.authUtils = new AuthUtils(context);
        this.userUtils = new UserUtils();

        setUpAddPointButton(btnAddPoint);
        setUpLogoutButton(btnLogOut);
        setUpRemoveAccountButton(btnRemoveAccount);
        fetchUserPoints();
    }

    private String getUserId() {
        return userUtils.getUserId();
    }

    private void navigateToLoginFragment() {
        ((FragmentActivity) context).getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new LoginFragment())
                .addToBackStack(null)
                .commit();
    }

    private void setUpAddPointButton(View view) {
        Button btnAddPoint = view.findViewById(R.id.btn_add_point);
        btnAddPoint.setOnClickListener(v -> addPoints());
    }

    private void addPoints() {
        String userId = getUserId();
        if (userId != null) {
            userUtils.getUserPoints(userId, new UserUtils.OnUserPointsFetchedListener() {
                @Override
                public void onSuccess(int points) {
                    int newPoints = points + 1;
                    updatePoints(userId, newPoints);
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(context, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void updatePoints(String userId, int newPoints) {
        userUtils.updateUserPoints(userId, newPoints,
                () -> {
                    String formattedPoints = context.getString(R.string.current_points, newPoints);
                    tvPoints.setText(formattedPoints);
                    Toast.makeText(context, "Points added!", Toast.LENGTH_SHORT).show();
                },
                () -> Toast.makeText(context, "Error updating points.", Toast.LENGTH_LONG).show()
        );
    }

    private void setUpLogoutButton(View view) {
        Button btnLogout = view.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(v -> authUtils.logout(() -> {
            Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show();
            navigateToLoginFragment();
        }));
    }

    // Set up the "Remove Account" button
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
        userUtils.deleteUserDataFromFirestore(getUserId(), () -> authUtils.removeAccount(() -> {
            Toast.makeText(context, "Account successfully removed.", Toast.LENGTH_SHORT).show();
            navigateToLoginFragment();
        }));
    }

    private void fetchUserPoints() {
        String userId = getUserId();
        if (userId != null) {
            userUtils.getUserPoints(userId, new UserUtils.OnUserPointsFetchedListener() {
                @Override
                public void onSuccess(int points) {
                    String formattedPoints = context.getString(R.string.current_points, points);
                    tvPoints.setText(formattedPoints);
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(context, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
