package com.dut.blablacarfinder;

import androidx.annotation.NonNull;

public class Point {
    protected double latitude;
    protected double longitude;
    protected int distanceFromUser;
    protected String placeName;
    protected int nbPlaces;
    protected String address;
    protected String code;
    protected String city;

    public Point(double latitude, double longitude, int distanceFromUser, String placeName,
                 int nbPlaces, String address, String city, String code) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.distanceFromUser = distanceFromUser;
        this.placeName = placeName;
        this.nbPlaces = nbPlaces;
        this.address = address;
        this.code = code;
        this.city = city;
    }

    public String getAddress(String address, String city, String code) {
        return address + " " + code + " " + city;
    }
}
