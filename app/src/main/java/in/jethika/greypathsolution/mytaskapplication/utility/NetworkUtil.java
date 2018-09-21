package in.jethika.greypathsolution.mytaskapplication.utility;

/**
 * Created by admin on 6/2/2017.
 */


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import in.jethika.greypathsolution.mytaskapplication.OnNetworkChangeListener;

public class NetworkUtil {

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;

    static OnNetworkChangeListener onNetworkChangeListener;

    public static  void setOnNetworkChangeListener(OnNetworkChangeListener onNetworkChangeListener) {
        NetworkUtil.onNetworkChangeListener = onNetworkChangeListener;
    }

    // check the type of network connection if connected

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    //  returns message based on network connection

    public static String getConnectivityStatusString(Context context) {
        int conn = getConnectivityStatus(context);
        String status = null;
        if (conn == NetworkUtil.TYPE_WIFI) {
            status = "Connected to Internet with WIFI";
        } else if (conn == NetworkUtil.TYPE_MOBILE) {
            status = "Connected to Internet with Mobile Data";
        } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
            status = "Internet connection required";
        }
        if(null != onNetworkChangeListener) {
            onNetworkChangeListener.onChange(status);
        }
        return status;
    }

}
