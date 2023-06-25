package com.dera.houseowner;


import static android.content.Intent.getIntent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dera.IpStatic;
import com.dera.R;
import com.dera.StaticClasses;
import com.dera.customer.UserDashboard;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class AddBasicInfoProperties extends Fragment {


    ImageView Photo, uploadBtn;
    TextView UploadPhoto;
    EditText priceEt;
    Bitmap bitmap;
    Bundle bundle;
    String category_Id;
    String tole;
    Spinner bedroomSp, livingroomSp, bathroomSp,
            kitchenSp, floorSp, carparkingSp,
            bikeparkingSp, householdwaterSp, drinkingwaterSp,
            sharinginternetSp, noofflatsSp, noofbathroomSp, noofstoreSp, noofshutterSp;

    String selectbedroom, selectlivingroom, selectbathroom, selectkitchen, selectfloor, selectcarparking,
            selectbikeparking, selecthouseholdwater, selectdrinkingwater, selectsharinginternet, selectonofflat,
            selectnoofbathroom, selectnoofstoreroom, selectnoofshutter;

    int provinceId,districtId,wardId,local_level_ID;
      String imageName;
      String jsonValue="{";
      String updatedJson;
    MaterialButton addpropertyBtn;
    private final int Gallery_Request_Code = 1000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_basic_info_properties, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle addPropertyDataBundle=getArguments();
        category_Id=addPropertyDataBundle.getString("PropertyType");
        tole=addPropertyDataBundle.getString("tole");
        provinceId=addPropertyDataBundle.getInt("provinceId");
        districtId=addPropertyDataBundle.getInt("districtId");
        wardId=addPropertyDataBundle.getInt("ward_noId");
        local_level_ID=addPropertyDataBundle.getInt("local_levelId");
        Log.d("AllId: ","Province: "+provinceId+"District: "+districtId+"Ward: "+wardId+"Local: "+local_level_ID);




        uploadBtn = view.findViewById(R.id.UploadImageIv);
        Photo = view.findViewById(R.id.propertyIv);
        UploadPhoto = view.findViewById(R.id.UploadImageTV);
//        spinner
        bedroomSp = view.findViewById(R.id.bedRoomSp);
        bathroomSp = view.findViewById(R.id.bathRoomSp);
        livingroomSp = view.findViewById(R.id.livingRoomSp);
        kitchenSp = view.findViewById(R.id.KitchenSp);
        floorSp = view.findViewById(R.id.floorSp);
        carparkingSp = view.findViewById(R.id.carParkingSp);
        bikeparkingSp = view.findViewById(R.id.bikeParkingSp);
        householdwaterSp = view.findViewById(R.id.householdWaterSp);
        drinkingwaterSp = view.findViewById(R.id.drinkingWaterSp);
        sharinginternetSp = view.findViewById(R.id.internetSp);
        noofbathroomSp = view.findViewById(R.id.noOfbathroomSp);
        noofflatsSp = view.findViewById(R.id.noOfFlatSp);
        noofshutterSp = view.findViewById(R.id.noOfroomSp);
        noofstoreSp = view.findViewById(R.id.noOfstoreRoomSp);
        priceEt=view.findViewById(R.id.priceSp);

        addpropertyBtn = view.findViewById(R.id.AddPropertybtn);
//insert Image from Gallery

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, Gallery_Request_Code);

            }
        });



