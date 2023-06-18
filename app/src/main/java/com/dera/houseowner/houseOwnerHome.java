package com.dera.houseowner;

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
import android.widget.ImageView;

import com.dera.R;
import com.dera.SimilarFiles.user_Advertisement;
import com.dera.SimilarFiles.user_properties;


public class houseOwnerHome extends Fragment {


    GridView propertiesList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        FragmentManager fragmentmanag = getFragmentManager();
        FragmentTransaction f = fragmentmanag.beginTransaction();
        Fragment advertisement = new user_Advertisement();
        f.add(R.id.advertiseMCV, advertisement);
        f.commit();


        FragmentManager fragmentManager1 = getFragmentManager();
        FragmentTransaction transaction = fragmentManager1.beginTransaction();
        Fragment property = new user_properties();
        transaction.add(R.id.propertiesTV, property);
        transaction.commit();


        return inflater.inflate(R.layout.fragment_user_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}