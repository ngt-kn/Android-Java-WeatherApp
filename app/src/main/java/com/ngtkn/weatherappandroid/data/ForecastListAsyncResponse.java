package com.ngtkn.weatherappandroid.data;

import com.ngtkn.weatherappandroid.model.Forecast;

import java.util.ArrayList;

/**
 *  Interface for callback
 */
public interface ForecastListAsyncResponse {
    void processFinished(ArrayList<Forecast> forecastArrayList);
}
