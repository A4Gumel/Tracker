package com.eamatracker.Tracker.Adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.eamatracker.Models.Banner;
import com.eamatracker.Models.BannerList;
import com.eamatracker.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class HomeBannerAdapter extends SliderViewAdapter<HomeBannerAdapter.BannerVH> {

    private final Context mContext;
    private final BannerList mBannerList;

    public HomeBannerAdapter(Context context, BannerList bannerList) {
        mContext = context;
        mBannerList = bannerList;
    }

    @Override
    public BannerVH onCreateViewHolder(ViewGroup parent) {
        View inflateView = LayoutInflater
                .from(mContext)
                .inflate(R.layout.home_banner_image, parent, false);

        return new BannerVH(inflateView);
    }

    @Override
    public void onBindViewHolder(BannerVH viewHolder, int position) {

        Banner banner = mBannerList.getBannerList().get(position);

        Glide.with(mContext)
                .load(banner.getImage())
                .fitCenter()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        viewHolder.bannerLoading.setVisibility(View.INVISIBLE);
                        return false;
                    }
                })
                .centerCrop()
                .into(viewHolder.bannerImage);

    }

    @Override
    public int getCount() {
        return mBannerList.getBannerList().size();
    }

    protected static class BannerVH extends SliderViewAdapter.ViewHolder {

        private final ImageView bannerImage;
        private final ProgressBar bannerLoading;

        public BannerVH(View itemView) {
            super(itemView);

            bannerImage = itemView.findViewById(R.id.bannerImg);
            bannerLoading = itemView.findViewById(R.id.bannerProgress);
        }
    }

}