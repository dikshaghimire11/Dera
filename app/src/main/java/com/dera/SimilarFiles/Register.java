package com.dera.SimilarFiles;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Register extends AppCompatActivity {

    MaterialButton signUpButton;
    AppCompatEditText emailET, nameET, mobileET;
    AppCompatCheckBox privacyPolicyCB;
    AppCompatTextView errorTV, privacyErrorTV;

    MaterialCardView emailMCV, nameMCV, mobileMCV;

    TextView loginTV;
    static int count =0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        signUpButton = findViewById(R.id.SignUpBtn);
        emailET = findViewById(R.id.emailET);
        mobileET = findViewById(R.id.mobileET);
        nameET = findViewById(R.id.nameET);
        privacyPolicyCB = findViewById(R.id.checkboxCB);
        errorTV = findViewById(R.id.errorTV);
        privacyErrorTV = findViewById(R.id.privacyerrorTV);
        emailMCV = findViewById(R.id.emailMCV);
        mobileMCV = findViewById(R.id.mobileMCV);
        nameMCV = findViewById(R.id.nameMCV);
        loginTV=findViewById(R.id.loginlink);
        Intent intent=getIntent();
        int userid=0;
        int userType= intent.getIntExtra("usertype",userid);

        loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                intent.putExtra("usertype",userType);
                startActivity(intent);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailET.getText().toString();
                String name = nameET.getText().toString();
                String mobile = mobileET.getText().toString();
                boolean privacyPolicy = privacyPolicyCB.isChecked();
                boolean emailError = false;
                boolean checkboxError = false;
                boolean nameError = false;
                boolean mobileError = false;
                boolean emailMatchError = false;
                boolean mobileMatchError = false;
                int errorCount = 0;
                Log.d("Email",""+email);
                Log.d("Mobile",""+mobile);

                /*if(email.length()==0){
                    emailError = true;
                    errorCount++;
                    emailMCV.setStrokeWidth(4);


                }*/
                nameError = StaticClasses.FormValidation.cantBeEmpty(name, nameMCV, 4,Register.this);
                if (nameError) {
                    errorCount++;
                }
                mobileError = StaticClasses.FormValidation.cantBeEmpty(mobile, mobileMCV, 4,Register.this);
                if (mobileError) {
                    errorCount++;
                }



                emailError = StaticClasses.FormValidation.cantBeEmpty(email, emailMCV, 4,Register.this);
                if (emailError) {
                    errorCount++;
                }
                if (privacyPolicy == false) {
                    checkboxError = true;

                } else {
                    privacyErrorTV.setText("");
                }
                if (!email.matches("^[_A-Za-z0-9-]+(.[_A-Za-z0-9-]+)@[a-z]+(.[a-z]+)(.[a-z]{2,})$"))
                {
                    errorCount++;
                    emailMatchError = true;
                }
//                if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$"))
//                {
//                    passwordMatchError = true;
//                    errorCount++;
//                }
                if (!mobile.matches("^(97|98)\\d{8}$"))
                {
                    mobileMatchError = true;
                    errorCount++;
                }

                if (errorCount != 0) {
                    errorTV.setText("Enter the necessary Fields!");
                    if(!nameError && !mobileError && !emailError  &&!mobileMatchError  ){
                        errorTV.setText("Please enter valid email address!");
                    }
                    if(!nameError && !mobileError && !emailError  && !emailMatchError ){
                        errorTV.setText("Please enter valid Number which start with 97 or 98 and must be 10 digits!");
                    }

                }else if(checkboxError){
                    privacyErrorTV.setText("Please Accpet our Privacy Policy!");
                }
                else {
                    ProgressDialog progressDialog=new ProgressDialog(Register.this);
                    progressDialog.setMessage("Please wait.....");
                    progressDialog.show();
                    errorTV.setText("");
                    String uri = "http://"+ IpStatic.IpAddress.ip+":80/api/AddUser";

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                progressDialog.dismiss();
                                // Log.d("Response: ",""+response);
                                JSONObject object=new JSONObject(response);
                                Log.d("Ball",""+response);
                                String status=object.getString("status");
                                if(status.compareTo("200")==0){
                                    Toast.makeText(Register.this,"Regisration Sucessful!",Toast.LENGTH_LONG).show();
                                    nameET.setText("");
                                    emailET.setText("");
                                    mobileET.setText("");
                                    privacyPolicyCB.setChecked(false);
                                    Intent intent=new Intent(Register.this, Login.class);
                                    intent.putExtra("usertype",userType);
                                    intent.putExtra("Origin","Register");
                                    startActivity(intent);
                                }


                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Log.d("Response Code: ",""+error.networkResponse.statusCode);
                            if(error.networkResponse.statusCode==422) {
                                try {
                                    JSONObject object = new JSONObject(new String(error.networkResponse.data));
                                    JSONObject errors = object.getJSONObject("errors");
                                    Iterator<String> iter = errors.keys();
                                    while (iter.hasNext()) {
                                        String key = iter.next();
                                        JSONArray errorArray = errors.getJSONArray(key);
                                        String errorMessages = "";
                                        for (int i = 0; i < errorArray.length(); i++) {
                                            errorMessages = errorMessages + errorArray.getString(i) + " ";
                                        }
                                        Toast.makeText(Register.this, errorMessages, Toast.LENGTH_LONG).show();
                                        errorTV.setText(errorMessages);

                                    }

                                }catch (Exception e){
                                    Log.d("Response Error", ""+e.getMessage());


                                }
                            }
                            Toast.makeText(Register.this,error.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }) {
                        public String getBodyContentType() {
                            return "application/json; charset=utf-8";
                        }

                        @Nullable
                        @Override
                        public byte[] getBody() throws AuthFailureError {
                            try {
                                JSONObject jsonBody = new JSONObject();
                                jsonBody.put("name", name);
                                jsonBody.put("email", email);
                                jsonBody.put("mobile", mobile);
                                jsonBody.put("type", String.valueOf(userType));
                                return jsonBody.toString().getBytes("utf-8");
                            } catch (JSONException | UnsupportedEncodingException e) {
                                throw new AuthFailureError(e.getMessage());
                            }
                        }

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {

                            Map<String,String> params =new HashMap<String,String>();
                            params.put("Accept","application/json");
                            params.put("Content-Type","application/json");
                            return params;
                        }
                    };
                    count++;
                    RequestQueue requestQueue= Volley.newRequestQueue(Register.this);
                    requestQueue.add(stringRequest);






                }

            }


        });
    }
}
