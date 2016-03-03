package com.example.jim.weatherdata.logic;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.jim.weatherdata.R;
import com.example.jim.weatherdata.sqlite.WeatherData;
import com.example.jim.weatherdata.sqlite.WeatherDataSource;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Jim on 03/03/2016.
 */
public class VisualizeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    Calendar cal = new GregorianCalendar();
    int station_id = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visualize_chooser);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                station_id = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void showDialog(View view) {

        DatePickerDialog tp1 = new DatePickerDialog(this, this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        tp1.show();
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.MONTH, monthOfYear);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    public void drawGraph(View view) {
        WeatherDataSource source = new WeatherDataSource(this);
        ArrayList<WeatherData> weatherDatas = source.getDataFromDbWhereStationId(station_id, 100);
        GraphView graphView = ((GraphView) findViewById(R.id.graphView));
        graphView.setWeatherDatas(weatherDatas);
        graphView.invalidate();
    }
}
