package com.dera.customer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dera.IpStatic;
import com.dera.R;
import com.dera.SimilarFiles.user_properties;
import com.dera.StaticClasses;

public class UserHistory extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction propertyTransaction = fragmentManager.beginTransaction();
        Fragment property=new user_properties();
        String url="http://"+ IpStatic.IpAddress.ip+":80/api/GetHistoryInfo?customer_id="+ StaticClasses.loginInfo.UserID;
        Log.d("Url",""+url);
        Bundle bundle=new Bundle();
        bundle.putString("url",url);
        bundle.putString("name","UserhistoryFragment");
        property.setArguments(bundle);
        propertyTransaction.add(R.id.propertiesFragment,property,"propertyFragment");
        propertyTransaction.commit();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_history, container, false);
    }
}