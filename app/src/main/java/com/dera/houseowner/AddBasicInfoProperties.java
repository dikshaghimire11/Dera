package com.dera.houseowner;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dera.IpStatic;
import com.dera.R;
import com.dera.SimilarFiles.Register;
import com.dera.StaticClasses;
import com.dera.VolleyMultipartRequest;
import com.dera.models.Property;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.UUID;


public class AddBasicInfoProperties extends Fragment {

    public interface IGetImageName {
        public String getName();
    }
    public interface ImageLoadCallback {
        void onImageLoaded(byte[] imageBytes);
        void onImageLoadFailed();
    }
    ProgressDialog progressDialog;

    ImageView Photo, uploadBtn;
    Bundle addPropertyDataBundle;
    TextView UploadPhoto;
    EditText priceEt;
    Bitmap bitmap;
    String Editprice;
    String imageUrl;
    Bundle bundle;
    TextView AddProperty;
    String category_Id, tole,extension;

    byte[] imagebytes,photo;
    String imageName;
    String addPropertyURL,editPropertyURL, Property_id;
    AppCompatTextView bedRoomTextView, kitchenTextView, livingRoomTv,
            bathroomTv, floorTv, carParkingTv, BikeParkingTv,
            sharingInternetTv, householdWaterTv, drinkingWaterTv, noOfRoomsTv, noOfStoreTv, noOfFlatsTv;
    MaterialCardView bedRoomCardView, kitchenCardView, livingRoomMcv,
            bathroomMcv, floorMcv, carParkingMcv, BikeParkingMcv,
            sharingInternetMcv, householdWaterMcv, drinkingWaterMcv, noOfRoomsMcv, noOfStoreMcv, noOfFlatsMcv;
    Spinner bedroomSp, livingroomSp, bathroomSp,
            kitchenSp, floorSp, carparkingSp,
            bikeparkingSp, householdwaterSp, drinkingwaterSp,
            sharinginternetSp, noofflatsSp, noofstoreSp, noofshutterSp;

    String selectbedroom, selectlivingroom, selectbathroom, selectkitchen, selectfloor, selectcarparking,
            selectbikeparking, selecthouseholdwater, selectdrinkingwater, selectsharinginternet, selectonofflat,
            selectnoofstoreroom, selectnoofshutter;

    int provinceId, districtId, wardId, local_level_ID;
    JSONObject propertyJson;

      String jsonValue="{";
      String updatedJson;
    MaterialButton addpropertyBtn;
    private void setImageLoadCallback(ImageLoadCallback callback) {
        this.imageLoadCallback = callback;
    }
    String fragmentName;
    private final int Storage_code = 4655;
    public Uri uri;
    private ImageLoadCallback imageLoadCallback;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_basic_info_properties, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        StaticClasses.backStackManager.setBackStack("addBasicInformationFragment","addressInformationFragment",getActivity().getSupportFragmentManager());
        addPropertyDataBundle=getArguments();
        propertyJson=new JSONObject();
        fragmentName=addPropertyDataBundle.getString("fragmentName");
        category_Id=addPropertyDataBundle.getString("PropertyType");
        uploadBtn = view.findViewById(R.id.UploadImageIv);
        Photo = view.findViewById(R.id.propertyIv);
        AddProperty=view.findViewById(R.id.AddProperty);
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
        noofflatsSp = view.findViewById(R.id.noOfFlatSp);
        noofshutterSp = view.findViewById(R.id.noOfroomSp);
        noofstoreSp = view.findViewById(R.id.noOfstoreRoomSp);
        priceEt=view.findViewById(R.id.priceSp);
// Material Card view
        bathroomMcv = view.findViewById(R.id.bathRoomMCV);
        bedRoomCardView = view.findViewById(R.id.bedRoomMCV);
        livingRoomMcv = view.findViewById(R.id.livingRoomMCV);
        kitchenCardView = view.findViewById(R.id.kitchenMCV);
        floorMcv = view.findViewById(R.id.floorMCV);
        carParkingMcv = view.findViewById(R.id.carParkingMCV);
        BikeParkingMcv = view.findViewById(R.id.bikeParkingMCV);
        householdWaterMcv = view.findViewById(R.id.householdWaterMCV);
        drinkingWaterMcv = view.findViewById(R.id.drinkingWaterMCV);
        sharingInternetMcv = view.findViewById(R.id.internetMCV);
        noOfFlatsMcv = view.findViewById(R.id.noOfFlatMCV);
        noOfRoomsMcv = view.findViewById(R.id.noOfroomsMCV);
        noOfStoreMcv = view.findViewById(R.id.noOfstoreRoomMCV);
        //Text View
        bathroomTv = view.findViewById(R.id.bathRoom);
        bedRoomTextView = view.findViewById(R.id.bedRoom);
        livingRoomTv = view.findViewById(R.id.livingRoom);
        kitchenTextView = view.findViewById(R.id.Kitchen);
        floorTv = view.findViewById(R.id.floor);
        carParkingTv = view.findViewById(R.id.carParking);
        BikeParkingTv = view.findViewById(R.id.bikeParking);
        householdWaterTv = view.findViewById(R.id.householdWater);
        drinkingWaterTv = view.findViewById(R.id.drinkingWater);
        sharingInternetTv = view.findViewById(R.id.internet);
        noOfFlatsTv = view.findViewById(R.id.noOfFlat);
        noOfRoomsTv = view.findViewById(R.id.noOfroom);
        noOfStoreTv = view.findViewById(R.id.noOfstoreRoom);


