package com.dera.models;

public class Property {
    public String category;
    public String location;
    public String price;
    public String number;
    public String photo;

    public Property(String category, String location, String price, String number, String photo) {
        this.category = category;
        this.location = location;
        this.price = price;
        this.number = number;
        this.photo = photo;
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
}
