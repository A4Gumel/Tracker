package com.eamatracker.Seller.Adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eamatracker.API.RetrofitClient;
import com.eamatracker.Models.Message;
import com.eamatracker.Models.ProductRequest;
import com.eamatracker.Models.ProductRequestList;
import com.eamatracker.Models.ProductSimple;
import com.eamatracker.R;
import com.google.android.material.button.MaterialButton;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.functions.Action;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductRequestListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /* Recyclerview adapter for Seller products request list */

    private static final String TAG = "ProductRequestListAdapter";

    private Context mContext;
    private ProductRequestList requestList;
    private String loginKey;
    public static final String DATE_FORMAT = "dd/MM/yyyy";

    public ProductRequestListAdapter(Context mContext, ProductRequestList requestList, String loginKey) {
        this.mContext = mContext;
        this.requestList = requestList;
        this.loginKey = loginKey;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Inflate the ui
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.seller_request_layout, parent, false);

        return new RequestVh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        // Get seller product list
        ProductRequest request = requestList.getProductRequestList().get(position);
        populateRequest((RequestVh) holder, request);

    }


    @Override
    public int getItemCount() {
        return requestList.getProductRequestList().size();
    }


    public class RequestVh extends RecyclerView.ViewHolder {


        // Define Views

        TextView dateInvalid, requestProductName, requestWeight, requestDeadline,
                requestGrade, requestQuantity, requestPrice;
        ProgressBar acceptingRequestProgress;
        RoundedImageView requestProductImage;
        MaterialButton requestAcceptBtn;

        public RequestVh(@NonNull View itemView) {
            super(itemView);

            dateInvalid = itemView.findViewById(R.id.dateInvalid);
            requestProductName = itemView.findViewById(R.id.requestProductName);
            requestWeight = itemView.findViewById(R.id.requestAmount);
            requestDeadline = itemView.findViewById(R.id.requestDeadline);
            acceptingRequestProgress = itemView.findViewById(R.id.acceptingRequestProgress);
            requestProductImage = itemView.findViewById(R.id.requestProductImage);
            requestAcceptBtn = itemView.findViewById(R.id.requestAcceptBtn);
            requestGrade = itemView.findViewById(R.id.requestGrade);
            requestQuantity = itemView.findViewById(R.id.requestQuantity);
            requestPrice = itemView.findViewById(R.id.requestPrice);
        }
    }

    private void populateRequest(RequestVh holder, ProductRequest request) {

        // ppultae ui views

        ProductSimple product = request.getProduct();

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        String deadline = formatter.format(request.getTimeInvalid());

        Glide.with(mContext).load(product.getPhoto1())
                .centerCrop()
                .into(holder.requestProductImage);

        holder.dateInvalid.setText(deadline);
        holder.requestProductName.setText(product.getName());

        if (request.getGrade().equals("B")) {

            holder.requestPrice.setText(mContext.getString(R.string.dollar_price, String.valueOf(product.getPriceB())));
            holder.requestGrade.setText(mContext.getText(R.string.grade_b));

        } else if (request.getGrade().equals("C")) {

            holder.requestPrice.setText(mContext.getString(R.string.dollar_price, String.valueOf(product.getPriceC())));
            holder.requestGrade.setText(mContext.getText(R.string.grade_c));

        } else {

            holder.requestPrice.setText(mContext.getString(R.string.dollar_price, String.valueOf(product.getPriceA())));
            holder.requestGrade.setText(mContext.getText(R.string.grade_a));
        }

        holder.requestQuantity.setText(mContext.getString(R.string.plc_quantity, String.valueOf(request.getQuantity())));

        if (product.getUnits().equals("KG")) {

            holder.requestWeight.setVisibility(View.GONE);

        } else {

            holder.requestWeight.setText(mContext.getString(R.string.plc_weight, String.valueOf(product.getWeight())));

        }

        holder.requestDeadline.setText(mContext.getString(R.string.plc_deadline, getDaysBetweenDates(deadline)));

        holder.requestAcceptBtn.setOnClickListener(view -> acceptRequest(request.getId(), holder));

    }

    private void acceptRequest(Integer id, RequestVh holder) {

        // Seller to accept product request

        holder.acceptingRequestProgress.setVisibility(View.VISIBLE);
        Log.d(TAG, "Accepting request");
        holder.requestAcceptBtn.setText(mContext.getString(R.string.please_wait));
        holder.requestAcceptBtn.setStrokeColor(ColorStateList.valueOf(mContext.getColor(R.color.colorSecondary)));
        holder.requestAcceptBtn.setTextColor(ColorStateList.valueOf(mContext.getColor(R.color.colorSecondary)));


        Call<Message> acceptRequestCall = RetrofitClient
                .getInstance()
                .getApiService()
                .acceptRequest("Token " + loginKey, id);

        acceptRequestCall.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {

                if (response.isSuccessful() && response.code() ==200) {

                    // Accept succes

                    //Show accepted views
                    holder.acceptingRequestProgress.setVisibility(View.INVISIBLE);
                    holder.requestAcceptBtn.setText(mContext.getString(R.string.rqst_accepted));
                    holder.requestAcceptBtn.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_check, 0, 0, 0);

                } else {

                    // Error

                    Log.e(TAG, "Accept request error "+response.code() +" "+response.body());
                    holder.requestAcceptBtn.setText(mContext.getString(R.string.error_occured));
                    holder.requestAcceptBtn.setStrokeColor(ColorStateList.valueOf(mContext.getColor(R.color.errorColor)));
                    holder.requestAcceptBtn.setTextColor(ColorStateList.valueOf(mContext.getColor(R.color.errorColor)));


                    // Remove accpetd views
                    holder.acceptingRequestProgress.setVisibility(View.INVISIBLE);
                    Log.d(TAG, "Accepting request");
                    holder.requestAcceptBtn.setText(mContext.getString(R.string.accept));
                    holder.requestAcceptBtn.setStrokeColor(ColorStateList.valueOf(mContext.getColor(R.color.colorAccent)));
                    holder.requestAcceptBtn.setTextColor(ColorStateList.valueOf(mContext.getColor(R.color.colorAccent)));

                }

            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {

                Log.e(TAG, t.getMessage());

                // Connection error

                holder.requestAcceptBtn.setText(mContext.getString(R.string.error_occured));
                holder.requestAcceptBtn.setStrokeColor(ColorStateList.valueOf(mContext.getColor(R.color.errorColor)));
                holder.requestAcceptBtn.setTextColor(ColorStateList.valueOf(mContext.getColor(R.color.errorColor)));


                // Remove views
                holder.acceptingRequestProgress.setVisibility(View.INVISIBLE);
                Log.d(TAG, "Accepting request");
                holder.requestAcceptBtn.setText(mContext.getString(R.string.accept));
                holder.requestAcceptBtn.setStrokeColor(ColorStateList.valueOf(mContext.getColor(R.color.colorAccent)));
                holder.requestAcceptBtn.setTextColor(ColorStateList.valueOf(mContext.getColor(R.color.colorAccent)));

            }
        });


    }


    public static String getDaysBetweenDates(String end) {

        // Calculate days between now and shipping date
        // to approximate remaining delivery date
        // Copied: From StackOverFlow

        String start = new SimpleDateFormat(DATE_FORMAT,
                Locale.getDefault()).format(new Date());

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        Date startDate, endDate;
        long numberOfDays = 0;
        try {
            startDate = dateFormat.parse(start);
            endDate = dateFormat.parse(end);
            assert startDate != null;
            assert endDate != null;
            numberOfDays = getUnitBetweenDates(startDate, endDate, TimeUnit.DAYS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return String.valueOf(numberOfDays);
    }

    private static long getUnitBetweenDates(Date startDate, Date endDate, TimeUnit unit) {
        long timeDiff = endDate.getTime() - startDate.getTime();
        return unit.convert(timeDiff, TimeUnit.MILLISECONDS);
    }

}
