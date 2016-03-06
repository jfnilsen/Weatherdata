package com.example.jim.weatherdata.sqlite;

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

    private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + WEATHER_TABLE + " (" + KEY_PRIMARY_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_ID
            + " INTEGER, " + KEY_STATION_NAME + " TEXT,  " + KEY_STATION_POSITION + " TEXT, "
            + KEY_TIMESTAMP + " TEXT, " + KEY_TEMPERATURE + " INTEGER, " + KEY_PRESSURE + " INTEGER, "
            + KEY_HUMIDITY + " INTEGER " + ");";


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
