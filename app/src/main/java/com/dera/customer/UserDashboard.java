package com.dera.customer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dera.IpStatic;
import com.dera.No_Login_UserDashboard;
import com.dera.R;
import com.dera.SimilarFiles.UserProfile;
import com.dera.StaticClasses;
import com.dera.callback.OnRemovedFragments;
import com.dera.houseowner.houseOwnerDashboard;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserDashboard extends AppCompatActivity {

    String passwordStatus;
    int errorCount = 0;
    boolean passwordMatchError = false, repasswordMatch = false, passworderror = false, repassworderror = false;
    int UserType;
    int defaultvalue = 0;
    public  boolean isPasswordVisible=false;
    Bundle bundle;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        bundle=new Bundle();
        Intent intent = getIntent();
        UserType = intent.getIntExtra("usertypeid", defaultvalue);
        bundle.putInt("usertypeid",UserType);
        String checkUserStatusURL="http://" + IpStatic.IpAddress.ip + ":80/api/CheckUserStatus?id="+StaticClasses.loginInfo.UserID;
        StringRequest request=new StringRequest(Request.Method.GET, checkUserStatusURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String status=jsonObject.getString("status");
                    String UserStatus=jsonObject.getString("Userstatus");
                    Log.d("UserStatus",""+UserStatus);
                    if(status.compareTo("200")==0){
                        if(UserStatus.equals("0")){
                            Toast.makeText(UserDashboard.this, "Your account has been suspended by Administrator!", Toast.LENGTH_LONG).show();
                            SharedPreferences sharedPreferences =getSharedPreferences("DeraPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.remove("AccessToken"); // Remove the "sessionToken" key
                            editor.remove("UserType");
                            editor.remove("UserId");
                            editor.apply();
                            Intent intent = new Intent(UserDashboard.this, No_Login_UserDashboard.class);
                            startActivity(intent);


                        }

                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserDashboard.this, "Something Went Wrong!", Toast.LENGTH_LONG).show();
            }
        }){
            public String getBodyContentType() {
                return "application/json";
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(request);
        String checkStatusURL = "http://" + IpStatic.IpAddress.ip + ":80/api/get_password_status";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, checkStatusURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.compareTo("200") == 0) {
                        passwordStatus = jsonObject.getString("password_status");
                        if (passwordStatus.compareTo("0") == 0) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(UserDashboard.this);
                            LayoutInflater inflater = getLayoutInflater();
                            View view = inflater.inflate(R.layout.change_password_dialog, null);
                            builder.setView(view);
                            EditText passwordEt = view.findViewById(R.id.passwordET);
                            EditText repasswordEt = view.findViewById(R.id.re_passwordET);
                            MaterialButton submitBtn = view.findViewById(R.id.submitbtn);
                            MaterialCardView passwordMcv = view.findViewById(R.id.passwordMCV);
                            MaterialCardView repasswordMcv = view.findViewById(R.id.re_passwordMCV);
                            TextView errorTv = view.findViewById(R.id.errorTV);
                            ImageView password=view.findViewById(R.id.showPasswordToggle);
                            ImageView rePassword=view.findViewById(R.id.showrePasswordToggle);
                            AlertDialog dialog = builder.create();

                            password.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if (isPasswordVisible) {
                                        passwordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                        password.setImageResource(R.drawable.show);
                                    } else {
                                        passwordEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                                        password.setImageResource(R.drawable.eye_password_hide);
                                    }

                                    passwordEt.setSelection(passwordEt.getText().length());
                                    isPasswordVisible = !isPasswordVisible;
                                }
                            });
                            rePassword.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (isPasswordVisible) {
                                        repasswordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                        rePassword.setImageResource(R.drawable.show);
                                    } else {
                                        repasswordEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                                        rePassword.setImageResource(R.drawable.eye_password_hide);
                                    }

                                    repasswordEt.setSelection(repasswordEt.getText().length());
                                    isPasswordVisible = !isPasswordVisible;
                                }
                            });
                            submitBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    errorCount = 0;
                                    repasswordMatch = false;
                                    passworderror = false;
                                    repassworderror = false;
                                    passwordMatchError = false;
                                    errorTv.setText("");
                                    String password = passwordEt.getText().toString();
                                    String confirmPassword = repasswordEt.getText().toString();
                                    passworderror = StaticClasses.FormValidation.cantBeEmpty(password, passwordMcv, 4, UserDashboard.this);
                                    if (passworderror) {
                                        errorCount++;
                                    }
                                    repassworderror = StaticClasses.FormValidation.cantBeEmpty(confirmPassword, repasswordMcv, 4, UserDashboard.this);
                                    if (repassworderror) {
                                        errorCount++;
                                    }
                                    if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$")) {
                                        passwordMatchError = true;
                                        errorCount++;
                                    }
                                    if ((password.compareTo(confirmPassword) != 0)) {
                                        repasswordMatch = true;
                                        errorCount++;
                                    }


                                    Log.d("Error", "" + errorCount);
                                    Log.d("Error", "repassworderror" + repassworderror + " passworderror" + passworderror + " passwordMatchError" + passwordMatchError + " repasswordmatchError" + repasswordMatch);
                                    if (errorCount != 0) {
                                        if (repassworderror || passworderror) {
                                            errorTv.setText("Enter valid password in both fields!");
                                        }
                                        Log.d("Password,Repassword: ", "" + password + "," + confirmPassword);

                                        if (!passworderror && !repassworderror && !repasswordMatch) {
                                            errorTv.setText("Password should be 8 digits, at least one Upper, Lower and number!");
                                        }
                                        if (!passworderror && !repassworderror && !passwordMatchError) {
                                            errorTv.setText("Password and Re-Password should match!");
                                        }
                                    } else {
                                        ProgressDialog progressDialog = new ProgressDialog(UserDashboard.this);
                                        progressDialog.setMessage("Please wait.....");
                                        progressDialog.show();
                                        errorTv.setText("");
                                        String changePasswordUrl = "http://" + IpStatic.IpAddress.ip + ":80/api/update_Password";
                                        JSONObject requestData = new JSONObject();
                                        try {
                                            requestData.put("password", password);
                                            requestData.put("id", StaticClasses.loginInfo.UserID);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        StringRequest stringRequest1 = new StringRequest(Request.Method.PUT, changePasswordUrl,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        progressDialog.dismiss();
                                                        try {
                                                            JSONObject object = new JSONObject(response);
                                                            String status = object.getString("status");
                                                            if (status.compareTo("200") == 0) {
                                                                Toast.makeText(UserDashboard.this, "Password Updated Successfully!", Toast.LENGTH_LONG).show();
                                                                dialog.dismiss();
                                                            } else if (status.compareTo("500") == 0) {
                                                                Toast.makeText(UserDashboard.this, "Something Went Wrong!", Toast.LENGTH_LONG).show();
                                                            }
                                                        } catch (JSONException e) {
                                                            throw new RuntimeException(e);
                                                        }
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        progressDialog.dismiss();

                                                        Toast.makeText(UserDashboard.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                }) {
                                            @Override
                                            public byte[] getBody() throws AuthFailureError {
                                                return requestData.toString().getBytes();
                                            }

                                            @Override
                                            public String getBodyContentType() {
                                                return "application/json";
                                            }

                                            public Map<String, String> getHeaders() throws AuthFailureError {

                                                Map<String, String> params = new HashMap<String, String>();
                                                params.put("Accept", "application/json");
                                                params.put("Content-Type", "application/json");
                                                params.put("Authorization", "Bearer " + StaticClasses.loginInfo.loginToken);
                                                return params;
                                            }
                                        };


                                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                        requestQueue.add(stringRequest1);

                                    }


                                }
                            });
                            dialog.show();
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("accept", "applicaiton/json");
                params.put("Authorization", "Bearer " + StaticClasses.loginInfo.loginToken);
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id", StaticClasses.loginInfo.UserID);
                    return jsonObject.toString().getBytes();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment myFragment = new UserHome();
        myFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.fragmentlayout, myFragment,"homeFragment");
        fragmentTransaction.commit();

        ImageView home = findViewById(R.id.homeIV);
        ImageView booking = findViewById(R.id.bookingIV);
        ImageView history = findViewById(R.id.historyIV);
        ImageView profile = findViewById(R.id.profileIV);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticClasses.backStackManager.stack.clear();
                StaticClasses.backStackManager.resetStackCount();
                Fragment fragment = new UserHome();
                FragmentManager manager = getSupportFragmentManager();
                StaticClasses.CloseAllFragments.removeByManager(manager, new OnRemovedFragments() {
                    @Override
                    public void removedFragments(FragmentTransaction transaction) {
                        fragment.setArguments(bundle);
                        transaction.add(R.id.fragmentlayout, fragment,"homeFragment");
                        transaction.commit();
                    }
                });





            }
        });
        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticClasses.backStackManager.stack.clear();
                StaticClasses.backStackManager.resetStackCount();
                Fragment fragment = new UserBooking();
                FragmentManager manager = getSupportFragmentManager();
                StaticClasses.CloseAllFragments.removeByManager(manager, new OnRemovedFragments() {
                    @Override
                    public void removedFragments(FragmentTransaction transaction) {
                        transaction.replace(R.id.fragmentlayout, fragment,"bookingFragment");
                        transaction.commit();
                    }
                });

            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticClasses.backStackManager.stack.clear();
                StaticClasses.backStackManager.resetStackCount();
                Fragment fragment = new UserHistory();
                FragmentManager manager = getSupportFragmentManager();

                StaticClasses.CloseAllFragments.removeByManager(manager, new OnRemovedFragments() {
                    @Override
                    public void removedFragments(FragmentTransaction transaction) {
                        transaction.replace(R.id.fragmentlayout, fragment,"historyFragment");
                        transaction.commit();
                    }
                });

            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticClasses.backStackManager.stack.clear();
                StaticClasses.backStackManager.resetStackCount();
                Fragment fragment = new UserProfile();
                FragmentManager manager = getSupportFragmentManager();
                ;
                StaticClasses.CloseAllFragments.removeByManager(manager, new OnRemovedFragments() {
                    @Override
                    public void removedFragments(FragmentTransaction transaction) {
                        transaction.replace(R.id.fragmentlayout, fragment,"profileFragment");
                        transaction.commit();
                    }
                });
            }
        });

        StaticClasses.keyboardSupport.hideSoftKeyboard(this);
    }
    public void onBackPressed(){
        Log.d("Back","Back is Pressed");
        if(StaticClasses.backStackManager.getIntStackCount()!=0){
            try{
                StaticClasses.backStackManager.performBackStack();

            }catch (NullPointerException e){
                Log.d("Fragment Not Found","Back Operation not performed");
                StaticClasses.backStackManager.showExitDialog(this);
            }

        }else{
            StaticClasses.backStackManager.showExitDialog(this);

        }
    }


}