        addpropertyBtn = view.findViewById(R.id.AddPropertybtn);
        if(fragmentName.equals("EditFragment")){
            Property property = (Property) addPropertyDataBundle.getSerializable("model");
            addpropertyBtn.setText("Edit Property");
            AddProperty.setText("Edit Property Information");
            UploadPhoto.setText("Edit Photo");
            Editprice =addPropertyDataBundle.getString("Price");
            Property_id=addPropertyDataBundle.getString("Property_id");
            priceEt.setText(Editprice);
            UploadPhoto.setText("Edit Photo");
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.mipmap.logo_in_bricks_foreground)
                    .error(R.mipmap.logo_in_bricks_foreground)
            .override(600,400);

             imageUrl = "http://" + IpStatic.IpAddress.ip + "/" + property.getPhoto();
            if (imageUrl != null) {

                Glide.with(this.getContext())
                        .asBitmap()
                        .load(imageUrl)
                        .apply(requestOptions)
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                // Image loaded successfully, resource is the Bitmap
                                if (resource != null) {
                                    Photo.setImageBitmap(resource);
                                    // Convert the Bitmap to bytes
                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                    imageName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                                    extension = imageName.substring(imageName.lastIndexOf(".") + 1);
                                    Log.d("Extensions", "" + extension);

                                    if (extension != null && (extension.equals("png") || extension.equals("jpg") || extension.equals("jpeg"))) {
                                        Log.d("Glide","This Line is Running");
                                        if (extension.equals("png")) {
                                            resource.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                                        } else {
                                            resource.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                        }
                                        imagebytes = byteArrayOutputStream.toByteArray();
                                        Log.d("imageBytes", "" + imagebytes);
                                        if (imageLoadCallback != null) {
                                            imageLoadCallback.onImageLoaded(imagebytes);
                                        }
                                        UploadPhoto.setText("Edit Photo");
                                    } else {
                                        // Handle the case when extension is null or unsupported
                                        Log.e("Glide", "Unsupported image format or extension is null");
                                    }
                                } else {
                                    Log.e("Glide", "Failed to load image: resource is null");
                                }

                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                // Called when the resource is cleared (e.g., when the ImageView is detached from the view hierarchy)
                            }

                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                // Called when the image loading fails
                                Log.e("Glide", "Failed to load image");
                                if (imageLoadCallback != null) {
                                    imageLoadCallback.onImageLoadFailed();
                                }
                            }
                        });
            } else {
                // Handle the case when imageUrl is null
                Log.e("Glide", "imageUrl is null");
                // Or show a Toast with an error message
                Toast.makeText(getContext(), "Failed to load image: imageUrl is null", Toast.LENGTH_LONG).show();
                if (imageLoadCallback != null) {
                    imageLoadCallback.onImageLoaded(null);
                }
            }


            JSONObject json = property.getJson();
            Iterator<?> keys = json.keys();

            if (keys.hasNext()) {
                Log.d("NUll", "" + keys.toString());
            }
            ArrayList<String> keyList = new ArrayList<>();
            ArrayList<String> valueList = new ArrayList<>();

            while (keys.hasNext()) {
                String temp = (String) keys.next();
                keyList.add(temp);
                try {
                    String value = json.getString(temp);
                    valueList.add(value);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
                addpropertyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        updatedJson = jsonValue.substring(0, jsonValue.length() - 1);
                        updatedJson = updatedJson + "}";
                        ByteArrayOutputStream byteArrayOutputStream;
                        byteArrayOutputStream = new ByteArrayOutputStream();
                        if (bitmap != null) {
                            String imagePath = getRealPathFromURI(uri);
                            extension = imagePath.substring(imagePath.lastIndexOf(".") + 1);
                            if (extension.equals("jpeg") || extension.equals("jpg")) {
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                Log.d("Extension", "" + extension);

                            } else if (extension.equals("png")) {
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                                Log.d("Extension", "" + extension);
                            }
                            imagebytes = byteArrayOutputStream.toByteArray();
                            progressDialog = new ProgressDialog(getContext());
                            progressDialog.setMessage("Please wait.....");
                            progressDialog.show();
                            editPropertyURL = "http://" + IpStatic.IpAddress.ip + ":80/api/edit_property";
                            SendData(new IGetImageName() {
                                @Override
                                public String getName() {

                                    return UUID.randomUUID().toString();
                                }
                            });
                        } else {

                            progressDialog = new ProgressDialog(getContext());
                            progressDialog.setMessage("Please wait.....");
                            progressDialog.show();
                            photo=imagebytes;
                            Log.d("image",""+photo);
                            editPropertyURL = "http://" + IpStatic.IpAddress.ip + ":80/api/edit_property";
                            sendImageFromDatabase(imageName);
                            Log.d("imageName",""+imageName);

                        }

                    }
                });

            if (category_Id.equals("1") || category_Id.equals("2")) {
                Spinner[] spinners = new Spinner[13];
                spinners[0] = bedroomSp;
                spinners[1] = kitchenSp;
                spinners[2] = livingroomSp;
                spinners[3] = bathroomSp;
                spinners[4] = floorSp;
                spinners[5] = carparkingSp;
                spinners[6] = bikeparkingSp;
                spinners[7] = sharinginternetSp;
                spinners[8] = householdwaterSp;
                spinners[9] = drinkingwaterSp;
                for (int i = 0; i < Math.min(keyList.size(), spinners.length); i++) {
                    Spinner spinner = spinners[i];
                    String defaultValue = valueList.get(i);

                    // Find the resource ID of the string-array for the current Spinner
                    String key = keyList.get(i);
                    int optionsResId = getResources().getIdentifier(key + "_options", "array", requireContext().getPackageName());

                    // Load the options array from the strings.xml
                    String[] optionsFromResource = getResources().getStringArray(optionsResId);

                    // Create a new ArrayList to hold the combined options
                    ArrayList<String> combinedOptions = new ArrayList<>();

                    // Add the defaultValue to the combinedOptions list
                    combinedOptions.add(defaultValue);

                    // Add the rest of the options from the string-array
                    for (String option : optionsFromResource) {
                        // Skip adding the defaultValue again, as it's already added at the beginning
                        if (!option.equals(defaultValue)) {
                            combinedOptions.add(option);
                        }
                    }

                    // Set the combinedOptions as the data source for the ArrayAdapter
                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, combinedOptions);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(spinnerAdapter);

                    // Find the index of the default value in the options array
                    int defaultValueIndex = combinedOptions.indexOf(defaultValue);
                    if (defaultValueIndex != -1) {
                        spinner.setSelection(defaultValueIndex);
                    } else {
                        // If the default value is not found in the list, set the first item as a fallback:
                        spinner.setSelection(0);
                    }
                }

            }
