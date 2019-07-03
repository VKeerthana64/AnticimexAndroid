package com.surveyor.app.surveyorapp.bean;

import android.location.Location;

public class ReverseGeoBean {
    Location location;
    String type ;
    String address ;
    String pincode ;

    public ReverseGeoBean(String address) {
        this.address = address;
    }

    public ReverseGeoBean(String address, String pincode) {
        this.address = address;
        this.pincode = pincode;
    }

    public ReverseGeoBean(Location location, String type) {
        this.location = location;
        this.type = type;
    }

/*    public ReverseGeoBean(String type, String address) {
        this.type = type;
        this.address = address;
    }*/

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
