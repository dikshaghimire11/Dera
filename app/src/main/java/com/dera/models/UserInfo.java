package com.dera.models;

import java.io.Serializable;

public class UserInfo implements Serializable {
    public String fullname;
    public String Usernumber;
    public  String BookedDate;
    public String Booking_id;

    public UserInfo(String fullname,String Usernumber,String BookedDate,String Booking_id){
        this.BookedDate=BookedDate;
        this.fullname=fullname;
        this.Usernumber=Usernumber;
        this.Booking_id=Booking_id;
    }

    public String getBookedDate() {
        return BookedDate;
    }

    public String getFullname() {
        return fullname;
    }

    public String getUsernumber() {
        return Usernumber;
    }
    public String getBooking_id(){return  Booking_id;}
}
