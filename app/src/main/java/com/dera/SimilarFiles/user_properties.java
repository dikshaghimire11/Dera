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

public class user_properties extends Fragment{


GridView propertiesList;
ScrollView homeScroll;

ArrayList<Property> properties;
private int gridViewScrollPosition=0;
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
        gridViewScrollPosition=propertiesList.getFirstVisiblePosition();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        propertiesList.setSelection(gridViewScrollPosition);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        homeScroll=getActivity().findViewById(R.id.homeScroll);
        properties=new ArrayList<Property>();
        propertiesList=view.findViewById(R.id.propertieslist);
        Bundle bundle=getArguments();
        String url=bundle.getString("url");
        String applyFilter=bundle.getString("applyFilter");
        int minPrice=bundle.getInt("minPrice");
        int maxPrice=bundle.getInt("maxPrice");
        ArrayList<String> categoryFilter=bundle.getStringArrayList("categoryFilter");
        ArrayList<String> subCategoryFilter=bundle.getStringArrayList("subCategoryFilter");
        ArrayList<String> facilityFilter=bundle.getStringArrayList("facilityFilter");
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray= new JSONArray(jsonObject.getString("property"));
                    int k=0;
                    for(int i=0;i<jsonArray.length();i++){
                        k++;
                        Property property=makePropertyObject(jsonArray.getJSONObject(i));
                      if(applyFilter!=null){ if(applyFilter.compareTo("true")==0){
                          int matchCount=0;
                          int tempCount=0;

                          if(categoryFilter.size()!=0){
                              tempCount=matchCount;
                              for(int j=0;j<categoryFilter.size();j++){
                                  if(categoryFilter.get(j).equals(property.getCategory())){
                                      matchCount++;

                                  }
                              }
                              if(tempCount==matchCount){
                                  property.setIgnoreInFilter(true);
                              }
                              Log.d(k+" Intersection",""+property.getLocation()+" Ignored: "+property.getIsIgnoreInFilter());

                          }
                          if(subCategoryFilter.size()!=0) {

                            if(property.getIsIgnoreInFilter()){
                            matchCount=0;
                            }else {
                                tempCount = matchCount;
                                for (int j = 0; j < subCategoryFilter.size(); j++) {

                                    if (subCategoryFilter.get(j).equals("B")) {
                                        if (property.getNumber().equals("1B")) {
                                            matchCount++;
                                        }
                                    } else if (subCategoryFilter.get(j).equals("BK")) {

                                        if (!property.getNumber().isEmpty()) {
                                            if (property.getNumber().substring(1).equals("BK")) {
                                                matchCount++;
                                            }
                                        }

                                    } else {
                                        if (!property.getNumber().isEmpty()) {
                                            if (property.getNumber().equals(subCategoryFilter.get(j))) {
                                                matchCount++;
                                            }
                                        }
                                    }

                                }
                            }
                              if(tempCount==matchCount){
                                  property.setIgnoreInFilter(true);
                              }
                              Log.d(k+" Intersection",""+property.getLocation()+" Ignored: "+property.getIsIgnoreInFilter());
                          }
                          if(facilityFilter.size()!=0) {
                              tempCount=matchCount;
                              if (property.getIsIgnoreInFilter()) {
                                    matchCount=0;
                              } else {
                                  tempCount=matchCount;
                                  JSONObject json = property.getJson();

                                  for (int j = 0; j < facilityFilter.size(); j++) {
                                      try {
                                          if (facilityFilter.get(j).equals("attachedBathroom")) {
                                              if (json.getString("BathroomType").equals("Attached")) {
                                                  matchCount++;
                                              }
                                          }
                                      } catch (JSONException e) {
                                          e.printStackTrace();
                                      }
                                      try {
                                          if (facilityFilter.get(j).equals("alwaysWater")) {
                                              if (json.getString("HouseHoldWater").equals("Plently")) {
                                                  matchCount++;
                                              }
                                          }
                                      } catch (JSONException e) {
                                          e.printStackTrace();
                                      }
                                      try {
                                          if (facilityFilter.get(j).equals("carParking")) {
                                              if (json.getString("CarParking").equals("Yes")) {
                                                  matchCount++;
                                              }
                                          }
                                      } catch (JSONException e) {
                                          e.printStackTrace();
                                      }
                                      try {
                                          if (facilityFilter.get(j).equals("bikeParking")) {
                                              if (json.getString("BikeParking").equals("Yes")) {
                                                  matchCount++;
                                              }
                                          }
                                      } catch (JSONException e) {
                                          e.printStackTrace();
                                      }
                                  }
                              }
                              if(tempCount==matchCount){
                                  property.setIgnoreInFilter(true);
                              }
                              Log.d(k+" Intersection",""+property.getLocation()+" Ignored: "+property.getIsIgnoreInFilter());
                          }

                          if(property.getIsIgnoreInFilter()==true){
                            matchCount=0;
                          }
                          else {
                              tempCount=matchCount;
                              if (minPrice != 0 && maxPrice != 0) {

                                  Log.d("Price", "MinPrice:" + minPrice + " MaxPrice:" + maxPrice);
                                  if (Integer.valueOf(property.getPrice()) >= minPrice && Integer.valueOf(property.getPrice()) <= maxPrice) {
                                      matchCount++;

                                  }
                              } else if (maxPrice != 0) {
                                  Log.d("Price", "MaxPrice:" + maxPrice);
                                  if (Integer.valueOf(property.getPrice()) <= maxPrice) {
                                      matchCount++;

                                  }

                              } else if (minPrice != 0) {
                                  Log.d("Price", "MinPrice:" + minPrice);
                                  if (Integer.valueOf(property.getPrice()) >= minPrice) {
                                      matchCount++;

                                  }
                              }
                          }
                          if(tempCount==matchCount){
                              property.setIgnoreInFilter(true);
                          }
                          Log.d(k+" Intersection",""+property.getLocation()+" Ignored: "+property.getIsIgnoreInFilter());

                          if(matchCount!=0) properties.add(property);


                        }

                      }else {
                            properties.add(property);
                        }
                    }
                    if(properties.size()==0){
                        return;
                    }
                    PropertyGridView propertyGridView=new PropertyGridView(getActivity(),properties);
                    propertiesList.setAdapter(propertyGridView);
                    propertiesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                            detailPropertyInformation detailFragment=new detailPropertyInformation();
                            Bundle bundle=new Bundle();
                            bundle.putSerializable("model",properties.get(i));
                            bundle.putInt("scrollPosition",homeScroll.getVerticalScrollbarPosition());
                            Log.d("Scroll",""+homeScroll.getVerticalScrollbarPosition());
                            detailFragment.setArguments(bundle);
                            FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                           fragmentTransaction.hide(fragmentManager.findFragmentByTag("propertyFragment"));
                            Log.d("Fragments",""+fragmentManager.getFragments());
                            homeScroll.setVerticalScrollbarPosition(300);
                            Log.d("HomeScroll",""+homeScroll);
                            fragmentTransaction.add(R.id.propertiesFragment,detailFragment,"detailFragment");

                            fragmentTransaction.commit();
                        }
                    });
                    if(jsonArray.length()!=0) {
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
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
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
    JSONObject details=new JSONObject(data.getString("property_details"));
    String CategoryName=data.getString("categoryName");
        Iterator<?> keys = details.keys();



    Property property=new Property(CategoryName,data.getString("districtName")+"- "+data.getString("tole"),data.getString("price"),giveRoomNumber(details,CategoryName),data.getString("photo"),details,keys);
    return property;
    }
    public String giveRoomNumber(JSONObject data,String category) throws JSONException {
    String value="";
    if(category.equals("Room")||category.equals("Flat")){
       if(!data.getString("BedRoom").equals("0")){
           value=value+data.getString("BedRoom")+"B";
       }
        if(!data.getString("LivingRoom").equals("No")){
            value=value+"H";
        }
        if(!data.getString("Kitchen").equals("No")){
            value=value+"K";
        }

    }

        return value;
    }

}