package in.jethika.greypathsolution.mytaskapplication;

import in.jethika.greypathsolution.mytaskapplication.quoteForTheDayModel.QuoteForTheDay;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RestApi {

    @GET("/qod.json")
    Call<QuoteForTheDay> getQuoteForTheDay();

}
