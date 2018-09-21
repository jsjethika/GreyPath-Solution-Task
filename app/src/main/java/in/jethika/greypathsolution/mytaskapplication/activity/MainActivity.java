package in.jethika.greypathsolution.mytaskapplication.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import org.json.JSONException;
import org.json.JSONObject;
import in.jethika.greypathsolution.mytaskapplication.AppTool;
import in.jethika.greypathsolution.mytaskapplication.OnNetworkChangeListener;
import in.jethika.greypathsolution.mytaskapplication.R;
import in.jethika.greypathsolution.mytaskapplication.databinding.ActivityMainBinding;
import in.jethika.greypathsolution.mytaskapplication.utility.NetworkUtil;
import in.jethika.greypathsolution.mytaskapplication.utility.Utils;

public class MainActivity extends AppCompatActivity implements OnNetworkChangeListener, View.OnClickListener, View.OnKeyListener, View.OnFocusChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    CallbackManager callbackManager;

    private String  profile_image, full_name, email_id;
    ActivityMainBinding binding;
    boolean isNetworkAvailable = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        initView();

    }


    public void initView() {

        isNetworkAvailable = AppTool.networkStatus();
        NetworkUtil.setOnNetworkChangeListener(this);
        callbackManager = CallbackManager.Factory.create();
        binding.loginButton.setReadPermissions("public_profile email");

        Utils.getKeyHash(MainActivity.this);
        initializeControls();

        callbackManager = CallbackManager.Factory.create();

        if(AccessToken.getCurrentAccessToken() != null){
            RequestData();
        }

    }


    private void initializeControls(){

        binding.editTextPassword.setOnKeyListener(this);
        binding.editTextEamil.setOnFocusChangeListener(this);
        binding.editTextPassword.setOnFocusChangeListener(this);
        binding.loginButton.setOnClickListener(this);

        binding.loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

             profile_image= full_name= email_id="";

                if(AccessToken.getCurrentAccessToken() != null){
                    RequestData();
                    launchHomeActivity();

                }else {
                    setErrorMessage("UnAuthorized User");
                }
            }

            @Override
            public void onCancel() {
                setErrorMessage("UnAuthorized User");
            }

            @Override
            public void onError(FacebookException exception) {

                setErrorMessage(exception.getMessage());
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if (isLoggedIn) {
            // user already signin
            Log.d(TAG, "Got cached sign-in");
            RequestData();
            launchHomeActivity();
        } else {
         //   AppTool.showToast("FaceBookSignInResult is null");

        }
    }
    public void launchHomeActivity() {

        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.putExtra("Name",full_name);
        intent.putExtra("Email",email_id);
        intent.putExtra("ProfileImage",profile_image);
        startActivity(intent);
        finish();

    }

    void setInputValidation(){

        Utils.showProgressDialog(MainActivity.this);

        if (Utils.hasText(binding.editTextEamil.getText().toString())||Utils.hasText(binding.editTextPassword.getText().toString())||!Utils.isValidEmailAddress(binding.editTextEamil.getText().toString())) {
            Utils.hideProgressDialog();
            setErrorMessage("Invalid Credentials");
        } else {
            binding.errorText.setVisibility(View.GONE);
            if(AccessToken.getCurrentAccessToken() != null) {
                AppTool.showToast("FBSignInResult is null");
            }
        }
    }


    void setErrorMessage(String errorMessage) {
        binding.errorText.setText(" ");
        binding.errorText.setVisibility(View.VISIBLE);
        binding.errorText.setText(errorMessage);
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



    public void RequestData(){
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object,GraphResponse response) {

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


}
