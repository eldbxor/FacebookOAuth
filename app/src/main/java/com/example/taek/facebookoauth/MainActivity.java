package com.example.taek.facebookoauth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context = this;
    private CallbackManager callbackManager;
    public LoginManager loginManager;
    public AccessToken accessToken;
    public boolean isLogin;

    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        findViewById(R.id.login_button).setOnClickListener(this);
        findViewById(R.id.login_button_test).setOnClickListener(this);
        findViewById(R.id.logout_button_test).setOnClickListener(this);
        isLogin = false;

        callbackManager = CallbackManager.Factory.create();

        // AccessToken.getCurrentAccessToken을 통해 사용자가 이미 로그인했는지 확인할 수 있습니다.
        accessToken = AccessToken.getCurrentAccessToken();
        loginManager = LoginManager.getInstance();

        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(context, "로그인되었습니다.", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        // loginManager.logInWithReadPermissions(this, Arrays.asList("public_profile"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:

                break;

            case R.id.login_button_test:
                loginManager.logInWithReadPermissions(this, Arrays.asList("public_profile"));
                break;

            case R.id.logout_button_test:
                new GraphRequest(accessToken, "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        loginManager.logOut();
                    }
                }).executeAsync();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Facebook SDK 로그인 또는 공유와 통합한 모든 액티비티와 프래그먼트에서 onActivityResult를 callbackManager에 전달해야 한다.
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
