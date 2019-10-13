package com.example.yallasyara.activity;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

public class Car implements Comparable<Car> {
    private int id;
    private String model_name;
    private int production_year;
    private double latitude;
    private double longitude;
    private String image_path;
    private int fuel_level;
    private double distance;
    private double cLat;
    private double cLng;

    public Car(int id, String model_name, int production_year, double latitude, double longitude, String image_path, int fuel_level,double cLat,double cLng) {
        this.id = id;
        this.model_name = model_name;
        this.production_year = production_year;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image_path = image_path;
        this.fuel_level = fuel_level;
        this.cLat=cLat;
        this.cLng=cLng;
        this.distance = getDistance();

    }

    public int getId() {
        return id;
    }

    public String getModel_name() {
        return model_name;
    }

    public int getProduction_year() {
        return production_year;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getImage_path() {
        return image_path;
    }

    public int getFuel_level() {
        return fuel_level;
    }

//    public void setDistance() {
//        this.distance = getDistance();
//    }
//
//    public double getDistance() {
//        //car location
//        double Lat2 = latitude;
//        double Lng2 = longitude;
//        //Calculating distance
//        double earthRadius = 6371;
//
//        double dLat = Math.toRadians(cLat - Lat2);
//        double dLng = Math.toRadians(cLng - Lng2);
//        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
//                Math.cos(Math.toRadians(Lat2)) * Math.cos(Math.toRadians(cLat)) *
//                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
//        double dist = earthRadius * c;
//        Log.d("distance",String.valueOf(dist));
//
//        return Double.valueOf(String.format("%.2f", dist));
//    }

    public double getDistance(){
        LatLng startLatLng = new LatLng(cLat, cLng);
        LatLng endLatLng = new LatLng(latitude, longitude);
        double total_distance = SphericalUtil.computeDistanceBetween(startLatLng, endLatLng);
        return Double.valueOf(String.format("%.2f", total_distance/1000));
    }


    @Override
    public int compareTo(Car c) {
        return Double.compare(this.getDistance(), c.getDistance());
    }


}






