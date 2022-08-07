package com.eamatracker.Models;

import com.firebase.geofire.GeoLocation;

public class DriverGeoModel {

    private String key;
    private GeoLocation mGeoLocation;
    private UserDetails mUserDetails;

    public DriverGeoModel(String key, GeoLocation geoLocation) {
        this.key = key;
        mGeoLocation = geoLocation;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public GeoLocation getGeoLocation() {
        return mGeoLocation;
    }

    public void setGeoLocation(GeoLocation geoLocation) {
        mGeoLocation = geoLocation;
    }

    public UserDetails getUserDetails() {
        return mUserDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        mUserDetails = userDetails;
    }
}
