package edu.uw.mao1001.sunspotter;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instantiateSearchButton();

        adapter = new ArrayAdapter<String>(this, R.layout.search_result_item, new ArrayList<String>());
        ListView listview = (ListView)findViewById(R.id.searchResults);
        if (listview != null) {
            listview.setAdapter(adapter);
        } else {
            Log.i(TAG, "Listview is null");
        }

    }

    private void instantiateSearchButton() {
        Button searchBtn = (Button)findViewById(R.id.searchButton);
        if (searchBtn != null) {
            searchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText searchBar = (EditText)findViewById(R.id.searchBar);
                    if (searchBar != null) {
                        String uri = buildURI(searchBar.getText().toString());
                        Log.i(TAG, uri);
                        new Search().execute(uri);
                    }
                }
            });
        }
    }

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

        //http://api.openweathermap.org/data/2.5/forecast?q=Seattle&format=json&appid=52999c99f1a294b470e1e57f2602a5e1

        return builder.build().toString();
    }

    private class Search extends AsyncTask<String, Void, ArrayList<Forecast>> {

        @Override
        protected ArrayList<Forecast> doInBackground(String...params) {
            Log.i(TAG, "Entering doInBackground");

            ArrayList<Forecast> results = ForecastDownloader.downloadForecastData(params[0]);

            Log.i(TAG, results.toString());
            return results;
        }

        @Override
        protected void onPostExecute(ArrayList<Forecast> forecasts) {
            super.onPostExecute(forecasts);
            Log.i(TAG, "Entering onPostExecute");

            //ViewStub bottomStub = (ViewStub)findViewById(R.id.bottomStub);
            //ViewStub middleStub = (ViewStub)findViewById(R.id.middleStub);

            //if (bottomStub != null && middleStub != null) {
                //bottomStub.inflate();
               // middleStub.inflate();
            adapter.clear();
            Forecast firstSun = null;
            for (Forecast item : forecasts) {
                Log.i(TAG, "Adding: " + item);
                String sunStatus = item.getSunStatus();
                adapter.add(item.toString());
                if (sunStatus.equals("Sun") && firstSun == null) {
                    firstSun = item;
                }
            }

            TextView sunStatusTxtView = (TextView)findViewById(R.id.sun_status_message);
            TextView sunStatusDetail = (TextView)findViewById(R.id.sun_status_detail);
            ImageView sunVisual = (ImageView)findViewById(R.id.sun_visual);

            if (firstSun != null) {
                sunStatusTxtView.setText(R.string.status_sun);
                sunStatusDetail.setText("Sun on " + firstSun.getDate().toString());
                sunVisual.setImageResource(R.drawable.sun_icon2);
            } else {
                sunStatusTxtView.setText(R.string.status_no_sun);
                sunStatusDetail.setText("There will be no sun for a while.");
                sunVisual.setImageResource(R.drawable.cloud_icon2);
            }


//            } else {
//                Log.e(TAG, "Either 'bottomStub' or 'middleStub' weren't initialized properly. Check identifiers.");
//            }
        }
    }
}
