package com.dera.houseowner;

import android.app.Activity;
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
import android.widget.ImageView;

import com.dera.R;
import com.dera.SimilarFiles.UserHome_Category_Fragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;


public class chooseCategory extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment myFragment = new UserHome_Category_Fragment();
        fragmentTransaction.add(R.id.categoryFragment, myFragment);

        fragmentTransaction.commit();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_category, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LayoutInflater inflater=getLayoutInflater();
        View v=inflater.inflate(R.layout.categorygridview,null,true);
        ImageView imageView=v.findViewById(R.id.imageIV);
        MaterialButton btn=view.findViewById(R.id.submit);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Fragment myFragment = new AddressInfo();
                fragmentTransaction.replace(R.id.fragmentlayout, myFragment);

                fragmentTransaction.commit();
            }
        });
    }
}