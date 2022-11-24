package com.example.weatherapp.api;

import com.example.weatherapp.entity.Main;
import com.example.weatherapp.entity.Root;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {
    String url = "http://api.openweathermap.org/";

    @GET("data/2.5/weather")
    Call<Root> getWeather(@Query("q") String countryName, @Query("appid") String appId);
}
