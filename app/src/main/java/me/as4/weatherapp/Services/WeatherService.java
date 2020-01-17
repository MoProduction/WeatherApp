package me.as4.weatherapp.Services;

import me.as4.weatherapp.Models.WeatherModel;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {


    @GET("data/2.5/find")
    Call<WeatherModel> getWeatherCurrent(@Query("q") String q, @Query("units") String units,@Query("appid") String appid);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
