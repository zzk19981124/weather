package com.example.weatherapp.logic.model;

/**
 * @author hello word
 * @desc 作用描述
 * @date 2021/8/9
 */
public class Place {
    private static String name;
    private static Location location;
    private static String address;

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        Place.name = name;
    }

    public static Location getLocation() {
        return location;
    }

    public static void setLocation(Location location) {
        Place.location = location;
    }

    public static String getAddress() {
        return address;
    }

    public static void setAddress(String address) {
        Place.address = address;
    }
}
