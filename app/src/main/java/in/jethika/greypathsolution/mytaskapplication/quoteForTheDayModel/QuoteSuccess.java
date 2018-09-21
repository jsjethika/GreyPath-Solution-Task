package in.jethika.greypathsolution.mytaskapplication.quoteForTheDayModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuoteSuccess {

    @SerializedName("total")
    @Expose
    private Integer total;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }
}
