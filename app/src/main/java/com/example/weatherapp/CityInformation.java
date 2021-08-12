package com.example.weatherapp;

/**
 * @author hello word
 * @desc 作用描述
 * @date 2021/8/12
 */
public class CityInformation {
    double latitude;    //获取纬度信息
    double longitude;    //获取经度信息
    float radius;    //获取定位精度，默认值为0.0f

    String coorType;
    //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准

    int errorCode;
    //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明

    String city;    //获取城市
    String district;    //获取区县
    String street;    //获取街道信息

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public String getCoorType() {
        return coorType;
    }

    public void setCoorType(String coorType) {
        this.coorType = coorType;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
