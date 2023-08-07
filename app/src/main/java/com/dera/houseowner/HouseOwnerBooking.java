package com.dera.houseowner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dera.IpStatic;
import com.dera.No_Login_UserDashboard;
import com.dera.R;
import com.dera.SimilarFiles.user_properties;
import com.dera.StaticClasses;

import org.json.JSONException;
import org.json.JSONObject;

public class HouseOwnerBooking extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

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
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction propertyTransaction = fragmentManager.beginTransaction();
        Fragment property=new user_properties();
        String url="http://"+ IpStatic.IpAddress.ip+":80/api/GetHouseOwnerBookedProperty?house_owner_id=" + StaticClasses.loginInfo.UserID;
        Log.d("Url",""+url);
        Bundle bundle=new Bundle();
        bundle.putString("url",url);
        bundle.putString("name","houseOwnerbookingFragment");
        property.setArguments(bundle);
        propertyTransaction.add(R.id.propertiesFragment,property,"propertyFragment");
        propertyTransaction.commit();
        return inflater.inflate(R.layout.fragment_user_booking, container, false);
    }
}