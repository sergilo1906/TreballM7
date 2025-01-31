package cat.insbaixcamp.gratitudejournal.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import cat.insbaixcamp.gratitudejournal.MainActivity;
import cat.insbaixcamp.gratitudejournal.R;
import cat.insbaixcamp.gratitudejournal.models.ShopAvatarItem;
import cat.insbaixcamp.gratitudejournal.models.ShopItem;
import cat.insbaixcamp.gratitudejournal.utils.UserUtils;

public class ShopAvatarsFragment extends Fragment {

    ShopAvatarItem[] shopItems;
    UserUtils userUtils;

    public ShopAvatarsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_avatars, container, false);

        userUtils = new UserUtils(getContext());

        shopItems = new ShopAvatarItem[]{
            new ShopAvatarItem(view.findViewById(R.id.tvPriceShopItem1), 0, R.drawable.profile_1, view.findViewById(R.id.clShopItem1), 0),
            new ShopAvatarItem(view.findViewById(R.id.tvPriceShopItem2), 0, R.drawable.profile_2, view.findViewById(R.id.clShopItem2), 1),
            new ShopAvatarItem(view.findViewById(R.id.tvPriceShopItem3), 5, R.drawable.profile_3, view.findViewById(R.id.clShopItem3), 2),
            new ShopAvatarItem(view.findViewById(R.id.tvPriceShopItem4), 5, R.drawable.profile_4, view.findViewById(R.id.clShopItem4), 3),
            new ShopAvatarItem(view.findViewById(R.id.tvPriceShopItem5), 5, R.drawable.profile_5, view.findViewById(R.id.clShopItem5), 4),
            new ShopAvatarItem(view.findViewById(R.id.tvPriceShopItem6), 5, R.drawable.profile_6, view.findViewById(R.id.clShopItem6), 5),
            new ShopAvatarItem(view.findViewById(R.id.tvPriceShopItem7), 5, R.drawable.profile_7, view.findViewById(R.id.clShopItem7), 6),
            new ShopAvatarItem(view.findViewById(R.id.tvPriceShopItem8), 5, R.drawable.profile_8, view.findViewById(R.id.clShopItem8), 7)
        };

        for (int i = 0; i < 8; i++) {
            int position = i;
            shopItems[i].getConstraint().setOnClickListener(v -> {
                userUtils.updateUserAttribute("currentAvatar", position, () -> {}, () -> {});
            });
        }

        fetchUnlockedAvatars();

        return view;
    }

    private void fetchUnlockedAvatars() {
        userUtils.getUserAttribute("avatars", new UserUtils.OnFetchCallback() {
            @Override
            public void onSuccess(Object dayStreak) {
                int num = ((Long)dayStreak).intValue();
                for (int i = 0; i < 8; i++) {
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
