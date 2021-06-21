package com.creatures.tsfgriptask5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

     GoogleApiClient googleApiClient;
     GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        int SPLASH_TIME = 3000; //This is 3 seconds

        gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        /*
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            // signed in. Show the "sign out" button and explanation.
            // ...
        } else {
            // not signed in. Show the "sign in" button and explanation.
            // ...
        }
         */

        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                if (googleApiClient != null)
                {
                    Intent mySuperIntent = new Intent(MainActivity.this, GoogleProfileActivity.class);
                    Log.e("hello", "this is if part if part ");
                    startActivity(mySuperIntent);
                    finish();
                }
                else
                {
                    Intent mySuperIntent = new Intent(MainActivity.this, SignUpActivity.class);
                    startActivity(mySuperIntent);
                    Log.e("hello", "else part ");
                    Log.i("Information","The Splash Screen is working in this application");
                    Toast.makeText(MainActivity.this, "Value 1", Toast.LENGTH_SHORT).show();

                    finish();
                }


            }
        }, SPLASH_TIME );

    }
    
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}