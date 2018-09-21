package in.jethika.greypathsolution.mytaskapplication;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {

    private static Retrofit retrofit;

    public static Retrofit getRetrofit(){

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor().
                setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().
                addInterceptor(httpLoggingInterceptor).build();
        retrofit = new Retrofit.Builder().baseUrl("http://quotes.rest").
                addConverterFactory(GsonConverterFactory.create()).
                client(okHttpClient).build();

        return retrofit;
    }
}
