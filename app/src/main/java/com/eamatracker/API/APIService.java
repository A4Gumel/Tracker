package com.eamatracker.API;


import com.eamatracker.Models.BannerList;
import com.eamatracker.Models.DriverDetails;
import com.eamatracker.Models.FAQS;
import com.eamatracker.Models.Login;
import com.eamatracker.Models.Message;
import com.eamatracker.Models.NotificationsList;
import com.eamatracker.Models.PasswordResetConfirm;
import com.eamatracker.Models.ProductDetails;
import com.eamatracker.Models.ProductList;
import com.eamatracker.Models.ProductRequestList;
import com.eamatracker.Models.ProductSimple;
import com.eamatracker.Models.Rating;
import com.eamatracker.Models.ReVerifyEmail;
import com.eamatracker.Models.Reset;
import com.eamatracker.Models.ReviewList;
import com.eamatracker.Models.SimpleUser;
import com.eamatracker.Models.UserDetails;
import com.eamatracker.Models.Wallet;
import com.eamatracker.Models.WarehouseList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIService {

    //The Main API Service for the main app that support the app

    @FormUrlEncoded
    @POST("en/api/v1/auth/login/")
    Call<Login> loginUser(
            @Field("username") String Username,
            @Field("email") String Email,
            @Field("password") String Password);

    @FormUrlEncoded
    @PATCH("en/api/v1/user/")
    Call<UserDetails> updateUserDetails(
            @Header("Authorization") String token,
            @Field("first_name") String firstName,
            @Field("middle_name") String middleName,
            @Field("last_name") String lastName,
            @Field("age_range") String ageRange,
            @Field("gender") String gender);

    @GET("en/api/v1/user/")
    Call<UserDetails> fetchUserDetails(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("en/api/v1/auth/password/reset/")
    Call<Reset> sendResetLink(@Field("email") String email);

    @FormUrlEncoded
    @POST("en/api/v1/auth/password/reset/confirm/")
    Call<PasswordResetConfirm> passwordResetConfirm(
            @Field("new_password1") String password1,
            @Field("new_password2") String password2,
            @Field("uid") String uid,
            @Field("token") String token);

    @POST("en/api/v1/auth/logout/")
    Call<ResponseBody> logOutUser();

    @FormUrlEncoded
    @POST("en/api/v1/auth/password/change/")
    Call<PasswordResetConfirm> passwordChange(
            @Field("old_password") String oldPassword,
            @Field("new_password1") String password1,
            @Field("new_password2") String password2);

    @FormUrlEncoded
    @POST("en/api/v1/registration/verify-email/")
    Call<ReVerifyEmail> resendVerification(@Field("key") String token);


    @GET("en/api/v1/product-review-list/{product_id}/")
    Call<ReviewList> productReviewsList(@Path("product_id") int productID);


    @GET("en/api/v1/product-details/{item_slug}/")
    Call<ProductDetails> productDetails(@Header("Authorization") String token,
                                        @Path("item_slug") String productSlug);

    @GET("en/api/v1/product-rating/{item_id}/")
    Call<Rating> productRating(@Path("item_id") Integer productId);

    @FormUrlEncoded
    @POST("en/api/v1/test-internet/")
    Call<ResponseBody> testInternet(@Field("version") Integer version);

    @FormUrlEncoded
    @PUT("en/api/v1/update-fcm-token/")
    Call<ResponseBody> updateFCMToken(
            @Header("Authorization") String token,
            @Field("fcm_token") String fcmToken);

    @GET("en/api/v1/banners/")
    Call<BannerList> banners();


    @GET("en/api/v1/product-simple-detail/{product_id}/")
    Call<ProductSimple> simpleProductDetail(@Path("product_id") Integer productId);


    @GET("en/api/v1/my-notifications/")
    Call<NotificationsList> myNotifications(@Header("Authorization") String token);

    @GET("en/api/v1/faqs/")
    Call<FAQS> faqs();

    @GET("en/api/v1/validate-driver/")
    Call<ResponseBody> validateDriver(@Header("Authorization") String token);

    @GET("en/api/v1/validate-seller/")
    Call<ResponseBody> validateSeller(@Header("Authorization") String token);

    @GET("en/api/v1/seller-products/")
    Call<ProductList> sellerProducts(@Header("Authorization") String token);

    @GET("en/api/v1/simple-user/{pk}/")
    Call<SimpleUser> simpleUser(@Header("Authorization") String token, @Path("pk") Integer pk);

    @POST("en/api/v1/withdraw/")
    Call<Message> walletWithdraw(@Header("Authorization") String token,
                                 @Field("amount") Integer amount,
                                 @Field("description") String description);

    @GET("en/api/v1/wallet/")
    Call<Wallet> wallet(@Header("Authorization") String token);

    @GET("en/api/v1/update-driver-phone-num/")
    Call<Message> updateDriverPhoneNum(@Header("Authorization") String token,
                          @Field("phone_num") String phoneNumber);

    @GET("en/api/v1/driver-details/")
    Call<DriverDetails> driverDetails(@Header("Authorization") String token);

    @GET("en/api/v1/warehouse-list/{locality}/")
    Call<WarehouseList> warehouseList(@Header("Authorization") String token);

    @GET("en/api/v1/product-request-list/")
    Call<ProductRequestList> productRequestList(@Header("Authorization") String token);

    @POST("en/api/v1/seller-accept-request/{request_id}/")
    Call<Message> acceptRequest(@Header("Authorization") String token,
                                @Path("request_id") Integer requestId);


}