//            if (imagebytes != null) {
//                ByteArrayInputStream inputStream = new ByteArrayInputStream(imagebytes);
//                // Continue with the rest of your code
//            } else {
//                // Handle the case when imagebytes is nul
//            }

            if(category_Id.equals("3")){
                Spinner[] spinners = new Spinner[13];
                spinners[0] = carparkingSp;
                spinners[1] = bikeparkingSp;
                spinners[2] = householdwaterSp;
                spinners[3] = drinkingwaterSp;
                spinners[4]=noofflatsSp;
                for (int i = 0; i < Math.min(keyList.size(), spinners.length); i++) {
                    Spinner spinner = spinners[i];
                    String defaultValue = valueList.get(i);

                    // Find the resource ID of the string-array for the current Spinner
                    String key = keyList.get(i);
                    int optionsResId = getResources().getIdentifier(key + "_options", "array", requireContext().getPackageName());

                    // Load the options array from the strings.xml
                    String[] optionsFromResource = getResources().getStringArray(optionsResId);

                    ArrayList<String> combinedOptions = new ArrayList<>();

                    // Add the defaultValue to the combinedOptions list
                    combinedOptions.add(defaultValue);

                    // Add the rest of the options from the string-array
                    for (String option : optionsFromResource) {
                        // Skip adding the defaultValue again, as it's already added at the beginning
                        if (!option.equals(defaultValue)) {
                            combinedOptions.add(option);
                        }
                    }

                    // Set the combinedOptions as the data source for the ArrayAdapter
                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, combinedOptions);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(spinnerAdapter);

                    // Find the index of the default value in the options array
                    int defaultValueIndex = combinedOptions.indexOf(defaultValue);
                    if (defaultValueIndex != -1) {
                        spinner.setSelection(defaultValueIndex);
                    } else {
                        // If the default value is not found in the list, set the first item as a fallback:
                        spinner.setSelection(0);
                    }
                }
            }
            if(category_Id.equals("4")){
                Spinner[] spinners = new Spinner[2];
                spinners[0] = noofshutterSp;
                spinners[1] = noofstoreSp;
                for (int i = 0; i < Math.min(keyList.size(), spinners.length); i++) {
                    Spinner spinner = spinners[i];
                    String defaultValue = valueList.get(i);

                    // Find the resource ID of the string-array for the current Spinner
                    String key = keyList.get(i);
                    int optionsResId = getResources().getIdentifier(key + "_options", "array", requireContext().getPackageName());

                    // Load the options array from the strings.xml
                    String[] optionsFromResource = getResources().getStringArray(optionsResId);

                    // Create a new ArrayList to hold the combined options
                    ArrayList<String> combinedOptions = new ArrayList<>();

                    // Add the defaultValue to the combinedOptions list
                    combinedOptions.add(defaultValue);

                    // Add the rest of the options from the string-array
                    for (String option : optionsFromResource) {
                        // Skip adding the defaultValue again, as it's already added at the beginning
                        if (!option.equals(defaultValue)) {
                            combinedOptions.add(option);
                        }
                    }

                    // Set the combinedOptions as the data source for the ArrayAdapter
                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, combinedOptions);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(spinnerAdapter);

                    // Find the index of the default value in the options array
                    int defaultValueIndex = combinedOptions.indexOf(defaultValue);
                    if (defaultValueIndex != -1) {
                        spinner.setSelection(defaultValueIndex);
                    } else {
                        // If the default value is not found in the list, set the first item as a fallback:
                        spinner.setSelection(0);
                    }
                }
            }


        }



