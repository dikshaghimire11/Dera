package com.dera;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.dera.Adapter.CategoryListView;
import com.dera.Adapter.PropertyGridView;

public class UserHome extends Fragment {
    GridView categorylist,propertiesList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        categorylist=view.findViewById(R.id.categoryList);
        String [] title={"Room","Flat","House","Shutter"};
        int[] image={R.drawable.roomicon,R.drawable.flaticon,R.drawable.houseicon,R.drawable.shuttericon};
        CategoryListView categoryListView=new CategoryListView(getActivity(),title,image);
        categorylist.setAdapter(categoryListView);



        propertiesList=view.findViewById(R.id.propertieslist);
        String[] heading={"Room","Flat","House","Shutter","Rooms"};
        String[] location={"kathmandu","bhaktapur","sankhapul","koteshwor","lokanthali"};
        String[] price={"2000","15000","2000000","20000","8000"};
        String[] number={"1","2bkh","1","2","3"};
        int[] photo={R.drawable.roomicon,R.drawable.flaticon,R.drawable.houseicon,R.drawable.shuttericon,R.drawable.roomicon};
        PropertyGridView propertyGridView=new PropertyGridView(getActivity(),heading,number,location,photo,price);
        propertiesList.setAdapter(propertyGridView);
    }
}