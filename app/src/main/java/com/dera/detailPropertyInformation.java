package com.dera;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dera.Adapter.PropertyDetailsGridView;
import com.dera.SimilarFiles.Login;
import com.dera.SimilarFiles.Register;
import com.dera.SimilarFiles.user_properties;
import com.dera.houseowner.houseOwnerDashboard;
import com.dera.models.Property;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class detailPropertyInformation extends Fragment {
    ImageView imageView;
    TextView textView, fullLocation, title, number, priceTV;
    GridView gridView;
    MaterialCardView backButton;
    MaterialButton bookButton,contactbtn;
    ScrollView homeScroll;
    String house_owner_id,Property_id;
    int usertypeid;
    MaterialCardView bottonView;
    ConstraintLayout detailProperty;
    LinearLayout linearLayoutButtons;
    String userId,houseOwner_number,house_ownername;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_property_information, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait.....");
        progressDialog.show();

        Bundle bundle = getArguments();
        String name=bundle.getString("name");
        homeScroll = getActivity().findViewById(R.id.homeScroll);
        imageView = view.findViewById(R.id.propertyIv);
        detailProperty=view.findViewById(R.id.detailsProperty);
        gridView = view.findViewById(R.id.detailsGridView);
        textView = view.findViewById(R.id.PropertyTypeTV);
        fullLocation = view.findViewById(R.id.locationTV);
        number = view.findViewById(R.id.number);

        bottonView=getActivity().findViewById(R.id.bottonV);
        title = view.findViewById(R.id.title);
        priceTV = view.findViewById(R.id.priceTV);
        backButton = view.findViewById(R.id.backMCV);
        bookButton = view.findViewById(R.id.bookingMbtn);
        contactbtn=view.findViewById(R.id.contactMbtn);
        linearLayoutButtons=view.findViewById(R.id.buttonsLayout);
        contactbtn.setClickable(false);
        ViewGroup contactbtnParent = (ViewGroup) contactbtn.getParent();
            contactbtnParent.removeView(contactbtn);

      bookButton.setClickable(false);
        ViewGroup bookButtonParent = (ViewGroup) bookButton.getParent();
        bookButtonParent.removeView(bookButton);

        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) linearLayoutButtons.getLayoutParams();
        layoutParams.setMargins(0,0,0,bottonView.getHeight());
        linearLayoutButtons.setLayoutParams(layoutParams);



        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("DeraPrefs", Context.MODE_PRIVATE);
        usertypeid = sharedPreferences.getInt("UserType", 0);
        userId = sharedPreferences.getString("UserId", null);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_properties user_properties = new user_properties();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(fragmentManager.findFragmentByTag("detailFragment"));
                fragmentTransaction.show(fragmentManager.findFragmentByTag("propertyFragment"));
                try {
                    fragmentTransaction.show(fragmentManager.findFragmentByTag("homeFragment"));
                }catch (NullPointerException e){
                    Log.d("Fragment Not Found","Removal Ignored");
                }
                Log.d("FragmentDetail",""+fragmentManager.findFragmentByTag("propertyFragment"));
                homeScroll.setVerticalScrollbarPosition(bundle.getInt("scrollPosition"));

                // fragmentTransaction.replace(R.id.propertiesFragment,user_properties);
                fragmentTransaction.commit();
            }
        });
        if (bundle != null) {
            Property property = (Property) bundle.getSerializable("model");
            Log.d("Property",""+bundle.getSerializable("model"));
            textView.setText(property.getCategory());

            title.setText(property.getCategory());
            fullLocation.setText(property.getLocation());
            priceTV.setText(property.getPrice());
            number.setText(property.getNumber());
            house_owner_id=property.getHouse_owner_id();
            Property_id=property.getProperty_id();
            house_ownername=property.getName();
            houseOwner_number=property.getHouseOwner_number();

            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.mipmap.logo_in_bricks_foreground)
                    .error(R.mipmap.logo_in_bricks_foreground);
            String imageUrl = "http://" + IpStatic.IpAddress.ip + "/" + property.getPhoto();

            Glide.with(this.getContext())
                    .load(imageUrl)
                    .apply(requestOptions)
                    .into(imageView);

            JSONObject json = property.getJson();
            Iterator<?> keys = json.keys();


            if(keys.hasNext()) {
                Log.d("NUll", "" + keys.toString());
            }
            ArrayList<String> keyList = new ArrayList<>();
            ArrayList<String> valueList = new ArrayList<>();

            while (keys.hasNext()) {
                String temp = (String) keys.next();
                Log.d("Keys",""+temp);
                keyList.add(temp);
                try {
                    valueList.add(json.getString(temp));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            PropertyDetailsGridView detailsAdapter = new PropertyDetailsGridView(getActivity(), keyList, valueList);
            gridView.setAdapter(detailsAdapter);
            StaticClasses.gridViewHeight.setDynamicHeight(gridView);

        }
        if(usertypeid!=0) {
        if(usertypeid==3) {
            String getBooking = "http://" + IpStatic.IpAddress.ip + ":80/api/GetBookingInfo?customer_id=" + userId + "&property_id=" + Property_id;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, getBooking, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    JSONObject jsonObject = null;
                    try {

                        jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.compareTo("200") == 0) {
                            if(name.equals("bookingFragment")){
                                contactbtn.setClickable(true);
                                contactbtnParent.addView(contactbtn);
                                progressDialog.dismiss();
                            }else {
                                contactbtn.setClickable(false);
                                ViewGroup parentView = (ViewGroup) contactbtn.getParent();
                                if (parentView != null) {
                                    parentView.removeView(contactbtn);
                                }
                                progressDialog.dismiss();
                            }

                            contactbtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    LayoutInflater inflater = getLayoutInflater();
                                    View view1 = inflater.inflate(R.layout.contact_house_owner, null);
                                    builder.setView(view1);

                                    TextView number = view1.findViewById(R.id.numberTv);
                                    TextView name = view1.findViewById(R.id.nameTv);
                                    MaterialButton okbtn = view1.findViewById(R.id.okbtn);
                                    AlertDialog dialog = builder.create();
                                    number.setText(houseOwner_number);
                                    name.setText(house_ownername);
                                    okbtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.cancel();
                                        }
                                    });
                                    dialog.show();
                                }

                            });


                        } else {
                            contactbtn.setClickable(false);
                            ViewGroup parentView = (ViewGroup) contactbtn.getParent();
                            if(parentView!=null){
                                parentView.removeView(contactbtn);
                            }

                            progressDialog.dismiss();
                        }


                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "Something went worng1!", Toast.LENGTH_LONG).show();
                }
            }) {
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                public Map<String, String> getHeaders() throws AuthFailureError {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Accept", "application/json");
                    params.put("Content-Type", "application/json");
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);

        } else if (usertypeid==2) {
            contactbtn.setClickable(false);
            ViewGroup parentView = (ViewGroup) contactbtn.getParent();
            if (parentView != null) {
                parentView.removeView(contactbtn);
            }
            progressDialog.dismiss();
        }
        }else{
            contactbtn.setClickable(false);
            ViewGroup parentView = (ViewGroup) contactbtn.getParent();
            if (parentView != null) {
                parentView.removeView(contactbtn);
            }
            progressDialog.dismiss();

        }
        progressDialog.show();
        if(usertypeid!=0) {
            if (usertypeid == 3) {
                String getBookingInfo = "http://" + IpStatic.IpAddress.ip + ":80/api/GetBookingInfo?customer_id=" + userId + "&property_id=" + Property_id;
                StringRequest stringRequest2 = new StringRequest(Request.Method.GET, getBookingInfo, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.compareTo("404") == 0) {
                                progressDialog.dismiss();
                                bookButton.setClickable(true);
                                bookButtonParent.addView(bookButton);
                                bookButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        progressDialog.show();
                                String Booking = "http://" + IpStatic.IpAddress.ip+":80/api/Booking";
                                StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Booking, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            progressDialog.dismiss();
                                            JSONObject  jsonObject = new JSONObject(response);
                                            String status = jsonObject.getString("status");
                                            if(status.compareTo("200")==0){
                                                Toast.makeText(getContext(),"Booked Sucessful!",Toast.LENGTH_LONG).show();

                                            }
                                            if(status.compareTo("422")==0){
                                                Toast.makeText(getContext(),"Something Went Wrong!",Toast.LENGTH_LONG).show();

                                            }

                                        } catch (JSONException e) {
                                            progressDialog.dismiss();
                                            throw new RuntimeException(e);
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getContext(),"Something went worng!",Toast.LENGTH_LONG).show();


                                    }
                                }) {
                                    public String getBodyContentType() {
                                        return "application/json; charset=utf-8";
                                    }
                                    public Map<String, String> getHeaders() throws AuthFailureError {

                                        Map<String,String> params =new HashMap<String,String>();
                                        params.put("Accept","application/json");
                                        params.put("Content-Type","application/json");
                                        return params;
                                    }
                                    public byte[] getBody() throws AuthFailureError {
                                        try {
                                            JSONObject jsonBody = new JSONObject();
                                            jsonBody.put("property_id", Property_id);
                                            jsonBody.put("house_owner_id", house_owner_id);
                                            jsonBody.put("customer_id", userId);
                                            return jsonBody.toString().getBytes("utf-8");
                                        } catch (JSONException | UnsupportedEncodingException e) {
                                            throw new AuthFailureError(e.getMessage());
                                        }
                                    }



                                };
                                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                                requestQueue.add(stringRequest1);
                                    }
                                });

                            } else if (status.compareTo("200")==0) {
                                bookButton.setClickable(false);
                                ViewGroup parentView = (ViewGroup) bookButton.getParent();
                                if(parentView!=null) {
                                    parentView.removeView(bookButton);
                                }
                                progressDialog.dismiss();
                            }


                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            throw new RuntimeException(e);
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(),"Something went worng1!",Toast.LENGTH_LONG).show();
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
                };
                RequestQueue requestQueue1 = Volley.newRequestQueue(getContext());
                requestQueue1.add(stringRequest2);



            } else if (usertypeid == 2) {
                bookButton.setClickable(false);
                ViewGroup parentView = (ViewGroup) bookButton.getParent();
                if(parentView!=null) {
                    parentView.removeView(bookButton);
                }
                progressDialog.dismiss();

            }
        }
        else{
            bookButton.setClickable(false);
            ViewGroup parentView = (ViewGroup) bookButton.getParent();
            if (parentView != null) {
                parentView.removeView(bookButton);
                progressDialog.dismiss();
            };
        }

    }
}