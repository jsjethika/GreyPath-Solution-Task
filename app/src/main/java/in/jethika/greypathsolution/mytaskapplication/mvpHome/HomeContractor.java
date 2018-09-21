package in.jethika.greypathsolution.mytaskapplication.mvpHome;

import java.util.List;
import in.jethika.greypathsolution.mytaskapplication.quoteForTheDayModel.Quotes;

public interface HomeContractor {

    interface View{

        void setResponseToView(List<Quotes> quotesList);
    }

    interface Model{

        void callAPI();

    }


    interface Presenter{

        void requestToAPI();

        void responseFromAPI(List<Quotes> quotesList);
    }

}
