package com.example.weatherapp.gson;

import com.google.gson.annotations.SerializedName;

/**
 * @author hello word
 * @desc 作用描述
 * @date 2021/8/18
 *
 * 开发文档   ：https://dev.qweather.com/docs/api/weather/weather-now/
 *
 * {
 * 	"code": "200",
 * 	"updateTime": "2021-09-01T14:17+08:00",
 * 	"fxLink": "http://hfx.link/1u111",
 * 	"now": {
 * 		"obsTime": "2021-09-01T14:12+08:00",
 * 		"temp": "31",
 * 		"feelsLike": "32",
 * 		"icon": "104",
 * 		"text": "阴",
 * 		"wind360": "135",
 * 		"windDir": "东南风",
 * 		"windScale": "4",
 * 		"windSpeed": "21",
 * 		"humidity": "72",
 * 		"precip": "0.0",
 * 		"pressure": "1003",
 * 		"vis": "30",
 * 		"cloud": "84",
 * 		"dew": "26"
 *        },
 * 	"refer": {
 * 		"sources": ["Weather China"],
 * 		"license": ["no commercial use"]
 *    }
 * }
 */
public class Weather {

    public String code; //状态码

    @SerializedName("obsTime")
    public String updateTime;//当前API的最近更新时间

    public Now now;

    public class Now{
        @SerializedName("temp")
        public String nowTemp;//温度

        @SerializedName("feelsLike")
        public String feelsTemp;//体感温度


        @SerializedName("text")
        public String weatherDescribe;//天气描述，如 ： 阴

        public String windDir;//风向
        public String Scale;//风力等级
        public String windSpeed;//风速，公里/小时
        public String humidity;//相对湿度，百分比数值
    }

    @Override
    public String toString() {
        return "Weather{" +
                "code='" + code + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", now=" + now +
                '}';
    }
}
