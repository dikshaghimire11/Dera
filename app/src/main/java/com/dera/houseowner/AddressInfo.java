package com.dera.houseowner;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.dera.SimilarFiles.Register;
import com.dera.StaticClasses;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AddressInfo extends Fragment {
    public interface SelectIdCallback {
        void onIdSelected(int currentID);
    }
    ArrayAdapter<String> provinceAdapter, districtAdapter, locallevelAdapter, wardnoAdapter;
    Bundle addPropertyDataBundle;
    MaterialButton continueButton;
    EditText toleNameEt;
    int container,provinceId,districtId,local_levelId,ward_noId;
    String tole;


    public static int addressid;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        addPropertyDataBundle = getArguments();
        return inflater.inflate(R.layout.fragment_address_info, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Spinner provinceSpinner, districtSpinner, localLevelSpinner, wardnoSpinner;
        ArrayList<String> provinceList = new ArrayList<>();
        ArrayList<String> districtList = new ArrayList<>();
        ArrayList<String> locallevelList = new ArrayList<>();
        ArrayList<String> wardnoList = new ArrayList<>();
        provinceSpinner = view.findViewById(R.id.stateSp);
        districtSpinner = view.findViewById(R.id.districtSp);
        localLevelSpinner = view.findViewById(R.id.locallevelSp);
        wardnoSpinner = view.findViewById(R.id.wardSp);
        continueButton=view.findViewById(R.id.continuebtn);
        toleNameEt=view.findViewById(R.id.toleET);


        String provinceurl = "http://" + IpStatic.IpAddress.ip + ":80/api/ProvinceInfo";
        SelectAddress(provinceurl, provinceSpinner, provinceList);

        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                districtList.clear();
                String selectProvince = parent.getSelectedItem().toString();
                String provinceID_URL="http://"+IpStatic.IpAddress.ip+":80/api/get_address_id?db=province&name="+selectProvince;
               selectId(provinceID_URL, new SelectIdCallback() {
                    @Override
                    public void onIdSelected(int currentID) {
                        addPropertyDataBundle.putInt("provinceId",currentID);


                    }
                });

                String districturl = "http://" + IpStatic.IpAddress.ip + ":80/api/DistrictInfo?province=" + selectProvince;
                SelectAddress(districturl, districtSpinner, districtList);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no selection

            }
        });

        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                locallevelList.clear();

               String selectDistrict = parent.getSelectedItem().toString();
                String districtID_URL="http://"+IpStatic.IpAddress.ip+":80/api/get_address_id?db=district&name="+selectDistrict;
                selectId(districtID_URL, new SelectIdCallback() {
                    @Override
                    public void onIdSelected(int currentID) {
                        addPropertyDataBundle.putInt("districtId",currentID);

                    }
                });

                String locallevelurl = "http://" + IpStatic.IpAddress.ip + ":80/api/LocalLevelInfo?district=" + selectDistrict;
                SelectAddress(locallevelurl, localLevelSpinner, locallevelList);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no selection

            }
        });

        localLevelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle item selection

                wardnoList.clear();
                String selectLocalLevel = parent.getSelectedItem().toString();
                String local_level_ID_URL="http://"+IpStatic.IpAddress.ip+":80/api/get_address_id?db=local_level&name="+selectLocalLevel;
                selectId(local_level_ID_URL, new SelectIdCallback() {
                    @Override
                    public void onIdSelected(int currentID) {
                        addPropertyDataBundle.putInt("local_levelId",currentID);

                    }
                });
                Log.d("Id",Integer.toString(local_levelId));

                String wardurl = "http://" + IpStatic.IpAddress.ip + ":80/api/WardNoInfo?locallevel="+selectLocalLevel;
                SelectAddress(wardurl, wardnoSpinner, wardnoList);



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle no selection
            }
        });
        wardnoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                String selectWard= parent.getSelectedItem().toString();
                String ward_ID_URL="http://"+IpStatic.IpAddress.ip+":80/api/get_address_id?db=ward_no&name="+selectWard;
               selectId(ward_ID_URL, new SelectIdCallback() {
                    @Override
                    public void onIdSelected(int currentID) {
                        addPropertyDataBundle.putInt("ward_noId",currentID);
                    }
                });
                Log.d("Id",Integer.toString(ward_noId));


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        super.onViewCreated(view, savedInstanceState);
  }

    public void SelectAddress(String url, Spinner spinner, ArrayList<String> List) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String addressName = jsonObject.getString("name");
                        List.add(addressName);
                        provinceAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, List);
                        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(provinceAdapter);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }) {


//Code for Sending User Authentication login token with API so that it only executes if user is logged in:
 //----------------------------------------------------------------------------------
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("Authorization", "Bearer " + StaticClasses.loginInfo.loginToken);
//                return headers;
//            }

            //---------------------------------------------------------------
        };
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.add(stringRequest);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tole=toleNameEt.getText().toString();
                addPropertyDataBundle.putString("tole",tole);

                if(tole.length()==0) {
                    Toast.makeText(getContext(),"Please enter tole!",Toast.LENGTH_LONG).show();
                }else {
                    Fragment addBasicInfoPropertiesFragment = new AddBasicInfoProperties();
                    addBasicInfoPropertiesFragment.setArguments(addPropertyDataBundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentlayout, addBasicInfoPropertiesFragment);
                    fragmentTransaction.commit();
                }
            }
        });
    }
    public void selectId(String url,final SelectIdCallback callback){


        StringRequest stringRequest= new StringRequest(Request.Method.GET,url,new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                     container=jsonObject.getInt("id");
                    callback.onIdSelected(container);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();
                Log.d("Error Message",""+error.getMessage());
            }
        }){


        };
        RequestQueue requestQueue=Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}