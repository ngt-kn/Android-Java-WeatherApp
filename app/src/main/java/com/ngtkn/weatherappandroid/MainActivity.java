package com.ngtkn.weatherappandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ngtkn.weatherappandroid.data.ForecastData;
import com.ngtkn.weatherappandroid.data.ForecastListAsyncResponse;
import com.ngtkn.weatherappandroid.model.Forecast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ForecastData forecastData = new ForecastData();
        forecastData.getforecast(new ForecastListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Forecast> forecastArrayList) {
                for (int i = 0; i < forecastArrayList.size(); i++){
                    Log.d("TAG", "processFinished: " + forecastArrayList.get(i).getForecastHighTemp());
                }
            }
        });
    }
}
