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

public class change_password extends Fragment {
    EditText oldPasswordEt, newPasswordEt,confirmPasswordEt;
    MaterialButton submitBtn,CancleBtn;
    TextView errorTv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        oldPasswordEt=view.findViewById(R.id.CurrentPasswordEt);
        newPasswordEt=view.findViewById(R.id.NewPasswordET);
        confirmPasswordEt=view.findViewById(R.id.ConfirmPasswordET);

        submitBtn=view.findViewById(R.id.submitBtn);
        CancleBtn=view.findViewById(R.id.cancleBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });
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