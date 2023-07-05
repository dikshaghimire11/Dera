package com.dera.Adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dera.R;

import java.util.ArrayList;

public class PropertyDetailsGridView extends BaseAdapter {
    ArrayList<String> keys;
    ArrayList<String> values;
    Activity context;

    public  PropertyDetailsGridView(Activity context, ArrayList<String> keys, ArrayList<String> values){
        this.keys=keys;
        this.values=values;
        this.context=context;
    }
    @Override
    public int getCount() {
        return keys.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater=context.getLayoutInflater();
       View rowView= inflater.inflate(R.layout.detail_property_grid_items,null,true);
        TextView headingTV=rowView.findViewById(R.id.heading);
        TextView valueTV=rowView.findViewById(R.id.value);
        headingTV.setText(keys.get(i));
        valueTV.setText(values.get(i));


        return rowView;
    }
}
