package com.events.Network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Arjun.
 */

public class NetworkStatus {
    public static boolean isNetworkAvailable(Context mContext){
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork!=null){
            return activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
        }else{
            return false;
        }
    }
}
