package in.jethika.greypathsolution.mytaskapplication.quoteForTheDayModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuoteForTheDay {

    @SerializedName("success")
    @Expose
    private QuoteSuccess quoteSuccess;

    @SerializedName("contents")
    @Expose
    private QuoteContents quoteContents;

    public QuoteSuccess getQuoteSuccess() {
        return quoteSuccess;
    }

    public void setQuoteSuccess(QuoteSuccess quoteSuccess) {
        this.quoteSuccess = quoteSuccess;
    }

    public QuoteContents getQuoteContents() {
        return quoteContents;
    }

    public void setQuoteContents(QuoteContents quoteContents) {
        this.quoteContents = quoteContents;
    }
}
