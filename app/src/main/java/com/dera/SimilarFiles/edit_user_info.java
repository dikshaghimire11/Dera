package com.dera.SimilarFiles;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.dera.IpStatic;
import com.dera.No_Login_UserDashboard;
import com.dera.R;
import com.dera.StaticClasses;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class edit_user_info extends Fragment {
    EditText nameEt, mobileEt;
    Bundle addUserInfo;
    TextView errorTv;

    MaterialButton editInfobtn,CancleBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        addUserInfo = getArguments();
        return inflater.inflate(R.layout.fragment_edit_user_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
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
                            Toast.makeText(getContext(), "Your account has been suspended by Administrator!", Toast.LENGTH_LONG).show();
                            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("DeraPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.remove("AccessToken"); // Remove the "sessionToken" key
                            editor.remove("UserType");
                            editor.remove("UserId");
                            editor.apply();
                            StaticClasses.loginInfo.UserID="";
                            StaticClasses.loginInfo.loginToken="";
                            StaticClasses.loginInfo.userName="";
                            Intent intent = new Intent(getContext(), No_Login_UserDashboard.class);
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
                Toast.makeText(getContext(), "Something Went Wrong!", Toast.LENGTH_LONG).show();
            }
        }){
            public String getBodyContentType() {
                return "application/json";
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(request);

        StaticClasses.backStackManager.setBackStack("editInformationFragment","profileFragment",getActivity().getSupportFragmentManager());
        nameEt = view.findViewById(R.id.nameET);
        mobileEt = view.findViewById(R.id.MobileET);
        editInfobtn = view.findViewById(R.id.EditInfoBtn);
        errorTv = view.findViewById(R.id.errorTV);
        CancleBtn=view.findViewById(R.id.cancleBtn);
        String name = addUserInfo.getString("name");
        nameEt.setText(name);
        mobileEt.setText(addUserInfo.getString("number"));
        editInfobtn.setOnClickListener(new View.OnClickListener() {
            String name, email, mobile;

            @Override
            public void onClick(View view) {
                name = nameEt.getText().toString();
                mobile = mobileEt.getText().toString();

              if (!mobile.matches("^(97|98)\\d{8}$")) {
                    errorTv.setText("Please enter valid Number which start with 97 or 98 and must be 10 digits!");
                } else if (name.length() == 0) {
                    errorTv.setText("Please enter the name!");
                } else {
                  ProgressDialog progressDialog=new ProgressDialog(getContext());
                  progressDialog.setMessage("Please wait.....");
                  progressDialog.show();
                    String updateProfileUrl = "http://" + IpStatic.IpAddress.ip + ":80/api/EditProfile";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, updateProfileUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                progressDialog.dismiss();
                                JSONObject object = new JSONObject(response);
                                String status = object.getString("status");
                                if (status.equals("200")) {
                                    Toast.makeText(getContext(), "Profile Edited successfully", Toast.LENGTH_SHORT).show();
                                    Fragment edit_user_info = new UserProfile();
                                    FragmentManager fragmentManager = getFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.fragmentlayout, edit_user_info);
                                    fragmentTransaction.commit();
                                } else {
                                    Toast.makeText(getContext(), "Error updating profile", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        public byte[] getBody() throws AuthFailureError {
                            try {
                                JSONObject jsonBody = new JSONObject();
                                jsonBody.put("name", name);
                                jsonBody.put("number", mobile);
                                jsonBody.put("id",StaticClasses.loginInfo.UserID);
                                return jsonBody.toString().getBytes("utf-8");
                            } catch (JSONException | UnsupportedEncodingException e) {
                                throw new AuthFailureError(e.getMessage());
                            }
                        }
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String,String> params =new HashMap<String,String>();
                            params.put("Accept","application/json");
                            params.put("Content-Type","application/json");
                            return params;
                        }
                        public String getBodyContentType() {
                            return "application/json";
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                    requestQueue.add(stringRequest);
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