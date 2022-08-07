package com.eamatracker.Seller;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.eamatracker.API.RetrofitClient;
import com.eamatracker.Account.LaunchActivity;
import com.eamatracker.HelpCenter.HelpCenterActivity;
import com.eamatracker.HelpCenter.LiveChatActivity;
import com.eamatracker.R;
import com.eamatracker.Utils.WebActivity;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.nihaskalam.progressbuttonlibrary.CircularProgressButton;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellerMainActivity extends AppCompatActivity {

    private static final String TAG = "SellerMainActivity";

    private ChipNavigationBar sellerBottomNav;
    private Fragment sellerFragment = null;
    private DrawerLayout drawerLayout;
    private BottomSheetDialog mBottomSheetDialog;
    private Dialog alertDialog;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_main);

        sellerBottomNav = findViewById(R.id.sellerBottomNav);

        //  Set Seller fragments
        sellerBottomNav.setItemSelected(R.id.sellerHome, true);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.sellerFragmentContainer,
                        new SellerHomeFragment())
                .commit();

        MaterialToolbar topAppBar = findViewById(R.id.sellerAppBar);

        NavigationView navigationView = findViewById(R.id.sellerNavView);

        drawerLayout = findViewById(R.id.sellerDrawerLayout);

        alertDialog = new Dialog(this);

        //Setup navDrawer layout
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                topAppBar,
                R.string.openNavDrawer,
                R.string.closeNavDrawer
        );

        //set color for naveDrawer icon
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getColor(R.color.colorSecondary));
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        //item selected listener for navDrawer
        navigationView.setNavigationItemSelectedListener(item -> {

            drawerLayout.closeDrawer(GravityCompat.START, true);
            if (item.getItemId() == R.id.navHelpCenter){

                startActivity(new Intent(this, HelpCenterActivity.class));

            } else if (item.getItemId() == R.id.navLiveChat){

                startActivity(new Intent(this, LiveChatActivity.class));

            } else if (item.getItemId() == R.id.navFacebook){
                sendUserToSocialMediaPage("https://facebook.com/a4gumel");
            } else if (item.getItemId() == R.id.navTwitter){
                sendUserToSocialMediaPage("https://twitter.com/a4gumel/");
            } else if (item.getItemId() == R.id.navLinkedIn){
                sendUserToSocialMediaPage("https://www.linkedin.com/in/a4gumel/");
            } else if (item.getItemId() == R.id.nav_open_source){

                showOpenSourceLicenses();

            } else if (item.getItemId() == R.id.nav_logout){

                showLogoutDialog();

            }
            return false;
        });

        //set toolbar elevation
        topAppBar.setElevation(8);


        sellerBottomNav.setOnItemSelectedListener(i -> {

            switch (i) {

                case R.id.sellerHome:

                    sellerFragment = new SellerHomeFragment();

                    break;

                case R.id.sellerRequest:
                    sellerFragment = new SellerProductsFragment();
                    break;

                case R.id.sellerSettings:
                    sellerFragment = new AccountFragment();
                    break;
            }

            if (sellerFragment != null) {

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.sellerFragmentContainer, sellerFragment)
                        .commit();
            }

        });

    }


    private void showDarkModeSheet(){

        mBottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetStyle);

        View darkModeSheet = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dark_mode_layout,
                (ViewGroup) findViewById(R.id.darkModeSheet));


        RadioButton onDarkMode = darkModeSheet.findViewById(R.id.darkModeOn);
        RadioButton offDarkMode = darkModeSheet.findViewById(R.id.darkModeOff);
        RadioButton followSysDarkMode = darkModeSheet.findViewById(R.id.followMode);
        RadioButton autoTimeDarkMode = darkModeSheet.findViewById(R.id.autoTime);
        RadioButton batterySaverDarkMode = darkModeSheet.findViewById(R.id.autoBattery);
        TextView onDarkModeTv = darkModeSheet.findViewById(R.id.onDarkModeTv);
        TextView offDarkModeTv = darkModeSheet.findViewById(R.id.offDarkModeTv);
        TextView followSysModeTv = darkModeSheet.findViewById(R.id.followModeTv);
        TextView autoTimeModeTv = darkModeSheet.findViewById(R.id.autoTimeTv);
        TextView batterySaverModeTv = darkModeSheet.findViewById(R.id.autoBatteryTv);
        TextView darkModeTitle = darkModeSheet.findViewById(R.id.darkModeTitle);

        onDarkModeTv.setTextColor(getColor(R.color.textColor));
        offDarkModeTv.setTextColor(getColor(R.color.textColor));
        followSysModeTv.setTextColor(getColor(R.color.textColor));
        autoTimeModeTv.setTextColor(getColor(R.color.textColor));
        batterySaverModeTv.setTextColor(getColor(R.color.textColor));
        darkModeTitle.setTextColor(getColor(R.color.colorSecondary));

        SharedPreferences remMePrefs = getSharedPreferences("STORED_PREFS", MODE_PRIVATE);
        String retThemeName = remMePrefs.getString("MODE", "light");


        onDarkModeTv.setTextColor(getColor(R.color.textColor));
        offDarkModeTv.setTextColor(getColor(R.color.textColor));
        followSysModeTv.setTextColor(getColor(R.color.textColor));
        autoTimeModeTv.setTextColor(getColor(R.color.textColor));
        batterySaverModeTv.setTextColor(getColor(R.color.textColor));
        darkModeTitle.setTextColor(getColor(R.color.colorSecondary));

        switch (retThemeName) {

            case "dark":

                offDarkMode.setChecked(true);

                break;

            case "follow":

                followSysDarkMode.setChecked(true);

                break;

            case "time":

                autoTimeDarkMode.setChecked(true);

                break;

            case "batterySaver":

                batterySaverDarkMode.setChecked(true);

                break;

            default:

                onDarkMode.setChecked(true);

                break;
        }


        onDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked){

                mBottomSheetDialog.setDismissWithAnimation(true);

                AppCompatDelegate
                        .setDefaultNightMode(
                                AppCompatDelegate
                                        .MODE_NIGHT_NO);

                saveThemeMode("light");
                mBottomSheetDialog.setDismissWithAnimation(true);
                mBottomSheetDialog.dismiss();
            }

        });

        offDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked){

                mBottomSheetDialog.setDismissWithAnimation(true);

                AppCompatDelegate
                        .setDefaultNightMode(
                                AppCompatDelegate
                                        .MODE_NIGHT_YES);

                saveThemeMode("dark");
                mBottomSheetDialog.setDismissWithAnimation(true);
                mBottomSheetDialog.dismiss();
            }

        });

        followSysDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked){

                mBottomSheetDialog.setDismissWithAnimation(true);


                AppCompatDelegate
                        .setDefaultNightMode(
                                AppCompatDelegate
                                        .MODE_NIGHT_FOLLOW_SYSTEM);

                saveThemeMode("follow");
                mBottomSheetDialog.setDismissWithAnimation(true);
                mBottomSheetDialog.dismiss();
            }

        });

        autoTimeDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked){

                mBottomSheetDialog.setDismissWithAnimation(true);


                AppCompatDelegate
                        .setDefaultNightMode(
                                AppCompatDelegate
                                        .MODE_NIGHT_AUTO_TIME);

                saveThemeMode("time");
                mBottomSheetDialog.setDismissWithAnimation(true);
                mBottomSheetDialog.dismiss();
            }

        });

        batterySaverDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked){

                mBottomSheetDialog.setDismissWithAnimation(true);


                AppCompatDelegate
                        .setDefaultNightMode(
                                AppCompatDelegate
                                        .MODE_NIGHT_AUTO_BATTERY);

                saveThemeMode("batterySaver");
                mBottomSheetDialog.setDismissWithAnimation(true);
                mBottomSheetDialog.dismiss();
            }

        });


        mBottomSheetDialog.setContentView(darkModeSheet);
        mBottomSheetDialog.setDismissWithAnimation(true);
        drawerLayout.closeDrawer(GravityCompat.START, true);

        mBottomSheetDialog.show();

    }

    private void saveThemeMode(String themeName) {
        SharedPreferences preferences = getSharedPreferences("STORED_PREFS", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("MODE", themeName);
        editor.apply();
    }

    private void  sendUserToSocialMediaPage(String link){
        //Launch an Intent to social media handles
        Uri uri = Uri.parse(link);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void testInternetCall() {


        Call<ResponseBody> testInternetRtfCall = RetrofitClient
                .getInstance()
                .getApiService()
                .testInternet(1);

        testInternetRtfCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {

                if (response.isSuccessful() && response.code() == 200) {

                    if (alertDialog != null) {

                        alertDialog.dismiss();
                    }

                } else {

                    inactiveInternetAlert();

                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {

                inactiveInternetAlert();

            }
        });

    }

    private void inactiveInternetAlert() {

        alertDialog.setContentView(R.layout.inactive_internet_alert);
        Objects.requireNonNull(alertDialog.getWindow()).getAttributes().windowAnimations = R.style.AlertDialogAnimation;
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.setCanceledOnTouchOutside(false);

        LottieAnimationView noInternetAnim = alertDialog.findViewById(R.id.noInternetAnim);
        CircularProgressButton retryButton = alertDialog.findViewById(R.id.retryBtn);

        noInternetAnim.setAnimation(R.raw.no_internet_connection);

        retryButton.setOnClickListener(v -> {
            retryButton.showProgress();
            testInternetCall();
        });

        alertDialog.show();


    }

    private void showLogoutDialog() {

        alertDialog.setContentView(R.layout.logout_dialog);
        Objects.requireNonNull(alertDialog.getWindow()).getAttributes().windowAnimations = R.style.AlertDialogAnimation;
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.setCanceledOnTouchOutside(false);

        MaterialButton cancelLogout = alertDialog.findViewById(R.id.cancelLogOut);
        MaterialButton yesLogout = alertDialog.findViewById(R.id.yesLogout);

        alertDialog.setCanceledOnTouchOutside(true);

        cancelLogout.setOnClickListener(v -> dismissLogoutDialog());

        yesLogout.setOnClickListener(v -> {

            alertDialog.dismiss();
            logOutCurrentUser();
        });

        alertDialog.show();
    }

    private void logOutCurrentUser() {

        alertDialog.setContentView(R.layout.loading_alert_dialog);

        Objects.requireNonNull(alertDialog.getWindow()).getAttributes().windowAnimations = R.style.AlertDialogAnimation;

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alertDialog.setCanceledOnTouchOutside(false);

        ProgressBar loadingProgressBar = alertDialog.findViewById(R.id.loginProgress);

        Sprite doubleBounce = new DoubleBounce();
        loadingProgressBar.setIndeterminateDrawable(doubleBounce);

        alertDialog.show();

        Call<ResponseBody> logoutCall = RetrofitClient
                .getInstance()
                .getApiService()
                .logOutUser();

        logoutCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {

                if (response.isSuccessful() & response.code() == 200) {

                    deleteUserToken();
                    sendUserToMainActivity();
                    alertDialog.dismiss();

                } else {

                    alertDialog.dismiss();
                    showErrorSnackBar(getString(R.string.error_occured));
                    Log.d(TAG, "Erroro logginn out " + response.code() + response.errorBody());
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {

                showErrorSnackBar(getString(R.string.logout_failure));
                Log.e(TAG, "Error logging out " + t.getMessage());

            }
        });

    }

    private void sendBugReportEmail() {
        //Email intent to send feedback to EAMA Customer service team
        Intent bugIntent = new Intent(Intent.ACTION_SENDTO,
                Uri.fromParts("mailto", "a4auwal@gmail.com",null));
        bugIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.report_bug));
        startActivity(Intent.createChooser(bugIntent, getString(R.string.report_bug)));
    }

    private void dismissLogoutDialog() {

        if (alertDialog != null) {

            alertDialog.dismiss();
        }
    }

    private void deleteUserToken() {

        SharedPreferences preferences = getSharedPreferences("USER_KEYS", Context.MODE_PRIVATE);
        preferences.edit().remove("KEY").clear().apply();

        if (alertDialog != null) {

            alertDialog.dismiss();
        }
    }

    private void showErrorSnackBar(String message) {

        Snackbar errorSnackBarView = Snackbar
                .make(getWindow().getDecorView().getRootView(), message,
                        Snackbar.LENGTH_LONG);
        errorSnackBarView
                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
        errorSnackBarView
                .setActionTextColor(getColor(R.color.textColor));
        errorSnackBarView
                .setBackgroundTint(getColor(R.color.design_default_color_error));
        errorSnackBarView
                .setActionTextColor(getColor(R.color.colorAccent));

        errorSnackBarView.show();

    }

    private void sendUserToMainActivity() {

        Intent mainIntent = new Intent(this, LaunchActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        supportFinishAfterTransition();

    }


    private void showOpenSourceLicenses() {

        OssLicensesMenuActivity.setActivityTitle(getString(R.string.custom_license_title));
        startActivity(new Intent(this,
                OssLicensesMenuActivity.class));

    }


}