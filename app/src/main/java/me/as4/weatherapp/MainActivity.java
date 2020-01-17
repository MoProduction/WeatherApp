package me.as4.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import me.as4.weatherapp.Models.Weather;
import me.as4.weatherapp.Models.WeatherModel;
import me.as4.weatherapp.Services.WeatherService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private WeatherService weatherService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherService = WeatherService.retrofit.create(WeatherService.class);
        Call<WeatherModel> call = weatherService.getWeatherCurrent("Seattle","metric","fcbdb0d5435fe8c6c8429082e9b8b0a1");
        call.enqueue(new Callback<WeatherModel>() {
            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
                if (response.isSuccessful()){
                    Log.d("SystemApi","Correct " + response.body().getList().get(0).getMain().getTemp());
                }else{
                    Log.d("SystemApi","Error " + call.request());
                }
            }

            @Override
            public void onFailure(Call<WeatherModel> call, Throwable t) {
                Log.d("SystemApi","Error " + t.getLocalizedMessage());
            }
        });

    }
}
