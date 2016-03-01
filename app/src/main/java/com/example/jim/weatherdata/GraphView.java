package com.example.jim.weatherdata;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Jim on 02/03/2016.
 */
public class GraphView extends View {
    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int w = getWidth();
        int h = getHeight();
        Paint paint = new Paint();
        paint.setARGB(255,255,0,0);
        canvas.drawLine(0,h/2,w,h/2,paint);
    }
}
