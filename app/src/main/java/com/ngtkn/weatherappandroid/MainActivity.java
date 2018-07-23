package com.ngtkn.weatherappandroid;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ngtkn.weatherappandroid.data.ForecastData;
import com.ngtkn.weatherappandroid.data.ForecastListAsyncResponse;
import com.ngtkn.weatherappandroid.data.ForecastViewPagerAdapter;
import com.ngtkn.weatherappandroid.model.Forecast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ForecastViewPagerAdapter forecastViewPagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        forecastViewPagerAdapter = new ForecastViewPagerAdapter(getSupportFragmentManager(),
                getFragments());
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(forecastViewPagerAdapter);
    }

    private List<Fragment> getFragments() {
        final List<Fragment> fragmentList = new ArrayList<>();

        new ForecastData().getforecast(new ForecastListAsyncResponse() {

            @Override
            public void processFinished(ArrayList<Forecast> forecastArrayList) {
                for (int i = 0; i < forecastArrayList.size(); i++){
                    Forecast forecast = forecastArrayList.get(i);
                    ForecastFragment forecastFragment =
                            ForecastFragment.newInstance(forecast);

                    fragmentList.add(forecastFragment);
                }
                forecastViewPagerAdapter.notifyDataSetChanged();
            }
        });

        return fragmentList;
    }
}
