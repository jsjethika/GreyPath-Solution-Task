package in.jethika.greypathsolution.mytaskapplication.activity;

import android.content.Intent;
import android.content.IntentSender;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import java.util.Arrays;

import in.jethika.greypathsolution.mytaskapplication.AppTool;
import in.jethika.greypathsolution.mytaskapplication.utility.NetworkUtil;
import in.jethika.greypathsolution.mytaskapplication.OnNetworkChangeListener;
import in.jethika.greypathsolution.mytaskapplication.R;
import in.jethika.greypathsolution.mytaskapplication.databinding.ActivitySigninBinding;
import in.jethika.greypathsolution.mytaskapplication.utility.Utils;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, OnNetworkChangeListener, View.OnKeyListener, View.OnFocusChangeListener{



    private static final String TAG = SignInActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 1;

    private GoogleApiClient googleApiClient;
    ActivitySigninBinding binding;

    boolean isNetworkAvailable = false;

    CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_signin);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        initView();
    }

    public void initView() {

        isNetworkAvailable = AppTool.networkStatus();
        NetworkUtil.setOnNetworkChangeListener(this);

        initializeControls();
        googleApiClient = AppTool.getGoogleApiClient();

        callbackManager = CallbackManager.Factory.create();

    }


    private void initializeControls(){

        binding.buttonLogin.setOnClickListener(this);
        binding.editTextPassword.setOnKeyListener(this);
        binding.editTextEamil.setOnFocusChangeListener(this);
        binding.editTextPassword.setOnFocusChangeListener(this);
        binding.loginButton.setOnClickListener(this);

    }



    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> optionalPendingResult = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (optionalPendingResult.isDone()) {
            // user already signin
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = optionalPendingResult.get();
            handleGPlusSignInResult(result,false);
        } else {
            // user not signin
            Utils.showProgressDialog(SignInActivity.this);
            optionalPendingResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    Utils.hideProgressDialog();
                    handleGPlusSignInResult(googleSignInResult,true);
                }
            });
        }


        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if (isLoggedIn) {
            // user already signin
            Log.d(TAG, "Got cached sign-in");
            binding.loginButton.setReadPermissions(Arrays.asList("user_status"));
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
            launchHomeActivity();
        } else {
            binding.loginLayout.setVisibility(View.VISIBLE);
            binding.alreadyLoginLayout.setVisibility(View.GONE);
            AppTool.showToast("FaceBookSignInResult is null");

        }
    }


    @Override
    public void onChange(String status) {
        isNetworkAvailable = AppTool.networkStatus(status);
    }


    @Override
    public void onClick(View v) {
        if (!isNetworkAvailable) {
            AppTool.showToast(getResources().getString(R.string.networkMsg));
        }else {
            setInputValidation();
        }

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (keyCode == EditorInfo.IME_ACTION_GO ||
                keyCode == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

            switch (v.getId()){
                case R.id.editTextPassword:

                    if (!isNetworkAvailable) {
                        AppTool.showToast(getResources().getString(R.string.networkMsg));
                    }else {
                        setInputValidation();
                    }

                    break;
            }
        }

        return false;
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            binding.errorText.setVisibility(View.GONE);
        }
    }


    void setInputValidation(){

        Utils.showProgressDialog(SignInActivity.this);

        if (Utils.hasText(binding.editTextEamil.getText().toString())||Utils.hasText(binding.editTextPassword.getText().toString())||!Utils.isValidEmailAddress(binding.editTextEamil.getText().toString())) {
            Utils.hideProgressDialog();
            setErrorMessage("Invalid Credentials");
        } else {

            binding.errorText.setVisibility(View.GONE);
            Utils.hideProgressDialog();
            GSignIn();

        }
    }

    private void GSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }




    void setErrorMessage(String errorMessage) {
        binding.errorText.setText(" ");
         binding.errorText.setVisibility(View.VISIBLE);
        binding.errorText.setText(errorMessage);
    }

    private void handleGPlusSignInResult(GoogleSignInResult result,boolean signInStatus) {

        if(null != result){
            Log.d(TAG, "handleSignInResult:" + result.isSuccess());
           // updateUI(signInStatus);
            if (result.isSuccess()) {
                // Signed in successfully, show authenticated UI.
                GoogleSignInAccount googleSignInAccount = result.getSignInAccount();
                AppTool.setGoogleSignInAccount(googleSignInAccount);
                launchHomeActivity();
            } else {
                binding.loginLayout.setVisibility(View.VISIBLE);
                binding.alreadyLoginLayout.setVisibility(View.GONE);
                // Signed out, show unauthenticated UI.
                setErrorMessage("UnAuthorized User");
            }
        }else {
            AppTool.showToast("GoogleSignInResult is null");
        }
    }


    public void launchHomeActivity() {

        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGPlusSignInResult(result,true);
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {

                // try to start the intent to resolve error

                connectionResult.startResolutionForResult(SignInActivity.this, RC_SIGN_IN);

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

    }


    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            binding.loginLayout.setVisibility(View.VISIBLE);
            binding.alreadyLoginLayout.setVisibility(View.GONE);
        } else {
            binding.loginLayout.setVisibility(View.GONE);
            binding.alreadyLoginLayout.setVisibility(View.VISIBLE);
        }
    }

}
