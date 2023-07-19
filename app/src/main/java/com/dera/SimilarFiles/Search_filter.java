package com.dera.SimilarFiles;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.dera.IpStatic;
import com.dera.R;
import com.dera.StaticClasses;
import com.google.android.material.card.MaterialCardView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.zip.Inflater;


public class Search_filter extends Fragment {
    Boolean roomStatus=false;
    Boolean flatStatus=false;
    Boolean shutterStatus=false;
    Boolean houseStatus=false;

    Boolean singleRoomStatus=false;
    Boolean roomandKitchenStatus=false;
    Boolean onebhkStatus=false;
    Boolean twobhkStatus=false;
    Boolean threebhkStatus=false;
    Boolean fourbhkStatus=false;
    Boolean attachedBathroomStatus=false;
    Boolean alwaysWaterStatus=false;
    Boolean carParkingStatus=false;
    Boolean bikeParkingStatus=false;

    Boolean minPriceStatus=false;
    Boolean maxPriceStatus=false;
    int totalFiltersCount=0;

    int minPrice=5000;
    int maxPrice=10000;
    int priceStep=5000;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_search_filter, container, false);
       ImageView filterMCV=view.findViewById(R.id.filter);
        ArrayList<String> categoryFilter=new ArrayList<>();
        ArrayList<String> subCategoryFilter=new ArrayList<>();
        ArrayList<String> facilityFilter=new ArrayList<>();
       filterMCV.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               AlertDialog.Builder builder= new AlertDialog.Builder(getContext());
               LayoutInflater inflater=getLayoutInflater();
               View dialogView=inflater.inflate(R.layout.filter_dialog,null,false);
               builder.setView(dialogView);
               AlertDialog alertDialog=builder.create();
               alertDialog.show();
               MaterialCardView roomCategoryMCV=dialogView.findViewById(R.id.categoryRoomMCV);
               MaterialCardView flatCategoryMCV=dialogView.findViewById(R.id.categoryFlatMCV);
               MaterialCardView houseCategoryMCV=dialogView.findViewById(R.id.categoryHouseMCV);
               MaterialCardView shutterCategoryMCV=dialogView.findViewById(R.id.categoryShutterMCV);
               MaterialCardView singleRoomMCV=dialogView.findViewById(R.id.singleroomMCV);
               MaterialCardView roomAndKitchenMCV=dialogView.findViewById(R.id.roomandkitchenMCV);
               MaterialCardView onebhkMCV=dialogView.findViewById(R.id.onebhkMCV);
               MaterialCardView twobhkMCV=dialogView.findViewById(R.id.twobhkMCV);
               MaterialCardView threebhkMCV=dialogView.findViewById(R.id.threebhkMCV);
               MaterialCardView fourbhkMCV=dialogView.findViewById(R.id.fourbhkMCV);
               MaterialCardView attachedBathroomMCV=dialogView.findViewById(R.id.attachedBathroomMCV);
               MaterialCardView alwaysWaterMCV=dialogView.findViewById(R.id.alwaysWaterMCV);
               MaterialCardView bikeParkingMCV=dialogView.findViewById(R.id.bikeParkingMCV);
               MaterialCardView carParkingMCV=dialogView.findViewById(R.id.carParkingMCV);
               MaterialCardView minPriceSubtractMCV=dialogView.findViewById(R.id.minPriceSubtractMCV);
               MaterialCardView maxPriceSubtractMCV=dialogView.findViewById(R.id.maxPriceSubtractMCV);
               MaterialCardView minPriceAddMCV=dialogView.findViewById(R.id.minPriceAddMCV);
               MaterialCardView maxPriceAddMCV=dialogView.findViewById(R.id.maxPriceAddMCV);
               TextView minPriceTV=dialogView.findViewById(R.id.minPriceTV);
               TextView maxPriceTV=dialogView.findViewById(R.id.maxPriceTV);
               CheckBox minPriceCheckbox=dialogView.findViewById(R.id.minPriceCheckbox);
               CheckBox maxPriceCheckbox=dialogView.findViewById(R.id.maxPriceCheckbox);
               MaterialCardView filterSearch=dialogView.findViewById(R.id.filterSearch);




               updatePrice(minPriceTV,maxPriceTV);

               minPriceSubtractMCV.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       minPrice=minPrice-priceStep;
                       if(minPrice<0){
                           minPrice=0;
                       }
                       updatePrice(minPriceTV,maxPriceTV);
                   }
               });
               maxPriceSubtractMCV.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       maxPrice=maxPrice-priceStep;
                       if(maxPrice<0){
                           maxPrice=0;
                       }
                       updatePrice(minPriceTV,maxPriceTV);
                   }
               });
               minPriceAddMCV.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       minPrice=minPrice+priceStep;
                       updatePrice(minPriceTV,maxPriceTV);
                   }
               });
               maxPriceAddMCV.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       maxPrice=maxPrice+priceStep;
                       updatePrice(minPriceTV,maxPriceTV);
                   }
               });







               Bundle bundle=new Bundle();

               if(roomStatus){
                   StaticClasses.filterGFXSupport.enableFilterGFX(roomCategoryMCV,getContext());
               }
               if(houseStatus){
                   StaticClasses.filterGFXSupport.enableFilterGFX(houseCategoryMCV,getContext());
               }
               if(shutterStatus){
                   StaticClasses.filterGFXSupport.enableFilterGFX(shutterCategoryMCV,getContext());
               }
               if(flatStatus){
                   StaticClasses.filterGFXSupport.enableFilterGFX(flatCategoryMCV,getContext());
               }
               if(singleRoomStatus){
                   StaticClasses.filterGFXSupport.enableFilterGFX(singleRoomMCV,getContext());
               }
               if(roomandKitchenStatus){
                   StaticClasses.filterGFXSupport.enableFilterGFX(roomAndKitchenMCV,getContext());
               }
               if(onebhkStatus){
                   StaticClasses.filterGFXSupport.enableFilterGFX(onebhkMCV,getContext());
               }if(twobhkStatus){
                   StaticClasses.filterGFXSupport.enableFilterGFX(twobhkMCV,getContext());
               }
               if(threebhkStatus){
                   StaticClasses.filterGFXSupport.enableFilterGFX(threebhkMCV,getContext());
               }
               if(fourbhkStatus){
                   StaticClasses.filterGFXSupport.enableFilterGFX(fourbhkMCV,getContext());
               }
               if(attachedBathroomStatus){
                   StaticClasses.filterGFXSupport.enableFilterGFX(attachedBathroomMCV,getContext());
               }if(alwaysWaterStatus){
                   StaticClasses.filterGFXSupport.enableFilterGFX(alwaysWaterMCV,getContext());
               }
               if(carParkingStatus){
                   StaticClasses.filterGFXSupport.enableFilterGFX(carParkingMCV,getContext());
               }
               if(bikeParkingStatus){
                   StaticClasses.filterGFXSupport.enableFilterGFX(bikeParkingMCV,getContext());
               }
               if(minPriceStatus){
                  minPriceCheckbox.setChecked(true);
               }
               if(maxPriceStatus){
                   maxPriceCheckbox.setChecked(true);
               }









               roomCategoryMCV.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       roomStatus=!roomStatus;
                       if(roomStatus) {
                           totalFiltersCount++;
                           categoryFilter.add("Room");
                           StaticClasses.filterGFXSupport.enableFilterGFX(roomCategoryMCV,getContext());
                       }else{
                           totalFiltersCount--;
                           categoryFilter.remove("Room");
                           StaticClasses.filterGFXSupport.disableFilterGFX(roomCategoryMCV,getContext());
                       }

                   }

               });
               flatCategoryMCV.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                    flatStatus=!flatStatus;
                    if(flatStatus){
                        totalFiltersCount++;
                        categoryFilter.add("Flat");
                        StaticClasses.filterGFXSupport.enableFilterGFX(flatCategoryMCV,getContext());
                    }else{
                        categoryFilter.remove("Flat");
                        totalFiltersCount--;
                        StaticClasses.filterGFXSupport.disableFilterGFX(flatCategoryMCV,getContext());
                    }

                   }

               });
               shutterCategoryMCV.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       shutterStatus=!shutterStatus;
                       if(shutterStatus){
                           totalFiltersCount++;
                           categoryFilter.add("Shutter");
                           StaticClasses.filterGFXSupport.enableFilterGFX(shutterCategoryMCV,getContext());
                       }else{
                           categoryFilter.remove("Shutter");
                           totalFiltersCount--;
                           StaticClasses.filterGFXSupport.disableFilterGFX(shutterCategoryMCV,getContext());
                       }
                   }

               });
               houseCategoryMCV.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       houseStatus=!houseStatus;
                       if(houseStatus){
                           totalFiltersCount++;
                           categoryFilter.add("House");
                           StaticClasses.filterGFXSupport.enableFilterGFX(houseCategoryMCV,getContext());
                       }else{
                           categoryFilter.remove("House");
                           totalFiltersCount--;
                           StaticClasses.filterGFXSupport.disableFilterGFX(houseCategoryMCV,getContext());
                       }
                   }

               });

               singleRoomMCV.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       singleRoomStatus=!singleRoomStatus;
                       if(singleRoomStatus){
                           totalFiltersCount++;
                           subCategoryFilter.add("B");
                           StaticClasses.filterGFXSupport.enableFilterGFX(singleRoomMCV,getContext());
                       }else{
                           subCategoryFilter.remove("B");
                           totalFiltersCount--;
                           StaticClasses.filterGFXSupport.disableFilterGFX(singleRoomMCV,getContext());
                       }
                   }
               });
               roomAndKitchenMCV.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       roomandKitchenStatus=!roomandKitchenStatus;
                       if(roomandKitchenStatus){
                           totalFiltersCount++;
                           subCategoryFilter.add("BK");
                           StaticClasses.filterGFXSupport.enableFilterGFX(roomAndKitchenMCV,getContext());
                       }else{
                           subCategoryFilter.remove("BK");
                           totalFiltersCount--;
                           StaticClasses.filterGFXSupport.disableFilterGFX(roomAndKitchenMCV,getContext());
                       }
                   }
               });
               onebhkMCV.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       onebhkStatus=!onebhkStatus;
                       if(onebhkStatus){
                           totalFiltersCount++;
                           subCategoryFilter.add("1BHK");
                           StaticClasses.filterGFXSupport.enableFilterGFX(onebhkMCV,getContext());
                       }else{
                           subCategoryFilter.remove("1BHK");
                           totalFiltersCount--;
                           StaticClasses.filterGFXSupport.disableFilterGFX(onebhkMCV,getContext());
                       }
                   }
               });
               twobhkMCV.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       twobhkStatus=!twobhkStatus;
                       if(twobhkStatus){
                           totalFiltersCount++;
                           subCategoryFilter.add("2BHK");
                           StaticClasses.filterGFXSupport.enableFilterGFX(twobhkMCV,getContext());
                       }else{
                           subCategoryFilter.remove("2BHK");
                           totalFiltersCount--;
                           StaticClasses.filterGFXSupport.disableFilterGFX(twobhkMCV,getContext());
                       }
                   }
               });
               threebhkMCV.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       threebhkStatus=!threebhkStatus;
                       if(threebhkStatus){
                           totalFiltersCount++;
                           subCategoryFilter.add("3BHK");
                           StaticClasses.filterGFXSupport.enableFilterGFX(threebhkMCV,getContext());
                       }else{
                           subCategoryFilter.remove("2BHK");
                           totalFiltersCount--;
                           StaticClasses.filterGFXSupport.disableFilterGFX(threebhkMCV,getContext());
                       }
                   }
               });
               fourbhkMCV.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       fourbhkStatus=!fourbhkStatus;
                       if(fourbhkStatus){
                           totalFiltersCount++;
                           subCategoryFilter.add("4BHK");
                           StaticClasses.filterGFXSupport.enableFilterGFX(fourbhkMCV,getContext());
                       }else{
                           subCategoryFilter.remove("4BHK");
                           totalFiltersCount--;
                           StaticClasses.filterGFXSupport.disableFilterGFX(fourbhkMCV,getContext());
                       }
                   }
               });
               attachedBathroomMCV.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       attachedBathroomStatus=!attachedBathroomStatus;
                       if(attachedBathroomStatus){
                           totalFiltersCount++;
                           facilityFilter.add("attachedBathroom");
                           StaticClasses.filterGFXSupport.enableFilterGFX(attachedBathroomMCV,getContext());
                       }else{
                           facilityFilter.remove("attachedBathroom");
                           totalFiltersCount--;
                           StaticClasses.filterGFXSupport.disableFilterGFX(attachedBathroomMCV,getContext());
                       }
                   }
               });
               alwaysWaterMCV.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       alwaysWaterStatus=!alwaysWaterStatus;
                       if(alwaysWaterStatus){
                           totalFiltersCount++;
                           facilityFilter.add("alwaysWater");
                           StaticClasses.filterGFXSupport.enableFilterGFX(alwaysWaterMCV,getContext());
                       }else{
                           facilityFilter.remove("alwaysWater");
                           totalFiltersCount--;
                           StaticClasses.filterGFXSupport.disableFilterGFX(alwaysWaterMCV,getContext());
                       }
                   }
               });
               carParkingMCV.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       carParkingStatus=!carParkingStatus;
                       if(carParkingStatus){
                           totalFiltersCount++;
                           facilityFilter.add("carParking");
                           StaticClasses.filterGFXSupport.enableFilterGFX(carParkingMCV,getContext());
                       }else{
                           facilityFilter.remove("carParking");
                           totalFiltersCount--;
                           StaticClasses.filterGFXSupport.disableFilterGFX(carParkingMCV,getContext());
                       }
                   }
               });
               bikeParkingMCV.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       bikeParkingStatus=!bikeParkingStatus;
                       if(bikeParkingStatus){
                           totalFiltersCount++;
                           facilityFilter.add("bikeParking");
                           StaticClasses.filterGFXSupport.enableFilterGFX(bikeParkingMCV,getContext());
                       }else{
                           facilityFilter.remove("bikeParking");
                           totalFiltersCount--;
                           StaticClasses.filterGFXSupport.disableFilterGFX(bikeParkingMCV,getContext());
                       }
                   }
               });

               minPriceCheckbox.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       minPriceStatus=!minPriceStatus;
                       if(minPriceStatus){
                           totalFiltersCount++;
                       }else{
                           totalFiltersCount--;
                       }
                   }
               });
               maxPriceCheckbox.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       maxPriceStatus=!maxPriceStatus;
                       if(maxPriceStatus){
                           totalFiltersCount++;
                       }else{
                           totalFiltersCount--;
                       }
                   }
               });

               filterSearch.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {

                       String url="http://"+ IpStatic.IpAddress.ip+":80/api/get_property";
                       bundle.putString("url",url);
                       Log.d("Total Filter Count",""+totalFiltersCount);
                       if(totalFiltersCount!=0) {
                           bundle.putString("applyFilter", "true");
                       }
                       if(minPriceStatus){
                           bundle.putInt("minPrice",minPrice);
                       }
                       if(maxPriceStatus){
                           bundle.putInt("maxPrice",maxPrice);
                       }
                           bundle.putStringArrayList("categoryFilter",categoryFilter);
                            bundle.putStringArrayList("subCategoryFilter",subCategoryFilter);
                            bundle.putStringArrayList("facilityFilter",facilityFilter);
                           FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                           FragmentTransaction transaction=fragmentManager.beginTransaction();
                           Fragment properties=new user_properties();
                           properties.setArguments(bundle);
                           transaction.remove(fragmentManager.findFragmentByTag("propertyFragment"));
                           transaction.add(R.id.propertiesFragment,properties,"propertyFragment");
                           transaction.commit();


                       alertDialog.cancel();



                   }
               });
           }
       });

        return view;


    }
    public void updatePrice(TextView minPriceTV,TextView maxPriceTV){
        minPriceTV.setText(String.valueOf(minPrice));
        maxPriceTV.setText(String.valueOf(maxPrice));
    }
}