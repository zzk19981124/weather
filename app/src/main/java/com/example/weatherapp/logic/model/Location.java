package com.example.weatherapp.logic.model;

/**
 * @author hello word
 * @desc 作用描述
 * @date 2021/8/9
 */
public class Location {
    private static String lat;
    private static String lng;

    public static String getLat() {
        return lat;
    }

    public static void setLat(String lat) {
        Location.lat = lat;
    }

    public static String getLng() {
        return lng;
    }

    public static void setLng(String lng) {
        Location.lng = lng;
    }
}
