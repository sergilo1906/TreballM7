package cat.insbaixcamp.gratitudejournal.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cat.insbaixcamp.gratitudejournal.R;
import cat.insbaixcamp.gratitudejournal.utils.SharedPrefsUtils;
import cat.insbaixcamp.gratitudejournal.utils.UserUtils;

public class UserAdapter {

    private final TextView tvPoints;
    private final Context context;
    private final UserUtils userUtils;
    private final SharedPrefsUtils sharedPrefsUtils;

    public UserAdapter(TextView tvPoints, Button btnAddPoint, Button btnShowBuyMenu, Context context) {
        this.tvPoints = tvPoints;
        this.context = context;
        userUtils = new UserUtils(context);
        sharedPrefsUtils = new SharedPrefsUtils(context, "backgrounds");

        setUpAddPointButton(btnAddPoint);
        setUpShowBuyMenuButton(btnShowBuyMenu);
        fetchUserPoints();
    }

    private void addPoints(int n) {
        userUtils.getUserPoints(new UserUtils.OnFetchCallback() {
            @Override
            public void onSuccess(Object points) {
                int newPoints = (int) points + n;
                userUtils.updateUserPoints(newPoints,
                        () -> {
                            String formattedPoints = context.getString(R.string.current_points, newPoints);
                            tvPoints.setText(formattedPoints);
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

    private void setUpAddPointButton(View view) {
        Button btnAddPoint = view.findViewById(R.id.btn_add_point);
        btnAddPoint.setOnClickListener(v -> addPoints(1));
    }

    private void setUpShowBuyMenuButton(Button btnShowBuyMenu) {
        btnShowBuyMenu.setOnClickListener(v -> showBuyBackgroundDialog());
    }

    private void showBuyBackgroundDialog() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_menu_buy, null);

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ViewGroup backgroundContainer = dialogView.findViewById(R.id.background_container);

        for (int backgroundIndex = 1; backgroundIndex <= 8; backgroundIndex++) {
            int backgroundCostValue = getBackgroundCost(backgroundIndex);
            String backgroundNameText = context.getString(R.string.background_number, backgroundIndex);

            View backgroundView = layoutInflater.inflate(R.layout.dialog_buy_background_item, backgroundContainer, false);

            ImageView backgroundPreviewImage = backgroundView.findViewById(R.id.background_preview_image);
            TextView backgroundName = backgroundView.findViewById(R.id.background_name);
            TextView backgroundCost = backgroundView.findViewById(R.id.background_cost);
            Button buyButton = backgroundView.findViewById(R.id.buy_button);

            int imageResId = getBackgroundImageResource(backgroundIndex);
            backgroundPreviewImage.setImageResource(imageResId);

            backgroundName.setText(backgroundNameText);
            backgroundCost.setText(String.format(context.getString(R.string.cost_format), backgroundCostValue));

            int finalBackgroundIndex = backgroundIndex;
            boolean isUnlocked = sharedPrefsUtils.get("bg_" + backgroundIndex, false);
            if (isUnlocked) {
                if (sharedPrefsUtils.get("currentBg", 0) == backgroundIndex) {
                    buyButton.setText("Enabled");
                    buyButton.setEnabled(false);
                } else {
                    buyButton.setText("Set default");
                    buyButton.setEnabled(true);
                }
                buyButton.setOnClickListener(v -> sharedPrefsUtils.createOrUpdate("currentBg", finalBackgroundIndex));
            } else {
                buyButton.setText("Buy");
                buyButton.setEnabled(true);
                buyButton.setOnClickListener(v -> purchaseBackground(finalBackgroundIndex, backgroundCostValue));
            }

            backgroundContainer.addView(backgroundView);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void purchaseBackground(int backgroundIndex, int cost) {
        userUtils.getUserPoints(new UserUtils.OnFetchCallback() {
            @Override
            public void onSuccess(Object points) {
                int currentPoints = (int) points;
                if (currentPoints >= cost) {
                    userUtils.updateUserPoints(currentPoints - cost,
                        () -> {
                            sharedPrefsUtils.createOrUpdate("bg_" + backgroundIndex, true);
                            Toast.makeText(context, "Background " + backgroundIndex + " unlocked!", Toast.LENGTH_SHORT).show();
                        },
                        () -> Toast.makeText(context, "Error updating points.", Toast.LENGTH_LONG).show());
                } else {
                    Toast.makeText(context, "Not enough points.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(context, "Error fetching points: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private int getBackgroundCost(int backgroundIndex) {
        switch (backgroundIndex) {
            case 1: return 4;
            case 2: return 8;
            case 3: return 26;
            case 4: return 60;
            case 5: return 140;
            case 6: return 260;
            case 7: return 420;
            case 8: return 890;
            default: return 0;
        }
    }

    private static final int[] BACKGROUND_RESOURCES = {
            R.drawable.bg1,
            R.drawable.bg2,
            R.drawable.bg3,
            R.drawable.bg4,
            R.drawable.bg5,
            R.drawable.bg6,
            R.drawable.bg7,
            R.drawable.bg8
    };

    public static int getBackgroundImageResource(int backgroundIndex) {
        if (backgroundIndex >= 0 && backgroundIndex < BACKGROUND_RESOURCES.length) {
            return BACKGROUND_RESOURCES[backgroundIndex];
        } else {
            return R.drawable.bg1;
        }
    }

    private void fetchUserPoints() {
        userUtils.getUserPoints(new UserUtils.OnFetchCallback() {
            @Override
            public void onSuccess(Object points) {
                int currentPoints = (int) points;
                String formattedPoints = context.getString(R.string.current_points, currentPoints);
                tvPoints.setText(formattedPoints);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(context, "Error fetching points: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
}
