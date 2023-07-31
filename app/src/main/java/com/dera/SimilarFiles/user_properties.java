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
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dera.Adapter.PropertyGridView;
import com.dera.R;
import com.dera.StaticClasses;
import com.dera.algorithms.FuzzySearch;
import com.dera.detailPropertyInformation;
import com.dera.models.Property;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;

public class user_properties extends Fragment {


    GridView propertiesList;
    ScrollView homeScroll;
    String fragemntName;
    ArrayList<Property> properties;

    ArrayList<String> searchSuggestionList;
    ArrayAdapter<String> suggestionAdapter;
    ListView suggestionsListView;
    Fragment searchFragment;
    View searchView;
    ProgressBar progressBar;
    ImageView noRecordsIV;
    ImageView checkConnectionIV;
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
        searchSuggestionList=new ArrayList<>();
        FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
        try {
            searchFragment = fragmentManager.findFragmentByTag("searchFragment");
            searchView = searchFragment.getView();
        }catch (NullPointerException e){
            Log.d("Exception","Search Fragment Not Found");
        }

        homeScroll=getActivity().findViewById(R.id.homeScroll);
        properties=new ArrayList<Property>();
        Log.d("Gipsy","New Array Created");
        propertiesList=view.findViewById(R.id.propertieslist);
        progressBar=view.findViewById(R.id.progressBar);
        noRecordsIV=view.findViewById(R.id.noRecordsFoundIV);
        noRecordsIV.setVisibility(View.GONE);
        checkConnectionIV=view.findViewById(R.id.checkConnectionIV);
        checkConnectionIV.setVisibility(View.GONE);
        Bundle bundle=getArguments();
        FrameLayout childFrameLayout = getActivity().findViewById(R.id.ChildFragment);
        String url=bundle.getString("url");
        String applyFilter=bundle.getString("applyFilter");
        String searchData=bundle.getString("searchData");
        int minPrice=bundle.getInt("minPrice");
        int maxPrice=bundle.getInt("maxPrice");
        String priceSearch=bundle.getString("priceSearch");
        ArrayList<String> categoryFilter=bundle.getStringArrayList("categoryFilter");
        ArrayList<String> facilityFilter=bundle.getStringArrayList("facilityFilter");
        ArrayList<String> subCategoryFilter=bundle.getStringArrayList("subCategoryFilter");
        if(bundle.getString("name")!=null){
            fragemntName =bundle.getString("name");
        }else{
            fragemntName="";
        }

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = new JSONArray(jsonObject.getString("property"));
                    int k = 0;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        k++;
                        Property property = makePropertyObject(jsonArray.getJSONObject(i));
                        if (applyFilter != null) {
                            if (applyFilter.compareTo("true") == 0) {
                                int matchCount = 0;
                                int tempCount = 0;

                                if (categoryFilter.size() != 0) {
                                    tempCount = matchCount;
                                    for (int j = 0; j < categoryFilter.size(); j++) {
                                        if (categoryFilter.get(j).equals(property.getCategory())) {
                                            matchCount++;

                                        }
                                    }
                                    if (tempCount == matchCount) {
                                        property.setIgnoreInFilter(true);
                                    }

                                }

                                if (subCategoryFilter.size() != 0) {

                                    if (property.getIsIgnoreInFilter()) {
                                        matchCount = 0;
                                    } else {
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
                                    if (tempCount == matchCount) {
                                        property.setIgnoreInFilter(true);
                                    }

                                }

                                if (facilityFilter.size() != 0) {
                                    tempCount = matchCount;
                                    if (property.getIsIgnoreInFilter()) {
                                        matchCount = 0;
                                    } else {
                                        tempCount = matchCount;
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
                                    if (tempCount == matchCount) {
                                        property.setIgnoreInFilter(true);
                                    }

                                }


                                if (priceSearch != null) {
                                    if (priceSearch.equals("true")) {
                                        if (property.getIsIgnoreInFilter() == true) {
                                            matchCount = 0;
                                        } else {
                                            tempCount = matchCount;
                                            if (minPrice != 0 && maxPrice != 0) {

                                                Log.d("Price", "MinPrice:" + minPrice + " MaxPrice:" + maxPrice);
                                                if (Integer.valueOf(property.getPrice()) >= minPrice && Integer.valueOf(property.getPrice()) <= maxPrice) {
                                                    matchCount++;

                                                }
                                                if (tempCount == matchCount) {
                                                    property.setIgnoreInFilter(true);
                                                }

                                            } else if (maxPrice != 0) {
                                                Log.d("Price", "MaxPrice:" + maxPrice);
                                                if (Integer.valueOf(property.getPrice()) <= maxPrice) {
                                                    matchCount++;

                                                }
                                                if (tempCount == matchCount) {
                                                    property.setIgnoreInFilter(true);
                                                }

                                            } else if (minPrice != 0) {
                                                Log.d("Price", "MinPrice:" + minPrice);
                                                if (Integer.valueOf(property.getPrice()) >= minPrice) {
                                                    matchCount++;

                                                }
                                                if (tempCount == matchCount) {
                                                    property.setIgnoreInFilter(true);
                                                }
                                            }

                                        }
                                    }
                                }


                                if (searchData != null) {
                                    if (!searchData.isEmpty()) {

                                        if (property.getIsIgnoreInFilter()) {
                                            matchCount = 0;
                                        } else {

                                            tempCount = matchCount;
                                            if (FuzzySearch.fuzzySearch(searchData.toString(), property.getFullLocation())) {

                                                matchCount++;
                                            } else {
                                                property.setIgnoreInFilter(true);

                                            }


                                        }


                                    }
                                }


                                if (property.getIsIgnoreInFilter() == false) {
                                    properties.add(property);

                                }


                            }

                        } else {
                            properties.add(property);

                        }
                    }
                    if (properties.size() != 0) {

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
                                }
                                try {
                                    fragmentTransaction.hide(fragmentManager.findFragmentByTag("historyFragment"));
                                } catch (NullPointerException e) {
                                }
                                try {
                                    fragmentTransaction.hide(fragmentManager.findFragmentByTag("bookingFragment"));
                                } catch (NullPointerException e) {
                                }
                                homeScroll.setVerticalScrollbarPosition(300);
                                fragmentTransaction.add(childFrameLayout.getId(), detailFragment, "detailFragment");

                                fragmentTransaction.commit();
                            }
                        });
                    }else{
                        noRecordsIV.setVisibility(View.VISIBLE);
                    }

                    if (jsonArray.length() != 0) {
                            StaticClasses.gridViewHeight.setDynamicHeight(propertiesList);
                        }

                    } catch(JSONException e){
                        throw new RuntimeException(e);
                    }
                progressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                checkConnectionIV.setVisibility(View.VISIBLE);
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

        try {
            StaticClasses.searchSupport.suggestionAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, searchSuggestionList);
            suggestionsListView = searchView.findViewById(R.id.suggestionListView);
            suggestionsListView.setAdapter(StaticClasses.searchSupport.suggestionAdapter);
        }catch (NullPointerException e){
            Log.d("Exceptin","Search View is Null");
        }
    }

    public Property makePropertyObject(JSONObject data) throws JSONException {
        JSONObject details = new JSONObject(data.getString("property_details"));
        String CategoryName = data.getString("categoryName");
        String House_Owner_id = data.getString("houseowner_id");
        String Property_id = data.getString("id");
        String name = data.has("name") ? data.getString("name") : "";
        String houseOwner_number = data.getString("mobile");
        String fullLocation=data.getString("provinceName")+" "+data.getString("districtName")+" "+data.getString("localLevelName")+" "+data.getString("wardName")+" "+data.getString("tole");
        String Status=data.has("HistoryStatus")?data.getString("HistoryStatus"):"";
        String HistoryDate=data.has("HistoryDate")? data.getString("HistoryDate"):"";

        Property property = new Property(CategoryName, data.getString("districtName") + "- " + data.getString("tole"),fullLocation, data.getString("price"), giveRoomNumber(details, CategoryName), data.getString("photo"), details, House_Owner_id, Property_id, name, houseOwner_number,Status,HistoryDate);

        if(searchView!=null) {
            searchSuggestionList.add(data.getString("tole"));
            searchSuggestionList.add(data.getString("localLevelName"));
            searchSuggestionList.add(data.getString("provinceName"));
        }
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