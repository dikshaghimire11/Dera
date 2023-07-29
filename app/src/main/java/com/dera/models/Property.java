package com.dera.models;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Iterator;

public class Property implements Serializable {
    public String category;
    public String location;
    public String fullLocation;
    public String price;
    public String number;
    public String photo;
    public String house_owner_id;
    public  String Property_id;
    public String houseOwner_number;
    JSONObject json;
    public String name;

    Iterator<?> keys;
    public boolean ignoreInFilter=false;

    public Property(String category, String location,String fullLocation, String price, String number, String photo,JSONObject json,String house_owner_id,String Property_id,String name,String houseOwner_number) {
        this.category = category;
        this.location = location;
        this.fullLocation=fullLocation;
        this.price = price;
        this.number = number;
        this.photo = photo;
        this.Property_id=Property_id;
        this.house_owner_id=house_owner_id;
        this.houseOwner_number=houseOwner_number;
        this.name=name;
        this.json=json;
        this.keys=keys;
    }

    public String getCategory() {
        return category;
    }

    public String getLocation() {
        return location;
    }

    public String getPrice() {
        return price;
    }

    public String getNumber() {
        return number;
    }

    public String getPhoto() {
        return photo;
    }

    public  String getFullLocation(){return fullLocation;}

    public JSONObject getJson(){return  json;}




    public void setIgnoreInFilter(boolean value) {
        this.ignoreInFilter=value;
    }
    public boolean getIsIgnoreInFilter(){
        return ignoreInFilter;
    }
    public String getProperty_id(){
        return Property_id;
    }
    public String getHouse_owner_id(){return house_owner_id;}
    public  String getName(){return name;}
    public String getHouseOwner_number(){return  houseOwner_number;}
}
