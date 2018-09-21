package in.jethika.greypathsolution.mytaskapplication.mvpHome;

import android.content.Context;


import in.jethika.greypathsolution.mytaskapplication.AppConstant;
import in.jethika.greypathsolution.mytaskapplication.RestApi;
import in.jethika.greypathsolution.mytaskapplication.RetrofitBuilder;
import in.jethika.greypathsolution.mytaskapplication.TaskPreferences;
import in.jethika.greypathsolution.mytaskapplication.quoteForTheDayModel.QuoteForTheDay;
import in.jethika.greypathsolution.mytaskapplication.utility.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeModel  implements  HomeContractor.Model{

    Context context;
    HomePresenter presenter;
    int apiHitCount = 0;

    public HomeModel(Context context, HomePresenter presenter) {
        this.context = context;
        this.presenter = presenter;
    }

    @Override
    public void callAPI() {

        RestApi restApi = RetrofitBuilder.getRetrofit().create(RestApi.class);
        Call<QuoteForTheDay> quoteForTheDay = restApi.getQuoteForTheDay();

        quoteForTheDay.enqueue(new Callback<QuoteForTheDay>() {
            @Override
            public void onResponse(Call<QuoteForTheDay> call, Response<QuoteForTheDay> response) {
                apiHitCount = TaskPreferences.getCount(AppConstant.refreshCountPreference)+1;
                TaskPreferences.saveCount(AppConstant.refreshCountPreference, apiHitCount);
                if(!TaskPreferences.getStatus(AppConstant.refreshStatusPreference))TaskPreferences.save(AppConstant.refreshTimePreference, Utils.currentTime());
                QuoteForTheDay quote = response.body();
                presenter.responseFromAPI(quote.getQuoteContents().getQuotes());
            }

            @Override
            public void onFailure(Call<QuoteForTheDay> call, Throwable t) {
                call.cancel();
            }
        });
    }


}
