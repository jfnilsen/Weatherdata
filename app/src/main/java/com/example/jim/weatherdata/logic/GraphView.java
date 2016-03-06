package com.example.jim.weatherdata.logic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.jim.weatherdata.sqlite.WeatherData;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Jim on 02/03/2016.
 */
public class GraphView extends View {
    int h;
    int w;
    float topbuffer = 200;
    String station_cur = "";
    String timestamp_cur = "";
    String humidity_cur = "";
    String temperature_cur = "";
    String pressure_cur = "";
    ArrayList<WeatherData> weatherDatas = new ArrayList<>();
    public static final int TEMPERATURE = 0;
    public static final int HUMIDITY = 1;
    public static final int PRESSURE = 2;

    private int type = TEMPERATURE;

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setGraphType(int type) {
        this.type = type;
    }

    public void setWeatherDatas(ArrayList<WeatherData> weatherDatas){
        this.weatherDatas = weatherDatas;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        w = canvas.getWidth();
        h = canvas.getHeight();
        float x_axis = (float)(h /2);
        float y_axis = (float)(w/2);
        Paint paint = new Paint();
        paint.setStrokeWidth(2);
        paint.setColor(Color.parseColor("#92cc72"));
        paint.setTextSize(50);

        canvas.drawLine(0, x_axis + topbuffer, w, x_axis + topbuffer, paint);
        canvas.drawLine(y_axis, 0, y_axis, h, paint);

        switch (type){
            case TEMPERATURE:
                drawTemperatureGraph(canvas, x_axis, y_axis, paint);
                break;
            case HUMIDITY:
                drawHumidityGraph(canvas, x_axis, y_axis, paint);
                break;
            case PRESSURE:
                drawPressureGraph(canvas,x_axis,y_axis,paint);
                break;
        }

    }

    private void drawTemperatureGraph(Canvas canvas, float x_axis, float y_axis, Paint paint) {

        int index = 0;
        double minTemp = 0;
        double maxTemp = 0;

        Paint infoPaint = new Paint();
        infoPaint.setColor(Color.WHITE);
        infoPaint.setTextSize(35);
        ArrayList<WeatherData> dataList = new ArrayList<>();
        dataList.addAll(weatherDatas);
        for(WeatherData data : dataList){
            ArrayList sortedList = new ArrayList(dataList);
            Collections.sort(sortedList,WeatherData.Order.ByTemperature);
            maxTemp = ((WeatherData)sortedList.get(sortedList.size()-1)).temperature;
            minTemp = ((WeatherData)sortedList.get(0)).temperature;
            double temperature = data.temperature;

            canvas.drawCircle(w /( weatherDatas.size()) * index, x_axis - ((float) temperature * x_axis / (float)maxTemp) + topbuffer, 5, paint);
            station_cur = data.station_name;
            timestamp_cur = data.timestamp;
            humidity_cur = String.valueOf(data.humidity);
            temperature_cur = String.valueOf(data.temperature);
            pressure_cur = String.valueOf(data.humidity);

            index++;
        }

        canvas.drawText("Newest data:", 0, 80, paint);

        canvas.drawText("Station: " + String.valueOf(station_cur), 0, 80 + infoPaint.getTextSize(), infoPaint);
        canvas.drawText("Time: " +String.valueOf(timestamp_cur),0, 80 + infoPaint.getTextSize()*2, infoPaint);
        canvas.drawText("Humidity: " +String.valueOf(humidity_cur),0, 80 + infoPaint.getTextSize()*3, infoPaint);
        canvas.drawText("Temperature: " +String.valueOf(temperature_cur),0, 80 + infoPaint.getTextSize() * 4, infoPaint);
        canvas.drawText("Pressure: " + String.valueOf(pressure_cur), 0, 80 + infoPaint.getTextSize() * 5, infoPaint);

        canvas.drawText(String.valueOf(maxTemp)+"°C",y_axis, x_axis - ((float) maxTemp * x_axis / (float)maxTemp) + topbuffer , infoPaint);
        canvas.drawText(String.valueOf(minTemp)+"°C",y_axis, x_axis - ((float) minTemp * x_axis / (float)maxTemp) + topbuffer, infoPaint);

        if(weatherDatas.size() != 0){
            canvas.drawText(weatherDatas.get(0).timestamp, 0, x_axis + infoPaint.getTextSize() + topbuffer, infoPaint);
            canvas.drawText(weatherDatas.get(weatherDatas.size()-1).timestamp, w-350, x_axis + infoPaint.getTextSize() + topbuffer, infoPaint);
        }

    }

