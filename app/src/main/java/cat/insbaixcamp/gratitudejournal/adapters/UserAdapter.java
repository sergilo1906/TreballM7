package cat.insbaixcamp.gratitudejournal.adapters;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cat.insbaixcamp.gratitudejournal.utils.UserUtils;
import cat.insbaixcamp.gratitudejournal.R;

public class UserAdapter {

    private final TextView tvPoints;
    private final Context context;
    private final UserUtils userUtils;

    public UserAdapter(TextView tvPoints, Button btnAddPoint, Context context) {
        this.tvPoints = tvPoints;
        this.context = context;
        this.userUtils = new UserUtils(context);

        setUpAddPointButton(btnAddPoint);
        fetchUserPoints();
    }

    private void setUpAddPointButton(View view) {
        Button btnAddPoint = view.findViewById(R.id.btn_add_point);
        btnAddPoint.setOnClickListener(v -> addPoints());
    }

    private void addPoints() {
        userUtils.getUserPoints(new UserUtils.OnFetchCallback() {
            @Override
            public void onSuccess(Object points) {
                int newPoints = ((Integer) points) + 1;
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

    private void fetchUserPoints() {
        userUtils.getUserPoints(new UserUtils.OnFetchCallback() {
            @Override
            public void onSuccess(Object points) {
                String formattedPoints = context.getString(R.string.current_points, (Integer) points);
                tvPoints.setText(formattedPoints);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(context, "Error fetching points: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
}
