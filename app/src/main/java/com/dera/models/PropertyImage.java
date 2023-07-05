package com.dera.models;

public class PropertyImage {
    private String containerID;
    private String imageurl;
    public PropertyImage(String containerID, String imageurl){
        this.containerID=containerID;
        this.imageurl=imageurl;
    }
    public String getContainerID(){
        return  containerID;
    }
    public String getImageurl(){
        return imageurl;
    }
}
