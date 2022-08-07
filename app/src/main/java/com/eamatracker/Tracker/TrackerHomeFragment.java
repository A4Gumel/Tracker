package com.eamatracker.Tracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eamatracker.API.RetrofitClient;
import com.eamatracker.Account.LaunchActivity;
import com.eamatracker.Models.BannerList;
import com.eamatracker.R;
import com.eamatracker.Tracker.Adapter.HomeBannerAdapter;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.smarteist.autoimageslider.SliderView;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackerHomeFragment extends Fragment {

    private static final String TAG = "TrackerHomeFragment";

    private SwipeRefreshLayout trackerHomeSwipe;
    private TextView drivingRequestTv;
    private RecyclerView drivingRequestRv;
    private String loginKey, lang = "en";
    private LinearLayout drivingRequestShimmer;
    private SliderView homeImageSlider;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View sellerHomeView = inflater.inflate(R.layout.fragment_tracker_home, container, false);
        trackerHomeSwipe = sellerHomeView.findViewById(R.id.requestSwipe);
        drivingRequestTv = sellerHomeView.findViewById(R.id.trackingRequestTv);
        drivingRequestRv = sellerHomeView.findViewById(R.id.trackingRequestRv);
        drivingRequestShimmer = sellerHomeView.findViewById(R.id.orderListShimmer);
        homeImageSlider = sellerHomeView.findViewById(R.id.homeImageSlider);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        drivingRequestRv.setLayoutManager(linearLayoutManager);

        retrieveUserToken();


        if (loginKey == null) {

            showErrorSnackBar(getString(R.string.you_need_login_err));
            sendUserToMainActivity();

        } else {

            fetchDrivingRequests();
            banners();
        }

        trackerHomeSwipe.setOnRefreshListener(() -> {

            if (loginKey != null) {

                banners();
            }
            trackerHomeSwipe.setRefreshing(true);

        });

        return sellerHomeView;
    }

    private void fetchDrivingRequests() {


    }

    public void retrieveUserToken() {

        SharedPreferences loginPrefs = this.requireActivity().getSharedPreferences("USER_KEYS", Context.MODE_PRIVATE);
        loginKey = loginPrefs.getString("USER_TOKEN", null);
        Log.d(TAG, "Login key = " + loginKey);

    }

    private void showErrorSnackBar(String message) {

        Snackbar errorSnackBarView = Snackbar
                .make(requireView(), message,
                        Snackbar.LENGTH_LONG);
        errorSnackBarView
                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
        errorSnackBarView
                .setActionTextColor(requireContext().getColor(R.color.textColor));
        errorSnackBarView
                .setBackgroundTint(requireContext().getColor(R.color.design_default_color_error));
        errorSnackBarView
                .setActionTextColor(requireContext().getColor(R.color.colorAccent));

        errorSnackBarView.show();

    }

    private void banners() {

        Call<BannerList> bannerListCall = RetrofitClient
                .getInstance()
                .getApiService()
                .banners();

        bannerListCall.enqueue(new Callback<BannerList>() {
            @Override
            public void onResponse(@NotNull Call<BannerList> call, @NotNull Response<BannerList> response) {

                if (response.isSuccessful() && response.code() == 200) {

                    assert response.body() != null;

                    BannerList mBannerList = response.body();

                    Log.d(TAG, "Banners called");

                    if (mBannerList.getCount() == 0) {

                        homeImageSlider.setVisibility(View.GONE);

                    } else {

                        homeImageSlider.setSliderAdapter(new HomeBannerAdapter(requireActivity(), mBannerList));
                    }

                }

            }

            @Override
            public void onFailure(@NotNull Call<BannerList> call, @NotNull Throwable t) {

                Log.e(TAG, Objects.requireNonNull(t.getMessage()));

            }
        });
    }

    private void sendUserToMainActivity() {

        Intent mainIntent = new Intent(requireContext(), LaunchActivity.class);

        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        requireActivity().supportFinishAfterTransition();

    }
}