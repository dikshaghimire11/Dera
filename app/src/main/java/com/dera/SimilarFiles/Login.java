package com.dera.SimilarFiles;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dera.StaticClasses;
import com.dera.IpStatic;
import com.dera.R;
import com.dera.customer.UserDashboard;
import com.dera.houseowner.houseOwnerDashboard;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    MaterialButton loginbtn;
    EditText emailET, passwordET;
    TextView signUpTv,errorTV;
   String userId;


    MaterialCardView emailMCV,passwordMCV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


// Commit the changes


        setContentView(R.layout.activity_login);
        Intent intent=getIntent();
       String origin=intent.getStringExtra("Origin");
       if(origin != null && origin.equals("Register")){
           AlertDialog.Builder builder = new AlertDialog.Builder(this);
           builder.setTitle("Password Sent");
           builder.setMessage("Your password has been sent to your email. Please check your inbox.");
           builder.setPositiveButton("Ok", (dialog, which) ->
           {
           });
           AlertDialog alert = builder.create();
           alert.show();
       }
        loginbtn=findViewById(R.id.SignIpBtn);
        emailET=findViewById(R.id.emailET);
        passwordET=findViewById(R.id.passwordET);
        signUpTv=findViewById(R.id.signUplink);
        emailMCV=findViewById(R.id.emailMCV);
        passwordMCV=findViewById(R.id.passwordMCV);
        errorTV=findViewById(R.id.errorTV);
        int usertype=0;

        int usertypeid=intent.getIntExtra("usertype",usertype);
        signUpTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                intent.putExtra("usertype",usertypeid);
                startActivity(intent);
            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean emailError = false;
                boolean passwordError = false;
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();
                int errorCount = 0;
                emailError = StaticClasses.FormValidation.cantBeEmpty(email, emailMCV, 4,Login.this);
                if (emailError) {
                    errorCount++;
                }

                passwordError = StaticClasses.FormValidation.cantBeEmpty(password, passwordMCV, 4,Login.this);
                if (passwordError) {
                    errorCount++;
                }
                if (errorCount != 0) {
                    errorTV.setText("Enter the necessary Fields!");
                }
                else {
                    ProgressDialog progressDialog=new ProgressDialog(Login.this);
                    progressDialog.setMessage("Please wait.....");
                    progressDialog.show();
                    String uri = "http://"+ IpStatic.IpAddress.ip+":80/api/authenticate";
                    StringRequest stringRequest=new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                progressDialog.dismiss();
                                JSONObject object=new JSONObject(response);
                                String status=object.getString("status");
                                if(status.compareTo("200")==0){
                                    userId=object.getString("id");


                                    // Obtain an instance of SharedPreferences
                                    SharedPreferences sharedPreferences = getSharedPreferences("DeraPrefs", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    String AccessToken = object.getString("access_token");
                                    editor.putString("AccessToken", AccessToken);
                                    editor.putString("UserId", userId);
                                    editor.putInt("UserType",usertypeid );
                                    editor.apply();
                                    StaticClasses.loginInfo.loginToken=AccessToken;
                                    Toast.makeText(Login.this,"Login Sucessful!",Toast.LENGTH_LONG).show();
                                    emailET.setText("");
                                    passwordET.setText("");
                                    if(usertypeid==2){
                                        StaticClasses.loginInfo.UserID=userId;
                                        Intent intent=new Intent(Login.this, houseOwnerDashboard.class);
                                        startActivity(intent);
                                    }
                                    if(usertypeid==3) {
                                        StaticClasses.loginInfo.UserID=userId;
                                        Intent intent = new Intent(Login.this,UserDashboard.class);
                                        startActivity(intent);
                                    }
                                }
                                if(status.compareTo("401")==0){
                                    Toast.makeText(Login.this,"Invalid Username/Password!",Toast.LENGTH_LONG).show();
                                    errorTV.setText("Invalid Username/Password!");
                                }

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Login.this,error.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }

                    ){
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params=new HashMap<String,String>();
                            params.put("email",email);
                            params.put("password",password);
                            params.put("type",String.valueOf(usertypeid));
                            return params;
                        }

                    };

                    RequestQueue requestQueue= Volley.newRequestQueue(Login.this);
                    requestQueue.add(stringRequest);
                    errorTV.setText(" ");

                }
            }
        });

    }
}