package com.eamatracker.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class TransferRecipientData {

    private boolean active;

    private Date createdAt;

    private String currency;

    private String domain;

    private Integer id;

    private Integer integration;

    private String name;

    @SerializedName("recipient_code")
    private String recipientCode;

    private String type;

    private Date updatedAt;

    @SerializedName("is_deleted")
    private boolean isDeleted;


    @SerializedName("details")
    private TransferRecipientDetails recipientDetails;

    public TransferRecipientData() {
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIntegration() {
        return integration;
    }

    public void setIntegration(Integer integration) {
        this.integration = integration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecipientCode() {
        return recipientCode;
    }

    public void setRecipientCode(String recipientCode) {
        this.recipientCode = recipientCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public TransferRecipientDetails getRecipientDetails() {
        return recipientDetails;
    }

    public void setRecipientDetails(TransferRecipientDetails recipientDetails) {
        this.recipientDetails = recipientDetails;
    }
}
