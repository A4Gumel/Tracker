package com.eamatracker.Models;

import com.google.gson.annotations.SerializedName;


public class Product {

    private int id;

    @SerializedName("quantity_a")
    public int quantityA;


    @SerializedName("quantity_b")
    public int quantityB;


    @SerializedName("quantity_c")
    public int quantityC;

    @SerializedName("price_a")
    private Double priceA;

    @SerializedName("price_b")
    private Double priceB;

    @SerializedName("price_c")
    private Double priceC;

    @SerializedName("old_price")
    private Double oldPrice;

    public String photo1;
    public String photo2;
    public String photo3;
    public String photo4;
    public String video;

    @SerializedName("min_order")
    private int minOrder;

    private Double weight;

    private String units;

    private Integer discount;

    @SerializedName("num_orders")
    private Integer numOrders;

    private boolean featured;

    @SerializedName("translations")
    private Translations translations;

    private Integer seller;

    public Product() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantityA() {
        return quantityA;
    }

    public void setQuantityA(int quantityA) {
        this.quantityA = quantityA;
    }

    public int getQuantityB() {
        return quantityB;
    }

    public void setQuantityB(int quantityB) {
        this.quantityB = quantityB;
    }

    public int getQuantityC() {
        return quantityC;
    }

    public void setQuantityC(int quantityC) {
        this.quantityC = quantityC;
    }

    public Double getPriceA() {
        return priceA;
    }

    public void setPriceA(Double priceA) {
        this.priceA = priceA;
    }

    public Double getPriceB() {
        return priceB;
    }

    public void setPriceB(Double priceB) {
        this.priceB = priceB;
    }

    public Double getPriceC() {
        return priceC;
    }

    public void setPriceC(Double priceC) {
        this.priceC = priceC;
    }

    public Double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(Double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public String getPhoto1() {
        return photo1;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }

    public String getPhoto2() {
        return photo2;
    }

    public void setPhoto2(String photo2) {
        this.photo2 = photo2;
    }

    public String getPhoto3() {
        return photo3;
    }

    public void setPhoto3(String photo3) {
        this.photo3 = photo3;
    }

    public String getPhoto4() {
        return photo4;
    }

    public void setPhoto4(String photo4) {
        this.photo4 = photo4;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public int getMinOrder() {
        return minOrder;
    }

    public void setMinOrder(int minOrder) {
        this.minOrder = minOrder;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Integer getNumOrders() {
        return numOrders;
    }

    public void setNumOrders(Integer numOrders) {
        this.numOrders = numOrders;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public Translations getTranslations() {
        return translations;
    }

    public void setTranslations(Translations translations) {
        this.translations = translations;
    }

    public Integer getSeller() {
        return seller;
    }

    public void setSeller(Integer seller) {
        this.seller = seller;
    }
}
