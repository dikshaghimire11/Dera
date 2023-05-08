package com.dera;

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

import com.dera.Adapter.CategoryListView;
import com.dera.Adapter.PropertyGridView;
import com.google.android.material.card.MaterialCardView;

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
        return inflater.inflate(R.layout.fragment_user_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




        propertiesList=view.findViewById(R.id.propertieslist);
        String[] heading={"Room","Flat","House","Shutter","Rooms","flat","house","flat","house"};
        String[] location={"kathmandu","bhaktapur","sankhapul","koteshwor","lokanthali","koteshwor","lokanthali","koteshwor","lokanthali"};
        String[] price={"2000","15000","2000000","20000","8000","20000","8000","2000000","20000"};
        String[] number={"1","2bkh","1","2","3","2","3","2bkh","1"};
        int[] photo={R.drawable.roomicon,R.drawable.flaticon,R.drawable.houseicon,R.drawable.shuttericon,R.drawable.roomicon,R.drawable.shuttericon,R.drawable.roomicon,R.drawable.shuttericon,R.drawable.roomicon};
        PropertyGridView propertyGridView=new PropertyGridView(getActivity(),heading,number,location,photo,price);
        propertiesList.setAdapter(propertyGridView);
    }
}