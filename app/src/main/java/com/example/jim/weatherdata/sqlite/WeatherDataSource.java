package com.example.jim.weatherdata.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;

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

    public void deleteAllStoredData(){
        database.delete(MySQLiteHelper.WEATHER_TABLE, null, null);
    }

    public ArrayList<WeatherData> getDataFromDbWhereStationId(int station_id) {
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        ArrayList<WeatherData> weatherDatas = new ArrayList<>();

        String[] projection = {
                MySQLiteHelper.KEY_PRIMARY_ID,
                MySQLiteHelper.KEY_ID, MySQLiteHelper.KEY_STATION_NAME,
                MySQLiteHelper.KEY_STATION_POSITION, MySQLiteHelper.KEY_TIMESTAMP,
                MySQLiteHelper.KEY_TEMPERATURE, MySQLiteHelper.KEY_PRESSURE,
                MySQLiteHelper.KEY_HUMIDITY
        };
        String whereClause = MySQLiteHelper.KEY_ID + " = ?";
        String[] whereArgs = new String[] {String.valueOf(station_id)};

        String sortOrder =
                MySQLiteHelper.KEY_PRIMARY_ID + " ASC";

        Cursor cursor = db.query(
                MySQLiteHelper.WEATHER_TABLE, projection, whereClause, whereArgs,
                null, null, sortOrder);
        cursor.moveToFirst();

        while (!cursor.isLast() && !cursor.isBeforeFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(MySQLiteHelper.KEY_ID));
            String station_name = cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.KEY_STATION_NAME));
            String station_position = cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.KEY_STATION_POSITION));
            String timestamp = cursor.getString(cursor.getColumnIndexOrThrow(MySQLiteHelper.KEY_TIMESTAMP));
            double temperature = cursor.getDouble(cursor.getColumnIndexOrThrow(MySQLiteHelper.KEY_TEMPERATURE));
            double pressure = cursor.getDouble(cursor.getColumnIndexOrThrow(MySQLiteHelper.KEY_PRESSURE));
            double humidity = cursor.getDouble( cursor.getColumnIndexOrThrow(MySQLiteHelper.KEY_HUMIDITY));
            weatherDatas.add(new WeatherData(id, station_name,station_position,timestamp,temperature,pressure,humidity));

            cursor.moveToNext();
        }

        close();
        return weatherDatas;
    }
}
