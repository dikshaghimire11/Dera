package com.dera.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dera.R;
import com.dera.models.UserInfo;

import java.util.ArrayList;


public class UserInfoAdapter extends ArrayAdapter<String> {
    ArrayList<UserInfo> userInfo;
    Activity context;
    public UserInfoAdapter(Activity context, ArrayList<UserInfo> userInfo){
        super(context, R.layout.user_information_list_view);
        this.context=context;
        this.userInfo=userInfo;
    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.user_information_list_view,null,true);
        TextView name=rowView.findViewById(R.id.nameTv);
        name.setText(userInfo.get(position).getFullname()+" "+userInfo.get(position).getBookedDate());
        return rowView;

    }
    public int getCount() {
        return userInfo.size(); // Return the number of items in the properties list
    }

}
