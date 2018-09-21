package in.jethika.greypathsolution.mytaskapplication.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import com.squareup.picasso.Picasso;
import java.util.List;
import in.jethika.greypathsolution.mytaskapplication.AppConstant;
import in.jethika.greypathsolution.mytaskapplication.R;
import in.jethika.greypathsolution.mytaskapplication.TaskPreferences;
import in.jethika.greypathsolution.mytaskapplication.adapter.QuoteRecyclerViewAdapter;
import in.jethika.greypathsolution.mytaskapplication.databinding.ActivityHomeBinding;
import in.jethika.greypathsolution.mytaskapplication.mvpHome.HomeContractor;
import in.jethika.greypathsolution.mytaskapplication.mvpHome.HomePresenter;
import in.jethika.greypathsolution.mytaskapplication.quoteForTheDayModel.Quotes;
import in.jethika.greypathsolution.mytaskapplication.utility.Utils;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,HomeContractor.View {

    private static final String TAG = HomeActivity.class.getSimpleName();

  //  GoogleApiClient googleApiClient = null;

 //   GoogleSignInAccount googleSignInAccount = null;

    ActivityHomeBinding binding;

    HomePresenter presenter;

    String name,emailId,profileUrl;

    Menu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home);
        onInitView();
    }

    public void onInitView() {
    //    googleApiClient = AppTool.getGoogleApiClient();
  //      googleSignInAccount =  AppTool.getGoogleSignInAccount();

        setActionBarSupport();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.appBar.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navView.setNavigationItemSelectedListener(this);


        setView();

    }


    private void setActionBarSupport() {

        setSupportActionBar(binding.appBar.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setTitle("Home");

    }


    private void setView(){


        name = getIntent().getStringExtra("Name");
        emailId = getIntent().getStringExtra("Email");
        profileUrl = getIntent().getStringExtra("ProfileImage");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        binding.quoteList.setLayoutManager(linearLayoutManager);

        setAPICall();

    }

    void setAPICall(){


        View navHeader = binding.navView.getHeaderView(0);
        ImageView profileImage = (ImageView) navHeader.findViewById(R.id.profileImage);
        TextView profileUserName = (TextView) navHeader.findViewById(R.id.profileUserName);
        TextView profileUserId = (TextView) navHeader.findViewById(R.id.profileUserId);

        profileUserName.setText(name);
        profileUserId.setText(emailId);

        Picasso.with(HomeActivity.this).load(profileUrl)
                .placeholder(android.R.drawable.btn_star)
                .error(android.R.drawable.btn_star)
                .into(profileImage);


        String startDateTime = TaskPreferences.get(AppConstant.refreshTimePreference);
        int apiHitCount = TaskPreferences.getCount(AppConstant.refreshCountPreference);

        if( (startDateTime== null|| (Utils.hoursAgo(startDateTime) < 1) ) && apiHitCount < 10){

            Utils.showProgressDialog(HomeActivity.this);
            presenter = new HomePresenter(this,this);
            presenter.requestToAPI();
        }else {

            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                TaskPreferences.saveStatus(AppConstant.refreshStatusPreference, false);
                TaskPreferences.saveCount(AppConstant.refreshCountPreference, 0);
                TaskPreferences.save(AppConstant.refreshCountPreference, null);

            }, 3600000);


            Utils.showAlertOk(HomeActivity.this, "Plz wait !!!, You Exceed the Limit for one Hour", "Alert!", (dialogInterface, i) -> {
                dialogInterface.dismiss();
                hideOption(R.id.action_refresh);
            });
        }
    }



    @Override
    public void onBackPressed() {

        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            signOutFacebook();
        }else if(id == R.id.action_refresh){
            setAPICall();
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void hideOption(int id) {
        MenuItem item = menu.findItem(id);
        item.setVisible(false);
    }

    public void showOption(int id) {
        MenuItem item = menu.findItem(id);
        item.setVisible(true);
    }


/*    private void signOutGoogle() {
        if(null != googleApiClient){

            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            googleSignInAccount = null;
                            finish();
                            System.exit(0);
                        }
                    });
        }else {
            AppTool.showToast(getString(R.string.gPlusLogout));
        }

    }*/


    public void signOutFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();
                launchFBSignInActivity();

            }
        }).executeAsync();
    }

    public void launchFBSignInActivity() {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void setResponseToView(List<Quotes> quotesList) {
        TaskPreferences.saveStatus(AppConstant.refreshStatusPreference, true);
        Utils.hideProgressDialog();
        QuoteRecyclerViewAdapter adapter = new QuoteRecyclerViewAdapter(HomeActivity.this,quotesList);
        binding.quoteList.setAdapter(adapter);
        showOption(R.id.action_refresh);
    }
}
