package com.dera;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

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
    public static class loginInfo{
        public static String loginToken;
        public static String UserID;
    }

    public static  class gridViewHeight{
        public static void setDynamicHeight(GridView gridView) {
            ListAdapter gridViewAdapter = gridView.getAdapter();
            if (gridViewAdapter == null) {
                // pre-condition
                return;
            }

            int totalHeight = 0;
            int items = gridViewAdapter.getCount();
            int rows = 0;

            View listItem = gridViewAdapter.getView(0, null, gridView);
            listItem.measure(0, 0);
            totalHeight = listItem.getMeasuredHeight();
            float x = 1;
            if( items > 2 ){
                x = items/2;
                rows = (int) (x + 1);
                totalHeight *= rows;

            }

            ViewGroup.LayoutParams params = gridView.getLayoutParams();
            params.height = totalHeight;
            gridView.setLayoutParams(params);
        }
    }
    public static class filterGFXSupport{
        public static void enableFilterGFX(MaterialCardView mcv,Context context){
            mcv.setStrokeWidth(4);


        }
        public static void disableFilterGFX(MaterialCardView mcv,Context context){
            mcv.setStrokeWidth(0);


        }
    }

}

