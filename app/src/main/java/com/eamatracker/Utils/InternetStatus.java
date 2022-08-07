package com.eamatracker.Utils;

import android.content.Context;
import android.net.ConnectivityManager;

public class InternetStatus {

    private Context mContext;

    public InternetStatus(Context context){
        this.mContext = context;
    }

    public boolean isConnectingToInternet(){

        ConnectivityManager cm = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected() == true)
        {
            return true;
        }

        return false;

    }
}