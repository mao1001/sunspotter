package edu.uw.mao1001.sunspotter;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instantiateSearchButton();

    }

    private void instantiateSearchButton() {
        Button searchBtn = (Button)findViewById(R.id.searchButton);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchBar = (EditText)findViewById(R.id.searchBar);
                String uri = buildURI(searchBar.getText().toString());
                Log.i(TAG, uri);
                new Search().execute(uri);
            }
        });
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
                .appendQueryParameter("appid", BuildConfig.OPEN_WEATHER_MAP_API_KEY);

        http://api.openweathermap.org/data/2.5/forecast?q=Seattle&format=json&appid=52999c99f1a294b470e1e57f2602a5e1

        return builder.build().toString();
    }

    private class Search extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String...params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String forecasts[] = null;

            try {

                URL url = new URL(buildURI(params[0]));

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
                results = results.replace("{\"Search\":[","");
                results = results.replace("]}","");
                results = results.replace("},", "},\n");

                Log.v(TAG, results); //for debugging purposes

                forecasts = results.split("\n");
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

            return null;
        }
    }
}
