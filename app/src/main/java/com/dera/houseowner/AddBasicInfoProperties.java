package com.dera.houseowner;


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

import com.dera.IpStatic;
import com.dera.R;
import com.dera.customer.UserDashboard;
import com.google.android.material.button.MaterialButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class AddBasicInfoProperties extends Fragment {


    ImageView Photo, uploadBtn;
    TextView UploadPhoto;
    EditText priceEt;
    Bitmap bitmap;
    Spinner bedroomSp, livingroomSp, bathroomSp,
            kitchenSp, floorSp, carparkingSp,
            bikeparkingSp, householdwaterSp, drinkingwaterSp,
            sharinginternetSp, noofflatsSp, noofbathroomSp, noofstoreSp, noofshutterSp;

    String selectbedroom, selectlivingroom, selectbathroom, selectkitchen, selectfloor, selectcarparking,
            selectbikeparking, selecthouseholdwater, selectdrinkingwater, selectsharinginternet, selectonofflat,
            selectnoofbathroom, selectnoofstoreroom, selectnoofshutter;
      String imageName;
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

        addpropertyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ByteArrayOutputStream byteArrayOutputStream;
                byteArrayOutputStream=new ByteArrayOutputStream();
                if(bitmap!=null){
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                byte[] bytes=byteArrayOutputStream.toByteArray();
                imageName= Base64.encodeToString(bytes,Base64.DEFAULT);
                }else {
                    Toast.makeText(getContext(),"Select the image first",Toast.LENGTH_LONG).show();
                }
                Intent intent=new Intent(getContext(), houseOwnerDashboard.class);
                startActivity(intent);
            }
        });

//finding the value of spinner's item selected
        bedroomSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectbedroom = parent.getSelectedItem().toString();
                Log.d("selectbedroom", "" + selectbedroom);
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no selection

            }
        });

//end of finding the value of spinner's item selected

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