package com.ngtkn.weatherappandroid.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ngtkn.weatherappandroid.controller.AppController;
import com.ngtkn.weatherappandroid.model.Forecast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.android.volley.VolleyLog.TAG;

/**
 * Class to retrieve/consume json data from yahoo api
 */
public class ForecastData {
    ArrayList<Forecast> forecastArrayList = new ArrayList<>();
    String urlLeft = "https://query.yahooapis.com/v1/public/yql?q=select*from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22";
    String urlRight = "%22)&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";

    public void getforecast( final ForecastListAsyncResponse callback, String locationText) {

        String url = urlLeft + locationText + urlRight;
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
            new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            forecastArrayList.clear();
                            JSONObject query = response.getJSONObject("query");
                            JSONObject results = query.getJSONObject("results");
                            JSONObject channel = results.getJSONObject("channel");

                            // Location object
                            JSONObject location = channel.getJSONObject("location");

                            // Item object
                            JSONObject item = channel.getJSONObject("item");

                            // Condition object
                            JSONObject conditionObject = item.getJSONObject("condition");

                            // Forecast json array
                            JSONArray forecastArray = new JSONArray();
                            forecastArray = item.getJSONArray("forecast");
                            for (int i = 0; i < forecastArray.length(); i++) {
                                JSONObject forecastObject = forecastArray.getJSONObject(i);
                                Forecast forecast  = new Forecast();

                                forecast.setForecastDate(forecastObject.getString("date"));
                                forecast.setForecastDay(forecastObject.getString("day"));
                                forecast.setForecastHighTemp(forecastObject.getString("high"));
                                forecast.setForecastLowTemp(forecastObject.getString("low"));
                                forecast.setCurrentWeatherDescription(forecastObject.getString("text"));

                                forecast.setCity(location.getString("city"));
                                forecast.setCurrentTemperature(conditionObject.getString("temp"));
                                forecast.setRegion(location.getString("region"));
                                forecast.setDate(conditionObject.getString("date"));

                                forecast.setDescriptionHTML(item.getString("description"));

                                forecastArrayList.add(forecast);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } if (null != callback) {
                            callback.processFinished(forecastArrayList);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        AppController.getmInstance().addToRequestQueue(jsonObjectRequest);
    }
}
