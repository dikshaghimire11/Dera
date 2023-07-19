package com.dera.customer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.dera.ChooseUserType;
import com.dera.IpStatic;
import com.dera.R;
import com.dera.SimilarFiles.Search_filter;
import com.dera.SimilarFiles.UserHome_Category_Fragment;
import com.dera.SimilarFiles.user_Advertisement;
import com.dera.SimilarFiles.user_properties;
import com.dera.StaticClasses;

public class UserHome extends Fragment {
    GridView propertiesList;

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu,menu);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction homeTransaction = fragmentManager.beginTransaction();

        Fragment userHomeFragment = new UserHome_Category_Fragment();
        homeTransaction.add(R.id.categoryFragment, userHomeFragment);

        homeTransaction.commit();


        FragmentTransaction searchTransaction = fragmentManager.beginTransaction();
        Fragment search_filter=new Search_filter();
        searchTransaction.add(R.id.search_filter,search_filter);
        searchTransaction.commit();


        FragmentTransaction advertisementTransaction = fragmentManager.beginTransaction();
        Fragment advertisement=new user_Advertisement();
        searchTransaction.add(R.id.advertiseMCV,advertisement);
        advertisementTransaction.commit();



        FragmentTransaction propertyTransaction = fragmentManager.beginTransaction();
        Fragment property=new user_properties();
        String url="http://"+ IpStatic.IpAddress.ip+":80/api/get_property";
        Log.d("Url",""+url);
        Bundle bundle=new Bundle();
        bundle.putString("url",url);
        property.setArguments(bundle);
        propertyTransaction.add(R.id.propertiesFragment,property,"propertyFragment");
        propertyTransaction.commit();

        return inflater.inflate(R.layout.fragment_user_home, container, false);
    }


}