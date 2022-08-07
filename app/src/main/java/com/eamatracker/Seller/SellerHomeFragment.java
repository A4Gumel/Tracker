package com.eamatracker.Seller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.eamatracker.Models.ProductList;
import com.eamatracker.Models.ProductRequestList;
import com.eamatracker.R;
import com.eamatracker.Seller.Adapters.ProductRequestListAdapter;
import com.eamatracker.Seller.Adapters.SellerProductsAdapter;
import com.eamatracker.Tracker.Adapter.HomeBannerAdapter;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.smarteist.autoimageslider.SliderView;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellerHomeFragment extends Fragment {

    private static final String TAG = "SellerHomeFragment";

    private SwipeRefreshLayout sellerHomeSwipeRefresh;
    private LinearLayout orderListShimmer;
    private SliderView homeImageSlider;
    private RecyclerView sellerRequestRv;
    private String loginKey, lang = "en";
    private MaterialCardView bannerCard, noRequestCard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View sellerHomeView = inflater.inflate(R.layout.fragment_seller_home, container, false);

        // Define views
        homeImageSlider = sellerHomeView.findViewById(R.id.homeImageSlider);
        sellerHomeSwipeRefresh = sellerHomeView.findViewById(R.id.sellerHomeSwipeRefresh);
        TextView sellerProductTv = sellerHomeView.findViewById(R.id.sellerProductTv);
        orderListShimmer = sellerHomeView.findViewById(R.id.orderListShimmer);
        sellerRequestRv = sellerHomeView.findViewById(R.id.sellerRequestRv);
        bannerCard = sellerHomeView.findViewById(R.id.bannerCard);
        noRequestCard = sellerHomeView.findViewById(R.id.noRequestCard);


        // Layout manager for RV
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        sellerRequestRv.setLayoutManager(linearLayoutManager);
        retrieveUserToken();

        if (loginKey != null && !loginKey.isEmpty()) {

            banners();
            fetchRequest();

        } else {

            sendUserToMainActivity();
        }

        sellerHomeSwipeRefresh.setOnRefreshListener(() -> {

            if (loginKey != null && !loginKey.isEmpty()) {

                banners();
                fetchRequest();
            }
        });



        return sellerHomeView;
    }

    private void fetchRequest() {


        // Seller product list API

        Call<ProductRequestList> productRequestListCall = RetrofitClient
                .getInstance()
                .getApiService()
                .productRequestList("Token " + loginKey);

        productRequestListCall.enqueue(new Callback<ProductRequestList>() {
            @Override
            public void onResponse(@NonNull Call<ProductRequestList> call, @NonNull Response<ProductRequestList> response) {

                sellerHomeSwipeRefresh.setRefreshing(false);

                if (response.isSuccessful() && response.code() == 200) {

                    ProductRequestList productRequestList = response.body();

                    if (Objects.requireNonNull(productRequestList).getCount() == 0) {

                        noRequestCard.setVisibility(View.VISIBLE);
                        orderListShimmer.setVisibility(View.GONE);

                    } else {

                        ProductRequestListAdapter requestListAdapter = new
                                ProductRequestListAdapter(requireContext(), productRequestList, loginKey);

                        sellerRequestRv.setAdapter(requestListAdapter);
                        noRequestCard.setVisibility(View.GONE);
                        orderListShimmer.setVisibility(View.GONE);
                        sellerRequestRv.setVisibility(View.VISIBLE);


                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<ProductRequestList> call, @NonNull Throwable t) {

                sellerHomeSwipeRefresh.setRefreshing(false);

                Log.e(TAG, t.getMessage());
                showErrorSnackBar(getString(R.string.error_occured));

            }
        });

    }


    private void retrieveUserToken() {

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
                .setBackgroundTint(requireContext().getColor(R.color.errorColor));
        errorSnackBarView
                .setActionTextColor(requireContext().getColor(R.color.colorAccent));

        errorSnackBarView.show();

    }

    private void banners() {


        // Home Top Banner / Carousel

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

                        bannerCard.setVisibility(View.GONE);

                    } else {

                        homeImageSlider.setSliderAdapter(new HomeBannerAdapter(requireActivity(), mBannerList));
                    }

                } else {

                    Log.e(TAG, "Banners cannot be fetched " +response.code() );
                }

            }

            @Override
            public void onFailure(@NotNull Call<BannerList> call, @NotNull Throwable t) {

                Log.e(TAG, Objects.requireNonNull(t.getMessage()));

            }
        });
    }


    private void sendUserToMainActivity() {

        // finish acticity
        Intent mainIntent = new Intent(requireContext(), LaunchActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        requireActivity().supportFinishAfterTransition();

    }
}