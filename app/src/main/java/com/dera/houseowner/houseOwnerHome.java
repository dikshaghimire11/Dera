package com.dera.houseowner;

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
import android.widget.GridView;

import com.dera.IpStatic;
import com.dera.R;
import com.dera.SimilarFiles.user_Advertisement;
import com.dera.SimilarFiles.user_properties;
import com.dera.StaticClasses;


public class houseOwnerHome extends Fragment {


    GridView propertiesList;

    int usertypeId;
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
        String url = "http://" + IpStatic.IpAddress.ip + ":80/api/get_houseowner_property?ownerId=" + StaticClasses.loginInfo.UserID;
        Log.d("Url", "" + url);
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putString("name","houseOwnerhomeFragment");
        property.setArguments(bundle);
        transaction.add(R.id.propertiesFragment, property, "propertyFragment");

        transaction.commit();


        return inflater.inflate(R.layout.fragment_user_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}