//finding the value of spinner's item selected
        bedroomSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectbedroom = parent.getSelectedItem().toString();
                Log.d("selectbedroom", "" + selectbedroom);
                jsonValue=jsonValue+"\"BedRoom\":\""+selectbedroom+"\",";

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no selection

            }
        });
        bathroomSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectbathroom = parent.getSelectedItem().toString();
                jsonValue=jsonValue+"\"BathroomType\":\""+selectbathroom+"\",";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no selection

            }
        });
        livingroomSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectlivingroom = parent.getSelectedItem().toString();
                jsonValue=jsonValue+"\"LivingRoom\":\""+selectlivingroom+"\",";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no selection

            }
        });
        floorSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectfloor = parent.getSelectedItem().toString();
                jsonValue=jsonValue+"\"Floor\":\""+selectfloor+"\",";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no selection

            }
        });
        carparkingSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectcarparking = parent.getSelectedItem().toString();
                jsonValue=jsonValue+"\"CarParking\":\""+selectcarparking+"\",";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no selection

            }
        });
        bikeparkingSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectbikeparking = parent.getSelectedItem().toString();
                jsonValue=jsonValue+"\"BikeParking\":\""+selectbikeparking+"\",";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no selection

            }
        });
        kitchenSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectkitchen = parent.getSelectedItem().toString();
                jsonValue=jsonValue+"\"Kitchen\":\""+selectkitchen+"\",";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no selection

            }
        });
        householdwaterSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selecthouseholdwater = parent.getSelectedItem().toString();
                jsonValue=jsonValue+"\"HouseHoldWater\":\""+selecthouseholdwater+"\",";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no selection

            }
        });

        drinkingwaterSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectdrinkingwater = parent.getSelectedItem().toString();
                jsonValue=jsonValue+"\"DrinkingWater\":\""+selectdrinkingwater+"\",";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no selection

            }
        });
        sharinginternetSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectsharinginternet = parent.getSelectedItem().toString();
                jsonValue=jsonValue+"\"SharingInternet\":\""+selectsharinginternet+"\",";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no selection

            }
        });
        noofstoreSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectnoofstoreroom = parent.getSelectedItem().toString();
                jsonValue=jsonValue+"\"NumberOfStore\":\""+selectnoofstoreroom+"\",";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no selection

            }
        });
        noofbathroomSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectnoofbathroom = parent.getSelectedItem().toString();
                jsonValue=jsonValue+"\"NoOfBathroom\":\""+selectnoofbathroom+"\",";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no selection

            }
        });
        noofflatsSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectonofflat = parent.getSelectedItem().toString();
                jsonValue=jsonValue+"\"NoOfFlats\":\""+selectonofflat+"\",";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no selection

            }
        });
        noofshutterSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectnoofshutter = parent.getSelectedItem().toString();
                jsonValue=jsonValue+"\"NoOfShutter\":\""+selectnoofshutter+"\",";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no selection

            }
        });

//end of finding the value of spinner's item selected

        addpropertyBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
               updatedJson=jsonValue.substring(0, jsonValue.length() - 1);
                updatedJson=updatedJson+"}";



                ByteArrayOutputStream byteArrayOutputStream;
                byteArrayOutputStream=new ByteArrayOutputStream();
                if(bitmap!=null){
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                    byte[] bytes=byteArrayOutputStream.toByteArray();
                    imageName= Base64.encodeToString(bytes,Base64.DEFAULT);
                }else {
                    Toast.makeText(getContext(),"Select the image first",Toast.LENGTH_LONG).show();
                }
                String addPropertyURL="http://"+IpStatic.IpAddress.ip+":80/api/add_property";
                StringRequest stringRequest=new StringRequest(Request.Method.POST, addPropertyURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String status=jsonObject.getString("status");
                            if(status.compareTo("200")==0){
                                Toast.makeText(getContext(),"Data Inserted Sucessfully!",Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(getContext(), houseOwnerDashboard.class);
                                startActivity(intent);

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),"Something Went Wrong:"+error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }){
                    @Nullable
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> propertyMap=new HashMap<>();
                        propertyMap.put("photo","imageName");
                        propertyMap.put("price",priceEt.getText().toString());
                        propertyMap.put("status","1");
                        propertyMap.put("total_bookings","1");
                        propertyMap.put("tole",tole);
                        propertyMap.put("category_id",category_Id);
                        propertyMap.put("houseowner_id","11");
                        propertyMap.put("province_id","1");
                        propertyMap.put("district_id","1");
                        propertyMap.put("local_level_id","7");
                        propertyMap.put("ward_no_id","1");
                        propertyMap.put("property_details",updatedJson);

                        return propertyMap;

                    }
                };
                RequestQueue requestQueue= Volley.newRequestQueue(getContext());
                requestQueue.add(stringRequest);

            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Gallery_Request_Code) {

            Uri selectedImageUri = data.getData();


            UploadPhoto.setText("Edit Photo");
            try {
                bitmap=MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),selectedImageUri);
                Photo.setImageBitmap(bitmap);
                UploadPhoto.setText("Edit Photo");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Log.d("imageName","Image"+selectedImageUri);

        }
    }


}