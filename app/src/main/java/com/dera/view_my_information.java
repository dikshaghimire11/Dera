package com.dera;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


public class view_my_information extends Fragment {

    TextView nameEt, mobileEt, emailEt;
    Bundle addUserInfo;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        addUserInfo = getArguments();
        return inflater.inflate(R.layout.fragment_view_my_information, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        nameEt = view.findViewById(R.id.nameET);
        mobileEt = view.findViewById(R.id.MobileET);
        emailEt=view.findViewById(R.id.emailET);
        String name = addUserInfo.getString("name");
        nameEt.setText(name);
        mobileEt.setText(addUserInfo.getString("number"));
        emailEt.setText(addUserInfo.getString("email"));
        super.onViewCreated(view, savedInstanceState);
    }
}