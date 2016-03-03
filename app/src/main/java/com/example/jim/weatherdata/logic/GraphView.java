package com.example.jim.weatherdata.logic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.jim.weatherdata.sqlite.WeatherData;

import java.util.ArrayList;

/**
 * Created by Jim on 02/03/2016.
 */
public class GraphView extends View {
    Canvas canvas;
    int h;
    int w;
    String station_cur = "";
    String timestamp_cur = "";
    String humidity = "";
    String temperature_cur = "";
    ArrayList<WeatherData> weatherDatas = new ArrayList<>();

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        w = getWidth();
        h = getHeight();
        float x_axis = (float)(h / 1.5);
        float y_axis = (float)(w/2);
        Paint paint = new Paint();
        paint.setStrokeWidth(2);
        paint.setColor(Color.parseColor("#92cc72"));
        paint.setTextSize(50);

        canvas.drawLine(0, x_axis, w, x_axis, paint);
        canvas.drawLine(y_axis,0,y_axis,h,paint);
        int index = 0;
        double minTemp = -99;
        double maxTemp = -99;

        Paint infoPaint = new Paint();
        infoPaint.setColor(Color.WHITE);
        infoPaint.setTextSize(35);
        ArrayList<WeatherData> dataList = new ArrayList<>();
        dataList.addAll(weatherDatas);
        for(WeatherData data : dataList){

            double temperature = data.temperature;
            if(minTemp == -99){
                minTemp = temperature;
                maxTemp = temperature;
            }

            if(minTemp > temperature){
                minTemp = temperature;
            }
            if(maxTemp < temperature){
                maxTemp = temperature;
            }
            canvas.drawCircle(w /( weatherDatas.size()) * index, x_axis - ((float) temperature * h / 10), 5, paint);
            station_cur = data.station_name;
            timestamp_cur = data.timestamp;
            humidity = String.valueOf(data.humidity);
            temperature_cur = String.valueOf(data.temperature);

            index++;
        }

        canvas.drawText("Newest data:", 0, 80, paint);

        canvas.drawText("Station: " + String.valueOf(station_cur), 0, 80 + infoPaint.getTextSize(), infoPaint);
        canvas.drawText("Time: " +String.valueOf(timestamp_cur),0, 80 + infoPaint.getTextSize()*2, infoPaint);
        canvas.drawText("Humidity: " +String.valueOf(humidity),0, 80 + infoPaint.getTextSize()*3, infoPaint);
        canvas.drawText("Temperature: " +String.valueOf(temperature_cur),0, 80 + infoPaint.getTextSize()*4, infoPaint);
        canvas.drawText(String.valueOf(maxTemp)+"°C",y_axis, x_axis - ((float) maxTemp * h / 10), infoPaint);
        canvas.drawText(String.valueOf(minTemp)+"°C",y_axis, x_axis - ((float) minTemp * h / 10), infoPaint);

        if(weatherDatas.size() != 0){
            canvas.drawText(weatherDatas.get(0).timestamp, 0,x_axis+50, infoPaint);
            canvas.drawText(weatherDatas.get(weatherDatas.size()-1).timestamp, w-350, x_axis+50, infoPaint);
        }

    }

    public void setWeatherDatas(ArrayList<WeatherData> weatherDatas){
        this.weatherDatas = weatherDatas;
    }
}
