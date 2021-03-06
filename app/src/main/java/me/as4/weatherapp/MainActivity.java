package me.as4.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import me.as4.weatherapp.Models.WeatherModel;
import me.as4.weatherapp.Services.WeatherService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private WeatherService weatherService;
    private SearchView searchView;
    private String city_name;
    private String city_name_api;
    private String temp_api;
    private String desc_api;
    private String humid_api;

    private TextView city_text;
    private TextView temp_text;
    private TextView des_text;
    private TextView humid_text;
    private GoogleMap mMap;
    private String[] days = new String[] {"TEST","TWO"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = findViewById(R.id.searchview);
        searchView.setQueryHint("Set place");
        searchView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        city_text = findViewById(R.id.textcit);
        temp_text = findViewById(R.id.textemp);
        des_text = findViewById(R.id.texdes);
        humid_text= findViewById(R.id.texpers);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                city_name = query;
                getData(city_name);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });



    }

    private void getData(String s){
        weatherService = WeatherService.retrofit.create(WeatherService.class);
        Call<WeatherModel> call = weatherService.getWeatherCurrent(s,"metric","fcbdb0d5435fe8c6c8429082e9b8b0a1");
        call.enqueue(new Callback<WeatherModel>() {
            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
                if (response.isSuccessful() && response.body().getCount() >0){
                    city_name_api = response.body().getList().get(0).getName();
                    temp_api = String.valueOf(Math.round(response.body().getList().get(0).getMain().getTemp()) + "°");
                    desc_api = response.body().getList().get(0).getWeather().get(0).getDescription();
                    humid_api = String.valueOf(Math.round(response.body().getList().get(0).getMain().getHumidity())) + "%";

                    city_text.setText(city_name_api);
                    temp_text.setText(temp_api);
                    des_text.setText(desc_api);
                    humid_text.setText(humid_api);

                    // Add a marker in Sydney and move the camera
                    LatLng sydney = new LatLng(response.body().getList().get(0).getCoord().getLat(), response.body().getList().get(0).getCoord().getLon());
                    mMap.addMarker(new MarkerOptions().position(sydney).title(city_name_api));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,10));


                    Log.d("SystemApi","Correct " + response.body().getList().get(0).getName());
                    Log.d("SystemApi","Correct " + Math.round(response.body().getList().get(0).getMain().getTemp()));
                }else{
                    if(response.body().getMessage().equals("bad query") || response.body().getCount() == 0){
                        Toast.makeText(getApplicationContext(),"Ошибка запроса",Toast.LENGTH_LONG).show();
                    }
                    Log.d("SystemApi","Error " + call.request());
                }
            }

            @Override
            public void onFailure(Call<WeatherModel> call, Throwable t) {
                Log.d("SystemApi","Error " + t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


    }
}
