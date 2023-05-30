package com.dera.customer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.dera.R;
import com.dera.SimilarFiles.Search_filter;
import com.dera.SimilarFiles.UserHome_Category_Fragment;
import com.dera.SimilarFiles.user_Advertisement;
import com.dera.SimilarFiles.user_properties;

public class UserHome extends Fragment {
    GridView propertiesList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment myFragment = new UserHome_Category_Fragment();
        fragmentTransaction.add(R.id.categoryFragment, myFragment);

        fragmentTransaction.commit();

        FragmentManager fragmentmanager = getFragmentManager();
        FragmentTransaction fragmenttransaction = fragmentManager.beginTransaction();
        Fragment search_filter=new Search_filter();
        fragmenttransaction.add(R.id.search_filter,search_filter);
        fragmenttransaction.commit();

        FragmentManager fragmentmanag = getFragmentManager();
        FragmentTransaction f = fragmentmanag.beginTransaction();
        Fragment advertisement=new user_Advertisement();
        fragmenttransaction.add(R.id.advertiseMCV,advertisement);
        f.commit();



        FragmentManager fragmentManager1 = getFragmentManager();
        FragmentTransaction transaction = fragmentManager1.beginTransaction();
        Fragment property=new user_properties();
        fragmenttransaction.add(R.id.propertiesTV,property);
        transaction.commit();

        return inflater.inflate(R.layout.fragment_user_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




    }
}