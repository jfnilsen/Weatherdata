package com.example.jim.weatherdata.logic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jim.weatherdata.R;
import com.example.jim.weatherdata.preferences.MyPreferenceActivity;
import com.example.jim.weatherdata.sqlite.WeatherData;
import com.example.jim.weatherdata.sqlite.WeatherDataSource;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RetainedFragment.DownloadTimeHelper, SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String PREFS_NAME = "MyWeatherPreferences";
    double totalSeconds = 120;
    int intervalSec = 1;
    int datapoints_number = 100;
    boolean keep_old_station = false;
    boolean downloadRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment);
        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(this);

        intervalSec = Integer.parseInt(preferences.getString("PREF_INTERVAL", "1"));
        totalSeconds = Double.parseDouble(preferences.getString("PREF_SECONDS", "120"));
        keep_old_station = preferences.getBoolean("PREF_KEEP_OLD", false);
        datapoints_number = Integer.parseInt(preferences.getString("PREF_DATA_POINTS", "100"));

        setListeners();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_settings:
                if(downloadRunning){
                    Toast.makeText(MainActivity.this, getString(R.string.options_disabled), Toast.LENGTH_SHORT).show();
                }else{
                    Intent i = new Intent(this, MyPreferenceActivity.class);
                    startActivity(i);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void setListeners() {
        Switch downloadSwitch = (Switch)findViewById(R.id.download_switch);
        downloadSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                download();
            }
        });
    }

    public void download() {
        if(((Switch)findViewById(R.id.download_switch)).isChecked()){

            RetainedFragment fragment = (RetainedFragment)getFragmentManager().findFragmentById(R.id.retained_fragment);
            if(!fragment.isRunning())
                fragment.getWeatherFromJson((int) totalSeconds, intervalSec);
        }else{
            ((RetainedFragment) getFragmentManager().findFragmentById(R.id.retained_fragment)).stopThread();
        }
    }
    public void localDBFetch(String stationValue) {

        WeatherDataSource source = new WeatherDataSource(this);
        ArrayList<WeatherData> weatherDatas;
        if(keep_old_station){
            weatherDatas = source.getAllDataFromDb();
        }else {
            weatherDatas = source.getDataFromDbWhereStationId(Integer.parseInt(stationValue));
        }
        if(weatherDatas.size() > datapoints_number){
            weatherDatas = new ArrayList<>(weatherDatas.subList(weatherDatas.size()-datapoints_number-1 ,weatherDatas.size()-1));
        }
        GraphView graphView = ((GraphView) findViewById(R.id.graphView));
        graphView.setWeatherDatas(weatherDatas);
        graphView.invalidate();
    }

    @Override
    public void onDownloadTimeDecrease(final int remainingTime, final String stationValue) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView downloadButton = (TextView) findViewById(R.id.countdown_view);
                downloadButton.setText(String.valueOf((int) (totalSeconds - remainingTime)));
                ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar);
                double progress = (remainingTime / totalSeconds) * 100;
                bar.setProgress((int) progress);
                localDBFetch(stationValue);

            }
        });
    }

    @Override
    public void downloadStarted() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                downloadRunning = true;
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

            }
        });
    }

    @Override
    public void downloadCompleted() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView downloadButton = (TextView) findViewById(R.id.countdown_view);
                downloadButton.setText("Complete!");
                Switch aSwitch = (Switch) findViewById(R.id.download_switch);
                aSwitch.setChecked(false);
                downloadRunning = false;
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Context context = getApplicationContext();
        SharedPreferences myprefs =
                PreferenceManager.getDefaultSharedPreferences(context);

        totalSeconds = Double.parseDouble(myprefs.getString("PREF_SECONDS", "120"));
        intervalSec = Integer.parseInt(myprefs.getString("PREF_INTERVAL", "1"));
        keep_old_station = myprefs.getBoolean("PREF_KEEP_OLD", false);
        datapoints_number = Integer.parseInt(myprefs.getString("PREF_DATA_POINTS", "100"));
    }

    private void writeToSharedPreference(){
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("PREF_INTERVAL", String.valueOf(intervalSec));
        editor.putString("PREF_SECONDS", String.valueOf(totalSeconds));
        editor.putBoolean("PREF_KEEP_OLD", false);
        editor.putString("PREF_DATA_POINTS", "100");
        editor.commit();
        super.onStop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        downloadCompleted();
        writeToSharedPreference();
    }

    public void showViewGraphArguments(View view) {
        if(!downloadRunning){
            Intent i = new Intent(this, VisualizeActivity.class);
            startActivity(i);
        }else {
            Toast.makeText(this, getString(R.string.view_graph_while_downloading),Toast.LENGTH_LONG).show();
        }

    }
}