package in.jethika.greypathsolution.mytaskapplication.mvpHome;

import android.content.Context;
import java.util.List;
import in.jethika.greypathsolution.mytaskapplication.quoteForTheDayModel.Quotes;


public class HomePresenter implements  HomeContractor.Presenter{

    Context context;
    HomeModel model;
    HomeContractor.View view;


    public HomePresenter(Context context, HomeContractor.View view) {
        this.context = context;
        this.view = view;
        model = new HomeModel(context,this);
    }

    @Override
    public void requestToAPI() {
        model.callAPI();
    }

    @Override
    public void responseFromAPI(List<Quotes> quotesList) {
       view.setResponseToView(quotesList);
    }
}
