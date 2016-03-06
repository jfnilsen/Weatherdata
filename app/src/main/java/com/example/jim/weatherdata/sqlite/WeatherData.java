package com.example.jim.weatherdata.sqlite;

import java.util.Comparator;
import java.util.Objects;

/**
 * Created by jim on 02.03.16.
 */
public class WeatherData {
    public int id;
    public String station_name;
    public String station_position;
    public String timestamp;
    public final double temperature;
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

    public enum Order implements Comparator {
        ByTemperature(){
            @Override
            public int compare(Object lhs, Object rhs) {
                return Double.compare(((WeatherData)lhs).temperature,((WeatherData)rhs).temperature);
            }
        },
        ByHumidity(){
            @Override
            public int compare(Object lhs, Object rhs) {
                return Double.compare(((WeatherData)lhs).humidity, ((WeatherData)rhs).humidity);
            }
        },
        ByPressure(){
            @Override
            public int compare(Object lhs, Object rhs) {
                return Double.compare(((WeatherData)lhs).pressure, ((WeatherData)rhs).pressure);
            }
        }
    }
}
