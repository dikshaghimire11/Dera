package com.dera.SimilarFiles;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.dera.Adapter.PropertyGridView;
import com.dera.R;

public class user_properties extends Fragment {

GridView propertiesList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_properties, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        propertiesList=view.findViewById(R.id.propertieslist);
        String[] heading={"Room","Flat","House","Shutter","Rooms","flat"};
        String[] location={"kathmandu","bhaktapur","sankhapul","koteshwor","lokanthali","koteshwor"};
        String[] price={"2000","15000","2000000","20000","8000","20000"};
        String[] number={"1","2bkh","1","2","3","2"};
        int[] photo={R.drawable.roomicon,R.drawable.flaticon,R.drawable.houseicon,R.drawable.shuttericon,R.drawable.roomicon,R.drawable.shuttericon};
        PropertyGridView propertyGridView=new PropertyGridView(getActivity(),heading,number,location,photo,price);
        propertiesList.setAdapter(propertyGridView);

        setDynamicHeight(propertiesList);
    }
    private void setDynamicHeight(GridView gridView) {
        ListAdapter gridViewAdapter = gridView.getAdapter();
        if (gridViewAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int items = gridViewAdapter.getCount();
        int rows = 0;

        View listItem = gridViewAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();
        float x = 1;
        if( items > 2 ){
            x = items/2;
            rows = (int) (x + 1);
            totalHeight *= rows;

        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);
    }
}