package com.ngtkn.weatherappandroid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ngtkn.weatherappandroid.data.ForecastData;
import com.ngtkn.weatherappandroid.data.ForecastListAsyncResponse;
import com.ngtkn.weatherappandroid.data.ForecastViewPagerAdapter;
import com.ngtkn.weatherappandroid.model.Forecast;
import com.ngtkn.weatherappandroid.util.Prefs;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private ForecastViewPagerAdapter forecastViewPagerAdapter;
    private ViewPager viewPager;
    private TextView locationText;
    private TextView currentTempText;
    private TextView currentDateText;
    private EditText userLocationText;
    private String userLocation;
    ImageView icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationText = findViewById(R.id.location_text_view);
        currentTempText = findViewById(R.id.current_temp_text);
        currentDateText = findViewById(R.id.current_date_text);
        userLocationText = findViewById(R.id.editTextLocation);
        //icon = findViewById(R.id.weather_icon);

        final Prefs prefs = new Prefs(this);
        String prefsLocation = prefs.getLocation();
        getWeather(prefsLocation);

        userLocationText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
                    userLocation = userLocationText.getText().toString();
                    prefs.setLocation(userLocation);
                    getWeather(userLocation);
                    return true;
                }
                return false;
            }
        });
    }

    // get the weather of passed location
    private void getWeather(String currentLocation) {
        forecastViewPagerAdapter = new ForecastViewPagerAdapter(getSupportFragmentManager(),
                getFragments(currentLocation));
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(forecastViewPagerAdapter);
    }

    private List<Fragment> getFragments(String locationString) {
        final List<Fragment> fragmentList = new ArrayList<>();
        new ForecastData().getforecast(new ForecastListAsyncResponse() {

            @Override
            public void processFinished(ArrayList<Forecast> forecastArrayList) {
                fragmentList.clear();
                locationText.setText(String.format("%s,\n%s", forecastArrayList.get(0).getCity(),
                        forecastArrayList.get(0).getRegion()));
                currentTempText.setText(String.format("%sF", forecastArrayList.get(0).getCurrentTemperature()));
                String[] date = forecastArrayList.get(0).getDate().split(" ");
                String splitDate =date[0] + " " + date[1] + " " + date[2] + " "+ date[3];
                currentDateText.setText(splitDate);

                // TODO: Add imageview for weather icon
//                String html = forecastArrayList.get(0).getDescriptionHTML();
//                Glide.with(MainActivity.this).asGif().load(getImageUrl(html)).into(icon);

                for (int i = 0; i < forecastArrayList.size(); i++){
                    Forecast forecast = forecastArrayList.get(i);
                    ForecastFragment forecastFragment = ForecastFragment.newInstance(forecast);
                    fragmentList.add(forecastFragment);
                }
                viewPager.setAdapter(forecastViewPagerAdapter);
                forecastViewPagerAdapter.notifyDataSetChanged();
            }
        }, locationString);
        return fragmentList;
    }

    // Parses xml to extract image weather icon url, returns the url
    private String getImageUrl(String html) {

        String imgRegex = "(?i)<img[^>]+?src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
        String imgSrc = null;

        Pattern p = Pattern.compile(imgRegex);
        Matcher m = p.matcher(html);

        while(m.find()) {
            imgSrc = m.group(1);
        }
        return imgSrc;
    }
}
