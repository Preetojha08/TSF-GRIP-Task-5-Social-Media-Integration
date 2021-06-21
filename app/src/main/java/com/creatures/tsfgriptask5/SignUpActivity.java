package com.creatures.tsfgriptask5;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import de.hdodenhof.circleimageview.CircleImageView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Arrays;

public class SignUpActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    LoginButton loginButton;
    CallbackManager callbackManager;
    TextView fb_tv_username,fb_tv_user_id,fb_tv_email_id,fb_tv_card_text;
    RelativeLayout welcome_main_relativeLayout,fb_profile_relativeLayout;
    CircleImageView fb_imageView_profile;
    CardView fb_cardView,google_cardview;
    ImageView imageView_logo;

    //Google SignUP
    SignInButton signInButton;
    GoogleApiClient googleApiClient;
    private static final int RC_SIGN_IN = 1;
    int counter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        fb_profile_relativeLayout=(RelativeLayout)findViewById(R.id.facebook_profile_layout);

        loginButton=(LoginButton)findViewById(R.id.login_button);

        google_cardview=(CardView)findViewById(R.id.card_view_google_login);

        fb_tv_username=(TextView)findViewById(R.id.fb_text_view_user_name);
        fb_tv_email_id=(TextView)findViewById(R.id.fb_text_view_email_id);
        fb_tv_user_id=(TextView)findViewById(R.id.fb_text_view_user_id);
        fb_tv_card_text=(TextView)findViewById(R.id.text_view_on_card_view);
        imageView_logo=(ImageView)findViewById(R.id.logo_image_view);


        fb_imageView_profile=(CircleImageView)findViewById(R.id.fb_image_view_profile);

        fb_cardView=(CardView)findViewById(R.id.card_view_facebook_login);

        signInButton=(SignInButton)findViewById(R.id.google_sign_up_button);

        callbackManager = CallbackManager.Factory.create();


        fb_cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();
            }
        });

        fb_profile_relativeLayout.setVisibility(View.GONE);

        loginButton.setPermissions(Arrays.asList("email","user_birthday","user_gender","user_hometown"));
        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        Toast.makeText(SignUpActivity.this, " Successful ", Toast.LENGTH_SHORT).show();
                        fb_profile_relativeLayout.setVisibility(View.VISIBLE);
                        imageView_logo.setVisibility(View.GONE);
                        google_cardview.setVisibility(View.GONE);
                        fb_tv_card_text.setText("Logout With Facebook");

                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(SignUpActivity.this, " Unsuccessful ", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Toast.makeText(SignUpActivity.this, " Error ", Toast.LENGTH_SHORT).show();
                    }
                });

        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        googleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();


        google_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,RC_SIGN_IN);
            }
        });
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,RC_SIGN_IN);
            }
        });

        if (AccessToken.getCurrentAccessToken()== null)
        {
            Log.e("Check ","if part hello world");
        }
        else
        {
            Log.e("Check ","else part hello world");
            fb_profile_relativeLayout.setVisibility(View.VISIBLE);
            imageView_logo.setVisibility(View.GONE);
            google_cardview.setVisibility(View.GONE);
            fb_tv_card_text.setText("Logout With Facebook");
            load_user_profile(AccessToken.getCurrentAccessToken());
        }

    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            gotoProfile();
        }else{
            Toast.makeText(getApplicationContext(),"Sign in cancel",Toast.LENGTH_LONG).show();
        }
    }

    private void gotoProfile(){
        Intent intent=new Intent(SignUpActivity.this,GoogleProfileActivity.class);
        counter++;
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {

        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            if (currentAccessToken == null)
            {
                Toast.makeText(SignUpActivity.this, "User Logout", Toast.LENGTH_SHORT).show();
                fb_profile_relativeLayout.setVisibility(View.GONE);
                imageView_logo.setVisibility(View.VISIBLE);
                google_cardview.setVisibility(View.VISIBLE);
                fb_tv_card_text.setText("Continue With Facebook");


            }
            else
            {
                load_user_profile(currentAccessToken);
            }

        }
    };


    private void load_user_profile(AccessToken newAccessToken)
    {

        GraphRequest graphRequest = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                String user_id;

                if (response.getError() != null)
                {
                    Toast.makeText(SignUpActivity.this, "Error in Fetching Data", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String user_lastname = object.optString("last_name");
                    String user_firstname = object.optString("first_name");
                    String user_email = response.getJSONObject().optString("email");
                    user_id = object.optString("id");

                    String fullname = user_firstname+" "+user_lastname;
                    String final_email_id=user_email;
                    String final_user_id=user_id;

                    fb_tv_email_id.setText(final_email_id);
                    fb_tv_username.setText(fullname);
                    fb_tv_user_id.setText(final_user_id);

                    String profile_url="https://graph.facebook.com/"+user_id+"/picture?return_ssl_resources=1";
                    Picasso.get().load(profile_url).into(fb_imageView_profile);

                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "last_name,first_name,email,id");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
        
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

}