package com.example.jim.weatherdata;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by jim on 29.02.16.
 */
public class RetainedFragment extends Fragment {

    ArrayList<WeatherData> dataList = new ArrayList<>();
    boolean running = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void getWeatherFromJson(final int times) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                {
                    running = true;
                    while (running){
                        dataList = new ArrayList<>();
                        String jsonURL = "http://kark.hin.no/~wfa/fag/android/2016/weather/vdata.php?id=2";
                        HttpURLConnection connection;
                        URL url;
                        try {
                            url = new URL(jsonURL);
                            for(int i = 0; i < times; i++) {
                                connection = (HttpURLConnection) url.openConnection();
                                connection.setRequestProperty("Content-Type", "text/plain; charset-utf-8");
                                int responseCode = connection.getResponseCode();

                                if (responseCode == HttpURLConnection.HTTP_OK) {
                                    Gson gson = new Gson();

                                    WeatherData data = gson.fromJson(new InputStreamReader(connection.getInputStream()), WeatherData.class);

                                } else {
                                    Log.d("MyTag", "HTTP error code: " + responseCode);
                                }
                            }
                            running = false;
                        } catch (IOException e) {
                            Log.d("MyTag", e.getMessage());
                        }
                    }
                }

            }
        });
        thread.start();
    }
    public void stopThread(){
        running = false;
    }
    class WeatherData {
        int id;
        String station_name;
        String station_position;
        String timestamp;
        double temperature;
        double pressure;
        double humidity;
    }

}
