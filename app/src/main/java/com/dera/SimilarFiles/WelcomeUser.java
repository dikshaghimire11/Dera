package com.dera.SimilarFiles;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dera.R;
import com.dera.StaticClasses;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Locale;

public class WelcomeUser extends Fragment {
    TextView timeTV;
    String currentTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_welcome_user, container, false);
        Handler handler=new Handler();
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                updateDate();
                handler.postDelayed(this, 5000);
            }
        };
        handler.postDelayed(runnable,0);
        TextView nameTV=view.findViewById(R.id.nameTV);
       timeTV=view.findViewById(R.id.TimeTV);
        String username=StaticClasses.loginInfo.userName;
        nameTV.setText("User");
        try {
            if (!username.isEmpty()) {
                String name=StaticClasses.loginInfo.userName;
                int spacePosition=name.indexOf(" ");
                name=name.substring(0,spacePosition);
                nameTV.setText(name);

            }
        }
        catch(NullPointerException e){
            Log.d("Name not found","Could be Not Login Screen");

            }
        return view;
    }

    public void updateDate(){
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            String timeFormat = "hh:mm a";
            SimpleDateFormat sdf = null;
            sdf = new SimpleDateFormat(timeFormat, Locale.getDefault());
            currentTime = sdf.format(calendar.getTime());
        }else{
            currentTime = hourOfDay+":"+minute;
        }
        timeTV.setText(currentTime);
    }
}