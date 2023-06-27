package com.dera.houseowner;


import static android.content.Intent.getIntent;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
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
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dera.IpStatic;
import com.dera.R;
import com.dera.VolleyMultipartRequest;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class AddBasicInfoProperties extends Fragment {

public interface IGetImageName{
    public String getName();
}
    ImageView Photo, uploadBtn;
    TextView UploadPhoto;
    EditText priceEt;
    Bitmap bitmap;
    Bundle bundle;
    String category_Id;
    String tole;
    byte[] bytes;
    String imageName;
    String addPropertyURL;
    Spinner bedroomSp, livingroomSp, bathroomSp,
            kitchenSp, floorSp, carparkingSp,
            bikeparkingSp, householdwaterSp, drinkingwaterSp,
            sharinginternetSp, noofflatsSp, noofbathroomSp, noofstoreSp, noofshutterSp;

    String selectbedroom, selectlivingroom, selectbathroom, selectkitchen, selectfloor, selectcarparking,
            selectbikeparking, selecthouseholdwater, selectdrinkingwater, selectsharinginternet, selectonofflat,
            selectnoofbathroom, selectnoofstoreroom, selectnoofshutter;

    int provinceId, districtId, wardId, local_level_ID;
    String jsonValue = "{";
    String updatedJson;
    MaterialButton addpropertyBtn;
    private final int Storage_code = 4655;
    public int Pick_image = 1;
    public Uri filepath, uri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_basic_info_properties, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle addPropertyDataBundle = getArguments();
        category_Id = addPropertyDataBundle.getString("PropertyType");
        tole = addPropertyDataBundle.getString("tole");
        provinceId = addPropertyDataBundle.getInt("provinceId");
        districtId = addPropertyDataBundle.getInt("districtId");
        wardId = addPropertyDataBundle.getInt("ward_noId");
        local_level_ID = addPropertyDataBundle.getInt("local_levelId");
        Log.d("AllId: ", "Province: " + provinceId + "District: " + districtId + "Ward: " + wardId + "Local: " + local_level_ID);


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
        priceEt = view.findViewById(R.id.priceSp);

        addpropertyBtn = view.findViewById(R.id.AddPropertybtn);
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


//finding the value of spinner's item selected
        bedroomSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectbedroom = parent.getSelectedItem().toString();
                Log.d("selectbedroom", "" + selectbedroom);
                jsonValue = jsonValue + "\"BedRoom\":\"" + selectbedroom + "\",";

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
        noofbathroomSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectnoofbathroom = parent.getSelectedItem().toString();
                jsonValue = jsonValue + "\"NoOfBathroom\":\"" + selectnoofbathroom + "\",";
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

//end of finding the value of spinner's item selected

        addpropertyBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                updatedJson = jsonValue.substring(0, jsonValue.length() - 1);
                updatedJson = updatedJson + "}";
//                String path=getPath(filepath);
//               Log.d("ImageName","hello"+path.toString());
                    ByteArrayOutputStream byteArrayOutputStream;
                    byteArrayOutputStream=new ByteArrayOutputStream();
                    if(bitmap !=null){
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                        bytes=byteArrayOutputStream.toByteArray();
                        //imageName =Base64.encodeToString(bytes,Base64.DEFAULT);
                        addPropertyURL = "http://" + IpStatic.IpAddress.ip + ":80/api/add_property";
                        SendData(new IGetImageName() {
                            @Override
                            public String getName() {

                                return uri.toString() ;
                            }
                        });
                    }

                    else{
                        Toast.makeText(getContext(), "Select image first!", Toast.LENGTH_LONG).show();
                    }

                Log.d("EncodedImage",""+imageName);


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
public void SendData(IGetImageName iGetImageName){
    VolleyMultipartRequest volleyMultipartRequest=new VolleyMultipartRequest(Request.Method.POST, addPropertyURL, new Response.Listener<NetworkResponse>() {
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
            Toast.makeText(getContext(), "Something Went Wrong:" + error.getMessage(), Toast.LENGTH_LONG).show();
        }
    }) {

        @Nullable
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> propertyMap = new HashMap<>();
            propertyMap.put("photo", "imageName");
            propertyMap.put("price", priceEt.getText().toString());
            propertyMap.put("status", "1");
            propertyMap.put("total_bookings", "1");
            propertyMap.put("tole", tole);
            propertyMap.put("category_id", category_Id);
            propertyMap.put("houseowner_id", "11");
            propertyMap.put("province_id", String.valueOf(provinceId));
            propertyMap.put("district_id", String.valueOf(districtId));
            propertyMap.put("local_level_id", String.valueOf(local_level_ID));
            propertyMap.put("ward_no_id", String.valueOf(wardId));
            propertyMap.put("property_details", updatedJson);
            return propertyMap;


        }

        @Override
        protected Map<String, DataPart> getByteData() throws AuthFailureError {
            Map<String,DataPart> imageMap=new HashMap<>();
            imageMap.put("image",new DataPart(iGetImageName.getName()+".jpg",bytes,"image/jpeg"));
            return imageMap;
        }
    };
    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
    requestQueue.add(volleyMultipartRequest);
}
}