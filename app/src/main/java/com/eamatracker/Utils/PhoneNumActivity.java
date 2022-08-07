package com.eamatracker.Utils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eamatracker.API.RetrofitClient;
import com.eamatracker.Models.Message;
import com.eamatracker.R;
import com.eamatracker.Seller.SellerMainActivity;
import com.eamatracker.Tracker.TrackerMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.nihaskalam.progressbuttonlibrary.CircularProgressButton;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhoneNumActivity extends AppCompatActivity {

    private static final String TAG = "PhoneNumActivity";

    private LinearLayout otpStartContent, otpSentContent;
    private CountryCodePicker countryCodePicker;
    private CircularProgressButton sendOtp, verifyOtp;
    private TextInputLayout phoneNumTextInput;
    private EditText phoneNumEdt;
    private TextView enterOtpInfo, resendOtpAfter;
    private OtpTextView otpTextView;
    private MaterialButton resendOtpBtn;

    private CountDownTimer mCountDownTimer;
    private static  final long OTP_EXPIRE_TIME = 120000;
    private long OTP_TIME_LEFT_MILLIS = OTP_EXPIRE_TIME;
    private String mVerificationId;

    private String phoneNumber;
    private boolean isOtpSent = false;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private InternetStatus mInternetStatus;
    private TextView otpTimer, providePhoneNum;

    private String loginKey, lang = "en";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_num);

        FirebaseApp.initializeApp(getApplicationContext());
        retrieveUserToken();

        mInternetStatus = new InternetStatus(this);
        mAuth = FirebaseAuth.getInstance();
        mAuth.useAppLanguage();

        otpStartContent = findViewById(R.id.otpStartContent);
        otpSentContent = findViewById(R.id.otpSentContent);
        countryCodePicker = findViewById(R.id.ccp);
        phoneNumTextInput = findViewById(R.id.phoneNumTextInput);
        phoneNumEdt = findViewById(R.id.phoneNumEdt);
        otpStartContent = findViewById(R.id.otpStartContent);
        enterOtpInfo = findViewById(R.id.enterOtpInfo);
        resendOtpAfter = findViewById(R.id.resendOtpAfter);
        otpTextView = findViewById(R.id.otpView);
        resendOtpBtn = findViewById(R.id.resendOtpBtn);

        sendOtp = findViewById(R.id.sendOtp);
        verifyOtp = findViewById(R.id.verifyOtp);


        countryCodePicker.registerCarrierNumberEditText(phoneNumEdt);

        mInternetStatus = new InternetStatus(this);

