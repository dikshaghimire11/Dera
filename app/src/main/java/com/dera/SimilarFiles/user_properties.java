package com.dera.SimilarFiles;

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
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ScrollView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dera.Adapter.PropertyGridView;
import com.dera.IpStatic;
import com.dera.R;
import com.dera.StaticClasses;
import com.dera.detailPropertyInformation;
import com.dera.models.Property;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class user_properties extends Fragment {


    GridView propertiesList;
    ScrollView homeScroll;
    String fragemntName;
    ArrayList<Property> properties;
    private int gridViewScrollPosition = 0;
    user_properties thisfragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_properties, container, false);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        gridViewScrollPosition = propertiesList.getFirstVisiblePosition();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        propertiesList.setSelection(gridViewScrollPosition);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        homeScroll = getActivity().findViewById(R.id.homeScroll);
        properties = new ArrayList<Property>();
        propertiesList = view.findViewById(R.id.propertieslist);
        Bundle bundle = getArguments();
        FrameLayout childFrameLayout = getActivity().findViewById(R.id.ChildFragment);
        String url = bundle.getString("url");
        if(bundle.getString("name")!=null){
           fragemntName =bundle.getString("name");
        }else{
            fragemntName="";
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("property"));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        properties.add(makePropertyObject(jsonArray.getJSONObject(i)));
                    }
                    PropertyGridView propertyGridView = new PropertyGridView(getActivity(), properties);
                    propertiesList.setAdapter(propertyGridView);
                    propertiesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                            detailPropertyInformation detailFragment = new detailPropertyInformation();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("model", properties.get(i));
                            bundle.putInt("scrollPosition", homeScroll.getVerticalScrollbarPosition());
                            bundle.putString("name", fragemntName);
                            detailFragment.setArguments(bundle);
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                            fragmentTransaction.hide(fragmentManager.findFragmentByTag("propertyFragment"));
                            try {
                                fragmentTransaction.hide(fragmentManager.findFragmentByTag("homeFragment"));
                            } catch (NullPointerException e) {
                                Log.d("Fragment Not Found", "Removal Ignored");
                            }
                            homeScroll.setVerticalScrollbarPosition(300);
                            fragmentTransaction.add(childFrameLayout.getId(), detailFragment, "detailFragment");

                            fragmentTransaction.commit();
                        }
                    });
                    if (jsonArray.length() != 0) {
                        StaticClasses.gridViewHeight.setDynamicHeight(propertiesList);
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
        requestQueue.add(stringRequest);

        //Property property=new Property("Room","Kathmandu","1700","2","abc.jpg");
//        Property property1=new Property("Shutter","Bhaktapur","1800","3","abfc.jpg");
//        Property property2=new Property("Room","Lalitpur","2000","4","abdc.jpg");
        //properties.add(property);
//        properties.add(property1);
//        properties.add(property2);

//        int[] photo={R.drawable.roomicon,R.drawable.flaticon,R.drawable.houseicon,R.drawable.shuttericon,R.drawable.roomicon,R.drawable.shuttericon};

    }

    public Property makePropertyObject(JSONObject data) throws JSONException {
        JSONObject details = new JSONObject(data.getString("property_details"));
        String CategoryName = data.getString("categoryName");
        String House_Owner_id = data.getString("houseowner_id");
        String Property_id = data.getString("id");
        String name = data.has("name") ? data.getString("name") : "";
        String houseOwner_number = data.getString("mobile");


        Property property = new Property(CategoryName, data.getString("districtName") + "- " + data.getString("tole"), data.getString("price"), giveRoomNumber(details, CategoryName), data.getString("photo"), details, House_Owner_id, Property_id, name, houseOwner_number);
        return property;
    }

    public String giveRoomNumber(JSONObject data, String category) throws JSONException {
        String value = "";
        if (category.equals("Room") || category.equals("Flat")) {
            if (!data.getString("BedRoom").equals("0")) {
                value = value + data.getString("BedRoom") + "B";
            }
            if (!data.getString("LivingRoom").equals("0")) {
                value = value + data.getString("LivingRoom") + "H";
            }
            if (!data.getString("Kitchen").equals("0")) {
                value = value + data.getString("Kitchen") + "K";
            }

        }

        return value;
    }

}