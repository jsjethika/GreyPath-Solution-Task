package in.jethika.greypathsolution.mytaskapplication;

/**
 * Created by admin on 6/2/2017.
 */


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import in.jethika.greypathsolution.mytaskapplication.utility.NetworkUtil;

public class NetworkChangeReceiver extends BroadcastReceiver {

    private static final String TAG = NetworkChangeReceiver.class.getName();

    @Override
    public void onReceive(final Context context, final Intent intent) {

        String status = NetworkUtil.getConnectivityStatusString(context);
        Log.d(TAG,status);

    }

}
