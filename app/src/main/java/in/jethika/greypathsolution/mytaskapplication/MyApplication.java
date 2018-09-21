package in.jethika.greypathsolution.mytaskapplication;


import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class MyApplication extends MultiDexApplication {

    private static final String TAG = MyApplication.class.getName();


    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        AppEventsLogger.activateApp(this);
    }



    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    public static Context getContext() {
        return mContext;
    }

}

