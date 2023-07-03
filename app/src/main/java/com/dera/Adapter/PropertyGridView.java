package com.dera.Adapter;

import android.app.Activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dera.ChooseUserType;
import com.dera.R;
import com.dera.models.Property;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PropertyGridView extends ArrayAdapter<String> {
    ArrayList<Property> properties;
   Activity context;
   String[] title;
   String [] number;
   String[] location;
   int[] photo;
   String[] price;
   public PropertyGridView(Activity context, ArrayList<Property> properties){
      super(context,R.layout.propertiesgridview);
      this.context=context;
      this.properties=properties;

//       this.title=title;
//       this.number=number;
//       this .location=location;
//       this.photo=photo;
//       this.price=price;
   }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.propertiesgridview,null,true);
        TextView titleView=rowView.findViewById(R.id.title);
        TextView priceView=rowView.findViewById(R.id.priceTV);
        TextView numberView=rowView.findViewById(R.id.number);
        ImageView photoView=rowView.findViewById(R.id.merotauko);
        TextView locationView=rowView.findViewById(R.id.locationTV);
//        titleView.setText(title[position]);
//        photoView.setImageResource(photo[position]);
//        priceView.setText(price[position]);
//        numberView.setText(number[position]);
//        locationView.setText(location[position]);
        titleView.setText(properties.get(position).getCategory());
        photoView.setImageResource(R.mipmap.myroom);
        priceView.setText(properties.get(position).getPrice());
        numberView.setText(properties.get(position).getNumber());
        locationView.setText(properties.get(position).getLocation());
        return rowView;



    }
    @NonNull
    @Override
    public int getCount() {
        return properties.size(); // Return the number of items in the properties list
    }
}

