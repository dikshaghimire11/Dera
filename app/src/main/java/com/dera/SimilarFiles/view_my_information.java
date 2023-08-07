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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;


public class view_my_information extends Fragment {

    TextView nameEt, mobileEt, emailEt;
    Bundle addUserInfo;
    MaterialButton CancleBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        addUserInfo = getArguments();
        return inflater.inflate(R.layout.fragment_view_my_information, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        String checkUserStatusURL="http://" + IpStatic.IpAddress.ip + ":80/api/CheckUserStatus?id="+ StaticClasses.loginInfo.UserID;
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
        nameEt = view.findViewById(R.id.nameET);
        mobileEt = view.findViewById(R.id.MobileET);
        emailEt=view.findViewById(R.id.emailET);
        CancleBtn=view.findViewById(R.id.cancleBtn);
        String name = addUserInfo.getString("name");
        nameEt.setText(name);
        mobileEt.setText(addUserInfo.getString("number"));
        emailEt.setText(addUserInfo.getString("email"));
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