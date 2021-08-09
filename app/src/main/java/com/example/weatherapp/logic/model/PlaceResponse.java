package com.example.weatherapp.logic.model;

import java.util.List;

/**
 * @author hello word
 * @desc 数据模型
 * @date 2021/8/9
 */
public class PlaceResponse {
    private static String status;
    private static List<Place> places;

    public static String getStatus() {
        return status;
    }

    public static void setStatus(String status) {
        PlaceResponse.status = status;
    }

    public static List<Place> getPlaces() {
        return places;
    }

    public static void setPlaces(List<Place> places) {
        PlaceResponse.places = places;
    }
}
