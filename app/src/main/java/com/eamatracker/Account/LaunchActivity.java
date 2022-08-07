package com.eamatracker.Account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.chaos.view.PinView;
import com.eamatracker.API.RetrofitClient;
import com.eamatracker.Models.DriverDetails;
import com.eamatracker.Models.Login;
import com.eamatracker.Models.Reset;
import com.eamatracker.Models.Seller;
import com.eamatracker.R;
import com.eamatracker.Seller.SellerMainActivity;
import com.eamatracker.Tracker.TrackerMainActivity;
import com.eamatracker.Utils.InternetStatus;
import com.eamatracker.Utils.PhoneNumActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.nihaskalam.progressbuttonlibrary.CircularProgressButton;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LaunchActivity extends AppCompatActivity {

    private static final String TAG = "LaunchActivity";

    private BottomSheetDialog mBottomSheetDialog;
    private TextInputLayout loginUsername, forgotEmailTextInput;
    private TextInputLayout loginPassword, phoneNumTextInput;
    private CircularProgressButton loginButton;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    //regex
    Pattern specialCharRegex = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
    private Dialog alertDialog;

    private String loginKey, lang = "en";

    private int loginAttempt = 0;

    private CountDownTimer mCountDownTimer;
    private static final long LOGIN_ATTEMPT_EXPIRE_TIME = 120000;
    private long TIME_LEFT_MILLIS = LOGIN_ATTEMPT_EXPIRE_TIME;
    private LinearLayout loginAttemptLayout, loginMainContent, loginSuccessContent;
    private TextView loginAttemptTimer;
    private boolean isSeller;
    private InternetStatus mInternetStatus;
    private LottieAnimationView successAnim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        alertDialog = new Dialog(this);

        mInternetStatus = new InternetStatus(this);

    }


    public void transporterLogin(View view) {

        isSeller = false;
        showLogin();


    }


    public void sellerLogin(View view) {

        isSeller = true;
        showLogin();
    }

    private void showLogin() {

        mBottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetStyle);

        //Inflate UI
        LayoutInflater inflater = LayoutInflater.from(new ContextThemeWrapper(getApplicationContext(), R.style.AppTheme));
        View loginLayout = inflater.inflate(R.layout.login_layout, null);

        // Define Login UI Views
        TextView loginTitle = loginLayout.findViewById(R.id.loginTitle);
        loginUsername = loginLayout.findViewById(R.id.loginUsername);
        loginPassword = loginLayout.findViewById(R.id.loginPassword);
        loginButton = loginLayout.findViewById(R.id.loginButton);
        //Login attempt views
        loginAttemptLayout = loginLayout.findViewById(R.id.loginAttemptLayout);
        loginAttemptTimer = loginLayout.findViewById(R.id.loginAttemptTimer);
        loginMainContent = loginLayout.findViewById(R.id.loginMainContent);
        loginSuccessContent = loginLayout.findViewById(R.id.loginSuccessContent);
        successAnim = loginLayout.findViewById(R.id.successAnim);

        if (isSeller) {
            // Change Dialog Title for Seller
            loginTitle.setText(getText(R.string.seller_login));
        }
        loginButton.setOnClickListener(view -> {
            loginUser();
            hideKeyboard(this);
        });


        mBottomSheetDialog.setContentView(loginLayout);
        mBottomSheetDialog.setDismissWithAnimation(true);
        mBottomSheetDialog.show();

    }


    private boolean validateUsername() {

        String username = Objects.requireNonNull(loginUsername.getEditText()).getText().toString();

        Matcher m = specialCharRegex.matcher(username);
        boolean isContainSpecialCharacters = m.find();

        Pattern pattern = Pattern.compile("\\s");
        Matcher matcher = pattern.matcher(username);
        boolean isWhiteSpaceFound = matcher.find();

        if (username.isEmpty()) {

            //Empty username field setError
            loginUsername.setError(getString(R.string.empty_username_field));

        } else if (username.length() <= 4) {

            //Username less than 4 digits set error
            loginUsername.setError(getString(R.string.username_too_small));

        } else if (username.length() >= 16) {

            //Username greater than 15 digits set error
            loginUsername.setError(getString(R.string.username_too_long));

        } else if (isContainSpecialCharacters) {

            //Username contain special characters set Error
            loginUsername.setError(getString(R.string.contain_special_char));

        } else if (isWhiteSpaceFound) {

            //Username contain white characters set Error
            loginUsername.setError(getString(R.string.white_space_error));

        } else {

            // Remove Error
            loginUsername.setError(null);
            return true;
        }
        return false;
    }


    private boolean validateEmailField() {

        String email = Objects.requireNonNull(forgotEmailTextInput.getEditText()).getText().toString();

        if (email.isEmpty()) {

            //Empty email address setError
            forgotEmailTextInput.setError(getString(R.string.empty_email_field));

        } else if (!email.matches(emailPattern)) {
            //Invalid email address setError
            forgotEmailTextInput.setError(getString(R.string.invalid_email_error));

        } else if (email.length() >= 100) {

            forgotEmailTextInput.setError(getString(R.string.email_too_long_err));

        } else {

            forgotEmailTextInput.setError(null);
            return true;
        }
        return false;
    }

    private boolean validateLoginPassword() {

        String password = Objects.requireNonNull(loginPassword.getEditText()).getText().toString();

        if (password.isEmpty()) {

            //empty password field setError
            loginPassword.setError(getString(R.string.login_email_empty_error));

        } else if (password.length() <= 7) {

            // Password Too short
            loginPassword.setError(getString(R.string.password_too_short_error));

        } else {

            return true;
        }
        return false;
    }


    private void loginUserCall(String username, String email, String password) {

        //Show loading anim
        loginButton.showProgress();
        hideKeyboard(this);

        Call<Login> loginCall = RetrofitClient
                .getInstance()
                .getApiService()
                .loginUser(username, email, password);

        loginCall.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(@NotNull Call<Login> call, @NotNull Response<Login> response) {

                loginAttempt += 1;

                if (response.isSuccessful() && response.code() == 200) {

                    // Login Success
                    Login loginSuccess = response.body();

                    Log.i(TAG, "Login Call Success");
                    assert loginSuccess != null;

                    if (isSeller) {

                        // Seller identity verification
                        validateSeller(loginSuccess.getKey());
                        Log.d(TAG, "Login success validating id seller");

                    } else {

                        // Driver identity verification
                        validateDriver(loginSuccess.getKey());
                        Log.d(TAG, "Login success validating id driver");
                    }

                } else if (response.code() == 400 || response.code() == 404) {

                    // Error invalid seller or driver
                    Log.d(TAG, "Login Error = 400 or 404 " +
                            response.code() + " " + response.errorBody() + " " + response.body());
                    dismissBottomSheet();
                    errorSnackbar(getString(R.string.login_error));

                } else {

                    // General error
                    errorSnackbar(getString(R.string.error_occured));

                }

            }

            @Override
            public void onFailure(@NotNull Call<Login> call, @NotNull Throwable t) {

                Log.d(TAG, Objects.requireNonNull(t.getMessage()));
                dismissBottomSheet();

                // Error due to connection to server

                if (mInternetStatus.isConnectingToInternet()) {

                    errorSnackbar(getString(R.string.error_occured));
                } else {

                    errorSnackbar(getString(R.string.no_internet));
                }


            }
        });

    }

    private void validateSeller(String key) {

        Call<ResponseBody> validateSellerCall = RetrofitClient
                .getInstance()
                .getApiService()
                .validateSeller("Token " + key);

        validateSellerCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {

                if (response.isSuccessful() && response.code() == 200) {

                    if (loginSuccessContent != null && loginMainContent != null && successAnim != null) {

                        // Valid Seller, Show Seller Activity

                        // Show lottie animation
                        successAnim.setAnimation(R.raw.account_done);
                        loginSuccessContent.setVisibility(View.VISIBLE);
                        loginMainContent.setVisibility(View.INVISIBLE);

                        // Stop loading animation
                        loginButton.showComplete();

                        saveType("SELLER");

                        saveUserToken(key);

                        dismissBottomSheet();

                        successSnackbar(getString(R.string.login_success_redirecting));


                        sendUserToMainActivity();
                    }


                } else {

                    // Failed to validate seller
                    Log.d(TAG, "Seller validate error :" + response.errorBody() + " " + response.code());
                    dismissBottomSheet();
                    showDismissDialog();

                }

            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

                Log.d(TAG, t.getMessage());

                // Server connection failure

                dismissBottomSheet();

                if (mInternetStatus.isConnectingToInternet()) {

                    errorSnackbar(getString(R.string.no_internet));

                } else {

                    errorSnackbar(getString(R.string.error_occured));

                }

            }
        });


    }

    private void validateDriver(String key) {

        Log.d(TAG, "Validating driver: " + key);

        // Forcely convert Token to String to avoid any error
        String token = "Token " +  String.valueOf(key);
        Log.d(TAG, token);

        Call<DriverDetails> userDetailsCall = RetrofitClient
                .getInstance()
                .getApiService()
                .driverDetails(token);

        userDetailsCall.enqueue(new Callback<DriverDetails>() {
            @Override
            public void onResponse(@NonNull Call<DriverDetails> call, @NonNull Response<DriverDetails> response) {

                if (response.isSuccessful() && response.code() == 200) {

                    saveUserToken(key);

                    if (loginSuccessContent != null && loginMainContent != null && successAnim != null) {

                        loginButton.showComplete();

                        //TODO: verify phone number
                        //Valid Driver
                        dismissBottomSheet();

                        DriverDetails driverDetails = response.body();

                        saveDriverId(Objects.requireNonNull(driverDetails).getId());

                        saveType("DRIVER");

                        sendUserToMainActivity();

                    }


                } else {

                    // Error
                    dismissBottomSheet();
                    showDismissDialog();
                    Log.e(TAG, "Validate driver error : " + response.code() + " " + response.body());

                }

            }

            @Override
            public void onFailure(@NonNull Call<DriverDetails> call, @NonNull Throwable t) {

                Log.e(TAG, t.getMessage());
                dismissBottomSheet();

                // Error connecting to server

                if (mInternetStatus.isConnectingToInternet()) {

                    errorSnackbar(getString(R.string.error_occured));

                } else {

                    errorSnackbar(getString(R.string.no_internet));
                }

            }
        });

    }

    private void showDismissDialog() {

        /* Dialog for when failed to validate driver */

        alertDialog.setContentView(R.layout.dismiss_dialog);
        Objects.requireNonNull(alertDialog.getWindow()).getAttributes().windowAnimations = R.style.AlertDialogAnimation;
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.setCanceledOnTouchOutside(false);

        MaterialButton dismissDialogBtn = alertDialog.findViewById(R.id.dismissAlertBtn);

        dismissDialogBtn.setOnClickListener(view -> alertDialog.dismiss());
        alertDialog.show();

    }

    private void saveUserToken(String key) {

        /* Store user token to shared prefs */

        SharedPreferences preferences = getSharedPreferences("USER_KEYS", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("USER_TOKEN", key);
        editor.apply();
    }

    private void saveType(String type) {

        /* Save user/ account type */
        SharedPreferences preferences = getSharedPreferences("USER_KEYS", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("TYPE", type);
        editor.apply();
    }

    public void forgotPassword(View view) {

        dismissBottomSheet();

        mBottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetStyle);

        // Inflate layout
        LayoutInflater inflater = LayoutInflater.from(new ContextThemeWrapper(getApplicationContext(), R.style.AppTheme));
        View forgotPassLayout = inflater.inflate(R.layout.forgot_password_dialog, null);

        //forgot password views
        CircularProgressButton resetPasswordBtn = forgotPassLayout.findViewById(R.id.resetPasswordBtn);
        LottieAnimationView resetPasswordAnim = forgotPassLayout.findViewById(R.id.forgotPassAnim);
        forgotEmailTextInput = forgotPassLayout.findViewById(R.id.resetPasswordEmail);
        ImageView resetPasswordImg = forgotPassLayout.findViewById(R.id.forgotPassImg);
        TextView resetPasswordBody = forgotPassLayout.findViewById(R.id.forgotPassBody);


        resetPasswordBtn.setOnClickListener(v -> {

            // Get email from text input
            String resetEmail = Objects.requireNonNull(forgotEmailTextInput.getEditText()).getText().toString();


            if (validateEmailField()) {

                resetPasswordBtn.showProgress();

                forgotEmailTextInput.setEnabled(false);

                hideKeyboard(this);

                //call the the API
                sendPasswordResetLink(resetEmail,
                        resetPasswordAnim,
                        resetPasswordBtn,
                        resetPasswordBody,
                        resetPasswordImg,
                        forgotEmailTextInput);
            }

        });

        // Set content to bottom sheet
        mBottomSheetDialog.setContentView(forgotPassLayout);
        mBottomSheetDialog.setDismissWithAnimation(true);
        mBottomSheetDialog.show();

    }


    public void sendPasswordResetLink(String resetEmail,
                                      LottieAnimationView resetPasswordAnim,
                                      CircularProgressButton resetPasswordBtn,
                                      TextView resetPasswordBody,
                                      ImageView resetPasswordImg,
                                      TextInputLayout forgotPassEmailInput) {

        Call<Reset> sendPasswordResetCall = RetrofitClient
                .getInstance()
                .getApiService()
                .sendResetLink(resetEmail);


        sendPasswordResetCall.enqueue(new Callback<Reset>() {
            @Override
            public void onResponse(@NotNull Call<Reset> call, @NotNull Response<Reset> response) {

                if (response.isSuccessful() && response.code() == 200) {

                    // Success sending Email

                    resetPasswordImg.setVisibility(View.INVISIBLE);
                    forgotPassEmailInput.setVisibility(View.GONE);
                    resetPasswordAnim.setVisibility(View.VISIBLE);
                    resetPasswordAnim.setAnimation(R.raw.email_sent);
                    resetPasswordBtn.showComplete();
                    resetPasswordBtn.setVisibility(View.GONE);

                    resetPasswordBody.setText(R.string.forgot_password_success_body);

                } else if (!response.isSuccessful() || response.code() == 400) {

                    // Failed to send email

                    // Show failed views
                    resetPasswordImg.setVisibility(View.INVISIBLE);
                    forgotPassEmailInput.setVisibility(View.GONE);
                    resetPasswordAnim.setVisibility(View.VISIBLE);
                    resetPasswordAnim.setAnimation(R.raw.failed);
                    resetPasswordBtn.showError();
                    resetPasswordBtn.setVisibility(View.GONE);
                    resetPasswordBody.setText(getString(R.string.reset_error));
                    resetPasswordBody.setTextColor(getResources().getColor(R.color.errorColor));


                    // Remove progress / loading views
                    resetPasswordBtn.showIdle();
                    resetPasswordImg.setVisibility(View.VISIBLE);
                    forgotPassEmailInput.setVisibility(View.VISIBLE);
                    resetPasswordAnim.setVisibility(View.INVISIBLE);
                    resetPasswordBtn.setVisibility(View.VISIBLE);
                    resetPasswordBody.setText(getString(R.string.forgot_password_body));
                    resetPasswordBody.setTextColor(getResources().getColor(R.color.textColor));
                    forgotPassEmailInput.setEnabled(true);

                }

            }

            @Override
            public void onFailure(@NotNull Call<Reset> call, @NotNull Throwable t) {

                Log.d(TAG, Objects.requireNonNull(t.getMessage()));

                // Failed to communicate to servers

                if (mInternetStatus.isConnectingToInternet()) {

                    errorSnackbar(getString(R.string.error_occured));
                } else {

                    errorSnackbar(getString(R.string.no_internet));
                }


            }
        });

    }

    private void loginUser() {

        // prepare to login user and call the api
        String username = Objects.requireNonNull(loginUsername.getEditText()).getText().toString();
        String password = Objects.requireNonNull(loginPassword.getEditText()).getText().toString();


        if (username.isEmpty()) {

            loginUsername.setError(getString(R.string.empty_email_field));

        } if (!username.matches(emailPattern)) {

            loginUsername.setError(getString(R.string.invalid_email_error));

        } else {

            loginUsername.setError(null);

            //check for login attempt
            hideKeyboard(LaunchActivity.this);

            if (loginAttempt >= 5) {

                showTimer();

            } else {

                //input field is email
                //login with email
                if (validateLoginPassword()) {

                    loginUsername.setEnabled(false);
                    loginPassword.setEnabled(false);
                    loginUserCall("", username, password);

                }

            }

        }

    }


    private static void hideKeyboard(Activity activity) {

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }


    private void sendUserToMainActivity() {

        //Navigate the User to main activity either Driver or Seller

        Intent mainIntent;

        if (isSeller) {

            mainIntent = new Intent(this, SellerMainActivity.class);

        } else {

            mainIntent = new Intent(this, TrackerMainActivity.class);
        }

        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        supportFinishAfterTransition();
    }


    private void errorSnackbar(String message) {

        // Show error snacker

        Snackbar errorSnackbar = Snackbar
                .make(this.getWindow().getDecorView().getRootView(),
                        message,
                        Snackbar.LENGTH_LONG);
        errorSnackbar
                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
        errorSnackbar
                .setActionTextColor(getResources().getColor(R.color.colorPrimary));
        errorSnackbar
                .setBackgroundTint(getResources().getColor(R.color.errorColor));
        errorSnackbar
                .setActionTextColor(getResources().getColor(R.color.colorAccent));


        errorSnackbar.setAction(getString(R.string.internet_error_snackbar), v -> errorSnackbar.dismiss());
        errorSnackbar.show();
    }

    private void successSnackbar(String message) {

        //success snackabar

        Snackbar errorSnackbar = Snackbar
                .make(this.getWindow().getDecorView().getRootView(),
                        message,
                        Snackbar.LENGTH_LONG);
        errorSnackbar
                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
        errorSnackbar
                .setActionTextColor(getColor(R.color.colorPrimary));
        errorSnackbar
                .setBackgroundTint(getColor(R.color.colorSecondary));
        errorSnackbar
                .setActionTextColor(getColor(R.color.colorAccent));


        errorSnackbar.setAction(getString(R.string.internet_error_snackbar), v -> errorSnackbar.dismiss());
        errorSnackbar.show();
    }


    private void showTimer() {

        //show tomer in validating time

        loginAttemptLayout.setVisibility(View.VISIBLE);
        loginButton.showError();
        loginButton.setEnabled(false);

        mCountDownTimer = new CountDownTimer(LOGIN_ATTEMPT_EXPIRE_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                TIME_LEFT_MILLIS = millisUntilFinished;
                UpdateExpireTimer();
            }

            @Override
            public void onFinish() {
                loginAttempt = 0;
                loginAttemptLayout.setVisibility(View.GONE);
                loginButton.showIdle();
                loginButton.setEnabled(true);

            }
        }.start();
    }

    private void dismissBottomSheet() {

        // Remove bottom sheet dialog

        if (mBottomSheetDialog != null && mBottomSheetDialog.isShowing()) {

            mBottomSheetDialog.setDismissWithAnimation(true);
            mBottomSheetDialog.dismiss();
        }

    }


    private void UpdateExpireTimer() {

        // Update expirty time

        int minutes = (int) (TIME_LEFT_MILLIS / 1000) / 60;
        int seconds = (int) (TIME_LEFT_MILLIS / 1000) % 60;

        String formattedTimeLeft = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        loginAttemptTimer.setText(formattedTimeLeft);
    }

    private void saveDriverId(Integer id) {

        /* Store river id in Shared pref */

        SharedPreferences preferences = getSharedPreferences("USER_KEYS", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("DRIVER_ID", id);
        editor.apply();
    }

}