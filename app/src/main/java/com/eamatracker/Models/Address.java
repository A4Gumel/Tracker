package com.eamatracker.Models;

import com.google.gson.annotations.SerializedName;

public class Address {

    //User Address model

    private int id;

    @SerializedName("phone_number")
    private String phoneNumber;

    private String country;

    private String state;

    @SerializedName("street_address")
    private String streetAddress;

    @SerializedName("contact_name")
    private String contactName;

    private String city;

    @SerializedName("additional_info")
    private String additionalInfo;

    @SerializedName("zip_code")
    private String zipCode;

    private Double longitude;
    private Double latitude;

    @SerializedName("default")
    private boolean isDefault;


    public Address(String phoneNumber, String country, String state, String streetAddress,
                   String contactName, String city, String additionalInfo, String zipCode,
                   Double longitude, Double latitude, boolean isDefault) {
        this.phoneNumber = phoneNumber;
        this.country = country;
        this.state = state;
        this.streetAddress = streetAddress;
        this.contactName = contactName;
        this.city = city;
        this.additionalInfo = additionalInfo;
        this.zipCode = zipCode;
        this.longitude = longitude;
        this.latitude = latitude;
        this.isDefault = isDefault;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
