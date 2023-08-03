package com.dera;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.dera.IpStatic;
import com.dera.R;
import com.dera.SimilarFiles.Login;
import com.dera.SimilarFiles.user_properties;
import com.dera.StaticClasses;
import com.dera.callback.OnRemovedFragments;
import com.dera.customer.UserBooking;
import com.dera.customer.UserHome;
import com.dera.houseowner.UserInformation;
import com.dera.houseowner.houseOwnerHome;
import com.dera.models.Property;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class detailPropertyInformation extends Fragment {
    ImageView imageView;
    TextView textView, fullLocation, title, number, priceTV, messageTv;
    GridView gridView;
    MaterialCardView backButton;
    ArrayList<Property> properties;
    MaterialButton bookButton, contactbtn, canclebtn, viewBookingbtn, gotoBooking, editPropertyBtn, deletePropertyBtn;
    ScrollView homeScroll;
    String house_owner_id, Property_id;
    int usertypeid;
    String name;
    String id;
    MaterialCardView bottonView;
    ConstraintLayout detailProperty;
    LinearLayout linearLayoutButtons;
    String userId, houseOwner_number, house_ownername, Status, HistoryDate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_property_information, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait.....");
        progressDialog.show();
        properties = new ArrayList<Property>();
        StaticClasses.backStackManager.setBackStack("detailFragment", "homeFragment", getActivity().getSupportFragmentManager());
        FrameLayout childFrameLayout = getActivity().findViewById(R.id.ChildFragment);
        Bundle bundle = getArguments();
        name = bundle.getString("name");
        homeScroll = getActivity().findViewById(R.id.homeScroll);
        imageView = view.findViewById(R.id.propertyIv);
        detailProperty = view.findViewById(R.id.detailsProperty);
        gridView = view.findViewById(R.id.detailsGridView);
        textView = view.findViewById(R.id.PropertyTypeTV);
        fullLocation = view.findViewById(R.id.locationTV);
        number = view.findViewById(R.id.number);
        canclebtn = view.findViewById(R.id.cancleMbtn);
        bottonView = getActivity().findViewById(R.id.bottonV);
        title = view.findViewById(R.id.title);
        gotoBooking = view.findViewById(R.id.GoToBookingMbtn);
        deletePropertyBtn = view.findViewById(R.id.deleteMbtn);
        priceTV = view.findViewById(R.id.priceTV);
        backButton = view.findViewById(R.id.backMCV);
        bookButton = view.findViewById(R.id.bookingMbtn);
        contactbtn = view.findViewById(R.id.contactMbtn);
        editPropertyBtn = view.findViewById(R.id.EditInfoMbtn);
        viewBookingbtn = view.findViewById(R.id.ViewBookingMbtn);
        linearLayoutButtons = view.findViewById(R.id.buttonsLayout);
        messageTv = view.findViewById(R.id.messageTV);
        ImageView create = getActivity().findViewById(R.id.createIV);
        if (create != null) {
            create.setImageResource(R.drawable.create);
            create.setClickable(true);
        }

        deletePropertyBtn.setClickable(false);
        ViewGroup deletebtnParent = (ViewGroup) deletePropertyBtn.getParent();
        deletebtnParent.removeView(deletePropertyBtn);


        contactbtn.setClickable(false);
        ViewGroup contactbtnParent = (ViewGroup) contactbtn.getParent();
        contactbtnParent.removeView(contactbtn);

        bookButton.setClickable(false);
        ViewGroup bookButtonParent = (ViewGroup) bookButton.getParent();
        bookButtonParent.removeView(bookButton);


        gotoBooking.setClickable(false);
        ViewGroup GotoButtonParent = (ViewGroup) gotoBooking.getParent();
        GotoButtonParent.removeView(gotoBooking);


        canclebtn.setClickable(false);
        ViewGroup cancle = (ViewGroup) canclebtn.getParent();
        cancle.removeView(canclebtn);


        viewBookingbtn.setClickable(false);
        ViewGroup viewBookig = (ViewGroup) viewBookingbtn.getParent();
        viewBookig.removeView(viewBookingbtn);

        editPropertyBtn.setClickable(false);
        ViewGroup editProperty = (ViewGroup) editPropertyBtn.getParent();
        editProperty.removeView(editPropertyBtn);

        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) linearLayoutButtons.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, bottonView.getHeight());
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
                    create.setImageResource(R.drawable.create);
                    create.setClickable(true);
                } catch (NullPointerException e) {
                    Log.d("Fragment Not Found", "Removal Ignored");
                }
                try {
                    fragmentTransaction.show(fragmentManager.findFragmentByTag("historyFragment"));
                    create.setImageResource(R.drawable.create);
                    create.setClickable(true);

                } catch (NullPointerException e) {
                    Log.d("Fragment Not Found", "Removal Ignored");
                }
                try {
                    fragmentTransaction.show(fragmentManager.findFragmentByTag("bookingFragment"));
                    create.setImageResource(R.drawable.create);
                    create.setClickable(true);
                } catch (NullPointerException e) {
                    Log.d("Fragment Not Found", "Removal Ignored");
                }

                Log.d("FragmentDetail", "" + fragmentManager.findFragmentByTag("propertyFragment"));
                homeScroll.setVerticalScrollbarPosition(bundle.getInt("scrollPosition"));

                // fragmentTransaction.replace(R.id.propertiesFragment,user_properties);
                fragmentTransaction.commit();

            }
        });
        if (bundle != null) {
            Property property = (Property) bundle.getSerializable("model");
            textView.setText(property.getCategory());
            title.setText(property.getCategory());
            fullLocation.setText(property.getLocation());
            priceTV.setText(property.getPrice());
            number.setText(property.getNumber());
            house_owner_id = property.getHouse_owner_id();
            Property_id = property.getProperty_id();
            house_ownername = property.getName();
            houseOwner_number = property.getHouseOwner_number();
            Status = property.getStatus();
            HistoryDate = property.getHistoryDate();
            if (name.equals("HouseOwnerhistoryFragment")) {
                String getStatusUrl = "http://" + IpStatic.IpAddress.ip + "/api/GetStatusOfHouseOwnerfromHistory?property_id=" + Property_id + "&house_owner_id=" + StaticClasses.loginInfo.UserID;
                StringRequest getStatue = new StringRequest(Request.Method.GET, getStatusUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject responseObj = new JSONObject(response);
                            JSONArray propertiesArray = responseObj.getJSONArray("property");
                            String BookingStatus, PropertyStatus, Historydate;
                            if (propertiesArray.length() > 0) {
                                JSONObject propertyObj = propertiesArray.getJSONObject(0);
                                PropertyStatus = propertyObj.getString("PropertyStatus");
                                Historydate = propertyObj.getString("HistoryDate");

                                if (PropertyStatus.equals("2")) {
                                    messageTv.setText("Deleted the Property at " + Historydate);
                                }
                                if (PropertyStatus.equals("1")) {
                                    messageTv.setText("Approved Booking at " + Historydate);
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
                });
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                requestQueue.add(getStatue);


                String getDeleteStatusUrl = "http://" + IpStatic.IpAddress.ip + "/api/GetStatusOfHouseOwnerfromDeleted?property_id=" + Property_id;
                StringRequest getDeleteStatue = new StringRequest(Request.Method.GET, getDeleteStatusUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject responseObj = new JSONObject(response);
                            JSONArray propertiesArray = responseObj.getJSONArray("property");
                            String PropertyStatus, Historydate;
                            if (propertiesArray.length() > 0) {
                                JSONObject propertyObj = propertiesArray.getJSONObject(0);
                                PropertyStatus = propertyObj.getString("PropertyStatus");
                                Historydate = propertyObj.getString("HistoryDate");

                                if (PropertyStatus.equals("2")) {
                                    messageTv.setText("Deleted the Property at " + Historydate);
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
                });
                RequestQueue requestQueue1 = Volley.newRequestQueue(getContext());
                requestQueue1.add(getDeleteStatue);

            }
            if(name.equals("UserhistoryFragment")){
                String bookingStatus=property.getBookingStatus();
                String propertyStatus=property.getPropertyStatus();
                String historydate=property.getHistoryDate();
                String PropertyFinalizedDate=property.getPropertyFinalizedDate();
                if(bookingStatus.equals("0")){
                    if(propertyStatus.equals("1")){
                        messageTv.setText("Property Occupied at " + PropertyFinalizedDate);
                    } else if (propertyStatus.equals("2")) {
                        messageTv.setText("Deleted By HouseOwner at " + PropertyFinalizedDate);
                    }
                }else{
                    if(bookingStatus.equals("1")){
                        messageTv.setText("Cancelled By Yourselves at " + historydate);
                    } else if (bookingStatus.equals("2")) {
                        messageTv.setText("Cancelled By Houseowner at " + historydate);
                    } else if (bookingStatus.equals("3")) {
                        messageTv.setText("Approved By Houseowner at " + historydate);
                    }
                }

            }

//            if (name.equals("UserhistoryFragment")) {
//
//                String getStatusUrl = "http://" + IpStatic.IpAddress.ip + "/api/GetStatusfromHistory?property_id=" + Property_id + "&customer_id=" + StaticClasses.loginInfo.UserID;
//                StringRequest getStatue = new StringRequest(Request.Method.GET, getStatusUrl, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject responseObj = new JSONObject(response);
//                            JSONArray propertiesArray = responseObj.getJSONArray("property");
//                            if (propertiesArray.length() > 0) {
//                               for(int i =0;i<propertiesArray.length();i++){
//                                   JSONObject propertyObj=propertiesArray.getJSONObject(i);
//                                   int BookingStatus = propertyObj.getInt("BookingStatus");
//                                   int PropertyStatus = propertyObj.getInt("PropertyStatus");
//                                   String historyDate = propertyObj.getString("HistoryDate");
//                                   String bookingFinalizeDate=propertyObj.getString("BookingFinalizeDate");
//                                   Log.d("Property_id",""+BookingStatus);
//                                   if (BookingStatus == 0) {
//                                       if (PropertyStatus == 2) {
//                                           messageTv.setText("Deleted By HouseOwner at " + bookingFinalizeDate);
//                                       } else if (PropertyStatus == 1) {
//                                           messageTv.setText("Property Occupied at " + bookingFinalizeDate);
//                                       }
//
//                                   } else if (BookingStatus == 1) {
//                                       messageTv.setText("Cancelled By Yourselves at " + historyDate);
//                                   } else if (BookingStatus == 2) {
//                                       messageTv.setText("Cancelled By Houseowner at " + historyDate);
//                                   } else if (BookingStatus == 3 && PropertyStatus == 1) {
//                                       messageTv.setText("Approved By Houseowner at " + historyDate);
//                                   }
//                               }
//
//                            }
//                        } catch (JSONException e) {
//                            throw new RuntimeException(e);
//                        }
//
//
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                });
//                RequestQueue response = Volley.newRequestQueue(getContext());
//                response.add(getStatue);
//
//            }


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


            if (keys.hasNext()) {
                Log.d("NUll", "" + keys.toString());
            }
            ArrayList<String> keyList = new ArrayList<>();
            ArrayList<String> valueList = new ArrayList<>();

            while (keys.hasNext()) {
                String temp = (String) keys.next();
                Log.d("Keys", "" + temp);
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
        if (usertypeid != 0) {
            if (usertypeid == 3) {
                String getBooking = "http://" + IpStatic.IpAddress.ip + ":80/api/GetBookingInfo?customer_id=" + userId + "&property_id=" + Property_id;

                StringRequest stringRequest = new StringRequest(Request.Method.GET, getBooking, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {

                            jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.compareTo("200") == 0) {
                                JSONObject dataObject = jsonObject.getJSONObject("data");
                                id = dataObject.getString("id");
                                Log.d("booking_id", "" + id);
                                if (name.equals("bookingFragment")) {
                                    contactbtn.setClickable(true);
                                    contactbtnParent.addView(contactbtn);
                                    canclebtn.setClickable(true);
                                    cancle.addView(canclebtn);
                                    progressDialog.dismiss();
                                }
                                if (name.equals("homeFragment")) {
                                    gotoBooking.setClickable(true);
                                    GotoButtonParent.addView(gotoBooking);

                                }
                                progressDialog.dismiss();

                                gotoBooking.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Fragment bookingFragment = new UserBooking();
                                        FragmentManager manager = getActivity().getSupportFragmentManager();
                                        StaticClasses.CloseAllFragments.removeByManager(manager, new OnRemovedFragments() {
                                            @Override
                                            public void removedFragments(FragmentTransaction transaction) {
                                                transaction.replace(R.id.fragmentlayout, bookingFragment, "bookingFragment");
                                                transaction.commit();
                                            }
                                        });
                                    }
                                });

                                contactbtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                        LayoutInflater inflater = getLayoutInflater();
                                        View view1 = inflater.inflate(R.layout.contact_house_owner, null);
                                        builder.setView(view1);

                                        TextView number = view1.findViewById(R.id.numberTv);
                                        TextView name = view1.findViewById(R.id.nameTv);
                                        ImageView callIv=view1.findViewById(R.id.callme);
                                        MaterialButton okbtn = view1.findViewById(R.id.okbtn);
                                        AlertDialog dialog = builder.create();
                                        number.setText(houseOwner_number);

                                        name.setText(house_ownername);

                                        callIv.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                String mobilenumber= number.getText().toString();
                                                if(!mobilenumber.isEmpty()){
                                                    mobilenumber=mobilenumber.replaceAll("[^0-9]","");
                                                    Intent intent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+mobilenumber));
                                                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                                        startActivity(intent);
                                                    } else {
                                                        // Request the CALL_PHONE permission if it's not granted
                                                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                                                    }
                                                }
                                            }
                                        });
                                        okbtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialog.cancel();
                                            }
                                        });
                                        dialog.show();
                                    }

                                });
                                canclebtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String cancelBooking = "http://" + IpStatic.IpAddress.ip + ":80/api/ChangeStatus? booking_id=" + id + "&status=" + 1;
                                        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, cancelBooking, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject jsonObject1 = new JSONObject(response);
                                                    String status = jsonObject1.getString("status");
                                                    if (status.compareTo("200") == 0) {
                                                        Toast.makeText(getContext(), "Booking Cancelled!", Toast.LENGTH_LONG).show();
                                                        String insertInto = "http://" + IpStatic.IpAddress.ip + ":80/api/StoreHistory";
                                                        StringRequest history = new StringRequest(Request.Method.POST, insertInto, new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {
                                                                try {
                                                                    JSONObject jsonObject2 = new JSONObject(response);
                                                                    String status = jsonObject2.getString("status");
                                                                    if (status.compareTo("200") == 0) {
                                                                        Fragment fragment = new UserHome();
                                                                        FragmentManager manager = getActivity().getSupportFragmentManager();
                                                                        StaticClasses.CloseAllFragments.removeByManager(manager, new OnRemovedFragments() {
                                                                            @Override
                                                                            public void removedFragments(FragmentTransaction transaction) {
                                                                                fragment.setArguments(bundle);
                                                                                transaction.add(R.id.fragmentlayout, fragment, "homeFragment");
                                                                                transaction.commit();
                                                                            }
                                                                        });
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

                                                            @Nullable
                                                            @Override
                                                            public byte[] getBody() throws AuthFailureError {
                                                                try {
                                                                    JSONObject jsonBody = new JSONObject();
                                                                    jsonBody.put("property_id", Property_id);
                                                                    jsonBody.put("status", "canceled by roomfinder");
                                                                    jsonBody.put("booking_id", id);
                                                                    return jsonBody.toString().getBytes("utf-8");
                                                                } catch (JSONException |
                                                                         UnsupportedEncodingException e) {
                                                                    throw new AuthFailureError(e.getMessage());
                                                                }
                                                            }

                                                            @Override
                                                            public Map<String, String> getHeaders() throws AuthFailureError {

                                                                Map<String, String> params = new HashMap<String, String>();
                                                                params.put("Accept", "application/json");
                                                                params.put("Content-Type", "application/json");
                                                                params.put("Authorization","Bearer "+StaticClasses.loginInfo.loginToken);
                                                                return params;
                                                            }
                                                        };

                                                        RequestQueue historyrequestQueue = Volley.newRequestQueue(getContext());
                                                        historyrequestQueue.add(history);
                                                    }
                                                } catch (JSONException e) {
                                                    throw new RuntimeException(e);
                                                }

                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(getContext(), "Something went worng!", Toast.LENGTH_LONG).show();
                                            }
                                        }){
                                            @Override
                                            public Map<String, String> getHeaders() throws AuthFailureError {

                                                Map<String, String> params = new HashMap<String, String>();
                                                params.put("Accept", "application/json");
                                                params.put("Content-Type", "application/json");
                                                params.put("Authorization","Bearer "+StaticClasses.loginInfo.loginToken);
                                                return params;
                                            }
                                        };
                                        RequestQueue requestQueue1 = Volley.newRequestQueue(getContext());
                                        requestQueue1.add(stringRequest1);
                                    }
                                });


                            }


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Something went worng!", Toast.LENGTH_LONG).show();
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

            } else if (usertypeid == 2) {
                String getBooking = "http://" + IpStatic.IpAddress.ip + ":80/api/GetHouseOwnerBookingInfo?house_owner_id=" + StaticClasses.loginInfo.UserID + "&property_id=" + Property_id;

                StringRequest stringRequest = new StringRequest(Request.Method.GET, getBooking, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {

                            jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            Log.d("name", "" + name);
                            if (status.compareTo("200") == 0) {
                                if (name.equals("houseOwnerbookingFragment")) {
                                    viewBookingbtn.setClickable(true);
                                    viewBookig.addView(viewBookingbtn);
                                    progressDialog.dismiss();
                                    viewBookingbtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Log.d("Click", "ViewBooking is click");
                                            Fragment viewBooking = new UserInformation();
                                            Bundle arguments = getArguments();
                                            Property property = (Property) arguments.getSerializable("model");
                                            int scrollPosition = arguments.getInt("scrollPosition");
                                            String name = arguments.getString("name");
                                            Bundle bundle1 = new Bundle();
                                            bundle1.putString("property_id", Property_id);
                                            bundle1.putInt("scrollPosition", scrollPosition);
                                            bundle1.putSerializable("model", property);
                                            bundle1.putString("name", name);
                                            viewBooking.setArguments(bundle1);
                                            FragmentManager manager = getActivity().getSupportFragmentManager();
                                            FragmentTransaction transaction = manager.beginTransaction();
                                            transaction.hide(manager.findFragmentByTag("detailFragment"));
                                            if (manager.findFragmentByTag("viewBookingFragment") == null) {
                                                transaction.add(childFrameLayout.getId(), viewBooking, "viewBookingFragment");
                                            } else {
                                                transaction.show(manager.findFragmentByTag("viewBookingFragment"));
                                            }
                                            transaction.commit();
                                        }
                                    });


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
                        Toast.makeText(getContext(), "Something went worng!", Toast.LENGTH_LONG).show();
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
                if (name.equals("houseOwnerhomeFragment")) {
                    editPropertyBtn.setClickable(true);
                    editProperty.addView(editPropertyBtn);
                    deletePropertyBtn.setClickable(true);
                    deletebtnParent.addView(deletePropertyBtn);

                    editPropertyBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                    if (isAdded() && getActivity() != null) {
                        deletePropertyBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
                                builder.setTitle("Delete Property?");
                                builder.setMessage("Do you want to delete this Property?");
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String deletePropertyurl = "http://" + IpStatic.IpAddress.ip + ":80/api/DeleteByHouseOwner?property_id=" + Property_id;
                                        StringRequest deleteProperty = new StringRequest(Request.Method.GET, deletePropertyurl, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    String status = jsonObject.getString("status");
                                                    if (status.compareTo("200") == 0) {

                                                        String insertInto = "http://" + IpStatic.IpAddress.ip + ":80/api/StoreHistory";
                                                        StringRequest history = new StringRequest(Request.Method.POST, insertInto, new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {
                                                                try {
                                                                    JSONObject jsonObject2 = new JSONObject(response);
                                                                    String status = jsonObject2.getString("status");
                                                                    if (status.compareTo("200") == 0) {
                                                                        Toast.makeText(getContext(), "Property Deleted Successfully!", Toast.LENGTH_LONG).show();
                                                                        Fragment fragment = new houseOwnerHome();
                                                                        FragmentManager manager = getActivity().getSupportFragmentManager();
                                                                        StaticClasses.CloseAllFragments.removeByManager(manager, new OnRemovedFragments() {
                                                                            @Override
                                                                            public void removedFragments(FragmentTransaction transaction) {
                                                                                fragment.setArguments(bundle);
                                                                                transaction.add(R.id.fragmentlayout, fragment, "homeFragment");
                                                                                transaction.commit();
                                                                            }
                                                                        });
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

                                                            @Nullable
                                                            @Override
                                                            public byte[] getBody() throws AuthFailureError {
                                                                try {
                                                                    JSONObject jsonBody = new JSONObject();
                                                                    jsonBody.put("property_id", Property_id);
                                                                    jsonBody.put("status", "deleted by houseowner");
                                                                    return jsonBody.toString().getBytes("utf-8");
                                                                } catch (JSONException |
                                                                         UnsupportedEncodingException e) {
                                                                    throw new AuthFailureError(e.getMessage());
                                                                }
                                                            }

                                                            @Override
                                                            public Map<String, String> getHeaders() throws AuthFailureError {

                                                                Map<String, String> params = new HashMap<String, String>();
                                                                params.put("Accept", "application/json");
                                                                params.put("Content-Type", "application/json");
                                                                return params;
                                                            }
                                                        };

                                                        RequestQueue historyrequestQueue = Volley.newRequestQueue(getContext());
                                                        historyrequestQueue.add(history);


                                            }
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getContext(), "Something went wrong!" + error.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }){
                                    @Override
                                    public Map<String, String> getHeaders() throws AuthFailureError {

                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("Accept", "application/json");
                                        params.put("Content-Type", "application/json");
                                        params.put("Authorization","Bearer "+StaticClasses.loginInfo.loginToken);
                                        return params;
                                    }

                                };
                                RequestQueue deleteQueue = Volley.newRequestQueue(getContext());
                                deleteQueue.add(deleteProperty);
                                    }
                                });
                                android.app.AlertDialog alertDialog = builder.create();
                                alertDialog.show();


                            }
                        });
                    }

                }
            }
        }
        progressDialog.show();
        if (usertypeid != 0) {
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
                                if (name.equals("UserhistoryFragment")) {
                                    bookButton.setClickable(false);
                                    bookButtonParent.removeView(bookButton);
                                } else {
                                    progressDialog.dismiss();
                                    bookButton.setClickable(true);
                                    bookButtonParent.addView(bookButton);
                                }
                                bookButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        progressDialog.show();
                                        String Booking = "http://" + IpStatic.IpAddress.ip + ":80/api/Booking";
                                        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Booking, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    progressDialog.dismiss();
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    String status = jsonObject.getString("status");
                                                    if (status.compareTo("200") == 0) {
                                                        progressDialog.show();
                                                        Toast.makeText(getContext(), "Booked Sucessfully!", Toast.LENGTH_LONG).show();
                                                        Bundle arguments = getArguments();
                                                        Property property = (Property) arguments.getSerializable("model");
                                                        int scrollPosition = arguments.getInt("scrollPosition");
                                                        String name = arguments.getString("name");
                                                        detailPropertyInformation detailFragment = new detailPropertyInformation();
                                                        Bundle b = new Bundle();
                                                        b.putSerializable("model", property);
                                                        b.putInt("scrollPosition", scrollPosition);
                                                        b.putString("name", name);
                                                        detailFragment.setArguments(bundle);
                                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                        fragmentTransaction.hide(fragmentManager.findFragmentByTag("propertyFragment"));
                                                        fragmentTransaction.hide(fragmentManager.findFragmentByTag("detailFragment"));
                                                        try {
                                                            fragmentTransaction.hide(fragmentManager.findFragmentByTag("homeFragment"));
                                                        } catch (NullPointerException e) {
                                                        }
                                                        homeScroll.setVerticalScrollbarPosition(300);
                                                        fragmentTransaction.add(childFrameLayout.getId(), detailFragment, "detailFragment");

                                                        fragmentTransaction.commit();
                                                        progressDialog.dismiss();
                                                    }
                                                    if (status.compareTo("422") == 0) {
                                                        Toast.makeText(getContext(), "Something Went Wrong!", Toast.LENGTH_LONG).show();

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
                                                Toast.makeText(getContext(), "Something went worng!", Toast.LENGTH_LONG).show();


                                            }
                                        }) {
                                            public String getBodyContentType() {
                                                return "application/json; charset=utf-8";
                                            }

                                            public Map<String, String> getHeaders() throws AuthFailureError {

                                                Map<String, String> params = new HashMap<String, String>();
                                                params.put("Accept", "application/json");
                                                params.put("Content-Type", "application/json");
                                                params.put("Authorization","Bearer "+StaticClasses.loginInfo.loginToken);
                                                return params;
                                            }

                                            public byte[] getBody() throws AuthFailureError {
                                                try {
                                                    JSONObject jsonBody = new JSONObject();
                                                    jsonBody.put("property_id", Property_id);
                                                    jsonBody.put("house_owner_id", house_owner_id);
                                                    jsonBody.put("customer_id", userId);
                                                    return jsonBody.toString().getBytes("utf-8");
                                                } catch (JSONException |
                                                         UnsupportedEncodingException e) {
                                                    throw new AuthFailureError(e.getMessage());
                                                }
                                            }


                                        };
                                        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                                        requestQueue.add(stringRequest1);
                                    }
                                });

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
                RequestQueue requestQueue1 = Volley.newRequestQueue(getContext());
                requestQueue1.add(stringRequest2);


            }
            progressDialog.dismiss();
        } else {
            bookButton.setClickable(true);
            bookButtonParent.addView(bookButton);
            bookButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), Login.class);
                    intent.putExtra("usertype", 3);
                    startActivity(intent);
                }
            });
        }
        progressDialog.dismiss();
    }
    private static final int REQUEST_PHONE_CALL = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PHONE_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, initiate the phone call again
                String mobileNumber = number.getText().toString().replaceAll("[^0-9]", "");
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobileNumber));
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(intent);
                }
            } else {
                // Permission denied. You can show a toast or display a message to the user.
                Toast.makeText(getContext(), "Phone call permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}