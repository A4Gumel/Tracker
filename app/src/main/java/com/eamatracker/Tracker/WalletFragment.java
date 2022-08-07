package com.eamatracker.Tracker;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eamatracker.API.PaystackAPI.PaystackClient;
import com.eamatracker.API.RetrofitClient;
import com.eamatracker.Models.Bank;
import com.eamatracker.Models.BankList;
import com.eamatracker.Models.DriverDetails;
import com.eamatracker.Models.Login;
import com.eamatracker.Models.Resolve;
import com.eamatracker.Models.ResolveData;
import com.eamatracker.Models.Transfer;
import com.eamatracker.Models.TransferRecipient;
import com.eamatracker.Models.TransferRecipientData;
import com.eamatracker.Models.Wallet;
import com.eamatracker.R;
import com.eamatracker.Tracker.Adapter.BankListAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.nihaskalam.progressbuttonlibrary.CircularProgressButton;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WalletFragment extends Fragment {

    private static final String TAG = "WalletFragment";

    private String loginKey, lang = "en";
    private BottomSheetDialog mBottomSheetDialog;

    private TextView walletDate, walletAmount;
    private MaterialButton withdrawBtn;
    private SwipeRefreshLayout walletSwipeRefresh;
    private LinearLayout mainWalletView, walletShimmer;
    private Bank selectedBank;
    private String username, recipientCode;
    private Integer accNum, amount, walletBalance;
    private boolean confirmAccountDetails = false;

    private Dialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View walletView = inflater.inflate(R.layout.fragment_wallet, container, false);
        walletDate = walletView.findViewById(R.id.walletDate);
        walletAmount = walletView.findViewById(R.id.walletAmount);
        withdrawBtn = walletView.findViewById(R.id.withdrawBtn);
        walletDate = walletView.findViewById(R.id.walletDate);
        walletSwipeRefresh = walletView.findViewById(R.id.walletSwipeRefresh);
        mainWalletView = walletView.findViewById(R.id.mainWalletView);
        walletShimmer = walletView.findViewById(R.id.walletShimmer);

        alertDialog = new Dialog(requireContext());


        retrieveUserToken();

        if (loginKey == null) {

            showErrorSnackBar(getString(R.string.you_need_login_err));

        } else {

            fetchMyWallet();
            fetchUserDetails();
        }

        walletSwipeRefresh.setOnRefreshListener(() -> {

            if (loginKey != null) {

                fetchMyWallet();
                fetchUserDetails();
            }
            walletSwipeRefresh.setRefreshing(true);

        });

        withdrawBtn.setOnClickListener(view -> showSelectBank());

        return walletView;
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
                    Log.d(TAG, driverDetails.getUserDetails().getUsername());
                    username = driverDetails.getUserDetails().getUsername();
                    walletSwipeRefresh.setRefreshing(false);

                } else {

                    showErrorSnackBar(getString(R.string.error_occured));
                    Log.e(TAG, response.code() + " " + response.body() + " " + response.errorBody());
                }

            }

            @Override
            public void onFailure(@NonNull Call<DriverDetails> call, @NonNull Throwable t) {

                Log.e(TAG, t.getMessage());

            }
        });

    }

    private void showSelectBank() {

        mBottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetStyle);

        LayoutInflater inflater = LayoutInflater.from(new ContextThemeWrapper(requireContext(), R.style.AppTheme));
        View selectBankLayout = inflater.inflate(R.layout.select_bank, null);
        TextInputLayout bankNameTxtInput = selectBankLayout.findViewById(R.id.bankNameTextInput);
        LinearLayout loadingLayout = selectBankLayout.findViewById(R.id.loadingLayout);
        RecyclerView banksListRv = selectBankLayout.findViewById(R.id.banksListRv);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        banksListRv.setLayoutManager(linearLayoutManager);
        fetchBanksList(banksListRv, loadingLayout, bankNameTxtInput);

        mBottomSheetDialog.setContentView(selectBankLayout);
        mBottomSheetDialog.setDismissWithAnimation(true);
        mBottomSheetDialog.show();
    }

    private void fetchBanksList(RecyclerView banksListRv,
                                LinearLayout loadingLayout,
                                TextInputLayout bankNameTxtInput) {

        Call<BankList> bankListCall = PaystackClient
                .getInstance()
                .getApiService()
                .banksList("Bearer " + getString(R.string.PSTK_PUBLIC_KEY));

        bankListCall.enqueue(new Callback<BankList>() {
            @Override
            public void onResponse(@NonNull Call<BankList> call, @NonNull Response<BankList> response) {

                if (response.isSuccessful() && response.code() == 200) {

                    assert response.body() != null;
                    Log.e(TAG, response.body().toString());

                    if (response.body().isStatus() && response.body().getBanks() != null) {

                        loadingLayout.setVisibility(View.GONE);

                        List<Bank> banks = response.body().getBanks();

                        BankListAdapter bankListAdapter = new BankListAdapter(requireContext(), banks);
                        banksListRv.setAdapter(bankListAdapter);
                        banksListRv.setVisibility(View.VISIBLE);
                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
                        banksListRv.addItemDecoration(dividerItemDecoration);


                        bankNameTxtInput.getEditText().addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                                bankListAdapter.getFilter().filter(editable.toString().toLowerCase());

                            }
                        });


                        bankListAdapter.setOnItemClickListener(position -> {

                            selectedBank = banks.get(position);
                            Log.d(TAG, selectedBank.toString());

                            Log.d(TAG, selectedBank.getName() + " " + selectedBank.getCode());

                            dismissBottomSheet();

                            showWithdraw(selectedBank);

                        });

                    }


                } else {

                    dismissBottomSheet();

                    showErrorSnackBar(getString(R.string.error_occured));
                }

            }

            @Override
            public void onFailure(@NonNull Call<BankList> call, @NonNull Throwable t) {

                dismissBottomSheet();

                showErrorSnackBar(getString(R.string.error_occured));
                Log.e(TAG, t.getMessage());

            }
        });
    }

    private void fetchMyWallet() {

        Call<Wallet> walletCall = RetrofitClient
                .getInstance()
                .getApiService()
                .wallet("Token " + loginKey);

        walletCall.enqueue(new Callback<Wallet>() {
            @Override
            public void onResponse(@NonNull Call<Wallet> call, @NonNull Response<Wallet> response) {


                if (response.isSuccessful() && response.code() == 200) {

                    Wallet wallet = response.body();

                    if (wallet != null) {

                        String currentDate = new SimpleDateFormat("dd-MM-yyyy",
                                Locale.getDefault()).format(new Date());

                        walletDate.setText(currentDate);
                        walletAmount.setText(String.valueOf(wallet.getBalance()));
                        withdrawBtn.setEnabled(true);
                        mainWalletView.setVisibility(View.VISIBLE);
                        walletShimmer.setVisibility(View.GONE);
                        walletBalance = (int) Math.round(wallet.getBalance());
                    }

                } else {

                    showErrorSnackBar(getString(R.string.error_occured));
                    Log.e(TAG, response.code() + " " + response.body());

                }

            }

            @Override
            public void onFailure(@NonNull Call<Wallet> call, @NonNull Throwable t) {

                showErrorSnackBar(requireContext().getString(R.string.error_occured));
                Log.e(TAG, t.getMessage());

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
                .setActionTextColor(getResources().getColor(R.color.textColor));
        errorSnackBarView
                .setBackgroundTint(getResources().getColor(R.color.design_default_color_error));
        errorSnackBarView
                .setActionTextColor(getResources().getColor(R.color.colorAccent));

        errorSnackBarView.show();

    }

    private void showWithdraw(Bank selectedBank) {

        mBottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetStyle);

        LayoutInflater inflater = LayoutInflater.from(new ContextThemeWrapper(requireContext(), R.style.AppTheme));
        View withdrawView = inflater.inflate(R.layout.withdraw_layout, null);

        TextView bankTv = withdrawView.findViewById(R.id.bankTv);
        TextView bankAccountName = withdrawView.findViewById(R.id.bankAccountName);
        TextInputLayout accNumTextInput = withdrawView.findViewById(R.id.accNumTextInput);
        TextInputLayout amountTextInput = withdrawView.findViewById(R.id.amountTextInput);
        TextInputLayout confirmPasswordTextInput = withdrawView.findViewById(R.id.confirmPasswordTextInput);
        CircularProgressButton withdrawContinueBtn = withdrawView.findViewById(R.id.withdrawContinueBtn);

        bankTv.setText(selectedBank.getName());

        withdrawContinueBtn.setOnClickListener(view -> {

            String accTxt = Objects.requireNonNull(accNumTextInput.getEditText()).getText().toString();
            String amountTxt = Objects.requireNonNull(amountTextInput.getEditText()).getText().toString();
            String password = Objects.requireNonNull(confirmPasswordTextInput.getEditText()).getText().toString();
            accNumTextInput.setEnabled(false);
            amountTextInput.setEnabled(false);
            confirmPasswordTextInput.setEnabled(false);

            Log.d(TAG, "acc text :" + accTxt);

            if (!confirmAccountDetails) {


                if (accTxt.isEmpty()) {

                    accNumTextInput.setError(getString(R.string.acc_num_required_err));

                } else if (accTxt.length() < 10) {

                    accNumTextInput.setError(getString(R.string.acc_num_too_short));

                } else {

                    try {


                        if (amountTxt.isEmpty()) {

                            amountTextInput.setError(getString(R.string.amount_required));

                        } else {

                            try {

                                if (Integer.parseInt(amountTxt) <= 0) {

                                    amountTextInput.setError(getString(R.string.invalid_inputs));

                                } else {

                                    amount = Integer.parseInt(amountTxt);

                                    if (walletBalance - amount < 0) {

                                        amountTextInput.setError(getString(R.string.pls_lesser_amount));

                                    } else {


                                        if (password.isEmpty()) {

                                            //empty password field setError
                                            confirmPasswordTextInput.setError(getString(R.string.login_email_empty_error));

                                        } else if (password.length() <= 7) {

                                            confirmPasswordTextInput.setError(getString(R.string.password_too_short_error));

                                        } else {


                                            accNum = Integer.parseInt(accTxt);
                                            verifyAccDetails(accNum, withdrawContinueBtn, bankAccountName, password);
                                            Log.e(TAG, "Try acc num: " + accNum + " " + accTxt);


                                        }


                                    }
                                }

                            } catch (NumberFormatException e) {
                                amountTextInput.setError(getString(R.string.invalid_inputs));
                            }

                        }


                    } catch (NumberFormatException e) {

                        accNumTextInput.setError(getString(R.string.invalid_inputs));
                        Log.e(TAG, "Convert error: " + e.getMessage());

                    }

                }


            } else {

                verifyConfirmPassword(password, withdrawContinueBtn);
            }




        });


        mBottomSheetDialog.setContentView(withdrawView);
        mBottomSheetDialog.setDismissWithAnimation(true);
        mBottomSheetDialog.show();

    }

    private void verifyAccDetails(Integer accNum, CircularProgressButton withdrawContinueBtn, TextView bankAccountName, String password) {

        Log.d(TAG, "verifying account details");
        withdrawContinueBtn.showProgress();

        Call<Resolve> resolveCall = PaystackClient
                .getInstance()
                .getApiService()
                .resolveAccNum("Bearer " + getString(R.string.PSTK_PUBLIC_KEY),
                        accNum, selectedBank.getCode());

        resolveCall.enqueue(new Callback<Resolve>() {
            @Override
            public void onResponse(@NonNull Call<Resolve> call, @NonNull Response<Resolve> response) {

                if (response.isSuccessful() && response.code() == 200) {

                    Resolve resolve = response.body();

                    Log.d(TAG, response.toString());

                    if (resolve != null && resolve.isStatus()) {

                        bankAccountName.setText(resolve.getResolveData().getAccountName());
                        bankAccountName.setVisibility(View.VISIBLE);
                        withdrawContinueBtn.showComplete();

                        Completable.timer(2, TimeUnit.SECONDS,
                                AndroidSchedulers.mainThread())
                                .subscribe(() -> {

                                    withdrawContinueBtn.showIdle();
                                    withdrawContinueBtn.setIdleText(getString(R.string.confirm_transfer));

                                });
                        Log.d(TAG, response.toString());


                    } else {

                        dismissBottomSheet();

                        showErrorSnackBar(getString(R.string.error_occured));

                        Log.e(TAG, "Error resolving " + response.code() + response.body());
                    }


                } else {

                    dismissBottomSheet();
                    showErrorSnackBar(getString(R.string.error_occured));
                    Log.e(TAG, "Error resolving " + response.code() + response.body());
                }

            }

            @Override
            public void onFailure(@NonNull Call<Resolve> call, @NonNull Throwable t) {


                dismissBottomSheet();
                showErrorSnackBar(getString(R.string.error_occured));
                Log.d(TAG, t.getMessage());

            }
        });
    }

    private void verifyConfirmPassword(String password, CircularProgressButton withdrawContinueBtn) {

        withdrawContinueBtn.showProgress();

        Call<Login> loginCall = RetrofitClient
                .getInstance()
                .getApiService()
                .loginUser(username, "", password);

        loginCall.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(@NotNull Call<Login> call, @NotNull Response<Login> response) {

                if (response.isSuccessful() && response.code() == 200) {

                    initiateTransfer(String.valueOf(accNum), withdrawContinueBtn);

                } else {

                    dismissBottomSheet();
                    showErrorSnackBar(getString(R.string.password_dont_match_error));
                }

            }

            @Override
            public void onFailure(@NotNull Call<Login> call, @NotNull Throwable t) {

                Log.d(TAG, Objects.requireNonNull(t.getMessage()));
                dismissBottomSheet();


            }
        });

    }

    private void showBankInfoAlert(Resolve resolve) {

        alertDialog.setContentView(R.layout.bank_account_info);
        Objects.requireNonNull(alertDialog.getWindow()).getAttributes().windowAnimations = R.style.AlertDialogAnimation;
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.setCanceledOnTouchOutside(true);

        TextView bankHolderName = alertDialog.findViewById(R.id.bankHolderName);
        TextView bankHolderAcc = alertDialog.findViewById(R.id.bankHolderAcc);
        TextView bankName = alertDialog.findViewById(R.id.bankName);

        MaterialButton confirmInfoBtn = alertDialog.findViewById(R.id.confirmInfoBtn);
        MaterialButton cancelInfoBtn = alertDialog.findViewById(R.id.cancelInfoBtn);

        ResolveData resolveData = resolve.getResolveData();

        bankHolderName.setText(resolveData.getAccountName());
        bankHolderAcc.setText(resolveData.getAccountNumber());
        bankName.setText(selectedBank.getName());

        cancelInfoBtn.setOnClickListener(view -> alertDialog.dismiss());
        alertDialog.show();
    }

    private void initiateTransfer(String accNum, CircularProgressButton withdrawContinueBtn) {

        Log.e(TAG, "Account details verified");

        Call<TransferRecipient> initiateTransferCall = PaystackClient
                .getInstance()
                .getApiService()
                .transferRecipient("Bearer " + getString(R.string.PSTK_PUBLIC_KEY),
                        accNum,
                        selectedBank.getCode(),
                        "NGN");

        initiateTransferCall.enqueue(new Callback<TransferRecipient>() {
            @Override
            public void onResponse(@NonNull Call<TransferRecipient> call, @NonNull Response<TransferRecipient> response) {

                if (response.isSuccessful() && response.code() == 201) {

                    TransferRecipient transferRecipient = response.body();

                    if (transferRecipient != null && transferRecipient.isStatus()) {

                        TransferRecipientData transferRecipientData = transferRecipient.getTransferRecipientData();
                        recipientCode = transferRecipientData.getRecipientCode();
                        makeTransfer(recipientCode);

                        Log.d(TAG, transferRecipient.toString());

                    } else {

                        dismissBottomSheet();
                        showErrorSnackBar(getString(R.string.error_occured));

                    }

                } else {

                    Log.d(TAG, "initiateTransferCall : " + response.code());
                    dismissBottomSheet();
                    showErrorSnackBar(getString(R.string.error_occured));
                }

            }

            @Override
            public void onFailure(@NonNull Call<TransferRecipient> call, @NonNull Throwable t) {

                Log.d(TAG, "initiateTransferCall : " + t.getMessage());
                dismissBottomSheet();
                showErrorSnackBar(getString(R.string.error_occured));

            }
        });
    }

    private void makeTransfer(String recipientCode) {

        Log.d(TAG, "Make transfer");

        Call<Transfer> transferCall = PaystackClient
                .getInstance()
                .getApiService()
                .makeTransfer("Bearer " + getString(R.string.PSTK_PUBLIC_KEY),
                        "balance",
                        String.valueOf(amount),
                        recipientCode,
                        getString(R.string.transfer_reason));

        transferCall.enqueue(new Callback<Transfer>() {
            @Override
            public void onResponse(@NonNull Call<Transfer> call, @NonNull Response<Transfer> response) {

                if (response.isSuccessful() && response.code() == 200) {

                    Transfer transfer = response.body();

                    Log.e(TAG, String.valueOf(transfer.isStatus()));

                    if (transfer != null && transfer.isStatus()) {

                        if (mBottomSheetDialog.isShowing()) {

                            mBottomSheetDialog.dismiss();
                        }
                        showSuccessSnackbar(getString(R.string.transfer_processing));
                    }

                } else {

                    Log.e(TAG, " Transfer call : " + response.code() + response.body());
                    dismissBottomSheet();
                    showErrorSnackBar(getString(R.string.error_occured));

                }


            }

            @Override
            public void onFailure(@NonNull Call<Transfer> call, @NonNull Throwable t) {

                Log.e(TAG, " Transfer call : " + t.getMessage());
                dismissBottomSheet();
                showErrorSnackBar(getString(R.string.error_occured));

            }
        });
    }

    private void showSuccessSnackbar(String message) {

        Snackbar successSnackbar = Snackbar
                .make(requireView(),
                        message,
                        Snackbar.LENGTH_SHORT);
        successSnackbar
                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
        successSnackbar
                .setActionTextColor(getResources().getColor(R.color.textColor));
        successSnackbar
                .setBackgroundTint(getResources().getColor(R.color.colorSecondary));
        successSnackbar
                .setActionTextColor(getResources().getColor(R.color.colorAccent));


        successSnackbar.setAction(getString(R.string.internet_error_snackbar), v -> successSnackbar.dismiss());
        successSnackbar.show();
    }


    private void dismissBottomSheet() {

        if (mBottomSheetDialog != null && mBottomSheetDialog.isShowing()) {

            mBottomSheetDialog.setDismissWithAnimation(true);
            mBottomSheetDialog.dismiss();
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
}