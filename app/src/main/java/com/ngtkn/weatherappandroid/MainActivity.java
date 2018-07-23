package com.ngtkn.weatherappandroid;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ngtkn.weatherappandroid.data.ForecastData;
import com.ngtkn.weatherappandroid.data.ForecastListAsyncResponse;
import com.ngtkn.weatherappandroid.data.ForecastViewPagerAdapter;
import com.ngtkn.weatherappandroid.model.Forecast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ForecastViewPagerAdapter forecastViewPagerAdapter;
    private ViewPager viewPager;
    private TextView locationText;
    private TextView currentTempText;
    private TextView currentDateText;
    private EditText userLocationText;
    private String userLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationText = findViewById(R.id.location_text_view);
        currentTempText = findViewById(R.id.current_temp_text);
        currentDateText = findViewById(R.id.current_date_text);
        userLocationText = findViewById(R.id.editTextLocation);

        userLocationText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {

                if((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    userLocation = userLocationText.getText().toString();
                    forecastViewPagerAdapter = new ForecastViewPagerAdapter(getSupportFragmentManager(),
                            getFragments(userLocation));

                    viewPager = findViewById(R.id.viewPager);
                    viewPager.setAdapter(forecastViewPagerAdapter);

                    return true;
                }

                return false;
            }
        });

        String location = userLocationText.getText().toString().trim();

//        forecastViewPagerAdapter = new ForecastViewPagerAdapter(getSupportFragmentManager(),
//                getFragments(location));

    }

    private List<Fragment> getFragments(String locationString) {
        final List<Fragment> fragmentList = new ArrayList<>();

        new ForecastData().getforecast(new ForecastListAsyncResponse() {

            @Override
            public void processFinished(ArrayList<Forecast> forecastArrayList) {

                locationText.setText(String.format("%s,\n%s", forecastArrayList.get(0).getCity(),
                        forecastArrayList.get(0).getRegion()));
                currentTempText.setText(String.format("%sF", forecastArrayList.get(0).getCurrentTemperature()));
                String[] date = forecastArrayList.get(0).getDate().split(" ");
                String splitDate =date[0] + " " + date[1] + " " + date[2] + " "+ date[3];
                currentDateText.setText(splitDate);


                for (int i = 0; i < forecastArrayList.size(); i++){
                    Forecast forecast = forecastArrayList.get(i);
                    ForecastFragment forecastFragment =
                            ForecastFragment.newInstance(forecast);

                    fragmentList.add(forecastFragment);
                }
                forecastViewPagerAdapter.notifyDataSetChanged();
            }
        }, locationString);

        return fragmentList;
    }
}
