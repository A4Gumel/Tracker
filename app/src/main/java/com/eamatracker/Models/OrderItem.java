package com.eamatracker.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class OrderItem {

    private int id;

    @SerializedName("date_created")
    private Date dateCreated;

    @SerializedName("date_completed")
    private Date dateCompleted;

    @SerializedName("date_shipped")
    private Date dateShipped;

    @SerializedName("date_received")
    private Date dateReceived;

    @SerializedName("order_status")
    private String orderStatus;

    @SerializedName("total_amount")
    private Double totalAmount;

    @SerializedName("total_weight")
    private Double totalWeight;

    @SerializedName("shipping_amount")
    private Double shippingAmount;

    private int quantity;
    private Integer discount;
    private String grade;

    @SerializedName("ref_code")
    private String refCode;

    @SerializedName("tracking_code")
    private String trackingCode;

    @SerializedName("refund_reason")
    private String refundReason;

    @SerializedName("refund_status")
    private String refundStatus;

    @SerializedName("refund_request_date")
    private Date refundRequestDate;

    @SerializedName("refund_grant_date")
    private Date refundGrantDate;

    private boolean reviewed;
    private int user;

    @SerializedName("shipping_company")
    private int shippingCompany;
    private int item;

    public OrderItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(Date dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public Date getDateShipped() {
        return dateShipped;
    }

    public void setDateShipped(Date dateShipped) {
        this.dateShipped = dateShipped;
    }

    public Date getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(Date dateReceived) {
        this.dateReceived = dateReceived;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(Double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public Double getShippingAmount() {
        return shippingAmount;
    }

    public void setShippingAmount(Double shippingAmount) {
        this.shippingAmount = shippingAmount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getRefCode() {
        return refCode;
    }

    public void setRefCode(String refCode) {
        this.refCode = refCode;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public Date getRefundRequestDate() {
        return refundRequestDate;
    }

    public void setRefundRequestDate(Date refundRequestDate) {
        this.refundRequestDate = refundRequestDate;
    }

    public Date getRefundGrantDate() {
        return refundGrantDate;
    }

    public void setRefundGrantDate(Date refundGrantDate) {
        this.refundGrantDate = refundGrantDate;
    }

    public boolean isReviewed() {
        return reviewed;
    }

    public void setReviewed(boolean reviewed) {
        this.reviewed = reviewed;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getShippingCompany() {
        return shippingCompany;
    }

    public void setShippingCompany(int shippingCompany) {
        this.shippingCompany = shippingCompany;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }
}
