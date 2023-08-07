package com.dera.SimilarFiles;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class UserProfile extends Fragment {

    TextView nameTv, mobileTv, MyInfoTv, LogoutTv, ChangePasswordTv, EditInfoTv, shortNameTv;
    Bundle addUserInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        addUserInfo = new Bundle();

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
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        nameTv = view.findViewById(R.id.NameTv);
        mobileTv = view.findViewById(R.id.numberTv);
        MyInfoTv = view.findViewById(R.id.MyInfoTV);
        LogoutTv = view.findViewById(R.id.MylogoutTV);
        EditInfoTv = view.findViewById(R.id.EditInformationTV);
        ChangePasswordTv = view.findViewById(R.id.ChangePasswordTV);
        shortNameTv = view.findViewById(R.id.shortNameTv);
        Log.d("UserId", "" + StaticClasses.loginInfo.UserID);
        String getUserInfourl = "http://" + IpStatic.IpAddress.ip + ":80/api/GetUserInfo";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, getUserInfourl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    if (status.compareTo("200") == 0) {
                        JSONObject userObject = object.getJSONObject("user");
                        String username = userObject.getString("name");
                        String email = userObject.getString("email");
                        String number = userObject.getString("mobile");
                        String firstLetter = String.valueOf(username.charAt(0));

                        // Find the index of the first space (if any)
                        int spaceIndex = username.indexOf(' ');

                        // Accessing the first letter after the space (if any)
                        String firstLetterAfterSpace = "";
                        if (spaceIndex != -1 && spaceIndex + 1 < username.length()) {
                            firstLetterAfterSpace = String.valueOf(username.charAt(spaceIndex + 1));
                        }

                        // Combine the first letters into a single string
                        String combinedLetters = firstLetter + firstLetterAfterSpace;
                        Log.d("Full Name", "" + combinedLetters);
                        nameTv.setText(username);
                        mobileTv.setText(number);
                        addUserInfo.putString("name", username);
                        addUserInfo.putString("email", email);
                        addUserInfo.putString("number", number);
                        shortNameTv.setText(combinedLetters);

                    }
                    if (status.compareTo("404") == 0) {
                        Toast.makeText(getContext(), "User Not found!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

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

            public byte[] getBody() throws AuthFailureError {
                try {
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("id", StaticClasses.loginInfo.UserID);
                    return jsonBody.toString().getBytes("utf-8");
                } catch (JSONException | UnsupportedEncodingException e) {
                    throw new AuthFailureError(e.getMessage());
                }
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

        MyInfoTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment edit_user_info = new view_my_information();
                edit_user_info.setArguments(addUserInfo);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide(fragmentManager.findFragmentByTag("profileFragment"));
                if(fragmentManager.findFragmentByTag("myInformationFragment")==null) {
                    fragmentTransaction.add(R.id.fragmentlayout, edit_user_info, "myInformationFragment");
                }else{
                    fragmentTransaction.show(fragmentManager.findFragmentByTag("myInformationFragment"));
                }
                fragmentTransaction.commit();
            }
        });
        LogoutTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Dera");
                builder.setMessage("Are you sure you want to logout?");
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("DeraPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("AccessToken"); // Remove the "sessionToken" key
                        editor.remove("UserType");
                        editor.remove("UserId");
                        editor.apply();
                        Intent intent = new Intent(getContext(), No_Login_UserDashboard.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }

        });
        ChangePasswordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment edit_user_info = new change_password();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide(fragmentManager.findFragmentByTag("profileFragment"));
                if(fragmentManager.findFragmentByTag("changePasswordFragment")==null) {
                    fragmentTransaction.add(R.id.fragmentlayout, edit_user_info, "changePasswordFragment");
                }else{
                    fragmentTransaction.show(fragmentManager.findFragmentByTag("changePasswordFragment"));
                }
                fragmentTransaction.commit();
            }
        });
        EditInfoTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment edit_user_info = new edit_user_info();
                edit_user_info.setArguments(addUserInfo);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide(fragmentManager.findFragmentByTag("profileFragment"));
                if(fragmentManager.findFragmentByTag("editInformationFragment")==null) {
                    fragmentTransaction.add(R.id.fragmentlayout, edit_user_info, "editInformationFragment");
                }else{
                    fragmentTransaction.show(fragmentManager.findFragmentByTag("editInformationFragment"));
                }
                fragmentTransaction.commit();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }
}