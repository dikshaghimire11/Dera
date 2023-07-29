package com.dera.SimilarFiles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.dera.R;

public class ChooseUserType extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_user_type);
        ImageView houseOwner=findViewById(R.id.houseownerIV);
        ImageView roomFinder=findViewById(R.id.roomfinderIV);
        houseOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int userType=2;
                Intent intent=new Intent(ChooseUserType.this, Login.class);
                intent.putExtra("usertype",userType);
                startActivity(intent);
                finish();
            }
        });
        roomFinder.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                int userType=3;
                Intent intent=new Intent(ChooseUserType.this, Login.class);
                intent.putExtra("usertype",userType);
                startActivity(intent);
                finish();
            }
        });
    }
}