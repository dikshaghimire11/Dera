package com.dera.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dera.ChooseUserType;
import com.dera.R;

public class CategoryListView extends ArrayAdapter<String> {
    Activity context;
    String[] title;

    View parentList;

    int[] image;
    public CategoryListView(Activity context, String[] title,int[] image){
        super(context , R.layout.categorygridview,title);
        this.context=context;
        this.title=title;
        this.image=image;

    }
    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.categorygridview,null,true);
        TextView textView=(TextView) rowView.findViewById(R.id.title);
        ImageView imageView=(ImageView) rowView.findViewById(R.id.imageIV);
        textView.setText(title[position]);
        imageView.setImageResource(image[position]);

        return  rowView;
    }
    public String getTextAt(int index){
        return  title[index];
    }
}
