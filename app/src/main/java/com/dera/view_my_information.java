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
import android.widget.EditText;
import android.widget.TextView;

import com.dera.SimilarFiles.UserProfile;
import com.google.android.material.button.MaterialButton;


public class view_my_information extends Fragment {

    TextView nameEt, mobileEt, emailEt;
    Bundle addUserInfo;
    MaterialButton CancleBtn;
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
        CancleBtn=view.findViewById(R.id.cancleBtn);
        String name = addUserInfo.getString("name");
        nameEt.setText(name);
        mobileEt.setText(addUserInfo.getString("number"));
        emailEt.setText(addUserInfo.getString("email"));
        CancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment edit_user_info = new UserProfile();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentlayout, edit_user_info);
                fragmentTransaction.commit();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
}