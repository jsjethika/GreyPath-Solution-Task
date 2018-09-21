package in.jethika.greypathsolution.mytaskapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class TaskPreferences {

    private static final String TAG = TaskPreferences.class.getName();

    public static void save(String Key, String Value) {
        SharedPreferences preference;
        SharedPreferences.Editor editor;
        preference = MyApplication.getContext().getSharedPreferences(MyApplication.getContext().getString(R.string.app_name),
                Context.MODE_PRIVATE);
        editor = preference.edit();
        editor.putString(Key, Value);
        editor.commit();
    }

    public static String get(String Key) {
        SharedPreferences preference;
        String text;
        preference = MyApplication.getContext().getSharedPreferences(MyApplication.getContext().getString(R.string.app_name),
                Context.MODE_PRIVATE);
        text = preference.getString(Key, null);
       Log.d(TAG, "text: "+text);
        return text;
    }

    public static void saveStatus(String Key, Boolean statusValue) {
        SharedPreferences preference;
        SharedPreferences.Editor editor;
        preference = MyApplication.getContext().getSharedPreferences(MyApplication.getContext().getString(R.string.app_name),
                Context.MODE_PRIVATE);
        editor = preference.edit();
        editor.putBoolean(Key, statusValue);
        editor.commit();
    }

    public static Boolean getStatus(String Key) {
        SharedPreferences preference;
        Boolean statusValue;
        preference = MyApplication.getContext().getSharedPreferences(MyApplication.getContext().getString(R.string.app_name),
                Context.MODE_PRIVATE);
        statusValue = preference.getBoolean(Key, false);
        Log.d(TAG, "statusValue: "+statusValue);
        return statusValue;
    }

    public static void saveCount(String Key, int countValue) {
        SharedPreferences preference;
        SharedPreferences.Editor editor;
        preference = MyApplication.getContext().getSharedPreferences(MyApplication.getContext().getString(R.string.app_name),
                Context.MODE_PRIVATE);
        editor = preference.edit();
        editor.putInt(Key, countValue);
        editor.commit();
    }

    public static int getCount(String Key) {
        SharedPreferences preference;
        int countValue;
        preference = MyApplication.getContext().getSharedPreferences(MyApplication.getContext().getString(R.string.app_name),
                Context.MODE_PRIVATE);
        countValue = preference.getInt(Key, 0);
        Log.d(TAG, "countValue: "+countValue);
        return countValue;
    }

    public static void clear(String Key) {
        SharedPreferences preference;
        SharedPreferences.Editor editor;
        preference = MyApplication.getContext().getSharedPreferences(MyApplication.getContext().getString(R.string.app_name),
                Context.MODE_PRIVATE);
        editor = preference.edit();
        editor.remove(Key);
        editor.commit();
    }

    public static void clearAll() {
        SharedPreferences preference;
        SharedPreferences.Editor editor;
        preference = MyApplication.getContext().getSharedPreferences(MyApplication.getContext().getString(R.string.app_name),
                Context.MODE_PRIVATE);
        editor = preference.edit();
        editor.clear();
        editor.commit();
    }
}


