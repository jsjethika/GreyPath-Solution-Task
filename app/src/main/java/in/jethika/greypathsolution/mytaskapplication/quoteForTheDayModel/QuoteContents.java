package in.jethika.greypathsolution.mytaskapplication.quoteForTheDayModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class QuoteContents {

    @SerializedName("quotes")
    @Expose
    private List<Quotes> quotes = new ArrayList<>();

    @SerializedName("copyright")
    @Expose
    private String copyright;

    public List<Quotes> getQuotes() {
        return quotes;
    }

    public void setQuotes(List<Quotes> quotes) {
        this.quotes = quotes;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }
}
