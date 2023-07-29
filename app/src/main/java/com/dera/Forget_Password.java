package com.dera;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.dera.SimilarFiles.Login;
import com.dera.SimilarFiles.Register;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.BreakIterator;
import java.util.HashMap;
import java.util.Map;

public class Forget_Password extends AppCompatActivity {
MaterialCardView backMCv,emailMCV;
MaterialButton submitBtn;
EditText emailEt;
TextView errorTv;
    String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        backMCv=findViewById(R.id.backMCV);
        submitBtn=findViewById(R.id.forgetPasswordBtn);
        emailEt=findViewById(R.id.EmailEt);
        emailMCV=findViewById(R.id.emailMCV);
        errorTv=findViewById(R.id.errorTV);
        Intent i=getIntent();
        int usertypeid=i.getIntExtra("usertype",0);
        backMCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Forget_Password.this, Login.class);
                intent.putExtra("usertype",usertypeid);
                startActivity(intent);
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean emailError = false;
                email = emailEt.getText().toString();

                int errorCount = 0;
                emailError = StaticClasses.FormValidation.cantBeEmpty(email, emailMCV, 4,Forget_Password.this);
                if (emailError) {
                    errorCount++;
                }
                if (errorCount != 0) {

                    errorTv.setText("Enter the necessary Fields!");
                }else{
                    errorTv.setText("");
                    ProgressDialog progressDialog=new ProgressDialog(Forget_Password.this);
                    progressDialog.setMessage("Please wait.....");
                    progressDialog.show();
                    String forgetPasswordUrl="http://"+ IpStatic.IpAddress.ip+":80/api/ForgetPassword";
                    StringRequest stringRequest=new StringRequest(Request.Method.POST, forgetPasswordUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                progressDialog.dismiss();
                                JSONObject jsonObject=new JSONObject(response);
                                String status=jsonObject.getString("status");
                                if(status.compareTo("200")==0){
                                    Toast.makeText(Forget_Password.this,"Password reset email sent successfully!",Toast.LENGTH_LONG).show();
                                    emailEt.setText("");
                                    Intent intent=new Intent(Forget_Password.this, Login.class);
                                    intent.putExtra("usertype",usertypeid);
                                    intent.putExtra("Origin","Register");
                                    startActivity(intent);
                                }else if(status.compareTo("400")==0){
                                    Toast.makeText(Forget_Password.this,"Email not found in the database!",Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(Forget_Password.this,"Something Went Wrong!",Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(Forget_Password.this,error.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }){
                        public String getBodyContentType() {
                            return "application/json; charset=utf-8";
                        }
                        public Map<String, String> getHeaders() throws AuthFailureError {

                            Map<String,String> params =new HashMap<String,String>();
                            params.put("Accept","application/json");
                            params.put("Content-Type","application/json");
                            return params;
                        }
                        @Override
                        public byte[] getBody() throws AuthFailureError {
                            try {
                                JSONObject jsonBody = new JSONObject();
                                jsonBody.put("email", email);
                                jsonBody.put("type", String.valueOf(usertypeid));
                                return jsonBody.toString().getBytes("utf-8");
                            } catch (JSONException | UnsupportedEncodingException e) {
                                throw new AuthFailureError(e.getMessage());
                            }
                        }

                    };
                    RequestQueue requestQueue= Volley.newRequestQueue(Forget_Password.this);
                    requestQueue.add(stringRequest);

                }
            }
        });


    }
}