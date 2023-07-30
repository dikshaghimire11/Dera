package com.dera;

import static androidx.core.content.ContextCompat.getMainExecutor;
import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dera.callback.OnRemovedFragments;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;


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
            params.height = totalHeight+500;
            gridView.setLayoutParams(params);
        }

    }
    public static class GetScreenHeight{
        public static int getScreenHeight() {
            return Resources.getSystem().getDisplayMetrics().heightPixels;
        }
    }
    public static class CloseAllFragments {
        public static  void removeByManager(FragmentManager manager, OnRemovedFragments removeFragment){

            FragmentTransaction transaction=manager.beginTransaction();
           List<Fragment> fragmentManagers=manager.getFragments();
           for(Fragment fragment:fragmentManagers){

               transaction.remove(fragment);
               Log.d("Removed Fragments",""+fragment);
           }
         removeFragment.removedFragments(transaction);
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
    public static class searchSupport{
       public static ArrayAdapter suggestionAdapter;
    }

    public static class keyboardSupport{
        public static void hideSoftKeyboard(Activity activity) {
            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
        public static void showSoftKeyboard(Activity activity,View view) {


            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);

        }
        public static void disableAutoOpenKeyboard(Activity activity){
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
    }
    public static class backStackManager{

        private static int stackCount=0;
        private static boolean useBackStack=false;
        private static String fromFragmentName;
        private static String toFragmentName;
        private static FragmentManager currentFragmentManager;

        public static int getIntStackCount(){
            return stackCount;
        }
        public static void incStackCount(){
            stackCount++;
        }
        public static void decStackCount(){
            stackCount--;
        }


        public static void setBackStack(boolean value,String from,String to,FragmentManager fragmentManager){
            useBackStack=value;
            toFragmentName=to;
            fromFragmentName=from;
            currentFragmentManager=fragmentManager;
            incStackCount();
            Log.d("Back","Increase: "+getIntStackCount());

            Log.d("Back","Value is Set: "+useBackStack);

        }
        public static void setUseBackStack(boolean value){
            useBackStack=value;

        }

        public static boolean getUseBackStack(){
            return useBackStack;
        }
        public static void performBackStack(){
            decStackCount();
            Log.d("Back","Decrease: "+getIntStackCount());
            Log.d("Back","FromName: "+fromFragmentName+" ToName: "+toFragmentName);
            FragmentTransaction currentTransaction=currentFragmentManager.beginTransaction();
            Fragment fromFragment=currentFragmentManager.findFragmentByTag(fromFragmentName);
            Log.d("Back","FromFragment: "+fromFragment);
            Fragment toFragment=currentFragmentManager.findFragmentByTag(toFragmentName);
            try {
                if (toFragmentName.equals("homeFragment")) {
                    Fragment extraFragment = currentFragmentManager.findFragmentByTag("propertyFragment");
                    Log.d("Back", "ExtraFragment: " + extraFragment);
                    currentTransaction.show(extraFragment);
                }
            }catch (Exception e){
                Log.d("Home Fragment Not Found","Home Not Shown");
            }
            Log.d("Back","FromFragment: "+toFragment);
            currentTransaction.hide(fromFragment);
            try {
                currentTransaction.show(toFragment);
            }catch (NullPointerException e){
                Log.d("To Fragment Not Found","ToFragment Now Shown");

            }
//            if(toFragment.equals("homeFragment")||toFragment.equals("bookingFragment")||toFragment.equals("profileFragment")||toFragment.equals("historyFragment")){
//                setUseBackStack(false);
//            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                currentTransaction.commitNow();
            }

            Log.d("Back","BackStack Performed");



        }


    }



}

