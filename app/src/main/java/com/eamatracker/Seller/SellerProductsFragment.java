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
import com.eamatracker.Models.ProductList;
import com.eamatracker.R;
import com.eamatracker.Seller.Adapters.SellerProductsAdapter;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellerProductsFragment extends Fragment {

    private static final String TAG = "SellerRequestFragment";

    private SwipeRefreshLayout sellerProductSwipe;
    private TextView urProductsTv;
    private RecyclerView urProductsRv;
    private String loginKey, lang = "en";
    private LinearLayout myProductsShimmer;
    private MaterialCardView noProductCard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View sellerRequestView = inflater.inflate(R.layout.fragment_seller_products, container, false);

        sellerProductSwipe = sellerRequestView.findViewById(R.id.sellerProductSwipe);
        urProductsTv = sellerRequestView.findViewById(R.id.urProductsTv);
        myProductsShimmer = sellerRequestView.findViewById(R.id.orderListShimmer);
        noProductCard = sellerRequestView.findViewById(R.id.noOrdersLayout);
        urProductsRv = sellerRequestView.findViewById(R.id.sellerProductRv);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        urProductsRv.setLayoutManager(linearLayoutManager);
        retrieveUserToken();

        if (loginKey == null || loginKey.isEmpty()) {

            showErrorSnackBar(getString(R.string.you_need_login_err));
            sendUserToMainActivity();

        } else {

            fetchMyProducts();
        }

        sellerProductSwipe.setOnRefreshListener(() -> {

            if (loginKey != null && !loginKey.isEmpty()) {

                fetchMyProducts();
            }
            sellerProductSwipe.setRefreshing(true);

        });

        return sellerRequestView;
    }


    private void fetchMyProducts() {

        Call<ProductList> myProductsCall = RetrofitClient
                .getInstance()
                .getApiService()
                .sellerProducts("Token " + loginKey);

        myProductsCall.enqueue(new Callback<ProductList>() {
            @Override
            public void onResponse(@NonNull Call<ProductList> call, @NonNull Response<ProductList> response) {


                sellerProductSwipe.setRefreshing(false);
                if (response.isSuccessful() && response.code() == 200) {

                    ProductList productList = response.body();

                    assert productList != null;

                    if (productList.getCount() == 0) {

                        showErrorSnackBar(getString(R.string.seller_no_product_err));
                        myProductsShimmer.setVisibility(View.GONE);
                        noProductCard.setVisibility(View.VISIBLE);


                    } else if (productList.getCount() > 0) {

                        SellerProductsAdapter sellerProductsAdapter = new SellerProductsAdapter(requireContext(), productList, loginKey);
                        urProductsRv.setAdapter(sellerProductsAdapter);
                        myProductsShimmer.setVisibility(View.GONE);
                        urProductsRv.setVisibility(View.VISIBLE);

                    } else {

                        showErrorSnackBar(getString(R.string.error_occured));
                    }
                } else {

                    showErrorSnackBar(getString(R.string.error_occured));
                }

            }

            @Override
            public void onFailure(@NonNull Call<ProductList> call, @NonNull Throwable t) {

                sellerProductSwipe.setRefreshing(false);
                showErrorSnackBar(getString(R.string.error_occured));
                Log.d(TAG, t.getMessage());

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
                .setBackgroundTint(requireContext().getColor(R.color.design_default_color_error));
        errorSnackBarView
                .setActionTextColor(requireContext().getColor(R.color.colorAccent));

        errorSnackBarView.show();

    }

    private void sendUserToMainActivity() {

        Intent mainIntent = new Intent(requireContext(), LaunchActivity.class);

        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        requireActivity().supportFinishAfterTransition();

    }
}