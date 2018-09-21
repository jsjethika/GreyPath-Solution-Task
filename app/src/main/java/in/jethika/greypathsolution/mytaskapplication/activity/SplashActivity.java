package in.jethika.greypathsolution.mytaskapplication.activity;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;

import org.json.JSONException;
import org.json.JSONObject;

import in.jethika.greypathsolution.mytaskapplication.AppTool;
import in.jethika.greypathsolution.mytaskapplication.utility.NetworkUtil;
import in.jethika.greypathsolution.mytaskapplication.OnNetworkChangeListener;
import in.jethika.greypathsolution.mytaskapplication.R;


public class SplashActivity extends AppCompatActivity implements OnNetworkChangeListener {

    private static final String TAG = SplashActivity.class.getSimpleName();

    private GoogleApiClient googleApiClient;

    private static final int RC_SIGN_IN = 1;

    boolean isNetworkAvailable = false;
    private String  profile_image, full_name, email_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        isNetworkAvailable = AppTool.networkStatus();
        NetworkUtil.setOnNetworkChangeListener(this);

      //  initializeGPlusSettings();

    }

    @Override
    public void onStart() {
        super.onStart();

        AppTool.setGoogleApiClient(googleApiClient);

        final Handler handler = new Handler();
        handler.postDelayed(() -> {


            if (!isNetworkAvailable) {
                AppTool.showToast(getResources().getString(R.string.networkMsg));
            }else {


                // TODO Commented my code because of developer console is not free

             /*   OptionalPendingResult<GoogleSignInResult> optionalPendingResult = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
                if (optionalPendingResult.isDone()) {
                    // user already signin
                    Log.d(TAG, "Got cached sign-in");
                    GoogleSignInResult result = optionalPendingResult.get();
                    handleGPlusSignInResult(result);
                } else {
                    launchSignInActivity();

                }*/

                if(AccessToken.getCurrentAccessToken() != null){
                    RequestData();
                    launchHomeActivity();

                }else {
                    launchFBSignInActivity();
                }

            }

        }, 100);

    }


    public void RequestData(){
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                JSONObject json = response.getJSONObject();
                Log.d(TAG, "Json data : "+json);
                try {
                    if(json != null){
                        String text = "<b>Name :</b> "+json.getString("name")+"<br><br><b>Email :</b> "+json.getString("email")+"<br><br><b>Profile link :</b> "+json.getString("link");
                        Log.d(TAG, "text : "+text);
                        email_id =  json.getString("email");
                    }

                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();


        Profile profile = Profile.getCurrentProfile();
        if (profile != null) {

            full_name=profile.getName();
            Log.d(TAG,"full_name: "+ full_name);
            profile_image=profile.getProfilePictureUri(400, 400).toString();
            Log.d(TAG,"profile_image: "+ profile_image);

        }
    }

    @Override
    public void onChange(String status) {
        isNetworkAvailable = AppTool.networkStatus(status);
    }



/*    private void initializeGPlusSettings(){

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(SplashActivity.this)
                .enableAutoManage(SplashActivity.this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

    }*/


    private void handleGPlusSignInResult(GoogleSignInResult result) {

        if(null != result){
            Log.d(TAG, "handleSignInResult:" + result.isSuccess());
            if (result.isSuccess()) {
                // Signed in successfully, show authenticated UI.
                GoogleSignInAccount googleSignInAccount = result.getSignInAccount();
                AppTool.setGoogleSignInAccount(googleSignInAccount);
                launchHomeActivity();
            } else {
                // Signed out, show unauthenticated UI.
                AppTool.showToast(result.getStatus().getStatusMessage());
            }
        }else {
            AppTool.showToast("GoogleSignInResult is null");
        }
    }


    public void launchGSignInActivity() {

        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(intent);
        finish();

    }

    public void launchFBSignInActivity() {

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();

    }

    public void launchHomeActivity() {

        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.putExtra("Name",full_name);
        intent.putExtra("Email",email_id);
        intent.putExtra("ProfileImage",profile_image);
        startActivity(intent);
        finish();

    }
/*
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {

                // try to start the intent to resolve error

                connectionResult.startResolutionForResult(SplashActivity.this, RC_SIGN_IN);

            } catch (IntentSender.SendIntentException e) {
                Log.e(TAG, "Could not resolve ConnectionResult.",e);

                if (googleApiClient != null) {
                    googleApiClient.disconnect();
                    googleApiClient.connect();
                }
            }
        }else {
            AppTool.showToast(connectionResult.getErrorMessage());
        }
    }*/
}
