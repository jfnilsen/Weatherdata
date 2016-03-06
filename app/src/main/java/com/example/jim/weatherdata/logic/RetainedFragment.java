package com.example.jim.weatherdata.logic;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.jim.weatherdata.R;
import com.example.jim.weatherdata.sqlite.WeatherData;
import com.example.jim.weatherdata.sqlite.WeatherDataSource;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

/**
 * Created by jim on 29.02.16.
 */
public class RetainedFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    boolean running = false;
    String stationValue = "0";
    DownloadTimeHelper mCallback;

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Context context = getActivity();
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        stationValue = preferences.getString("PREF_STATION", "0");
    }

    public interface DownloadTimeHelper {
        void onDownloadTimeDecrease(int remainingTime);
        void downloadStarted();
        void downloadCompleted();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.retained_fragment, container);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        preferences.registerOnSharedPreferenceChangeListener(this);
        stationValue = preferences.getString("PREF_STATION", "0");

        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);

        try{
            mCallback = (DownloadTimeHelper) context;
        }catch (ClassCastException e){
            e.printStackTrace();
        }
    }

    public void getWeatherFromJson(final int maxRunningTime, final int interval) {
        Thread countdownThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int secondsRemaining = maxRunningTime;
                mCallback.downloadStarted();

                while (secondsRemaining >= 0 && running){
                    try {
                        Thread.sleep(1000);
                        mCallback.onDownloadTimeDecrease(maxRunningTime - secondsRemaining--);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                running = false;
                mCallback.downloadCompleted();
            }
        });

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                {
                    String jsonURL = "http://kark.hin.no/~wfa/fag/android/2016/weather/vdata.php?id=" + stationValue;
                    HttpURLConnection connection;
                    URL url;
                    WeatherDataSource src;
                    while (running){
                        try {
                            Thread.sleep(1000*interval);
                            if(!running) break;

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

        running = true;
        countdownThread.start();
        thread.start();
    }

    public void stopThread(){
        running = false;
    }
    public boolean isRunning() {
        return running;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("RUNNING", running);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null){
            running =  savedInstanceState.getBoolean("RUNNING", false);
        }
    }
}
