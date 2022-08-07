package com.eamatracker.Seller;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eamatracker.API.RetrofitClient;
import com.eamatracker.Account.LaunchActivity;
import com.eamatracker.Models.DriverDetails;
import com.eamatracker.Models.UserDetails;
import com.eamatracker.R;
import com.eamatracker.Utils.InternetStatus;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AccountFragment extends Fragment {

    private static final String TAG = "AccountFragment";

    private CircleImageView profileImage;
    private TextView fullNameTv, usernameAccTv, emailAccTv, countryAccTv, dobAccTv,
            currencyInfoTv, genderAccTv, logOutTv;

    private String loginKey, lang = "en", type;

    private InternetStatus mInternetStatus;
    private Dialog alertDialog;
    private LinearLayout basicInfoShimmer;
    private MaterialCardView profileCardView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View accountView = inflater.inflate(R.layout.fragment_account, container, false);

        //Define views
        fullNameTv = accountView.findViewById(R.id.fullNameTv);
        profileImage = accountView.findViewById(R.id.profileImage);
        usernameAccTv = accountView.findViewById(R.id.usernameAccTv);
        emailAccTv = accountView.findViewById(R.id.emailAccTv);
        countryAccTv = accountView.findViewById(R.id.countryAccTv);
        dobAccTv = accountView.findViewById(R.id.dobAccTv);
        currencyInfoTv = accountView.findViewById(R.id.currencyInfoTv);
        genderAccTv = accountView.findViewById(R.id.genderAccTv);
        logOutTv = accountView.findViewById(R.id.logoutTv);
        basicInfoShimmer = accountView.findViewById(R.id.basicInfoShimmer);
        profileCardView = accountView.findViewById(R.id.basicInfoCard);

        alertDialog = new Dialog(requireContext());
        mInternetStatus = new InternetStatus(requireContext());

        retrieveUserToken();
        fetchType();

        if (loginKey == null || loginKey.isEmpty()) {

            sendUserToMainActivity();

        } else {

            if (type.equals("SELLER")) {

                fetchSellerDetails();

            } if (type.equals("DRIVER")) {

                fetchDriverDetails();
            }
        }


        logOutTv.setOnClickListener(view -> showLogoutDialog());

        return accountView;
    }

    private void showLogoutDialog() {

        // inflate the layout
        alertDialog.setContentView(R.layout.logout_dialog);
        Objects.requireNonNull(alertDialog.getWindow()).getAttributes().windowAnimations = R.style.AlertDialogAnimation;
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.setCanceledOnTouchOutside(false);

        // Define views
        MaterialButton cancelLogout = alertDialog.findViewById(R.id.cancelLogOut);
        MaterialButton yesLogout = alertDialog.findViewById(R.id.yesLogout);

        alertDialog.setCanceledOnTouchOutside(true);

        cancelLogout.setOnClickListener(v -> alertDialog.dismiss());

        yesLogout.setOnClickListener(v -> {

            alertDialog.dismiss();

            // Call log out API
            logOutCurrentUser();
        });

        alertDialog.show();
    }


    private void logOutCurrentUser() {

        // Inflate layout and define views
        alertDialog.setContentView(R.layout.loading_alert_dialog);

        Objects.requireNonNull(alertDialog.getWindow()).getAttributes().windowAnimations = R.style.AlertDialogAnimation;

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialog.setCanceledOnTouchOutside(false);

        ProgressBar loadingProgressBar = alertDialog.findViewById(R.id.loginProgress);

        Sprite doubleBounce = new DoubleBounce();
        loadingProgressBar.setIndeterminateDrawable(doubleBounce);

        alertDialog.show();

        // call the api
        Call<ResponseBody> logoutCall = RetrofitClient
                .getInstance()
                .getApiService()
                .logOutUser();

        logoutCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {

                if (response.isSuccessful() & response.code() == 200) {

                    // logout success
                    deleteUserToken();
                    sendUserToMainActivity();
                    alertDialog.dismiss();

                } else {

                    // Error
                    alertDialog.dismiss();
                    showErrorSnackBar(getString(R.string.error_occured));
                    Log.e(TAG, "Error logging out " + response.code());
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {

                Log.e(TAG, t.getMessage());
                showErrorSnackBar(getString(R.string.logout_failure));

            }
        });

    }

    private void retrieveUserToken() {

        // Retrieved stored user token
        SharedPreferences loginPrefs = this.requireActivity().getSharedPreferences("USER_KEYS", Context.MODE_PRIVATE);
        loginKey = loginPrefs.getString("USER_TOKEN", null);
        Log.d(TAG, "Login key = " + loginKey);

    }

    private void fetchSellerDetails() {

        // Driver / Seller details api

        Call<DriverDetails> userDetailsCall = RetrofitClient
                .getInstance()
                .getApiService()
                .driverDetails("Token " + loginKey);

        userDetailsCall.enqueue(new Callback<DriverDetails>() {
            @Override
            public void onResponse(@NonNull Call<DriverDetails> call, @NonNull Response<DriverDetails> response) {

                if (response.isSuccessful() && response.code() == 200) {

                    // Success


                    DriverDetails driverDetails = response.body();

                    if (driverDetails != null) {

                        populateUserDetails(driverDetails);

                    }

                } else {


                    // Error

                    showErrorSnackBar(getString(R.string.error_occured));
                    Log.e(TAG, response.code() + " " + response.body() + " " + response.errorBody());
                }

            }

            @Override
            public void onFailure(@NonNull Call<DriverDetails> call, @NonNull Throwable t) {

                Log.e(TAG, t.getMessage());

                if (mInternetStatus.isConnectingToInternet()) {

                    showErrorSnackBar(getString(R.string.error_occured));

                } else {

                    showErrorSnackBar(getString(R.string.no_internet));
                }

            }
        });

    }

    private void populateUserDetails(DriverDetails driverDetails) {


        // popultae views with data

        UserDetails userDetails = driverDetails.getUserDetails();

        usernameAccTv.setText(userDetails.getUsername());
        if (userDetails.getProfilePhoto() != null && !userDetails.getProfilePhoto().isEmpty()) {

            Glide.with(this)
                    .load(userDetails.getProfilePhoto())
                    .into(profileImage);

            String fullName = "";

            if (userDetails.getFirstName() != null && !userDetails.getFirstName().isEmpty()) {

                fullName += userDetails.getFirstName();
            }

            if (userDetails.getMiddleName() != null && !userDetails.getMiddleName().isEmpty()) {

                fullName += " " + userDetails.getMiddleName();

            }

            if (userDetails.getLastName() != null && !userDetails.getLastName().isEmpty()) {

                fullName += " " + userDetails.getLastName();

            }

            Log.d(TAG, fullName);

            if (fullName != null && !fullName.isEmpty()) {

                fullNameTv.setText(fullName);

            }

            if (userDetails.getCountry() != null && !userDetails.getCountry().isEmpty()) {

                countryAccTv.setText(userDetails.getCountry());

            }

            if (userDetails.getEmail() != null && !userDetails.getEmail().isEmpty()) {

                emailAccTv.setText(userDetails.getEmail());

            }

            if (userDetails.getAgeRange() != null && !userDetails.getAgeRange().isEmpty()) {

                dobAccTv.setText(userDetails.getAgeRange());

            }

            if (userDetails.getGender() != null && !userDetails.getGender().isEmpty()) {

                genderAccTv.setText(userDetails.getGender());

            }

            if (userDetails.getAgeRange() != null && !userDetails.getAgeRange().isEmpty()) {

                dobAccTv.setText(userDetails.getAgeRange());
            }

            basicInfoShimmer.setVisibility(View.GONE);
            profileCardView.setVisibility(View.VISIBLE);
        }
    }

    private void showErrorSnackBar(String message) {

        Snackbar errorSnackBarView = Snackbar
                .make(this.requireActivity().getWindow().getDecorView().getRootView(), message,
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

    public void deleteUserToken() {

        SharedPreferences preferences = this.requireActivity().getSharedPreferences("USER_KEYS", Context.MODE_PRIVATE);
        preferences.edit().remove("KEY").clear().apply();

        if (alertDialog != null) {

            alertDialog.dismiss();
        }
    }

    private void fetchDriverDetails() {

        Call<DriverDetails> userDetailsCall = RetrofitClient
                .getInstance()
                .getApiService()
                .driverDetails("Token " + loginKey);

        userDetailsCall.enqueue(new Callback<DriverDetails>() {
            @Override
            public void onResponse(@NonNull Call<DriverDetails> call, @NonNull Response<DriverDetails> response) {

                if (response.isSuccessful() && response.code() == 200) {


                    DriverDetails driverDetails = response.body();
                    UserDetails userDetails = driverDetails.getUserDetails();
                    Log.d(TAG, driverDetails.getUserDetails().getUsername());

                    populateUserDetails(driverDetails);


//                    if (userDetails.getProfilePhoto() != null && !userDetails.getProfilePhoto().isEmpty()) {
//
//                        Glide.with(requireContext()).load(userDetails.getProfilePhoto())
//                                .placeholder(R.drawable.profile_image)
//                                .into(profileImage);
//                    }
//
//                    String fullName = null;
//
//                    if (userDetails.getFirstName() != null && !userDetails.getFirstName().isEmpty()) {
//
//                        fullName += userDetails.getFirstName();
//                    }
//
//                    if (userDetails.getMiddleName() != null && !userDetails.getMiddleName().isEmpty()) {
//
//                        fullName += " " + userDetails.getMiddleName();
//                    }
//
//                    if (userDetails.getLastName() != null && !userDetails.getLastName().isEmpty()) {
//
//                        fullName += " " + userDetails.getLastName();
//                    }
//
//                    if (fullName != null && !fullName.isEmpty()) {
//
//                        fullNameTv.setText(fullName);
//                    }
//
//                    Log.d(TAG, "Full name : " + fullName);
//
//                    if (userDetails.getAgeRange() != null && !userDetails.getAgeRange().isEmpty()) {
//
//                        dobAccTv.setText(userDetails.getAgeRange());
//                    }
//
//                    if (userDetails.getEmail() != null && !userDetails.getEmail().isEmpty()) {
//
//                        emailAccTv.setText(userDetails.getEmail());
//                    }
//
//                    Log.d(TAG, "Email : " + userDetails.getEmail());
//
//                    if (userDetails.getGender() != null && !userDetails.getGender().isEmpty()) {
//
//                        genderAccTv.setText(userDetails.getGender());
//                    }
//
//                    Log.d(TAG, "Gender : " + userDetails.getGender());

                } else {

                    showErrorSnackBar(getString(R.string.error_occured));
                    Log.e(TAG, response.code() + " " + response.body() + " " + response.errorBody());
                }

            }

            @Override
            public void onFailure(@NonNull Call<DriverDetails> call, @NonNull Throwable t) {

                Log.e(TAG, t.getMessage());

                if (alertDialog.isShowing()) {

                    alertDialog.dismiss();
                }

                if (mInternetStatus.isConnectingToInternet()) {

                    showErrorSnackBar(getString(R.string.error_occured));

                } else {

                    showErrorSnackBar(getString(R.string.no_internet));
                }

            }
        });

    }

    private void fetchType() {

        SharedPreferences loginPrefs = requireActivity().getSharedPreferences("USER_KEYS", Context.MODE_PRIVATE);
        type = loginPrefs.getString("TYPE", "");
    }
}