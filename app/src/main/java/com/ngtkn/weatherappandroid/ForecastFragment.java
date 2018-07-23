package com.ngtkn.weatherappandroid;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ngtkn.weatherappandroid.model.Forecast;



/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastFragment extends Fragment {


    public ForecastFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View forecastView = inflater.inflate(R.layout.fragment_forecast, container, false);
        ImageView forecastIcon = forecastView.findViewById(R.id.forecastIconId);
        //TextView forecastTemp = forecastView.findViewById(R.id.forecastTemp);
        TextView forecastDate = forecastView.findViewById(R.id.forecastDateText);
        TextView forecastHigh = forecastView.findViewById(R.id.forecastHighText);
        TextView forecastLow = forecastView.findViewById(R.id.forecastLowText);
        TextView forecastDescription = forecastView.findViewById(R.id.forecastDescriptionText);

        Forecast forecast = (Forecast) getArguments().getSerializable("forecast");

        //forecastTemp.setText(forecast.getCurrentTemperature());
        forecastDate.setText(forecast.getForecastDate());
        forecastHigh.setText(forecast.getForecastHighTemp());
        forecastLow.setText(forecast.getForecastLowTemp());
        forecastDescription.setText(forecast.getCurrentWeatherDescription());

        // Inflate the layout for this fragment
        return forecastView;
    }

    // static instance for fragment
    public static final ForecastFragment newInstance(Forecast forecast) {
        ForecastFragment forecastFragment = new ForecastFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("forecast", forecast);

        forecastFragment.setArguments(bundle);

        return forecastFragment;
    }

}
