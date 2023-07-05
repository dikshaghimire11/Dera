package com.dera.models;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Iterator;

public class Property implements Serializable {
    public String category;
    public String location;
    public String price;
    public String number;
    public String photo;
    JSONObject json;
    Iterator<?> keys;

    public Property(String category, String location, String price, String number, String photo,JSONObject json,Iterator<?> keys) {
        this.category = category;
        this.location = location;
        this.price = price;
        this.number = number;
        this.photo = photo;
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

    public JSONObject getJson(){return  json;}

    public Iterator<?> getKeys(){return  keys;}
}
