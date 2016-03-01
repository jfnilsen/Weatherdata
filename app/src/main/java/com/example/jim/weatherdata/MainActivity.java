package com.example.jim.weatherdata;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.net.CookieHandler;
import java.net.CookieManager;

public class MainActivity extends AppCompatActivity implements RetainedFragment.DownloadTimeHelper {

    double total = 60;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment);
        //Aktiver cookies:
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        fixSeekBar();
    }



    public void fixSeekBar() {
        SeekBar seekBar = (SeekBar)findViewById(R.id.seekBar);
        seekBar.setProgress((int)total);
        seekBar.setMax(120);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                total = progress;
                ((TextView) findViewById(R.id.seekBar_value)).setText(progress + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void download(View view) {
        if(((Switch)findViewById(R.id.download_button)).isChecked()){
            RetainedFragment fragment = new RetainedFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.retained_fragment,fragment);
            transaction.commit();
            fragment.getWeatherFromJson((int)total);
        }else{
            ((RetainedFragment) getFragmentManager().findFragmentById(R.id.retained_fragment)).stopThread();
        }

    }
    public void localDBFetch(View view) {
        WeatherDataSource source = new WeatherDataSource(this);
        source.getDataFromDb((int)total);
    }

    @Override
    public void onDownloadTimeDecrease(final int remainingTime) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Button downloadButton = (Button) findViewById(R.id.download_button);
                downloadButton.setText(total-remainingTime + "");
                ProgressBar bar = (ProgressBar)findViewById(R.id.progressBar);
                double progress = (remainingTime/total)*100;
                bar.setProgress((int)progress);

            }
        });
    }
}
