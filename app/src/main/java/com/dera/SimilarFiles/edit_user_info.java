package com.dera.SimilarFiles;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.dera.R;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class edit_user_info extends Fragment {
    EditText nameEt, mobileEt;
    Bundle addUserInfo;
    TextView errorTv;

    MaterialButton editInfobtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        addUserInfo = getArguments();
        return inflater.inflate(R.layout.fragment_edit_user_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        nameEt = view.findViewById(R.id.nameET);
        mobileEt = view.findViewById(R.id.MobileET);
        editInfobtn = view.findViewById(R.id.EditInfoBtn);
        errorTv = view.findViewById(R.id.errorTV);
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
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("id", "2"); // Replace with the actual user ID
                            params.put("name", name);
                            params.put("number", mobile);
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                    requestQueue.add(stringRequest);
                }
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }
}