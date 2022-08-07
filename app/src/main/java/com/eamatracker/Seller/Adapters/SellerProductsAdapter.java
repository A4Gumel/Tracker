package com.eamatracker.Seller.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.eamatracker.API.RetrofitClient;
import com.eamatracker.Models.EnglishTrans;
import com.eamatracker.Models.Product;
import com.eamatracker.Models.ProductList;
import com.eamatracker.Models.ProductSimple;
import com.eamatracker.Models.Rating;
import com.eamatracker.Models.Seller;
import com.eamatracker.Models.SimpleUser;
import com.eamatracker.Models.Translations;
import com.eamatracker.R;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellerProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /* Recyclerview adapter for showing seller product in main */


    private static final String TAG = "SellerProductsAdapter";

    private Context mContext;
    private ProductList productList;
    private String loginKey;

    public SellerProductsAdapter(Context mContext, ProductList productList, String loginKey) {
        this.mContext = mContext;
        this.productList = productList;
        this.loginKey = loginKey;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Inflate the ui
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.product_list_layout, parent, false);

        return new sellerProductVh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ProductSimple product = productList.getProductSimpleList().get(position);

        populateProductList((sellerProductVh) holder, product);

    }


    @Override
    public int getItemCount() {
        return productList.getProductSimpleList().size();
    }

    public class sellerProductVh extends RecyclerView.ViewHolder {

        // Define the views

        TextView productDiscountTag, productName, productPrice, productPriceLast,
                personNumRating, numOrders;
        ImageView productImage;
        ProgressBar featuredProgressBar;
        CircleImageView sellerImage;
        RatingBar productRatingBar;

        public sellerProductVh(@NonNull View itemView) {
            super(itemView);

            productDiscountTag = itemView.findViewById(R.id.productDiscountTag);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productPriceLast = itemView.findViewById(R.id.productPriceLast);
            personNumRating = itemView.findViewById(R.id.personNumRating);
            numOrders = itemView.findViewById(R.id.numOrders);
            productImage = itemView.findViewById(R.id.productImage);
            featuredProgressBar = itemView.findViewById(R.id.featuredProgressBar);
            sellerImage = itemView.findViewById(R.id.sellerImage);
            productRatingBar = itemView.findViewById(R.id.productRatingBar);
        }
    }

    private void populateProductList(sellerProductVh holder, ProductSimple product) {


        //populate views

        holder.productName.setText(product.getName());

        Glide.with(mContext)
                .load(product.getPhoto1())
                .fitCenter()
                .centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.featuredProgressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }
                })
                .into(holder.productImage);

        SimpleUser seller = product.getSimpleUser();

        if (seller.getProfilePhoto() != null && !seller.getProfilePhoto().isEmpty()) {

            Glide.with(mContext)
                    .load(seller.getProfilePhoto())
                    .fitCenter()
                    .placeholder(R.drawable.profile_image)
                    .centerCrop()
                    .into(holder.sellerImage);

        }

        if (product.getNumOrders() != null && product.getNumOrders() > 0) {

            holder.numOrders.setText(Integer.toString(product.getNumOrders()));

        } else {

            holder.numOrders.setVisibility(View.GONE);
        }

        holder.productPrice.setText(mContext.getString(R.string.dollar_price, String.valueOf(product.getPriceA())));

        if (product.getPriceC() != null) {

            holder.productPriceLast.setText(mContext.getString(R.string.dollar_price_2, String.valueOf(product.getPriceC())));

        } else if (product.getPriceB() != null) {

            holder.productPriceLast.setText(mContext.getString(R.string.dollar_price_2, String.valueOf(product.getPriceB())));

        } else {

            holder.productPriceLast.setVisibility(View.GONE);
        }

        if (product.getDiscount() != null && product.getDiscount() > 0) {

            holder.productDiscountTag.setText(product.getDiscount() + " %");

        } else {

            holder.productDiscountTag.setVisibility(View.INVISIBLE);
        }


        fetchRating(product.getId(), holder);
    }

    private void fetchRating(int id, sellerProductVh holder) {


        Call<Rating> ratingCall = RetrofitClient
                .getInstance()
                .getApiService()
                .productRating(id);


        ratingCall.enqueue(new Callback<Rating>() {
            @Override
            public void onResponse(@NonNull Call<Rating> call, @NonNull Response<Rating> response) {


                if (response.isSuccessful() && response.code() == 200) {

                    Rating rating = response.body();

                    if (Objects.requireNonNull(rating).getTotalRatings() > 0) {

                        holder.personNumRating.setText(Integer.toString(rating.getTotalRatings()));

                    } else {

                        holder.personNumRating.setText(Integer.toString(0));
                    }

                    if (Objects.requireNonNull(rating).getAverageRating() > 0) {

                        holder.productRatingBar.setRating(rating.getAverageRating());

                    } else {

                        holder.productRatingBar.setRating(0);
                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<Rating> call, @NonNull Throwable t) {

            }
        });

    }
}
