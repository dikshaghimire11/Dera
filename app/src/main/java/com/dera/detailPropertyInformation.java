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
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dera.Adapter.PropertyDetailsGridView;
import com.dera.SimilarFiles.user_properties;
import com.dera.models.Property;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


public class detailPropertyInformation extends Fragment {
    ImageView imageView;
    TextView textView,fullLocation,title,number,priceTV;
    GridView gridView;
    MaterialCardView backButton;
    ScrollView homeScroll;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_property_information, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle=getArguments();
        homeScroll=getActivity().findViewById(R.id.homeScroll);
        imageView=view.findViewById(R.id.propertyIv);
        gridView=view.findViewById(R.id.detailsGridView);
        textView=view.findViewById(R.id.PropertyTypeTV);
        fullLocation=view.findViewById(R.id.locationTV);
        number=view.findViewById(R.id.number);
        title=view.findViewById(R.id.title);
        priceTV=view.findViewById(R.id.priceTV);
        backButton=view.findViewById(R.id.backMCV);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_properties user_properties=new user_properties();
                FragmentManager fragmentManager= getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.remove(fragmentManager.findFragmentByTag("detailFragment"));
                fragmentTransaction.show(fragmentManager.findFragmentByTag("propertyFragment"));
                homeScroll.setVerticalScrollbarPosition(bundle.getInt("scrollPosition"));

               // fragmentTransaction.replace(R.id.propertiesFragment,user_properties);
                fragmentTransaction.commit();
            }
        });


        if(bundle!=null){
            Property property= (Property) bundle.getSerializable("model");
            textView.setText(property.getCategory());
            title.setText(property.getCategory());
            fullLocation.setText(property.getLocation());
            priceTV.setText(property.getPrice());
            number.setText(property.getNumber());

            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.mipmap.logo_in_bricks_foreground)
                    .error(R.mipmap.logo_in_bricks_foreground)
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