package com.example.jim.weatherdata;

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
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;

import java.net.CookieHandler;
import java.net.CookieManager;

public class MainActivity extends AppCompatActivity implements RetainedFragment.DownloadTimeHelper, SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String PREFS_NAME = "MyWeatherPreferences";
    double totalSeconds = 120;
    int intervalSec = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment);
        //Aktiver cookies:
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(this);

        preferences.registerOnSharedPreferenceChangeListener(this);
        intervalSec = Integer.parseInt(preferences.getString("PREF_INTERVAL", "1"));
        totalSeconds = Double.parseDouble(preferences.getString("PREF_SECONDS", "120"));

        setListeners();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_settings:
                Intent i = new Intent(this, MyPreferenceActivity.class);
                startActivity(i);
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
                fragment.getWeatherFromJson((int) totalSeconds +1, intervalSec);
        }else{
            ((RetainedFragment) getFragmentManager().findFragmentById(R.id.retained_fragment)).stopThread();
        }

    }
    public void localDBFetch(View view) {

        WeatherDataSource source = new WeatherDataSource(this);
        source.getDataFromDb(10);
    }

    @Override
    public void onDownloadTimeDecrease(final int remainingTime) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Switch downloadButton = (Switch) findViewById(R.id.download_switch);
                downloadButton.setText(String.valueOf((int)(totalSeconds -remainingTime)));
                ProgressBar bar = (ProgressBar)findViewById(R.id.progressBar);
                double progress = (remainingTime/ totalSeconds)*100;
                bar.setProgress((int)progress);

            }
        });
    }

    @Override
    public void downloadStarted() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //TODO:Screen set always on
            }
        });
    }

    @Override
    public void downloadCompleted() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Switch downloadButton = (Switch) findViewById(R.id.download_switch);
                downloadButton.setText("Complete!");
                downloadButton.setChecked(false);
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
    }

    private void writeToSharedPreference(){
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("PREF_INTERVAL", String.valueOf(intervalSec));
        editor.putString("PREF_SECONDS", String.valueOf(totalSeconds));
        editor.commit();
        super.onStop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        writeToSharedPreference();
    }
}
