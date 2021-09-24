package com.bwap.weatherapp.WeatherApp.com.bwap.weatherapp.WeatherApp.controller;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {
    private OkHttpClient client;
    private Response response;
    private String cityName;
    String unit;
    private String API="c72b60d19ae90de6f54ba159b5e09d29";

    public JSONObject getWeather(){
        client =new OkHttpClient();
        Request request= new Request.Builder()
                .url("http://api.openweathermap.org/data/2.5/weather?q="+getCityName()+"&units="+getUnit()+"&appid="+API)
                .build();
        try  {
            response = client.newCall(request).execute();
            return new JSONObject(response.body().string());
        }catch(IOException e){
            e.printStackTrace();
        }

        return null;

    }

    public JSONArray returnWeatherArray()
    {
        JSONArray weatherArray = getWeather().getJSONArray("weather");
        return weatherArray;
    }

    public JSONObject returnMain(){
        JSONObject main= getWeather().getJSONObject("main");
        return main;
    }

    public JSONObject returnWind(){
        JSONObject wind= getWeather().getJSONObject("wind");
        return wind;
    }

    public JSONObject returnSys(){
        JSONObject sys= getWeather().getJSONObject("sys");
        return sys;
    }



    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
