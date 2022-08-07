package com.eamatracker.API.PaystackAPI;

import com.eamatracker.Models.BankList;
import com.eamatracker.Models.Resolve;
import com.eamatracker.Models.Transfer;
import com.eamatracker.Models.TransferRecipient;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PaystackService {

    // Paymnet Gateway urls to withdraw, transfer, recharge and get banks list


    @GET("bank/resolve")
    Call<Resolve> resolveAccNum(@Header("Authorization") String token,
                          @Query("account_number") Integer accountNumber,
                          @Query("bank_code") String bankCode);

    //
    @FormUrlEncoded
    @POST("transferrecipient")
    Call<TransferRecipient> transferRecipient(@Header("Authorization") String token,
                                              @Field("account_number") String accountNumber,
                                              @Field("bank_code") String bankCode,
                                              @Field("currency") String currency);

    @FormUrlEncoded
    @POST("transfer")
    Call<Transfer> makeTransfer(@Header("Authorization") String token,
                                @Field("source") String source,
                                @Field("amount") String amount,
                                @Field("recipient") String recipient,
                                @Field("reason") String reason);

    @GET("bank?country=nigeria&currency=ngn&perPage=100")
    Call<BankList> banksList(@Header("Authorization") String token);

}
