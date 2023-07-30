package com.dera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.dera.customer.UserHome;

public class No_Login_UserDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_login_user_dashboard);

        SharedPreferences sharedPreferences = getSharedPreferences("DeraPrefs", Context.MODE_PRIVATE);

        String AccessToken = sharedPreferences.getString("AccessToken", null);

        if (AccessToken != null) {
            Log.d("AccessToken",""+AccessToken);

        } else {
            Log.d("AccessToken","Empty");
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment myFragment = new UserHome();
        fragmentTransaction.add(R.id.fragmentlayout, myFragment,"homeFragment");
        fragmentTransaction.commit();
        ImageView profile = findViewById(R.id.profileIV);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(No_Login_UserDashboard.this,ChooseUserType.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed(){
        Log.d("Back","Back is Pressed");
        if(StaticClasses.backStackManager.getUseBackStack()){
            try{
                StaticClasses.backStackManager.performBackStack();

            }catch (NullPointerException e){
                Log.d("Fragment Not Found","Back Operation not performed");
            }

        }else{
            super.onBackPressed();
        }
    }
}