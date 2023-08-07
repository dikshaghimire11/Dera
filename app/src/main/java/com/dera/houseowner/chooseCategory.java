package com.dera.houseowner;

import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
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
import com.dera.SimilarFiles.UserHome_Category_Fragment;
import com.dera.StaticClasses;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONException;
import org.json.JSONObject;


public class chooseCategory extends Fragment {

    UserHome_Category_Fragment userHome;
    View mainFragmentView;
    View childFragmentView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userHome=new UserHome_Category_Fragment();
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.categoryFragment, userHome);
        fragmentTransaction.commit();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        StaticClasses.backStackManager.setBackStack("chooseCategoryFragment","homeFragment",getActivity().getSupportFragmentManager());
        // Inflate the layout for this fragment
        mainFragmentView= inflater.inflate(R.layout.fragment_choose_category, container, false);
        return mainFragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LayoutInflater inflater=getLayoutInflater();

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
                            StaticClasses.loginInfo.UserID="";
                            StaticClasses.loginInfo.loginToken="";
                            StaticClasses.loginInfo.userName="";
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
        childFragmentView=mainFragmentView.findViewById(R.id.categoryFragment);
        View fragmentView=inflater.inflate(R.layout.fragment_user_home__category_,null,true);
        GridView gridView=childFragmentView.findViewById(R.id.categoryList);
        Log.d("GridView",""+gridView);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Bundle addPropertyDataBundle=new Bundle();
               if(i==0){
                   Log.d("Item Clicked","Room");
                   addPropertyDataBundle.putString("PropertyType","1");
               }
                if(i==1){
                    Log.d("Item Clicked","Flat");
                    addPropertyDataBundle.putString("PropertyType","2");
                }
                if(i==2){
                    Log.d("Item Clicked","House");
                    addPropertyDataBundle.putString("PropertyType","3");
                }
                if(i==3){
                    Log.d("Item Clicked","Shutter");
                    addPropertyDataBundle.putString("PropertyType","4");
                }
                Fragment addressInfoFragment = new AddressInfo();
                addressInfoFragment.setArguments(addPropertyDataBundle);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide(fragmentManager.findFragmentByTag("chooseCategoryFragment"));
                if(fragmentManager.findFragmentByTag("addressInformationFragment")==null) {
                    fragmentTransaction.add(R.id.fragmentlayout, addressInfoFragment, "addressInformationFragment");
                }else{
                    fragmentTransaction.show(fragmentManager.findFragmentByTag("addressInformationFragment"));
                }
                fragmentTransaction.commit();

            }
        });

    }
}