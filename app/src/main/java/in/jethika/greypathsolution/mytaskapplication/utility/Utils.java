package in.jethika.greypathsolution.mytaskapplication.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import in.jethika.greypathsolution.mytaskapplication.R;

public class Utils {

    static String TAG = Utils.class.getName();
    static ProgressDialog progressDialog;


    public static void showProgressDialog(Activity activity) {

        activity.runOnUiThread(() -> {

            if (progressDialog == null) {
                progressDialog = new ProgressDialog(activity);
                progressDialog.setMessage(activity.getResources().getString(R.string.loading));
                progressDialog.setIndeterminate(true);
            }
            progressDialog.show();
        });

    }

    public static void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }

    public static String currentTime()
    {
        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String dateNow = formatter.format(currentDate.getTime());
        return dateNow;
    }


    public static int hoursAgo(String datetime) {
        Calendar date = Calendar.getInstance();
        try {
            date.setTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH).parse(datetime)); // Parse into Date object
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }
        Calendar now = Calendar.getInstance(); // Get time now
        long differenceInMillis = now.getTimeInMillis() - date.getTimeInMillis();
        long differenceInHours = (differenceInMillis) / 1000L / 60L / 60L; // Divide by millis/sec, secs/min, mins/hr
        return (int)differenceInHours;
    }



    public static boolean hasText(String string) {
        return string != null && !string.trim().equals("");
    }



    public static boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = Pattern.compile(ePattern);
        Matcher m = p.matcher(email);
        return m.matches();
    }



    public static void showAlertOk(Context context, String message, String title, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", onClickListener);
        builder.show();
    }


    public static void getKeyHash(Activity activity) {
        try {
            PackageInfo info = activity.getPackageManager().getPackageInfo(
                    activity.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d(TAG,"KeyHash: "+Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e(TAG,"name not found"+ e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG,"no such an algorithm"+ e.toString());
        } catch (Exception e) {
            Log.e(TAG,"exception"+ e.toString());
        }
    }
}
