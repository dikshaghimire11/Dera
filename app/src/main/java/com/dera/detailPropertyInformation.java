package com.dera;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dera.Adapter.PropertyDetailsGridView;
import com.dera.models.Property;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


public class detailPropertyInformation extends Fragment {
    ImageView imageView;
    GridView gridView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_property_information, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView=view.findViewById(R.id.propertyIv);
        gridView=view.findViewById(R.id.detailsGridView);
        Bundle bundle=getArguments();
        if(bundle!=null){
            Property property= (Property) bundle.getSerializable("model");
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.mipmap.myroom)
                    .error(R.mipmap.myroom)
                    ;
            String imageUrl ="http://"+ IpStatic.IpAddress.ip+"/"+property.getPhoto();

            Glide.with(this.getContext())
                    .load(imageUrl)
                    .apply(requestOptions)
                    .into(imageView);

            JSONObject json=property.getJson();
            Iterator<?> keys=property.getKeys();
            ArrayList<String> keyList=new ArrayList<>();
            ArrayList<String> valueList=new ArrayList<>();

            while (keys.hasNext()){
                String temp=(String) keys.next();
            keyList.add(temp);
                try {
                    valueList.add(json.getString(temp));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
            PropertyDetailsGridView detailsAdapter=new PropertyDetailsGridView(getActivity(),keyList,valueList);
            gridView.setAdapter(detailsAdapter);
            StaticClasses.gridViewHeight.setDynamicHeight(gridView);

        }
    }
}