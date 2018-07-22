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

    String url = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather" +
            ".forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)" +
            "%20where%20text%3D%22nome%2C%20ak%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

    public void getforecast( final ForecastListAsyncResponse callback) {

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,
            new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject query = response.getJSONObject("query");
                            JSONObject results = query.getJSONObject("results");
                            JSONObject channel = results.getJSONObject("channel");

                            // Location object
                            JSONObject location = channel.getJSONObject("location");



                            // Item object
                            JSONObject item = channel.getJSONObject("item");

                            // Condition object
                            JSONObject conditionObject = item.getJSONObject("condition");
//                            forecast.setDate(conditionObject.getString("date"));
//                            forecast.setCurrentTemperature(conditionObject.getString("temp"));
//                            forecast.setCurrentWeatherDescription(conditionObject.getString("text"));

                            // Forecast json array
                            JSONArray forecastArray = item.getJSONArray("forecast");
                            for (int i = 0; i < forecastArray.length(); i++) {
                                JSONObject forecastObject = forecastArray.getJSONObject(i);

                                Forecast forecast  = new Forecast();

                                forecast.setForecastDate(forecastObject.getString("date"));
                                forecast.setForecastDay(forecastObject.getString("day"));
                                forecast.setForecastHighTemp(forecastObject.getString("high"));
                                forecast.setCurrentWeatherDescription(forecastObject.getString("text"));

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
