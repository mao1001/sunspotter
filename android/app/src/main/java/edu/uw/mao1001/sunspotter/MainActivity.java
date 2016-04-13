package edu.uw.mao1001.sunspotter;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ArrayAdapter<String> adapter;

    //-----------------------//
    //   O V E R R I D E S   //
    //-----------------------//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instantiateSearchButton();

        //Creates a new ArrayAdapter to the result list view.
        adapter = new ArrayAdapter<>(this, R.layout.search_result_item, new ArrayList<String>());
        ListView listview = (ListView)findViewById(R.id.searchResults);
        if (listview != null) {
            listview.setAdapter(adapter);
        } else {
            Log.e(TAG, "ListView is null");
        }
    }

    //-----------------------------------//
    //   P R I V A T E   H E L P E R S   //
    //-----------------------------------//

    /**
     * Instantiates the search button by attaching a click listener to it.
     */
    private void instantiateSearchButton() {
        Button searchBtn = (Button)findViewById(R.id.searchButton);
        if (searchBtn != null) {
            searchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText searchBar = (EditText)findViewById(R.id.searchBar);
                    if (searchBar != null) {
                        String uri = buildURI(searchBar.getText().toString());
                        new Search().execute(uri);
                    }
                }
            });
        }
    }

    /**
     * Builds a URI based on the passed in city name. The data is returned in
     * a JSON format with imperial units where applicable.
     * @ params String city: City to be included as a query with the URI
     *
     * @ return String: The built URI.
     */
    private String buildURI(String city) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("api.openweathermap.org")
                .appendPath("data")
                .appendPath("2.5")
                .appendPath("forecast")
                .appendQueryParameter("q", city)
                .appendQueryParameter("format", "JSON")
                .appendQueryParameter("units", "imperial")
                .appendQueryParameter("appid", BuildConfig.OPEN_WEATHER_MAP_API_KEY);

        String uri = builder.build().toString();
        //Log.i(TAG, uri);
        return uri;
    }

    /**
     * Displays data to the user with the passed list of forecasts.
     *
     * @ param ArrayList<Forecast> forecasts: List of forecasts to be displayed.
     */
    private void displayResults(ArrayList<Forecast> forecasts) {
        //ViewStub bottomStub = (ViewStub)findViewById(R.id.bottomStub);
        //ViewStub middleStub = (ViewStub)findViewById(R.id.middleStub);

        //if (bottomStub != null && middleStub != null) {
        //bottomStub.inflate();
        // middleStub.inflate();
        adapter.clear();
        Forecast firstSun = null;
        for (Forecast item : forecasts) {
            adapter.add(parseData(item));
            if (item.getSunStatus() && firstSun == null) {
                firstSun = item;
            }
        }

        TextView sunStatusTxtView = (TextView)findViewById(R.id.sun_status_message);
        TextView sunStatusDetail = (TextView)findViewById(R.id.sun_status_detail);
        ImageView sunVisual = (ImageView)findViewById(R.id.sun_visual);

        if (firstSun != null) {
            sunStatusTxtView.setText(R.string.status_sun);
            sunStatusDetail.setText("on " + firstSun.getDate().toString());
            sunVisual.setImageResource(R.drawable.sun_icon2);
        } else {
            sunStatusTxtView.setText(R.string.status_no_sun);
            sunStatusDetail.setText("Not for a while anyway =(");
            sunVisual.setImageResource(R.drawable.cloud_icon2);
        }


//            } else {
//                Log.e(TAG, "Either 'bottomStub' or 'middleStub' weren't initialized properly. Check identifiers.");
//            }
    }

    /**
     * Takes a forecast and parses out a string representation of that data.
     *
     * @ param Forecast forecast: The forecast to be parsed
     *
     * @ return String: A string representation of the data.
     */
    private String parseData(Forecast forecast) {
        String parsedString = "";

        parsedString += getString(R.string.temperature_lead_message) + " " + forecast.getTemperature().intValue() + ". ";

        if (forecast.getSunStatus()) {
            parsedString += getString(R.string.sun_message);
        } else {
            parsedString += getString(R.string.no_sun_message);
        }

        parsedString += " " + forecast.getDate();
        return parsedString;
    }

    //-------------------------------//
    //   P R I V A T E   C L A S S   //
    //----- -------------------------//

    /**
     * Private class that extends AsyncTask. Primary function is to find the forecast
     * based on the passed in URI and download the data.
     */
    private class Search extends AsyncTask<String, Void, ArrayList<Forecast>> {

        @Override
        protected ArrayList<Forecast> doInBackground(String...params) {
            //Downloads data.
            return ForecastDownloader.downloadForecastData(params[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<Forecast> forecasts) {
            super.onPostExecute(forecasts);
            displayResults(forecasts);
        }
    }
}
