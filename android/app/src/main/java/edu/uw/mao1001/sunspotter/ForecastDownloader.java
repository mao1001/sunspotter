package edu.uw.mao1001.sunspotter;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Nick on 4/11/2016.
 */
public class ForecastDownloader {
    private static final String TAG = "ForecastDownloader";

    public static ArrayList<Forecast> downloadForecastData(String URI) {
        Log.i(TAG, "Entering downloadForecastData");

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        ArrayList<Forecast> forecasts = null;

        try {
            URL url = new URL(URI);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line = reader.readLine();
            while (line != null) {
                buffer.append(line + "\n");
                line = reader.readLine();
            }

            if (buffer.length() == 0) {
                return null;
            }

            String results = buffer.toString();

            //Log.v(TAG, results); //for debugging purposes

            try {
                JSONObject jsonObject = new JSONObject(results);
                Log.i(TAG, jsonObject.toString());
                forecasts = formatJSON(jsonObject);
            } catch(JSONException e) {
                Log.e(TAG, e.toString());
            }
        }
        catch (IOException e) {
            return null;
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e) {
                }
            }
        }

        return forecasts;
    }

    private static ArrayList<Forecast> formatJSON(JSONObject jsonObject) {
        ArrayList<Forecast> forecasts = new ArrayList<>();
        try {
            JSONArray list = jsonObject.getJSONArray("list");
            for (int i = 0; i < list.length(); i++) {
                JSONObject item = list.getJSONObject(i);
                JSONObject main = item.getJSONObject("main");
                JSONObject weather = item.getJSONArray("weather").getJSONObject(0);
                Forecast forecast = new Forecast(item.getString("dt"), weather.getString("description"), main.getDouble("temp"));
                forecasts.add(forecast);
            }

            return forecasts;

        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }

        return null;
    }
}
