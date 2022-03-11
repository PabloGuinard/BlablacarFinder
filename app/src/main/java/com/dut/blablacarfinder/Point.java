package com.dut.blablacarfinder;

import androidx.annotation.NonNull;

public class Point {
    private double latitude;
    private double longitude;
    private int distanceFromUser;
    private String placeName;
    private String epsiName;
    private int nbPlaces;
    private String address;

    public Point(double latitude, double longitude, int distanceFromUser, String placeName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.distanceFromUser = distanceFromUser;
        this.placeName = placeName;
    }

    public void setAddress(String address, String city) {
        this.address = address + " " + city;
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
