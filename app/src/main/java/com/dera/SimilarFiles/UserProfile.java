package com.dera.SimilarFiles;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.dera.SimilarFiles.edit_user_info;
import com.dera.change_password;
import com.dera.houseowner.AddBasicInfoProperties;
import com.dera.view_my_information;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class UserProfile extends Fragment {

    TextView nameTv, mobileTv, MyInfoTv, LogoutTv, ChangePasswordTv, EditInfoTv;
    Bundle addUserInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        addUserInfo = new Bundle();
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
                        nameTv.setText(username);
                        mobileTv.setText(number);
                        addUserInfo.putString("name", username);
                        addUserInfo.putString("email", email);
                        addUserInfo.putString("number", number);

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
                return params;
            }

            public byte[] getBody() throws AuthFailureError {
                try {
                    JSONObject jsonBody = new JSONObject();
                    jsonBody.put("id", String.valueOf(2));
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
                fragmentTransaction.replace(R.id.fragmentlayout, edit_user_info);
                fragmentTransaction.commit();
            }
        });
        LogoutTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("DeraPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.remove("AccessToken"); // Remove the "sessionToken" key
                editor.remove("UserType");
                editor.remove("UserId");
                editor.apply();
                Intent intent=new Intent(getContext(), No_Login_UserDashboard.class);
                startActivity(intent);

            }
        });
        ChangePasswordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment edit_user_info = new change_password();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentlayout, edit_user_info);
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
                fragmentTransaction.replace(R.id.fragmentlayout, edit_user_info);
                fragmentTransaction.commit();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }
}