package cat.insbaixcamp.gratitudejournal.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import cat.insbaixcamp.gratitudejournal.MainActivity;
import cat.insbaixcamp.gratitudejournal.R;
import cat.insbaixcamp.gratitudejournal.models.ShopItem;
import cat.insbaixcamp.gratitudejournal.utils.UserUtils;

public class ShopFragment extends Fragment {

    ShopItem[] shopItems;
    UserUtils userUtils;

    public ShopFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);

        userUtils = new UserUtils(getContext());

        shopItems = new ShopItem[]{
            new ShopItem(view.findViewById(R.id.tvPriceShopItem1), 2, R.drawable.background_1, view.findViewById(R.id.clShopItem1), 0),
            new ShopItem(view.findViewById(R.id.tvPriceShopItem2), 4, R.drawable.background_2, view.findViewById(R.id.clShopItem2), 1),
            new ShopItem(view.findViewById(R.id.tvPriceShopItem3), 8, R.drawable.background_3, view.findViewById(R.id.clShopItem3), 2),
            new ShopItem(view.findViewById(R.id.tvPriceShopItem4), 15, R.drawable.background_4, view.findViewById(R.id.clShopItem4), 3),
            new ShopItem(view.findViewById(R.id.tvPriceShopItem5), 30, R.drawable.background_5, view.findViewById(R.id.clShopItem5), 4),
            new ShopItem(view.findViewById(R.id.tvPriceShopItem6), 60, R.drawable.background_6, view.findViewById(R.id.clShopItem6), 5)
        };

        for (int i = 0; i < 6; i++) {
            int position = i;
            shopItems[i].getConstraint().setOnClickListener(v -> {
                MainActivity.changeBackground(shopItems[position].getBackground());
                userUtils.updateUserAttribute("currentBackground", position, () -> {}, () -> {});
            });
        }

        fetchUnlockedBackgrounds();

        return view;
    }

    private void fetchUnlockedBackgrounds() {
        userUtils.getUserAttribute("backgrounds", new UserUtils.OnFetchCallback() {
            @Override
            public void onSuccess(Object dayStreak) {
                int num = ((Long)dayStreak).intValue();
                for (int i = 0; i < 6; i++) {
                    if (((num >> i) & 1) == 1) {
                        shopItems[i].unlock();
                    }
                }

            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getContext(), "Error fetching backgrounds: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
}
