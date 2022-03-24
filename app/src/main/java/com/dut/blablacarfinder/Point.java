package com.dut.blablacarfinder;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Point implements Serializable {
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

    @NonNull
    @Override
    public String toString() {
        return "Point{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", distanceFromUser=" + distanceFromUser +
                ", placeName='" + placeName + '\'' +
                ", nbPlaces=" + nbPlaces +
                ", address='" + address + '\'' +
                ", code='" + code + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
