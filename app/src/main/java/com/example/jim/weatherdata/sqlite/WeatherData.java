package com.example.jim.weatherdata.sqlite;

/**
 * Created by jim on 02.03.16.
 */
public class WeatherData {
    public int id;
    public String station_name;
    public String station_position;
    public String timestamp;
    public double temperature;
    public double pressure;
    public double humidity;

    public WeatherData(int id, String station_name, String station_position, String timestamp, double temperature, double pressure, double humidity) {
        this.id = id;
        this.station_name = station_name;
        this.station_position = station_position;
        this.timestamp = timestamp;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
    }
}