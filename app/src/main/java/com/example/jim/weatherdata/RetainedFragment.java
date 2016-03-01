package com.example.jim.weatherdata;

import android.app.Fragment;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;


import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by jim on 29.02.16.
 */
public class RetainedFragment extends Fragment {

    ArrayList<WeatherData> dataList = new ArrayList<>();
    boolean running = true;
    DownloadTimeHelper mCallback;
    public interface DownloadTimeHelper {
        void onDownloadTimeDecrease(int remainingTime);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mCallback = (DownloadTimeHelper) context;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    public void getWeatherFromJson(final int times) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                {
                    running = true;
                    int runsRemaining = times;
                    while (running){
                        if(runsRemaining <= 0 || !running){
                            running = false;
                            break;
                        }
                        dataList = new ArrayList<>();
                        String jsonURL = "http://kark.hin.no/~wfa/fag/android/2016/weather/vdata.php?id=2";
                        HttpURLConnection connection;
                        URL url;
                        WeatherDataSource src = null;
                        try {
                            url = new URL(jsonURL);
                                connection = (HttpURLConnection) url.openConnection();
                                connection.setRequestProperty("Content-Type", "text/plain; charset-utf-8");
                                int responseCode = connection.getResponseCode();

                                if (responseCode == HttpURLConnection.HTTP_OK) {
                                    Gson gson = new Gson();

                                    WeatherData data = gson.fromJson(new InputStreamReader(connection.getInputStream()), WeatherData.class);
                                    src = new WeatherDataSource(getActivity());
                                    src.open();
                                    src.createWeatherData(data.id, data.station_name, data.station_position, data.timestamp, data.temperature, data.pressure, data.humidity);
                                    src.close();
                                    mCallback.onDownloadTimeDecrease(times - --runsRemaining);
                                    Thread.sleep(1000);
                                } else {
                                    Log.d("MyTag", "HTTP error code: " + responseCode);
                                }


                        } catch (IOException e) {
                            Log.d("MyTag", e.getMessage());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
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
