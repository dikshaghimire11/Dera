package com.dera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.dera.SimilarFiles.Login;
import com.dera.customer.UserDashboard;
import com.dera.houseowner.houseOwnerDashboard;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferences sharedPreferences = getSharedPreferences("DeraPrefs", Context.MODE_PRIVATE);
        String AccessToken = sharedPreferences.getString("AccessToken", null);
        String userId=sharedPreferences.getString("UserId",null);
        int usertypeid=sharedPreferences.getInt("UserType",0);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (AccessToken != null) {
                    StaticClasses.loginInfo.loginToken=AccessToken;
                    StaticClasses.loginInfo.UserID=userId;
                    if(usertypeid==2){
                        Intent intent=new Intent(SplashActivity.this, houseOwnerDashboard.class);
                        startActivity(intent);
                    }
                    if(usertypeid==3) {
                        Intent intent = new Intent(SplashActivity.this,UserDashboard.class);
                        startActivity(intent);
                    }

                } else {
                    Intent intent= new Intent(SplashActivity.this, No_Login_UserDashboard.class);
                    startActivity(intent);
                }


                finish();
            }
        },4000);
    }
}