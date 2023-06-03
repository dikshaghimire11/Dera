package com.dera.SimilarFiles;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dera.FormValidation;
import com.dera.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Register extends AppCompatActivity {

    MaterialButton signUpButton;
    AppCompatEditText emailET, passwordET, confirmPasswordEt, nameET, mobileET;
    AppCompatCheckBox privacyPolicyCB;
    AppCompatTextView errorTV, privacyErrorTV;

    MaterialCardView emailMCV, passwordMCV, repasswordMCV, nameMCV, mobileMCV;
    FormValidation fm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fm = new FormValidation();
        signUpButton = findViewById(R.id.SignUpBtn);
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        confirmPasswordEt = findViewById(R.id.repasswordET);
        mobileET = findViewById(R.id.mobileET);
        nameET = findViewById(R.id.nameET);
        privacyPolicyCB = findViewById(R.id.checkboxCB);
        errorTV = findViewById(R.id.errorTV);
        privacyErrorTV = findViewById(R.id.privacyerrorTV);
        emailMCV = findViewById(R.id.emailMCV);
        passwordMCV = findViewById(R.id.passwordMCV);
        repasswordMCV = findViewById(R.id.repasswordMCV);
        mobileMCV = findViewById(R.id.mobileMCV);
        nameMCV = findViewById(R.id.nameMCV);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();
                String confirmPassword = confirmPasswordEt.getText().toString();
                String name = nameET.getText().toString();
                String mobile = mobileET.getText().toString();
                boolean privacyPolicy = privacyPolicyCB.isChecked();
                boolean emailError = false;
                boolean passwordError = false;
                boolean rePasswordError = false;
                boolean checkboxError = false;
                boolean nameError = false;
                boolean mobileError = false;
                int errorCount = 0;


                /*if(email.length()==0){
                    emailError = true;
                    errorCount++;
                    emailMCV.setStrokeWidth(4);


                }*/
                nameError = fm.cantBeEmpty(name, nameMCV, 4);
                if (nameError) {
                    errorCount++;
                }
                mobileError = fm.cantBeEmpty(mobile, mobileMCV, 4);
                if (mobileError) {
                    errorCount++;
                }
                emailError = fm.cantBeEmpty(email, emailMCV, 4);
                if (emailError) {
                    errorCount++;
                }

                passwordError = fm.cantBeEmpty(password, passwordMCV, 4);
                if (passwordError) {
                    errorCount++;
                }
                rePasswordError = fm.cantBeEmpty(confirmPassword, repasswordMCV, 4);
                if (rePasswordError) {
                    errorCount++;
                }
                if (password.compareTo(confirmPassword) != 0) {
                    errorCount++;
                }
                if (privacyPolicy == false) {
                    checkboxError = true;
                    privacyErrorTV.setText("Please Accpet our Privacy Policy!");
                } else {
                    privacyErrorTV.setText("");
                }

                if (errorCount != 0) {
                    errorTV.setText("Enter the necessary Fields!");
                    if (!nameError && !mobileError && !emailError && !passwordError && !rePasswordError) {
                        errorTV.setText("Password did not match");
                    }
                } else {
                    errorTV.setText("");
                    String uri = "http://192.168.43.143:80/api/AddUser";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, uri, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object=new JSONObject(response);
                                String status=object.getString("status");


                                if(status.compareTo("200")==0){
                                   Toast.makeText(Register.this,"Regisration Sucessful!",Toast.LENGTH_LONG).show();
                                }
                                if(status.compareTo("422")==0){
                                    JSONObject errors = object.getJSONObject("errors");
                                    Iterator<String> iter = errors.keys();
                                while(iter.hasNext()){
                                    String key=iter.next();
                                    JSONArray errorArray=errors.getJSONArray(key);
                                    String errorMessages="";
                                    for(int i=0;i<errorArray.length();i++){
                                        errorMessages=errorMessages+errorArray.getString(i)+" ";
                                    }
                                    Toast.makeText(Register.this,errorMessages,Toast.LENGTH_LONG).show();
                                    errorTV.setText(errorMessages);

                                }

                                }

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Register.this,error.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params=new HashMap<String,String>();
                            params.put("name",name);
                            params.put("mobile",mobile);
                            params.put("email",email);
                            params.put("password",password);
                            params.put("type","1");
                            return params;
                        }
                    };
                    RequestQueue requestQueue= Volley.newRequestQueue(Register.this);
                    requestQueue.add(stringRequest);


                }

            }


        });
    }
}
