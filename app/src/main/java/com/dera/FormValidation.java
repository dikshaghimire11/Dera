package com.dera;

import static com.dera.R.*;

import android.content.Context;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.google.android.material.card.MaterialCardView;

public class FormValidation{
    public boolean cantBeEmpty(String string, MaterialCardView mcv, int stroke,Context context) {
        if (string.length() == 0) {
            mcv.setStrokeWidth(stroke);
            mcv.setStrokeColor(ContextCompat.getColor(context, R.color.red));
            return true;
        } else {
            mcv.setStrokeWidth(0);
            return false;
        }
    }


}

