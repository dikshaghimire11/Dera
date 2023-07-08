package com.dera;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.dera.SimilarFiles.UserProfile;
import com.dera.houseowner.houseOwnerDashboard;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class change_password extends Fragment {
    EditText oldPasswordEt, newPasswordEt,confirmPasswordEt;
    MaterialButton submitBtn,CancleBtn;
    MaterialCardView newPasswordMcv,oldPasswordMcv,repasswordMcv;
    TextView errorTv;
    int errorCount=0;
    boolean passwordMatchError=false,repasswordMatch=false,oldpassworderror=false,repassworderror=false,passworderror=false,oldPasswordMatch=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        oldPasswordEt=view.findViewById(R.id.CurrentPasswordEt);
        newPasswordEt=view.findViewById(R.id.NewPasswordET);
        confirmPasswordEt=view.findViewById(R.id.ConfirmPasswordET);
        newPasswordMcv=view.findViewById(R.id.newPasswordMCV);
        oldPasswordMcv=view.findViewById(R.id.oldPasswordMcv);
        repasswordMcv=view.findViewById(R.id.rePasswordMCV);
        errorTv=view.findViewById(R.id.errorTV);

        submitBtn=view.findViewById(R.id.submitBtn);
        CancleBtn=view.findViewById(R.id.cancleBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String newPassword=newPasswordEt.getText().toString();
               String oldPassword=oldPasswordEt.getText().toString();
               String ConfirmPassword=confirmPasswordEt.getText().toString();
                errorCount=0;
                repasswordMatch=false;
                passworderror=false;
                repassworderror=false;
                passwordMatchError=false;
                errorTv.setText("");
                oldpassworderror = StaticClasses.FormValidation.cantBeEmpty(oldPassword, oldPasswordMcv, 4, getContext());
                if (oldpassworderror) {
                    errorCount++;
                }
                passworderror = StaticClasses.FormValidation.cantBeEmpty(newPassword, newPasswordMcv, 4, getContext());
                if (passworderror) {
                    errorCount++;
                }
                repassworderror = StaticClasses.FormValidation.cantBeEmpty(ConfirmPassword, repasswordMcv, 4, getContext());
                if (repassworderror) {
                    errorCount++;
                }
                if (!newPassword.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$")) {
                    passwordMatchError = true;
                    errorCount++;
                }
                if ((newPassword.compareTo(ConfirmPassword)!=0)) {
                    repasswordMatch = true;
                    errorCount++;
                }
                if((oldPassword.compareTo(newPassword)==0)){
                    oldPasswordMatch=true;
                    errorCount++;
                }


                Log.d("Error",""+errorCount);
                Log.d("Error","repassworderror" +repassworderror+" passworderror"+passworderror+" passwordMatchError"+passwordMatchError+" repasswordmatchError"+repasswordMatch);
                if(errorCount !=0) {
                    if(repassworderror || passworderror || oldpassworderror ){
                        errorTv.setText("Enter valid password in all fields!");
                    }

                    if(!passworderror && !repassworderror && !repasswordMatch && !oldPasswordMatch && !oldpassworderror){
                        errorTv.setText("Password should be 8 digits, at least one Upper, Lower and number!");
                    }
                    if(!passworderror && !repassworderror && !passwordMatchError &&!oldPasswordMatch && !oldpassworderror){
                        errorTv.setText("Password and Re-Password should match!");
                    }
                    if(!passworderror && !repassworderror && !passwordMatchError  && !oldpassworderror && !repasswordMatch){
                        errorTv.setText("Try a different password!");
                    }
                } else{
                    ProgressDialog progressDialog=new ProgressDialog(getContext());
                    progressDialog.setMessage("Please wait.....");
                    progressDialog.show();
                    errorTv.setText("");
                    String changePasswordUrl = "http://" + IpStatic.IpAddress.ip + ":80/api/ChangePassword";

// Create a JSONObject with the data to send
                    JSONObject requestData = new JSONObject();
                    try {
                        requestData.put("currentpassword",oldPassword);
                        requestData.put("newpassword",newPassword);
                        requestData.put("id",StaticClasses.loginInfo.UserID);
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
                                            Toast.makeText(getContext(), "Password Changed Successfully!", Toast.LENGTH_LONG).show();
                                            Fragment edit_user_info = new UserProfile();
                                            FragmentManager fragmentManager = getFragmentManager();
                                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                            fragmentTransaction.replace(R.id.fragmentlayout, edit_user_info);
                                            fragmentTransaction.commit();
                                        } else if (status.compareTo("400")==0) {
                                            Toast.makeText(getContext(), "The current password is incorrect!", Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(getContext(), "Something Went Wrong!", Toast.LENGTH_LONG).show();
                                        throw new RuntimeException(e);
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();

                                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
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

                            Map<String,String> params =new HashMap<String,String>();
                            params.put("Accept","application/json");
                            params.put("Content-Type","application/json");
                            params.put("Authorization", "Bearer " + StaticClasses.loginInfo.loginToken);

                            return params;
                        }
                    };


                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                    requestQueue.add(stringRequest1);

                }


            }
        });
        CancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment edit_user_info = new UserProfile();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentlayout, edit_user_info);
                fragmentTransaction.commit();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
}