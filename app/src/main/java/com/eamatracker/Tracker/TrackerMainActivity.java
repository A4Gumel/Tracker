package com.eamatracker.Tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import com.bumptech.glide.Glide;
import com.eamatracker.API.RetrofitClient;
import com.eamatracker.Account.LaunchActivity;
import com.eamatracker.HelpCenter.HelpCenterActivity;
import com.eamatracker.HelpCenter.LiveChatActivity;
import com.eamatracker.Models.DriverDetails;
import com.eamatracker.Models.UserDetails;
import com.eamatracker.R;
import com.eamatracker.Seller.AccountFragment;
import com.eamatracker.Utils.InternetStatus;
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
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener;
import com.nihaskalam.progressbuttonlibrary.CircularProgressButton;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackerMainActivity extends AppCompatActivity {

    private static final String TAG = "TrackerMainActivity";

    private ChipNavigationBar driverBottomNav;
    private Fragment driverFragment = null;
    private DrawerLayout drawerLayout;
    private BottomSheetDialog mBottomSheetDialog;
    private Dialog alertDialog;
    private CircleImageView driverProfileImage;
    private TextView navUsername, navPhoneNumber;
    private SwitchMaterial driverActive;

    private String loginKey, lang = "en";

    private InternetStatus mInternetStatus;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker_main);

        alertDialog = new Dialog(this);

        testInternetCall();
        retrieveUserToken();

        if (loginKey != null) {

            fetchUserDetails();
        }

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            checkPermissions();
        }
        mInternetStatus = new InternetStatus(this);
        driverBottomNav = findViewById(R.id.driverBottomNav);


        driverBottomNav.setItemSelected(R.id.driverHomeMap, true);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.driverFragmentContainer,
                        new DriverMapFragment())
                .commit();

        MaterialToolbar topAppBar = findViewById(R.id.driverAppBar);

        NavigationView navigationView = findViewById(R.id.driverNavView);

        View headerView  = navigationView.getHeaderView(0);

        driverProfileImage = headerView.findViewById(R.id.profileImage);
        navUsername = headerView.findViewById(R.id.navUsername);
        navPhoneNumber = headerView.findViewById(R.id.navPhoneNumber);
        driverActive = headerView.findViewById(R.id.navActive);

        drawerLayout = findViewById(R.id.driverDrawerLayout);

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
            }
            else if (item.getItemId() == R.id.navTwitter){
                sendUserToSocialMediaPage("https://twitter.com/a4gumel/");
            }else if (item.getItemId() == R.id.navLinkedIn){
                sendUserToSocialMediaPage("https://www.linkedin.com/in/a4gumel/");
            } else if (item.getItemId() == R.id.nav_report_bug){

                sendBugReportEmail();


            } else if (item.getItemId() == R.id.nav_open_source){

                showOpenSourceLicenses();

            } else if (item.getItemId() == R.id.nav_logout){

                showLogoutDialog();

            }
            return false;
        });


        //set toolbar elevation
        topAppBar.setElevation(8);

        driverBottomNav.setOnItemSelectedListener(i -> {

            switch (i) {

                case R.id.driverHomeMap:

                    driverFragment = new DriverMapFragment();

                    break;

                case R.id.driverHome:

                    driverFragment = new TrackerHomeFragment();

                    break;

                case R.id.driverWallet:

                    driverFragment = new WalletFragment();
                    break;

                case R.id.driverAccount:

                    driverFragment = new AccountFragment();
                    break;
            }

            if (driverFragment != null) {

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.driverFragmentContainer, driverFragment)
                        .commit();
            }

        });
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

    private void fetchUserDetails() {

        Call<DriverDetails> userDetailsCall = RetrofitClient
                .getInstance()
                .getApiService()
                .driverDetails("Token " + loginKey);

        userDetailsCall.enqueue(new Callback<DriverDetails>() {
            @Override
            public void onResponse(@NonNull Call<DriverDetails> call, @NonNull Response<DriverDetails> response) {

                if (response.isSuccessful() && response.code() == 200) {


                    DriverDetails driverDetails = response.body();

                    if (driverDetails != null) {

                        populateUserDetails(driverDetails);
                        saveDriverId(driverDetails.getId());

                    }

                } else {

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

    private void saveDriverId(Integer id) {

        SharedPreferences preferences = getSharedPreferences("USER_KEYS", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("DRIVER_ID", id);
        editor.apply();
    }

    private void populateUserDetails(DriverDetails driverDetails) {

        UserDetails userDetails = driverDetails.getUserDetails();

        navUsername.setText(userDetails.getUsername());

        if (driverDetails.getPhoneNumber() != null && !driverDetails.getPhoneNumber().isEmpty()) {

            navPhoneNumber.setText(driverDetails.getPhoneNumber());

        }


        if (userDetails.getProfilePhoto() != null && !userDetails.getProfilePhoto().isEmpty()) {

            Glide.with(this)
                    .load(userDetails.getProfilePhoto())
                    .into(driverProfileImage);
        }
    }

    private void retrieveUserToken() {

        SharedPreferences loginPrefs = getSharedPreferences("USER_KEYS", Context.MODE_PRIVATE);
        loginKey = loginPrefs.getString("USER_TOKEN", null);
        Log.d(TAG, "Login key = " + loginKey);

    }

    private void showErrorSnackBar(String message) {

        Snackbar errorSnackBarView = Snackbar
                .make(this.getWindow().getDecorView().getRootView(), message,
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

    @Override
    protected void onStart() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            checkPermissions();
        }
        super.onStart();
    }

    @Override
    protected void onResume() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            checkPermissions();
        }
        super.onResume();
    }

    @Override
    protected void onRestart() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            checkPermissions();
        }
        super.onRestart();
    }

    private void checkPermissions() {

        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.RECORD_AUDIO
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {

                if (!report.areAllPermissionsGranted()) {

                    MultiplePermissionsListener snackbarMultiplePermissionsListener =
                            SnackbarOnAnyDeniedMultiplePermissionsListener.Builder
                                    .with(getWindow().getDecorView().getRootView(), "Location is required")
                                    .withOpenSettingsButton("Settings")
                                    .withCallback(new Snackbar.Callback() {
                                        @Override
                                        public void onShown(Snackbar snackbar) {
                                            // Event handler for when the given Snackbar is visible
                                        }
                                        @Override
                                        public void onDismissed(Snackbar snackbar, int event) {
                                            // Event handler for when the given Snackbar has been dismissed
                                            supportFinishAfterTransition();
                                        }
                                    })
                                    .build();
                }

            }
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions,
                                                                     PermissionToken token) {

            }
        }).check();


    }

    private void sendBugReportEmail() {
        //Email intent to send feedback to EAMA Customer service team
        Intent bugIntent = new Intent(Intent.ACTION_SENDTO,
                Uri.fromParts("mailto", "gumel.developer@gmail.com",null));
        bugIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.report_bug));
        startActivity(Intent.createChooser(bugIntent, getString(R.string.report_bug)));
    }

    private void dismissLogoutDialog() {

        if (alertDialog != null) {

            alertDialog.dismiss();
        }
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
                    Log.d(TAG, "Error logging out " + response.code() + response.errorBody());
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {

                showErrorSnackBar(getString(R.string.logout_failure));
                Log.e(TAG, "Error logging out " + t.getMessage());

            }
        });

    }

    private void deleteUserToken() {

        SharedPreferences preferences = getSharedPreferences("USER_KEYS", Context.MODE_PRIVATE);
        preferences.edit().remove("KEY").clear().apply();

        if (alertDialog != null) {

            alertDialog.dismiss();
        }
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