//        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//            @Override
//            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//
//                Log.d(TAG, "onVerificationCompleted: " + phoneAuthCredential);
//
//                sendOtp.showComplete();
//                updatePhoneNum(phoneNumber);
//
//            }
//
//            @Override
//            public void onVerificationFailed(@NonNull FirebaseException e)
//            {
//
//                // This callback is invoked in an invalid request for verification is made,
//                // for instance if the the phone number format is not valid.
//                Log.w(TAG, "onVerificationFailed", e);
//
//                sendOtp.showError();
//
//                Completable.timer(4, TimeUnit.SECONDS,
//                        AndroidSchedulers.mainThread())
//                        .subscribe(() -> {
//
//                            sendOtp.showIdle();
//
//                        });
//                if (e instanceof FirebaseAuthInvalidCredentialsException) {
//
//                    errorSnackBar(getString(R.string.invalid_request));
//
//                    // Invalid request
//                } else if (e instanceof FirebaseTooManyRequestsException) {
//
//                    errorSnackBar(getString(R.string.sms_qty_exceeded));
//                    // The SMS quota for the project has been exceeded
//                } else {
//
//                    errorSnackBar(getString(R.string.error_occured));
//                }
//
//                // Show a message and update the UI
//
//            }
//
//            @Override
//            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                super.onCodeSent(s, forceResendingToken);
//
//
//                // The SMS verification code has been sent to the provided phone number, we
//                // now need to ask the user to enter the code and then construct a credential
//                // by combining the code with a verification ID.
//                mVerificationId = s;
//
//                Log.d(TAG, "onCodeSent: " + mVerificationId);
//
//                sendOtp.showComplete();
//
//                // Save verification ID and resending token so we can use them later
//                mResendToken = forceResendingToken;
//
//                mVerificationId = s;
//                otpSentContent.setVisibility(View.VISIBLE);
//                otpStartContent.setVisibility(View.INVISIBLE);
//                startCountDown();
//                verifyOtpCode();
//
//            }
//        };

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
            {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e)
            {

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                mVerificationId = s;
                mResendToken = forceResendingToken;


                startCountDown();

            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential)
    {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            updatePhoneNum(phoneNumber);

                        }
                        else
                        {
                            errorSnackBar(getString(R.string.error_occured));
                            //TODO: delete user token
                        }
                    }
                });
    }

    private void verifyOtpCode() {

        otpTextView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {

            }

            @Override
            public void onOTPComplete(@NonNull String otp) {

                if (otp.isEmpty() || otp.length() > 6) {

                    otpTextView.showError();
                    verifyOtp.showError();

                } else {

                    if (mVerificationId.equals(otp)) {

                        updatePhoneNum(phoneNumber);

                    } else {

                        otpTextView.showError();
                        errorSnackBar(getString(R.string.invalid_otp));

                    }

                }

            }
        });

    }

    public void sendOTP(View view) {

        hideKeyboard(this);
        phoneNumber = countryCodePicker.getFullNumberWithPlus();
        String rawNum = phoneNumEdt.getText().toString();


        if (!isOtpSent) {

            if (rawNum.isEmpty()) {

                phoneNumTextInput.setError(getString(R.string.phone_number_err));

            } else if (!mInternetStatus.isConnectingToInternet()) {

                phoneNumTextInput.setError(getString(R.string.no_internet));

            } else {

                Log.d(TAG, phoneNumber);
                sendFirebaseOtp(phoneNumber);
            }
        }

    }

    public void verifyOTP(View view) {

        verifyOtp.showProgress();
        verifyOtpCode();

    }

    private void startCountDown()
    {
        mCountDownTimer = new CountDownTimer(OTP_EXPIRE_TIME, 1000)
        {
            @Override
            public void onTick(long millisUntilFinished)
            {
                OTP_TIME_LEFT_MILLIS = millisUntilFinished;
                UpdateOTPTimer();
            }

            @Override
            public void onFinish()
            {

                resendOtpBtn.setText(R.string.resend_otp);
                resendOtpBtn.setStrokeColor(ColorStateList.valueOf(getColor(R.color.colorSecondary)));
                resendOtpBtn.setTextColor(ColorStateList.valueOf(getColor(R.color.colorSecondary)));
                resendOtpAfter.setVisibility(View.INVISIBLE);
            }
        }.start();
    }

    private void UpdateOTPTimer()
    {
        int minutes = (int) (OTP_TIME_LEFT_MILLIS / 1000) / 60;
        int seconds = (int) (OTP_TIME_LEFT_MILLIS / 1000) % 60;

        String formattedTimeLeft = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        resendOtpBtn.setText(formattedTimeLeft);
    }

    private void sendFirebaseOtp(String phoneNumber) {

        sendOtp.showProgress();

        sendOtp.showProgress();
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        isOtpSent = true;
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

    private void errorSnackBar(String message) {

        Snackbar errorSnackBar = Snackbar
                .make(this.getWindow().getDecorView().getRootView(),
                        message,
                        Snackbar.LENGTH_LONG);
        errorSnackBar
                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
        errorSnackBar
                .setActionTextColor(getResources().getColor(R.color.colorPrimary));
        errorSnackBar
                .setBackgroundTint(getResources().getColor(R.color.errorColor));
        errorSnackBar
                .setActionTextColor(getResources().getColor(R.color.colorAccent));


        errorSnackBar.setAction(getString(R.string.internet_error_snackbar), v -> errorSnackBar.dismiss());
        errorSnackBar.show();
    }

    private void successSnackBar(String message) {

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

    private void sendUserToMainActivity() {

        Intent mainIntent = new Intent(this, TrackerMainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        supportFinishAfterTransition();
    }

    private void updatePhoneNum(String phoneNumber) {

        Call<Message> updatePhoneNum = RetrofitClient
                .getInstance()
                .getApiService()
                .updateDriverPhoneNum("Token " + loginKey, phoneNumber);

        updatePhoneNum.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(@NonNull Call<Message> call, @NonNull Response<Message> response) {

                if (response.isSuccessful() && response.code() == 200) {

                    otpTextView.showSuccess();
                    successSnackBar(getString(R.string.login_success_redirecting));
                    Completable.timer(4, TimeUnit.SECONDS,
                            AndroidSchedulers.mainThread())
                            .subscribe(() -> {

                                sendUserToMainActivity();

                            });

                } else {

                    errorSnackBar(getString(R.string.error_occured));
                }

            }

            @Override
            public void onFailure(@NonNull Call<Message> call, @NonNull Throwable t) {

                if (mInternetStatus.isConnectingToInternet()) {

                    errorSnackBar(getString(R.string.error_occured));
                } else {

                    errorSnackBar(getString(R.string.no_internet));
                }

            }
        });
    }

    private void retrieveUserToken() {

        SharedPreferences loginPrefs = getSharedPreferences("USER_KEYS", Context.MODE_PRIVATE);
        loginKey = loginPrefs.getString("USER_TOKEN", null);
        Log.d(TAG, "Login key = " + loginKey);

    }
}