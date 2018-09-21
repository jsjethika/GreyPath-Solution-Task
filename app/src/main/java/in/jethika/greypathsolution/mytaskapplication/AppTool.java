package in.jethika.greypathsolution.mytaskapplication;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;

import in.jethika.greypathsolution.mytaskapplication.utility.NetworkUtil;


/**
 * Created by admin on 6/1/2017.
 */

public class AppTool {

    private static final String TAG =AppTool.class.getName();

    private static GoogleApiClient googleApiClient;
    private static GoogleSignInAccount googleSignInAccount;

    public static GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    public static void setGoogleApiClient(GoogleApiClient googleApiClient) {
        AppTool.googleApiClient = googleApiClient;
    }

    public static GoogleSignInAccount getGoogleSignInAccount() {
        return googleSignInAccount;
    }

    public static void setGoogleSignInAccount(GoogleSignInAccount googleSignInAccount) {
        AppTool.googleSignInAccount = googleSignInAccount;
    }

    public static boolean networkStatus() {
        boolean isNetworkAvailable;
        String status = NetworkUtil.getConnectivityStatusString(MyApplication.getContext());
        Log.d(TAG, "Network Availability");
        Log.d(TAG, status);

        switch (status) {
            case "Connected to Internet with Mobile Data":
                isNetworkAvailable = true;
                break;
            case "Connected to Internet with WIFI":
                isNetworkAvailable = true;
                break;
            default:
                AppTool.showToast(status);
                isNetworkAvailable = false;
                break;
        }
        return isNetworkAvailable;
    }

    public static boolean networkStatus(String status) {
        boolean isNetworkAvailable;
       Log.d(TAG, "Network Availability");
       Log.d(TAG, status);

        switch (status) {
            case "Connected to Internet with Mobile Data":
                isNetworkAvailable = true;
                break;
            case "Connected to Internet with WIFI":
                isNetworkAvailable = true;
                break;
            default:
                AppTool.showToast(status);
                isNetworkAvailable = false;
                break;
        }
        return isNetworkAvailable;
    }

    public static void showToast(String message) {
        try {
            final Context context = MyApplication.getContext();
            final CharSequence text = message;
            final int duration = Toast.LENGTH_SHORT;
            Handler mainHandler = new Handler(context.getMainLooper());
            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {
                    Toast toast = Toast.makeText(context, "  " + text + "  ", duration);
                    toast.show();
                }
            };
            mainHandler.post(myRunnable);
        } catch (Exception e) {
           Log.e(TAG, e.toString());
        }

    }




}
