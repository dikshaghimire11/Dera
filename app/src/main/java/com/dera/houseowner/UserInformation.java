package com.dera.houseowner;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dera.Adapter.UserInfoAdapter;
import com.dera.IpStatic;
import com.dera.R;
import com.dera.StaticClasses;
import com.dera.callback.OnRemovedFragments;
import com.dera.customer.UserHome;
import com.dera.detailPropertyInformation;
import com.dera.models.Property;
import com.dera.models.UserInfo;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserInformation extends Fragment {
    ListView listView;
    ArrayList<UserInfo> userInfo;
    String fullName, number, date;
    String Property_id;
    int scrollPosition;
    MaterialCardView backMcv;
    Property property;
    String name;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_information, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        listView = view.findViewById(R.id.userlist);
        FrameLayout childFrameLayout = getActivity().findViewById(R.id.ChildFragment);
        backMcv=view.findViewById(R.id.backMCV);
        Bundle bundle = getArguments();
        Property_id = bundle.getString("property_id");
         property = (Property) bundle.getSerializable("model");
         scrollPosition = bundle.getInt("scrollPosition");
         name = bundle.getString("name");
        userInfo = new ArrayList<UserInfo>();
        String url = "http://" + IpStatic.IpAddress.ip + ":80/api/GetUserListedFromBooking?property_id=" + Property_id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = new JSONArray(jsonObject.getString("property"));
                    for (int i = 0; i < jsonArray.length(); i++) {

                        UserInfo userInfo1 = makeUserInfo(jsonArray.getJSONObject(i));
                        userInfo.add(userInfo1);

                    }
                    if (userInfo.size() == 0) {
                        return;
                    }

                    UserInfoAdapter userInfoAdapter = new UserInfoAdapter(getActivity(), userInfo);
                    listView.setAdapter(userInfoAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                           UserInfo userInfo1 = userInfo.get(i);
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            LayoutInflater inflater = getLayoutInflater();
                            View view1 = inflater.inflate(R.layout.contact_room_finder, null);
                            builder.setView(view1);

                            TextView numberTv = view1.findViewById(R.id.numberTv);
                            TextView name = view1.findViewById(R.id.nameTv);
                            MaterialButton approvebtn = view1.findViewById(R.id.approvebtn);
                            MaterialButton canclebtn = view1.findViewById(R.id.canclebtn);
                            MaterialCardView backMCV = view1.findViewById(R.id.backMCV);
                            AlertDialog dialog = builder.create();
                            name.setText(userInfo1.getFullname());
                            numberTv.setText(userInfo1.getUsernumber());
                            Log.d("Booking",""+userInfo1.getBooking_id());
                            backMCV.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.cancel();
                                }
                            });
                            approvebtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String canceBooking = "http://" + IpStatic.IpAddress.ip + ":80/api/ChangeStatus? booking_id=" +userInfo1.getBooking_id()+"&status="+3;
                                    StringRequest stringRequest1 = new StringRequest(Request.Method.GET, canceBooking, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonObject1 = new JSONObject(response);
                                                String status = jsonObject1.getString("status");
                                                if (status.compareTo("200") == 0) {
                                                    dialog.cancel();
                                                    Toast.makeText(getContext(), "Booking Approved!", Toast.LENGTH_LONG).show();
                                                    String propertyChangeStatusUrl="http://" + IpStatic.IpAddress.ip + ":80/api/PropertyChangeStatus? property_id="+Property_id;
                                                        StringRequest propertyChangeStatus=new StringRequest(Request.Method.GET, propertyChangeStatusUrl, new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String response) {

                                                            }
                                                        }, new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {

                                                            }
                                                        });
                                                    RequestQueue changeStatus = Volley.newRequestQueue(getContext());
                                                    changeStatus.add(propertyChangeStatus);

                                                    String insertInto = "http://" + IpStatic.IpAddress.ip + ":80/api/StoreHistory";
                                                    StringRequest history = new StringRequest(Request.Method.POST, insertInto, new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject jsonObject2 = new JSONObject(response);
                                                                String status = jsonObject2.getString("status");
                                                                if (status.compareTo("200") == 0) {
                                                                    Fragment fragment = new houseOwnerHome();
                                                                    FragmentManager manager = getActivity().getSupportFragmentManager();
                                                                    StaticClasses.CloseAllFragments.removeByManager(manager, new OnRemovedFragments() {
                                                                        @Override
                                                                        public void removedFragments(FragmentTransaction transaction) {
                                                                            fragment.setArguments(bundle);
                                                                            transaction.add(R.id.fragmentlayout, fragment, "homeFragment");
                                                                            transaction.commit();
                                                                        }
                                                                    });
                                                                }
                                                            } catch (JSONException e) {
                                                                throw new RuntimeException(e);
                                                            }
                                                        }
                                                    }, new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {

                                                        }
                                                    }) {
                                                        public String getBodyContentType() {
                                                            return "application/json; charset=utf-8";
                                                        }

                                                        @Nullable
                                                        @Override
                                                        public byte[] getBody() throws AuthFailureError {
                                                            try {
                                                                JSONObject jsonBody = new JSONObject();
                                                                jsonBody.put("property_id", Property_id);
                                                                jsonBody.put("status", "approve");
                                                                return jsonBody.toString().getBytes("utf-8");
                                                            } catch (JSONException |
                                                                     UnsupportedEncodingException e) {
                                                                throw new AuthFailureError(e.getMessage());
                                                            }
                                                        }

                                                        @Override
                                                        public Map<String, String> getHeaders() throws AuthFailureError {

                                                            Map<String, String> params = new HashMap<String, String>();
                                                            params.put("Accept", "application/json");
                                                            params.put("Content-Type", "application/json");
                                                            return params;
                                                        }
                                                    };

                                                    RequestQueue historyrequestQueue = Volley.newRequestQueue(getContext());
                                                    historyrequestQueue.add(history);
                                                }
                                            } catch (JSONException e) {
                                                throw new RuntimeException(e);
                                            }

                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getContext(), "Something went worng1!", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    RequestQueue requestQueue1 = Volley.newRequestQueue(getContext());
                                    requestQueue1.add(stringRequest1);
                                }
                            });
                            canclebtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String canceBooking = "http://" + IpStatic.IpAddress.ip + ":80/api/ChangeStatus? booking_id=" +userInfo1.getBooking_id()+"&status="+2;
                                    StringRequest stringRequest1 = new StringRequest(Request.Method.GET, canceBooking, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonObject1 = new JSONObject(response);
                                                String status = jsonObject1.getString("status");
                                                if (status.compareTo("200") == 0) {

                                                    Toast.makeText(getContext(), "Booking Cancel!", Toast.LENGTH_LONG).show();
                                                    String insertInto = "http://" + IpStatic.IpAddress.ip + ":80/api/StoreHistory";
                                                    StringRequest history = new StringRequest(Request.Method.POST, insertInto, new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject jsonObject2 = new JSONObject(response);
                                                                String status = jsonObject2.getString("status");
                                                                if (status.compareTo("200") == 0) {

                                                                    Fragment fragment = new houseOwnerHome();;
                                                                    FragmentManager manager = getActivity().getSupportFragmentManager();
                                                                    StaticClasses.CloseAllFragments.removeByManager(manager, new OnRemovedFragments() {
                                                                        @Override
                                                                        public void removedFragments(FragmentTransaction transaction) {
                                                                            fragment.setArguments(bundle);
                                                                            transaction.add(R.id.fragmentlayout, fragment, "homeFragment");
                                                                            transaction.commit();
                                                                        }
                                                                    });
                                                                    dialog.cancel();
                                                                }
                                                            } catch (JSONException e) {
                                                                throw new RuntimeException(e);
                                                            }
                                                        }
                                                    }, new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {

                                                        }
                                                    }) {
                                                        public String getBodyContentType() {
                                                            return "application/json; charset=utf-8";
                                                        }

                                                        @Nullable
                                                        @Override
                                                        public byte[] getBody() throws AuthFailureError {
                                                            try {
                                                                JSONObject jsonBody = new JSONObject();
                                                                jsonBody.put("property_id",Property_id);
                                                                jsonBody.put("status", "canceled by houseowner");
                                                                return jsonBody.toString().getBytes("utf-8");
                                                            } catch (JSONException |
                                                                     UnsupportedEncodingException e) {
                                                                throw new AuthFailureError(e.getMessage());
                                                            }
                                                        }

                                                        @Override
                                                        public Map<String, String> getHeaders() throws AuthFailureError {

                                                            Map<String, String> params = new HashMap<String, String>();
                                                            params.put("Accept", "application/json");
                                                            params.put("Content-Type", "application/json");
                                                            return params;
                                                        }
                                                    };

                                                    RequestQueue historyrequestQueue = Volley.newRequestQueue(getContext());
                                                    historyrequestQueue.add(history);
                                                }
                                            } catch (JSONException e) {
                                                throw new RuntimeException(e);
                                            }

                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getContext(), "Something went worng1!", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    RequestQueue requestQueue1 = Volley.newRequestQueue(getContext());
                                    requestQueue1.add(stringRequest1);
                                }
                            });

                            dialog.show();

                        }
                    });
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
        backMcv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detailPropertyInformation detailFragment = new detailPropertyInformation();
                Bundle b=new Bundle();
                b.putSerializable("model", property);
                b.putInt("scrollPosition", scrollPosition);
                b.putString("name", name);
                detailFragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.hide(fragmentManager.findFragmentByTag("viewBooking"));
                fragmentTransaction.add(childFrameLayout.getId(), detailFragment, "detailFragment");
                fragmentTransaction.commit();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    public UserInfo makeUserInfo(JSONObject data) throws JSONException {
        fullName = data.getString("name");
        number = data.getString("mobile");
        date = data.getString("created_at");
        String bookingId = data.getString("booking_id");
        return new UserInfo(fullName, number, date,bookingId);
    }
}