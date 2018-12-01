package com.example.abhishek.homework3;

public class listentries {
    private String Latitude;
    private String Longitude;
    private String Name;
    private String Timestamp;
    private String Address;

    public listentries(String latitude, String longitude, String name, String timestamp, String address) {
        Latitude = latitude;
        Longitude = longitude;
        Name = name;
        Timestamp = timestamp;
        Address = address;

    }
    public String getLatitude(){
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude(){
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
