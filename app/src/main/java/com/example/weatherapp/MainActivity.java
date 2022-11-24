package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherapp.api.WeatherApi;
import com.example.weatherapp.entity.Root;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static Root root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Spinner staticSpinner = findViewById(R.id.static_spinner);
        String[] items = new String[] { "Chọn", "VietNam", "London,uk", "China", "Malaysia"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, items);
        staticSpinner.setAdapter(adapter);
        staticSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String countryName = (String) parent.getItemAtPosition(position);
                getWeather(countryName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                root = null;
            }
        });
    }

    public void getWeather(String countryName) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WeatherApi.url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherApi weatherApi = retrofit.create(WeatherApi.class);
        weatherApi.getWeather(countryName, "66d9be5f6f21f670ac388097b92353d5").enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                TextView result = findViewById(R.id.result);
                TextView temp = findViewById(R.id.temp);
                TextView minTemp = findViewById(R.id.minTemp);
                TextView maxTemp = findViewById(R.id.maxTemp);
                if (response.code() == 200) {
                    root = response.body();
                    if(root != null) {
                        result.setText("Thông tin thời tiết: ");
                        temp.setText("Nhiệt độ: " + root.getMain().getTemp() + "K");
                        minTemp.setText("Nhiệt độ thấp nhất: " + root.getMain().getTemp_min() + "K");
                        maxTemp.setText("Nhiệt độ cao nhất: " + root.getMain().getTemp_max() + "K");
                    }
                    else {
                        result.setText("");
                        temp.setText("");
                        minTemp.setText("");
                        maxTemp.setText("");
                    }
                } else {
                    String msg = response.message();
                    result.setText("");
                    temp.setText("");
                    minTemp.setText("");
                    maxTemp.setText("");
                    Log.d("Respondse msg: ", msg);
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Log.d("MainActivity", "onFailure: " + t);
            }
        });
    }
}