package com.dera.SimilarFiles;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.dera.Adapter.CategoryListView;
import com.dera.R;


public class UserHome_Category_Fragment extends Fragment {
    GridView categorylist;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_home__category_, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        categorylist=view.findViewById(R.id.categoryList);
        String [] title={"Room","Flat","House","Shutter"};
        int[] image={R.mipmap.myroom,R.drawable.flaticon,R.drawable.houseicon,R.drawable.shuttericon};
        CategoryListView categoryListView=new CategoryListView(getActivity(),title,image);
        categorylist.setAdapter(categoryListView);
        super.onViewCreated(view, savedInstanceState);
    }
}