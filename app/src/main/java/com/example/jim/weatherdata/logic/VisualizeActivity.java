package com.example.jim.weatherdata.logic;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.jim.weatherdata.R;
import com.example.jim.weatherdata.sqlite.WeatherData;
import com.example.jim.weatherdata.sqlite.WeatherDataSource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Jim on 03/03/2016.
 */
public class VisualizeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    int station_id = -1;
    TextView dateToBeSet;
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
        Calendar cal = new GregorianCalendar();
        ((TextView) findViewById(R.id.stop_date_textView)).setText( cal.get(Calendar.DAY_OF_MONTH)+ "-"+ (cal.get(Calendar.MONTH)+1)+"-"+ cal.get(Calendar.YEAR));

    }

    public void showDateDialog(View view) {
        if(view.getId() == R.id.start_date_button){
            dateToBeSet = (TextView) findViewById(R.id.start_date_textView);
        }else if (view.getId() == R.id.stop_date_button){
            dateToBeSet = (TextView) findViewById(R.id.stop_date_textView);
        }
        Calendar cal = new GregorianCalendar();
        DatePickerDialog tp1 = new DatePickerDialog(this, this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        tp1.show();

    }
    public void showTimeDialog(View view) {
        if(view.getId() == R.id.start_time_button){
            dateToBeSet = (TextView) findViewById(R.id.start_time_textView);
        }else if (view.getId() == R.id.stop_time_button){
            dateToBeSet = (TextView) findViewById(R.id.stop_time_textView);
        }
        Calendar cal = new GregorianCalendar();
        TimePickerDialog tp1 = new TimePickerDialog(this, this, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
        tp1.show();
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        dateToBeSet.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        dateToBeSet.setText(hourOfDay + ":" + minute + ":00");
    }

    public void drawGraph(View view) {
        WeatherDataSource source = new WeatherDataSource(this);
        ArrayList<WeatherData> weatherDatas = source.getDataFromDbWhereStationId(station_id);
        GraphView graphView = ((GraphView) findViewById(R.id.graphView));
        graphView.setWeatherDatas(filterOutDatasOutsideDate(weatherDatas));
        graphView.setGraphType(((Spinner)findViewById(R.id.spinner_datatype)).getSelectedItemPosition());
        graphView.invalidate();
    }

    private ArrayList<WeatherData> filterOutDatasOutsideDate(ArrayList<WeatherData> weatherDatas) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date startDate = null;
        Date stopDate = null;
        ArrayList<WeatherData> returnData = new ArrayList<>();
        try {
            startDate = dateFormat.parse(
                    ((TextView)findViewById(R.id.start_date_textView)).getText().toString() + " " +
                            ((TextView)findViewById(R.id.start_time_textView)).getText().toString());

            stopDate = dateFormat.parse(((TextView)findViewById(R.id.stop_date_textView)).getText().toString()+ " " +
                    ((TextView)findViewById(R.id.stop_time_textView)).getText().toString());
            for(WeatherData data : weatherDatas) {
                Date dataDate = dateFormat.parse(data.timestamp);
                if(startDate.getTime() - dataDate.getTime() < 0 && stopDate.getTime() - dataDate.getTime() > 0){
                    returnData.add(data);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnData;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("STATION_ID", station_id);
        outState.putString("START_DATE", ((TextView) findViewById(R.id.start_date_textView)).getText().toString());
        outState.putString("START_TIME", ((TextView) findViewById(R.id.start_time_textView)).getText().toString());
        outState.putString("END_DATE", ((TextView) findViewById(R.id.stop_date_textView)).getText().toString());
        outState.putString("END_TIME", ((TextView) findViewById(R.id.stop_time_textView)).getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        station_id = savedInstanceState.getInt("STATION_ID",-1);
        ((TextView) findViewById(R.id.start_date_textView)).setText(savedInstanceState.getString("START_DATE"));
        ((TextView) findViewById(R.id.start_time_textView)).setText(savedInstanceState.getString("START_TIME"));
        ((TextView) findViewById(R.id.stop_date_textView)).setText(savedInstanceState.getString("END_DATE"));
        ((TextView) findViewById(R.id.stop_time_textView)).setText(savedInstanceState.getString("END_TIME"));
        drawGraph(null);
    }
}
