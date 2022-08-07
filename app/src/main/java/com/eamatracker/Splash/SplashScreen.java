package com.eamatracker.Splash;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.airbnb.lottie.LottieAnimationView;
import com.eamatracker.API.RetrofitClient;
import com.eamatracker.Account.LaunchActivity;
import com.eamatracker.Models.Message;
import com.eamatracker.R;
import com.eamatracker.Seller.SellerMainActivity;
import com.eamatracker.Service.MyFirebaseMessagingService;
import com.eamatracker.Tracker.TrackerMainActivity;
import com.eamatracker.Utils.InternetStatus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.nihaskalam.progressbuttonlibrary.CircularProgressButton;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.functions.Action;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {

    private static final String TAG = "SplashScreen";

    private String loginKey, type;
    private Dialog alertDialog;
    private static final int TIME_LIMIT = 1500;
    private static long backPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen);
        alertDialog = new Dialog(this);

        testInternetCall();
    }

    private void testInternetCall() {


        // Test for active internet / server connectio

        Call<ResponseBody> testInternetRtfCall = RetrofitClient
                .getInstance()
                .getApiService()
                .testInternet(1);

        testInternetRtfCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {

                if (response.isSuccessful() && response.code() == 200) {

                    retrieveUserToken();
                    fetchType();

                    if (type != null && !type.isEmpty()) {

                        if (type.equals("SELLER") && loginKey != null) {

                            fetchFCMToken();
                            Intent intent = new Intent(SplashScreen.this, SellerMainActivity.class);
                            startActivity(intent);
                            finish();

                        } else if (type.equals("DRIVER") && loginKey != null) {

                            fetchFCMToken();
                            Intent intent = new Intent(SplashScreen.this, TrackerMainActivity.class);
                            startActivity(intent);
                            finish();

                        } else {

                            fetchFCMToken();
                            Intent intent = new Intent(SplashScreen.this, LaunchActivity.class);
                            startActivity(intent);
                            finish();

                        }


                    } else {

                        Intent intent = new Intent(SplashScreen.this, LaunchActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } else {

                    inactiveInternetAlert();

                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {

                inactiveInternetAlert();
                Log.e(TAG, t.getMessage());

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

//    private void retrieveMode() {
//
//        SharedPreferences remMePrefs = getSharedPreferences("STORED_PREFS", MODE_PRIVATE);
//        String retThemeName = remMePrefs.getString("MODE", "light");
//
//        switch (retThemeName) {
//
//            case "dark":
//
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//
//                break;
//            case "follow":
//
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
//
//                break;
//
//            case "time":
//
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_TIME);
//
//                break;
//
//            case "batterySaver":
//
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
//
//                break;
//
//            default:
//
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//
//                break;
//        }
//
//    }

    private void retrieveUserToken() {

        SharedPreferences loginPrefs = getSharedPreferences("USER_KEYS", Context.MODE_PRIVATE);
        loginKey = loginPrefs.getString("USER_TOKEN", null);

    }

    private void fetchType() {

        SharedPreferences loginPrefs = getSharedPreferences("USER_KEYS", Context.MODE_PRIVATE);
        type = loginPrefs.getString("TYPE", "");
    }

    private void fetchFCMToken() {

        // FCM Token for sending push notifications

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();

                    Log.d(TAG, token);
                    uploadTokenToServer(token);
                });

    }

    private void uploadTokenToServer(String fcmToken) {

        if (loginKey !=null) {

            Call<ResponseBody> updateFCMCall = RetrofitClient
                    .getInstance()
                    .getApiService()
                    .updateFCMToken("Token " + loginKey, fcmToken);

            updateFCMCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                    if (response.isSuccessful() && response.code() == 200) {

                        Log.i(TAG, "Token upload success: " + fcmToken);

                    } else {

                        Log.e(TAG, "error uploading token " + response.code() + " " + response.errorBody());

                    }

                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

                    Log.e(TAG, "error uploading token " + t.getMessage());
                }
            });


        }


        alertDialog.setOnDismissListener(dialogInterface -> supportFinishAfterTransition());

        alertDialog.setOnCancelListener(dialogInterface -> supportFinishAfterTransition());

    }

}
