package com.dera.houseowner;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dera.IpStatic;
import com.dera.R;
import com.dera.SimilarFiles.Login;
import com.dera.StaticClasses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AddressInfo extends Fragment {
    Spinner provinceSpinner;
    ArrayList<String> provinceList=new ArrayList<>();
    ArrayAdapter<String> provinceAdapter;
    Bundle addPropertyDataBundle;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        addPropertyDataBundle=getArguments();
        return inflater.inflate(R.layout.fragment_address_info, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d("Token Receive",""+StaticClasses.loginInfo.loginToken);
        provinceSpinner=view.findViewById(R.id.stateSp);
<<<<<<< HEAD
        String url = "http://"+IpStatic.IpAddress.ip+":80/api/ProvinceInfo";
=======
        String url = "http://"+ IpStatic.IpAddress.ip+":80/api/ProvinceInfo";
        super.onViewCreated(view, savedInstanceState);
    }
    public void SelectAddress(String url){
>>>>>>> a39645624101b9a1083e419c250256d15578781c
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String provinceName = jsonObject.getString("name");
                        provinceList.add(provinceName);
                        provinceAdapter=new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item,provinceList);
                        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        provinceSpinner.setAdapter(provinceAdapter);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer "+StaticClasses.loginInfo.loginToken);
                return headers;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);
    }
}