    private void drawHumidityGraph(Canvas canvas, float x_axis, float y_axis, Paint paint) {
        int index = 0;
        double minHumidity = 0;
        double maxHumidity = 0;

        Paint infoPaint = new Paint();
        infoPaint.setColor(Color.WHITE);
        infoPaint.setTextSize(35);
        ArrayList<WeatherData> dataList = new ArrayList<>();
        dataList.addAll(weatherDatas);
        for (WeatherData data : dataList) {
            ArrayList sortedList = new ArrayList(dataList);
            Collections.sort(sortedList, WeatherData.Order.ByHumidity);
            maxHumidity = ((WeatherData) sortedList.get(sortedList.size() - 1)).humidity;
            minHumidity = ((WeatherData) sortedList.get(0)).humidity;
            double humidity = data.humidity;

            canvas.drawCircle(w / (weatherDatas.size()) * index, x_axis - ((float) humidity * x_axis / (float) maxHumidity) + topbuffer, 5, paint);
            station_cur = data.station_name;
            timestamp_cur = data.timestamp;
            humidity_cur = String.valueOf(data.humidity);
            temperature_cur = String.valueOf(data.temperature);
            pressure_cur = String.valueOf(data.humidity);

            index++;
        }

        canvas.drawText("Newest data:", 0, 80, paint);

        canvas.drawText("Station: " + String.valueOf(station_cur), 0, 80 + infoPaint.getTextSize(), infoPaint);
        canvas.drawText("Time: " + String.valueOf(timestamp_cur), 0, 80 + infoPaint.getTextSize() * 2, infoPaint);
        canvas.drawText("Humidity: " + String.valueOf(humidity_cur), 0, 80 + infoPaint.getTextSize() * 3, infoPaint);
        canvas.drawText("Temperature: " + String.valueOf(temperature_cur), 0, 80 + infoPaint.getTextSize() * 4, infoPaint);
        canvas.drawText("Pressure: " + String.valueOf(pressure_cur), 0, 80 + infoPaint.getTextSize() * 5, infoPaint);

        canvas.drawText(String.valueOf(maxHumidity) + "%", y_axis, x_axis - ((float) maxHumidity * x_axis / (float) maxHumidity) + topbuffer, infoPaint);
        canvas.drawText(String.valueOf(minHumidity) + "%", y_axis, x_axis - ((float) minHumidity * x_axis / (float) maxHumidity) + topbuffer, infoPaint);

        if (weatherDatas.size() != 0) {
            canvas.drawText(weatherDatas.get(0).timestamp, 0, x_axis + infoPaint.getTextSize() + topbuffer, infoPaint);
            canvas.drawText(weatherDatas.get(weatherDatas.size() - 1).timestamp, w - 350, x_axis + infoPaint.getTextSize() + topbuffer, infoPaint);
        }
    }
    private void drawPressureGraph(Canvas canvas, float x_axis, float y_axis, Paint paint) {
        int index = 0;
        double minHumidity = 0;
        double maxPressure = 0;

        Paint infoPaint = new Paint();
        infoPaint.setColor(Color.WHITE);
        infoPaint.setTextSize(35);
        ArrayList<WeatherData> dataList = new ArrayList<>();
        dataList.addAll(weatherDatas);
        for (WeatherData data : dataList) {
            ArrayList sortedList = new ArrayList(dataList);
            Collections.sort(sortedList, WeatherData.Order.ByPressure);
            maxPressure = ((WeatherData) sortedList.get(sortedList.size() - 1)).pressure;
            minHumidity = ((WeatherData) sortedList.get(0)).pressure;
            double pressure = data.pressure;

            canvas.drawCircle(w / (weatherDatas.size()) * index, x_axis - ((float) pressure * x_axis / (float) maxPressure) + topbuffer, 5, paint);
            station_cur = data.station_name;
            timestamp_cur = data.timestamp;
            humidity_cur = String.valueOf(data.humidity);
            temperature_cur = String.valueOf(data.temperature);
            pressure_cur = String.valueOf(data.humidity);

            index++;
        }

        canvas.drawText("Newest data:", 0, 80, paint);

        canvas.drawText("Station: " + String.valueOf(station_cur), 0, 80 + infoPaint.getTextSize(), infoPaint);
        canvas.drawText("Time: " + String.valueOf(timestamp_cur), 0, 80 + infoPaint.getTextSize() * 2, infoPaint);
        canvas.drawText("Humidity: " + String.valueOf(humidity_cur), 0, 80 + infoPaint.getTextSize() * 3, infoPaint);
        canvas.drawText("Temperature: " + String.valueOf(temperature_cur), 0, 80 + infoPaint.getTextSize() * 4, infoPaint);
        canvas.drawText("Pressure: " + String.valueOf(pressure_cur), 0, 80 + infoPaint.getTextSize() * 5, infoPaint);

        canvas.drawText(String.valueOf(maxPressure) + "mbar", y_axis, x_axis - ((float) maxPressure * x_axis / (float) maxPressure) + topbuffer, infoPaint);
        canvas.drawText(String.valueOf(minHumidity) + "mbar", y_axis, x_axis - ((float) minHumidity * x_axis / (float) maxPressure) + topbuffer, infoPaint);

        if (weatherDatas.size() != 0) {
            canvas.drawText(weatherDatas.get(0).timestamp, 0, x_axis + infoPaint.getTextSize() + topbuffer, infoPaint);
            canvas.drawText(weatherDatas.get(weatherDatas.size() - 1).timestamp, w - 350, x_axis + infoPaint.getTextSize() + topbuffer, infoPaint);
        }
    }

}
