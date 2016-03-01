package com.example.jim.weatherdata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/**
 * Created by Jim on 29/02/2016.
 */
public class WeatherDataSource {
    private SQLiteDatabase database = null;
    private MySQLiteHelper dbhelper = null;

    public WeatherDataSource(Context context){
        dbhelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        if(dbhelper!=null)database = dbhelper.getWritableDatabase();
    }
    public void close(){
        dbhelper.close();
    }


    public void createWeatherData(int id, String station_name, String station_position,
                                  String timestamp,double temperature,
                                  double pressure, double humidity){

        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.KEY_ID, id);
        values.put(MySQLiteHelper.KEY_STATION_NAME, station_name);
        values.put(MySQLiteHelper.KEY_STATION_POSITION, station_position);
        values.put(MySQLiteHelper.KEY_TIMESTAMP, timestamp);
        values.put(MySQLiteHelper.KEY_TEMPERATURE, temperature);
        values.put(MySQLiteHelper.KEY_PRESSURE, pressure);
        values.put(MySQLiteHelper.KEY_HUMIDITY, humidity);

        database.insert(MySQLiteHelper.WEATHER_TABLE, null, values);

    }

    public void getDataFromDb(int rowsFromTheBottom) {
        SQLiteDatabase db = dbhelper.getReadableDatabase();


        String[] projection = {
                MySQLiteHelper.KEY_PRIMARY_ID,
                MySQLiteHelper.KEY_ID, MySQLiteHelper.KEY_STATION_NAME,
                MySQLiteHelper.KEY_STATION_POSITION, MySQLiteHelper.KEY_TIMESTAMP,
                MySQLiteHelper.KEY_TEMPERATURE, MySQLiteHelper.KEY_PRESSURE,
                MySQLiteHelper.KEY_HUMIDITY
        };


        String sortOrder =
                MySQLiteHelper.KEY_PRIMARY_ID + " ASC";

        Cursor cursor = db.query(
                MySQLiteHelper.WEATHER_TABLE, projection, null, null, null, null, sortOrder
        );
        cursor.moveToLast();

        for (int i = 0; i < rowsFromTheBottom; i++){
            cursor.moveToPrevious();
        }

        while (!cursor.isLast()) {
            double temperature = cursor.getDouble(
                    cursor.getColumnIndexOrThrow(MySQLiteHelper.KEY_TEMPERATURE));
            System.out.println(temperature);
            cursor.moveToNext();
        }
    }
}
