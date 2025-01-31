package cat.insbaixcamp.gratitudejournal.models;

import static cat.insbaixcamp.gratitudejournal.MainActivity.MainActivity;

import android.app.AlertDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import cat.insbaixcamp.gratitudejournal.R;
import cat.insbaixcamp.gratitudejournal.utils.UserUtils;

public class ShopAvatarItem {
    private TextView textView;
    private Integer price;
    private Integer imageRes;
    private ConstraintLayout constraint;
    private int position;

    public ShopAvatarItem(TextView textView, Integer price, Integer imageRes, ConstraintLayout constraint, int position) {
        this.textView = textView;
        this.price = price;
        this.imageRes = imageRes;
        this.constraint = constraint;
        this.position = position;

        updateTextViewContent();
        getLockedContraint().setOnClickListener(v -> {
            // Usa el context de l'activitat
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Purchase Avatar");
            builder.setMessage("Do you want to buy this avatar for " + price + " points?");

            builder.setPositiveButton("Yes", (dialog, which) -> {
                UserUtils userUtils = new UserUtils(v.getContext());
                userUtils.getUserAttribute("points", new UserUtils.OnFetchCallback<Long>() {
                    @Override
                    public void onSuccess(Long value) {
                        if (value.intValue() >= price) {
                            unlock();
                            userUtils.increaseUserPoints(-price);
                            userUtils.increaseUserAttribute("avatars", (int) Math.pow(2, position));
                            getConstraint().callOnClick();
                        } else {
                            Toast.makeText(v.getContext(), "You don't have enough points", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(v.getContext(), "Error fetching user points: " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                });

                dialog.dismiss();
            });
            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

            builder.create().show();
        });
    }

    public TextView getTextView() {
        return textView;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getImageRes() {
        return imageRes;
    }

    public ConstraintLayout getConstraint() {
        return constraint;
    }

    private void updateTextViewContent() {
        this.getTextView().setText(MainActivity.getApplicationContext().getString(R.string.shop_item_price, this.getPrice()));
    }

    private ConstraintLayout getLockedContraint() {
        int count = getConstraint().getChildCount();
        for (int i = 0; i < count; i++) {
            View v = getConstraint().getChildAt(i);
            if (v instanceof ConstraintLayout) {
                return (ConstraintLayout) v;
            }
        }
        return null;
    }

    public void unlock() {
        getLockedContraint().setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
    }
}
