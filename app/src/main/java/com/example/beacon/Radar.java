package com.example.beacon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.lang.Math;

/**
 * Created by 밈석 on 2017-01-16.
 */
public class Radar extends View {
    static final double PI =  3.1415926535;

    private float r1, r2;
    private float x, y;
    private float arrowX, arrowY;
    private float viewWidthCenter;
    private float distance;
    private float arrowLength = 100, arrowAngle = 0;
    private double unitAngle = PI/30;

    private Paint pBeacon, pDetected, pText;

    private boolean bDetected = false;


    public Radar(Context context) {
        super(context);
        init();
    }

    public Radar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Radar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init(){
        pBeacon = new Paint();
        pBeacon.setColor(Color.YELLOW);
        pBeacon.setStrokeWidth(3);
        pBeacon.setStyle(Paint.Style.STROKE);

        pDetected = new Paint();
        pDetected.setColor(Color.RED);
        pDetected.setStrokeWidth(10);
        pDetected.setStrokeCap(Paint.Cap.ROUND);

        pText = new Paint();
        pText.setColor(Color.WHITE);
    }

    protected void setRadius(double b1, double b2){
        r1 = (float)meterToPixel(b1);
        r2 = (float)meterToPixel(b2);

        double angle = Math.acos((Math.pow(r1,2) + Math.pow(getWidth(),2) - Math.pow(r2,2))/(2*r1*getWidth()));

        if(angle>0 || angle<PI/2)
            bDetected = true;
        else bDetected = false;


        x = ((float) (r1 * Math.cos(angle)));
        y = ((float) (r1 * Math.sin(angle)));
        distance = ((float) Math.sqrt((Math.pow(r1, 2) + Math.pow(r2, 2))/ 2 - Math.pow(viewWidthCenter, 2)));

        arrowX = viewWidthCenter +(x-viewWidthCenter)*arrowLength/distance;
        arrowY = y*arrowLength/distance;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidthCenter = getWidth()/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);

        if(bDetected) {
            canvas.drawCircle(2, 0, r1, pBeacon);
            canvas.drawCircle(getWidth() - 2, 0, r2, pBeacon);

            canvas.drawCircle(x, y, 10, pDetected);
            canvas.drawLine(viewWidthCenter, 0, arrowX, arrowY, pDetected);
            canvas.drawText(Float.toString( distance), arrowX, arrowY, pText);
            return;
        }
        arrowAngle += unitAngle;

        if(arrowAngle > PI || arrowAngle < 0)
            unitAngle = -unitAngle;

        canvas.drawLine(viewWidthCenter, 0, viewWidthCenter- ((float) (arrowLength * Math.cos(arrowAngle))), ((float) (arrowLength * Math.sin(arrowAngle))), pDetected);



    }
    protected double meterToPixel(double meter){
        //1.4m : 700px
        return meter*700/1.4;
    }
}