//insert Image from Gallery
        ActivityResultLauncher<Intent> activityResultLauncher
                = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    uri = data.getData();
                    try {

                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);

                        Photo.setImageBitmap(bitmap);
                        UploadPhoto.setText("Edit Photo");

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }


                }
            }
        });
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storagePermission();
//                Intent intent = new Intent();
//                intent.setType("image/*");
//
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), Pick_image);
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intent1);

            }
        });

        if (category_Id.equals("1") || category_Id.equals("2")) {
// Assuming you have references to the necessary views

            noOfFlatsTv.setVisibility(View.GONE);
            noOfFlatsMcv.setVisibility(View.GONE);
            noofflatsSp.setVisibility(View.GONE);
            noOfRoomsMcv.setVisibility(View.GONE);
            noOfRoomsTv.setVisibility(View.GONE);
            noofshutterSp.setVisibility(View.GONE);
            noOfStoreMcv.setVisibility(View.GONE);
            noOfStoreTv.setVisibility(View.GONE);
            noofstoreSp.setVisibility(View.GONE);


//finding the value of spinner's item selected
        bedroomSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectbedroom = parent.getSelectedItem().toString();
                Log.d("selectbedroom", "" + selectbedroom);
                jsonValue=jsonValue+"\"BedRoom\":\""+selectbedroom+"\",";
                try {
                    propertyJson.put("BedRoom",selectbedroom);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

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
                    jsonValue = jsonValue + "\"BathroomType\":\"" + selectbathroom + "\",";
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
                    jsonValue = jsonValue + "\"LivingRoom\":\"" + selectlivingroom + "\",";
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
                    jsonValue = jsonValue + "\"Floor\":\"" + selectfloor + "\",";
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
                    jsonValue = jsonValue + "\"CarParking\":\"" + selectcarparking + "\",";
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
                    jsonValue = jsonValue + "\"BikeParking\":\"" + selectbikeparking + "\",";
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
                    jsonValue = jsonValue + "\"Kitchen\":\"" + selectkitchen + "\",";
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
                    jsonValue = jsonValue + "\"HouseHoldWater\":\"" + selecthouseholdwater + "\",";
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
                    jsonValue = jsonValue + "\"DrinkingWater\":\"" + selectdrinkingwater + "\",";
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
                    jsonValue = jsonValue + "\"SharingInternet\":\"" + selectsharinginternet + "\",";
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Handle no selection

                }
            });
        }
        if (category_Id.equals("3")) {
            bedroomSp.setVisibility(View.GONE);
            bedRoomTextView.setVisibility(View.GONE);
            bedRoomCardView.setVisibility(View.GONE);
            kitchenTextView.setVisibility(View.GONE);
            kitchenSp.setVisibility(View.GONE);
            kitchenCardView.setVisibility(View.GONE);
            livingRoomTv.setVisibility(View.GONE);
            livingroomSp.setVisibility(View.GONE);
            livingRoomMcv.setVisibility(View.GONE);
            floorTv.setVisibility(View.GONE);
            floorMcv.setVisibility(View.GONE);
            floorSp.setVisibility(View.GONE);
            bathroomSp.setVisibility(View.GONE);
            bathroomTv.setVisibility(View.GONE);
            bathroomMcv.setVisibility(View.GONE);
            sharinginternetSp.setVisibility(View.GONE);
            sharingInternetMcv.setVisibility(View.GONE);
            sharingInternetTv.setVisibility(View.GONE);
            noOfRoomsMcv.setVisibility(View.GONE);
            noOfRoomsTv.setVisibility(View.GONE);
            noofshutterSp.setVisibility(View.GONE);
            noOfStoreMcv.setVisibility(View.GONE);
            noOfStoreTv.setVisibility(View.GONE);
            noofstoreSp.setVisibility(View.GONE);

            carparkingSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectcarparking = parent.getSelectedItem().toString();
                    jsonValue = jsonValue + "\"CarParking\":\"" + selectcarparking + "\",";
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
                    jsonValue = jsonValue + "\"BikeParking\":\"" + selectbikeparking + "\",";
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
                    jsonValue = jsonValue + "\"HouseHoldWater\":\"" + selecthouseholdwater + "\",";
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
                    jsonValue = jsonValue + "\"DrinkingWater\":\"" + selectdrinkingwater + "\",";
                    Log.d("Drinking",""+selectdrinkingwater);
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
                    jsonValue = jsonValue + "\"NoOfFlats\":\"" + selectonofflat + "\",";
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Handle no selection

                }
            });

        }
        if (category_Id.equals("4")) {
            floorTv.setVisibility(View.GONE);
            floorMcv.setVisibility(View.GONE);
            floorSp.setVisibility(View.GONE);
            bathroomSp.setVisibility(View.GONE);
            bathroomTv.setVisibility(View.GONE);
            bathroomMcv.setVisibility(View.GONE);
            sharinginternetSp.setVisibility(View.GONE);
            sharingInternetMcv.setVisibility(View.GONE);
            sharingInternetTv.setVisibility(View.GONE);
            noOfFlatsTv.setVisibility(View.GONE);
            noOfFlatsMcv.setVisibility(View.GONE);
            noofflatsSp.setVisibility(View.GONE);
            bedroomSp.setVisibility(View.GONE);
            bedRoomTextView.setVisibility(View.GONE);
            bedRoomCardView.setVisibility(View.GONE);
            kitchenTextView.setVisibility(View.GONE);
            kitchenSp.setVisibility(View.GONE);
            kitchenCardView.setVisibility(View.GONE);
            livingRoomTv.setVisibility(View.GONE);
            livingroomSp.setVisibility(View.GONE);
            livingRoomMcv.setVisibility(View.GONE);
            carparkingSp.setVisibility(View.GONE);
            carParkingTv.setVisibility(View.GONE);
            carParkingMcv.setVisibility(View.GONE);
            bikeparkingSp.setVisibility(View.GONE);
            BikeParkingMcv.setVisibility(View.GONE);
            BikeParkingTv.setVisibility(View.GONE);
            householdWaterMcv.setVisibility(View.GONE);
            householdwaterSp.setVisibility(View.GONE);
            householdWaterTv.setVisibility(View.GONE);
            drinkingWaterTv.setVisibility(View.GONE);
            drinkingwaterSp.setVisibility(View.GONE);
            drinkingWaterMcv.setVisibility(View.GONE);

            noofstoreSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectnoofstoreroom = parent.getSelectedItem().toString();
                    jsonValue = jsonValue + "\"NumberOfStore\":\"" + selectnoofstoreroom + "\",";
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
                    jsonValue = jsonValue + "\"NoOfShutter\":\"" + selectnoofshutter + "\",";
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Handle no selection

                }
            });

        }

        if(fragmentName.equals("AddressFragment")) {
            addpropertyBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    updatedJson = jsonValue.substring(0, jsonValue.length() - 1);
                    updatedJson = updatedJson + "}";
                    Log.d("UpdatedJson", "" + updatedJson);
//                String path=getPath(filepath);
//               Log.d("ImageName","hello"+path.toString());

//                    ByteArrayOutputStream imagebyteArrayOutputStream=new ByteArrayOutputStream();
//                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,imagebyteArrayOutputStream);
//                        imagebytes =imagebyteArrayOutputStream.toByteArray();
//                        //imageName =Base64.encodeToString(bytes,Base64.DEFAULT);
                    ByteArrayOutputStream byteArrayOutputStream;
                    byteArrayOutputStream = new ByteArrayOutputStream();

                    Log.d("Extension", "" + extension);
                    if (bitmap != null) {
                        String imagePath = getRealPathFromURI(uri);
                        extension = imagePath.substring(imagePath.lastIndexOf(".") + 1);
                        if (extension.equals("jpeg") || extension.equals("jpg")) {
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                            Log.d("Extension", "" + extension);

                        } else if (extension.equals("png")) {
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                            Log.d("Extension", "" + extension);
                        }
                        imagebytes = byteArrayOutputStream.toByteArray();
                        if (priceEt.getText().toString().length() == 0) {
                            Toast.makeText(getContext(), "Enter price!", Toast.LENGTH_LONG).show();
                        } else {
                            progressDialog = new ProgressDialog(getContext());
                            progressDialog.setMessage("Please wait.....");
                            progressDialog.show();
                            addPropertyURL = "http://" + IpStatic.IpAddress.ip + ":80/api/add_property";
                            SendData(new IGetImageName() {
                                @Override
                                public String getName() {

                                    return UUID.randomUUID().toString();
                                }
                            });

                        }

                        //imageName =Base64.encodeToString(bytes,Base64.DEFAULT);


                    } else {
                        Toast.makeText(getContext(), "Select image first!", Toast.LENGTH_LONG).show();
                    }


                    Log.d("EncodedImage", "" + imageName);


//                StringRequest stringRequest = new StringRequest(Request.Method.POST, addPropertyURL, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            String status = jsonObject.getString("status");
//                            if (status.compareTo("200") == 0) {
//                                Toast.makeText(getContext(), "Data Inserted Sucessfully!", Toast.LENGTH_LONG).show();
//                                Intent intent = new Intent(getContext(), houseOwnerDashboard.class);
//                                startActivity(intent);
//
//                            }
//                        } catch (JSONException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getContext(), "Something Went Wrong:" + error.getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                }) {
//                    @Nullable
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        Map<String, String> propertyMap = new HashMap<>();
//                        propertyMap.put("photo","imageName");
//                        propertyMap.put("price", priceEt.getText().toString());
//                        propertyMap.put("status", "1");
//                        propertyMap.put("total_bookings", "1");
//                        propertyMap.put("tole", tole);
//                        propertyMap.put("category_id", category_Id);
//                        propertyMap.put("houseowner_id", "11");
//                        propertyMap.put("province_id", String.valueOf(provinceId));
//                        propertyMap.put("district_id", String.valueOf(districtId));
//                        propertyMap.put("local_level_id", String.valueOf(local_level_ID));
//                        propertyMap.put("ward_no_id", String.valueOf(wardId));
//                        propertyMap.put("property_details", updatedJson);
//
//                        return propertyMap;
//
//                    }
//
//                    @Override
//                    public byte[] getBody() throws AuthFailureError {
//                        return bytes;
//                    }
//                };
//                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//                requestQueue.add(stringRequest);
                }


            });
        }

            super.onViewCreated(view, savedInstanceState);
    }




    //    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode ==Pick_image && data !=null && data.getData()!=null) {
