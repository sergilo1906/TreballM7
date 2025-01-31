package cat.insbaixcamp.gratitudejournal.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import cat.insbaixcamp.gratitudejournal.R;
import cat.insbaixcamp.gratitudejournal.utils.FragmentUtils;
import cat.insbaixcamp.gratitudejournal.utils.UserUtils;

public class UserFragment extends Fragment {

    Integer currentPoints = null;

    UserUtils userUtils;

    TextView tvUserPoints, tvDayStreak;
    Button btShop;
    ImageView ivShopAvatar, ivAvatar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        this.userUtils = new UserUtils(getContext());

        tvUserPoints = view.findViewById(R.id.tv_user_points);
        tvDayStreak = view.findViewById(R.id.tv_day_streak);
        btShop = view.findViewById(R.id.bt_shop);
        ivAvatar = view.findViewById(R.id.iv_avatar);
        ivShopAvatar = view.findViewById(R.id.iv_shop_avatar);

        btShop.setOnClickListener(v -> {
            FragmentUtils.navigateTo(getContext(), new ShopFragment(), false);
        });
        ivShopAvatar.setOnClickListener(v -> {
            FragmentUtils.navigateTo(getContext(), new ShopAvatarsFragment(), false);
        });

        fetchUserAvatar();
        fetchUserPoints();
        fetchUserDayStreak();

        return view;
    }

    private void fetchUserAvatar() {
        userUtils.getUserAttribute("currentAvatar", new UserUtils.OnFetchCallback() {
            @Override
            public void onSuccess(Object dayStreak) {
                Integer[] avatarsRes = new Integer[]{
                        R.drawable.profile_1,
                        R.drawable.profile_2,
                        R.drawable.profile_3,
                        R.drawable.profile_4,
                        R.drawable.profile_5,
                        R.drawable.profile_6,
                        R.drawable.profile_7,
                        R.drawable.profile_8
                };
                ivAvatar.setImageDrawable(getContext().getDrawable(avatarsRes[((Long) dayStreak).intValue()]));
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getContext(), "Error fetching current avatar: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fetchUserPoints() {
        userUtils.getUserPoints(new UserUtils.OnFetchCallback() {
            @Override
            public void onSuccess(Object points) {
                currentPoints = (Integer) points;
                String formattedPoints = getContext().getString(R.string.current_points, (Integer) points);
                tvUserPoints.setText(formattedPoints);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getContext(), "Error fetching points: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fetchUserDayStreak() {
        userUtils.getUserAttribute("dayStreak", new UserUtils.OnFetchCallback() {
            @Override
            public void onSuccess(Object dayStreak) {
                String formattedDayStreak = getContext().getString(R.string.day_streak, ((Long) dayStreak).intValue());
                tvDayStreak.setText(formattedDayStreak);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getContext(), "Error fetching day streak: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
}
