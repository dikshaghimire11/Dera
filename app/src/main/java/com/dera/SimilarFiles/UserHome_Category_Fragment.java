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
import android.widget.ImageView;

import com.dera.Adapter.CategoryListView;
import com.dera.R;
import com.dera.StaticClasses;
import com.google.android.material.card.MaterialCardView;


public class UserHome_Category_Fragment extends Fragment {
    GridView categorylist;
    String categorySelected;
    Boolean selectStatus;

    MaterialCardView categoryMCV,previousMCV;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_user_home__category_, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        selectStatus=false;
        categorylist=view.findViewById(R.id.categoryList);
        String [] title={"Room","Flat","House","Shutter"};
        int[] image={R.mipmap.room_category_foreground,R.mipmap.flat_category_foreground,R.mipmap.house_category_foreground,R.mipmap.shutter_category_foreground};
        CategoryListView categoryListView=new CategoryListView(getActivity(),title,image);
        categorylist.setAdapter(categoryListView);
        categorylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



              try {
                  previousMCV = categoryMCV;
                  StaticClasses.filterGFXSupport.disableFilterGFX(previousMCV,getContext());

              }catch (NullPointerException e){
                  Log.d("Category MCV not found", "Could be first time fragment loaded");
              }

                categoryMCV=view.findViewById(R.id.categoryMCV);
                if(selectStatus){
                    StaticClasses.filterGFXSupport.disableFilterGFX(categoryMCV,getContext());
                    selectStatus=!selectStatus;
                    categorySelected=categoryListView.getTextAt(i);
                }else{

                    StaticClasses.filterGFXSupport.enableFilterGFX(categoryMCV,getContext(),5);
                    selectStatus=!selectStatus;
                    categorySelected="";
                }
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                FragmentTransaction transaction= fragmentManager.beginTransaction();
                Fragment properties=new user_properties();
                Bundle bundle=new Bundle();
                properties.setArguments(bundle);
                transaction.remove(fragmentManager.findFragmentByTag("propertyFragment"));
                transaction.add(R.id.propertiesFragment,properties,"propertyFragment");



            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

}