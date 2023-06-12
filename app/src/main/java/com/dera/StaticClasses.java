package com.dera;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.google.android.material.card.MaterialCardView;

public class StaticClasses {

    public static class FormValidation {
        public static boolean cantBeEmpty(String string, MaterialCardView mcv, int stroke, Context context) {
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

}

