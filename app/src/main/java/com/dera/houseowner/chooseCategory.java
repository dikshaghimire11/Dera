package com.dera.houseowner;

import android.app.Activity;
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

import com.dera.R;
import com.dera.SimilarFiles.UserHome_Category_Fragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;


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
        // Inflate the layout for this fragment
        mainFragmentView= inflater.inflate(R.layout.fragment_choose_category, container, false);
        return mainFragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LayoutInflater inflater=getLayoutInflater();
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
                fragmentTransaction.replace(R.id.fragmentlayout, addressInfoFragment);
                fragmentTransaction.commit();

            }
        });

    }
}