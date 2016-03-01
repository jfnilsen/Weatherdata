package com.example.jim.weatherdata;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jim on 29/02/2016.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "weather_data.db";
    private static final int DATABASE_VERSION = 2;
    public static final String WEATHER_TABLE = "WeatherTable";

    public static final String KEY_PRIMARY_ID = "primary_id" ;
    public static final String KEY_ID = "id";
    public static final String KEY_STATION_NAME = "station_name";
    public static final String KEY_STATION_POSITION = "station_position";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_TEMPERATURE = "temperature";
    public static final String KEY_PRESSURE = "pressure";
    public static final String KEY_HUMIDITY = "humidity";

    private static final String DATABASE_CREATE = "create table "
            + WEATHER_TABLE + " (" + KEY_PRIMARY_ID +" integer primary key autoincrement, " + KEY_ID
            + " integer, " + KEY_STATION_NAME + " text,  " + KEY_STATION_POSITION + " text, "
            + KEY_TIMESTAMP + " text, " + KEY_TEMPERATURE + " integer, " + KEY_PRESSURE + " integer, "
            + KEY_HUMIDITY + " integer " + ");";


    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + WEATHER_TABLE);
        onCreate(db);
    }
}
