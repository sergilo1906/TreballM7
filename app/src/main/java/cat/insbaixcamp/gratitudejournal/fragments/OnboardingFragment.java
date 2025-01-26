package cat.insbaixcamp.gratitudejournal.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import cat.insbaixcamp.gratitudejournal.R;
import cat.insbaixcamp.gratitudejournal.adapters.OnboardingAdapter;
import cat.insbaixcamp.gratitudejournal.utils.FragmentUtils;
import cat.insbaixcamp.gratitudejournal.utils.InitUtils;

public class OnboardingFragment extends Fragment {

    private Button buttonFinish;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_onboarding, container, false);
        Context context = getContext();

        ViewPager2 viewPager = view.findViewById(R.id.viewPagerOnboarding);
        buttonFinish = view.findViewById(R.id.button_finish);

        OnboardingAdapter onboardingAdapter = new OnboardingAdapter(context);
        viewPager.setAdapter(onboardingAdapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (position == onboardingAdapter.getItemCount() - 1) {
                    buttonFinish.setVisibility(View.VISIBLE);
                } else {
                    buttonFinish.setVisibility(View.GONE);
                }
            }
        });

        buttonFinish.setOnClickListener(v -> {
            new InitUtils(context).setOnboardingAsCompleted();
            FragmentUtils.navigateTo(context, new LoginFragment(), true);
        });

        return view;
    }
}
