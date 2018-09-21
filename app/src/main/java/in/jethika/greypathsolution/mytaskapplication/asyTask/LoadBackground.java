package in.jethika.greypathsolution.mytaskapplication.asyTask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import in.jethika.greypathsolution.mytaskapplication.CallBackForDrawable;

public class LoadBackground extends AsyncTask<String, Void, Bitmap> {

    CallBackForDrawable callBackForDrawable;

    public LoadBackground(CallBackForDrawable callBackForDrawable) {
        this.callBackForDrawable = callBackForDrawable;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Bitmap doInBackground(String... urls) {


            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

    }


    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
        Drawable drawable = new BitmapDrawable(result);
        callBackForDrawable.setDrawableFromURL(drawable);

    }
}