package com.dera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.dera.customer.UserHome;

public class No_Login_UserDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_login_user_dashboard);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment myFragment = new UserHome();
        fragmentTransaction.add(R.id.fragmentlayout, myFragment);
        fragmentTransaction.commit();
        ImageView profile = findViewById(R.id.profileIV);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(No_Login_UserDashboard.this,ChooseUserType.class);
                startActivity(intent);
            }
        });
    }
}