//
//           filepath = data.getData();
//            UploadPhoto.setText("Edit Photo");
//            try {
//                bitmap=MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),filepath);
//                Photo.setImageBitmap(bitmap);
//                UploadPhoto.setText("Edit Photo");
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//
//
//        }
//    }
    private void storagePermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Storage_code);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Storage_code) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permission granted!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "Permission Denied!", Toast.LENGTH_LONG).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //    private String getPath(Uri uri){
//        Cursor cursor =getActivity().getContentResolver().query(uri, null, null, null, null);
//        cursor.moveToFirst();
//        String document_id = cursor.getString(0);
//        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
//        cursor =getActivity().getContentResolver().query(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + "=?", new String[]{document_id}, null
//        );
//        cursor.moveToFirst();
//       @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//
//        cursor.close();
//        String extension = null;
//        if (path != null) {
//            int extensionIndex = path.lastIndexOf(".");
//            if (extensionIndex >= 0) {
//                extension = path.substring(extensionIndex + 1);
//                Log.d("ImageExtension",""+extension);
//            }
//        }
//        return path;
//
//    }
//    private void uploadImage() {
//        String path = getPath(filepath);
//        try {
//            String uploadId = UUID.randomUUID().toString();
//            new MultipartUploadRequest(getActivity(), uploadId, addPropertyURL).addFileToUpload(path, "upload").addParameter("price", priceEt.getText().toString())
//                    .addParameter("status", "1")
//                    .addParameter("total_bookings", "1")
//                    .addParameter("tole", tole)
//                    .addParameter("category_id", category_Id)
//                    .addParameter("houseowner_id", "11")
//                    .addParameter("province_id", String.valueOf(provinceId))
//                    .addParameter("district_id", String.valueOf(districtId))
//                    .addParameter("local_level_id", String.valueOf(local_level_ID))
//                    .addParameter("ward_no_id", String.valueOf(wardId))
//                    .addParameter("property_details", updatedJson)
//                    .setNotificationConfig(new UploadNotificationConfig())
//                    .setMaxRetries(3)
//                    .startUpload();
//
//        } catch (Exception ex) {
//
//
//        }
//
//    }

    public void SendData(IGetImageName iGetImageName) {
        if (fragmentName.equals("AddressFragment")) {
            tole=addPropertyDataBundle.getString("tole");
            provinceId=addPropertyDataBundle.getInt("provinceId");
            districtId=addPropertyDataBundle.getInt("districtId");
            wardId=addPropertyDataBundle.getInt("ward_noId");
            local_level_ID=addPropertyDataBundle.getInt("local_levelId");
            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, addPropertyURL, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    String resultResponse = new String(response.data);
                    try {
                        JSONObject jsonObject = new JSONObject(resultResponse);
                        String status = jsonObject.getString("status");
                        if (status.compareTo("200") == 0) {
                            Toast.makeText(getContext(), "Data Inserted Sucessfully!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getContext(), houseOwnerDashboard.class);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Something Went Wrong:" + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {

                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> propertyMap = new HashMap<>();
                    propertyMap.put("price", priceEt.getText().toString());
                    propertyMap.put("status", "0");
                    propertyMap.put("total_bookings", "0");
                    propertyMap.put("tole", tole);
                    propertyMap.put("category_id", category_Id);
                    propertyMap.put("houseowner_id", StaticClasses.loginInfo.UserID);
                    propertyMap.put("province_id", String.valueOf(provinceId));
                    propertyMap.put("district_id", String.valueOf(districtId));
                    propertyMap.put("local_level_id", String.valueOf(local_level_ID));

                    propertyMap.put("ward_no_id", String.valueOf(wardId));
                    propertyMap.put("property_details", updatedJson);
                    return propertyMap;
                }

                @Override
                protected Map<String, DataPart> getByteData() throws AuthFailureError {
                    Map<String, DataPart> imageMap = new HashMap<>();
                    if (extension.equals("jpeg")) {
                        imageMap.put("image", new DataPart(iGetImageName.getName() + ".jpeg", imagebytes, "image/jpeg"));
                    } else if (extension.equals("png")) {
                        imageMap.put("image", new DataPart(iGetImageName.getName() + ".png", imagebytes, "image/png"));
                    } else if (extension.equals("jpg")) {
                        imageMap.put("image", new DataPart(iGetImageName.getName() + ".jpg", imagebytes, "image/jpeg"));
                    }
                    return imageMap;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + StaticClasses.loginInfo.loginToken);
                    return headers;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(volleyMultipartRequest);
        }
        if(fragmentName.equals("EditFragment")){
            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, editPropertyURL, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    String resultResponse = new String(response.data);
                    try {
                        JSONObject jsonObject = new JSONObject(resultResponse);
                        String status = jsonObject.getString("status");
                        if (status.compareTo("200") == 0) {
                            Toast.makeText(getContext(), "Data Edited Sucessfully!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getContext(), houseOwnerDashboard.class);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Something Went Wrong:" + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {

                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> propertyMap = new HashMap<>();
                    propertyMap.put("id",Property_id);
                    propertyMap.put("price", priceEt.getText().toString());
                    propertyMap.put("property_details", updatedJson);
                    return propertyMap;
                }

                @Override
                protected Map<String, DataPart> getByteData() throws AuthFailureError {
                    Map<String, DataPart> imageMap = new HashMap<>();
                        if (extension.equals("jpeg")) {
                            imageMap.put("image", new DataPart(iGetImageName.getName() + ".jpeg", imagebytes, "image/jpeg"));
                        } else if (extension.equals("png")) {
                            imageMap.put("image", new DataPart(iGetImageName.getName() + ".png", imagebytes, "image/png"));
                        } else if (extension.equals("jpg")) {
                            imageMap.put("image", new DataPart(iGetImageName.getName() + ".jpg", imagebytes, "image/jpeg"));
                        }

                    return imageMap;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + StaticClasses.loginInfo.loginToken);
                    return headers;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(volleyMultipartRequest);
        }


    }

        private String getRealPathFromURI (Uri uri){
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String imagePath = cursor.getString(column_index);
            Log.d("Image", "" + imagePath);
            cursor.close();
            return imagePath;
        }
    private void sendImageFromDatabase(String imageName) {
        if (imageName != null) {
            Log.d("ImageName1",""+imageName);
            Log.d("ImgaeBytes1",""+imagebytes);
            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, editPropertyURL, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    String resultResponse = new String(response.data);
                    try {
                        JSONObject jsonObject = new JSONObject(resultResponse);
                        String status = jsonObject.getString("status");
                        if (status.compareTo("200") == 0) {
                            Toast.makeText(getContext(), "Data Edited Successfully!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getContext(), houseOwnerDashboard.class);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Toast.makeText(getContext(), "Something Went Wrong:" + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {

                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> propertyMap = new HashMap<>();
                    propertyMap.put("id", Property_id);
                    propertyMap.put("price", priceEt.getText().toString());
                    propertyMap.put("property_details", updatedJson);
                    return propertyMap;
                }

                @Override
                protected Map<String, DataPart> getByteData() throws AuthFailureError {
                    Map<String, DataPart> imageMap = new HashMap<>();
                    if (extension.equals("jpeg")) {
                        imageMap.put("image", new DataPart(imageName ,imagebytes, "image/jpeg"));
                    } else if (extension.equals("png")) {
                        imageMap.put("image", new DataPart(imageName, imagebytes, "image/png"));
                    } else if (extension.equals("jpg")) {
                        imageMap.put("image", new DataPart(imageName , imagebytes, "image/jpeg"));
                    }
                    return imageMap;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + StaticClasses.loginInfo.loginToken);
                    return headers;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(volleyMultipartRequest);
        } else {
            // Handle the case when imageName is null
            // For example, show an error message or log the error
            Log.e("Image Error", "imageName is null");
            // Or show a Toast with an error message
            Toast.makeText(getContext(), "Invalid image data from database", Toast.LENGTH_LONG).show();
        }
    }

}