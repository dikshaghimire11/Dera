package com.dera;

import com.google.android.material.card.MaterialCardView;

public class FormValidation {
    public boolean cantBeEmpty(String string, MaterialCardView mcv, int stroke) {
        if (string.length() == 0) {
            mcv.setStrokeWidth(stroke);
            return true;
        } else {
            mcv.setStrokeWidth(0);
            return false;
        }
    }


}

