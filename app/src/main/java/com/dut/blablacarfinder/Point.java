package com.dut.blablacarfinder;

import androidx.annotation.NonNull;

public class Point {
    protected double latitude;
    protected double longitude;
    protected int distanceFromUser;
    protected String placeName;
    protected String epsiName;
    protected int nbPlaces;
    protected String address;

    public Point(double latitude, double longitude, int distanceFromUser, String placeName,
                 String epsiName, int nbPlaces, String address, String city, String code) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.distanceFromUser = distanceFromUser;
        this.placeName = placeName;
        this.epsiName = epsiName;
        this.nbPlaces = nbPlaces;
        setAddress(address, city, code);
    }

    public void setAddress(String address, String city, String code) {
        this.address = address + " " + city + " " + code;
    }

    @NonNull
    @Override
    public String toString() {
        return "Point{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", distanceFromUser=" + distanceFromUser +
                ", placeName='" + placeName + '\'' +
                ", epsiName='" + epsiName + '\'' +
                ", nbPlaces=" + nbPlaces +
                ", address='" + address + '\'' +
                '}';
    }
}
