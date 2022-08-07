package com.eamatracker.Models;

import com.google.gson.annotations.SerializedName;

public class Rating {

    @SerializedName("av_rating")
    private float averageRating;

    @SerializedName("rating_five_stars")
    private int fiveStarsRating;

    @SerializedName("rating_four_stars")
    private int fourStarRating;

    @SerializedName("rating_three_stars")
    private int threeStarsRating;

    @SerializedName("rating_two_stars")
    private int twoStarsRating;

    @SerializedName("rating_one_star")
    private int oneStarRating;

    @SerializedName("total_ratings")
    private int totalRatings;


    public Rating() {
    }

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    public int getFiveStarsRating() {
        return fiveStarsRating;
    }

    public void setFiveStarsRating(int fiveStarsRating) {
        this.fiveStarsRating = fiveStarsRating;
    }

    public int getFourStarRating() {
        return fourStarRating;
    }

    public void setFourStarRating(int fourStarRating) {
        this.fourStarRating = fourStarRating;
    }

    public int getThreeStarsRating() {
        return threeStarsRating;
    }

    public void setThreeStarsRating(int threeStarsRating) {
        this.threeStarsRating = threeStarsRating;
    }

    public int getTwoStarsRating() {
        return twoStarsRating;
    }

    public void setTwoStarsRating(int twoStarsRating) {
        this.twoStarsRating = twoStarsRating;
    }

    public int getOneStarRating() {
        return oneStarRating;
    }

    public void setOneStarRating(int oneStarRating) {
        this.oneStarRating = oneStarRating;
    }

    public int getTotalRatings() {
        return totalRatings;
    }

    public void setTotalRatings(int totalRatings) {
        this.totalRatings = totalRatings;
    }
}