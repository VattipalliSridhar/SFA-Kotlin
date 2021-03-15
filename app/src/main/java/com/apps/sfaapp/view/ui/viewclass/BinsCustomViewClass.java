package com.apps.sfaapp.view.ui.viewclass;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;


public class BinsCustomViewClass extends View {
    private int canvas_width, canvas_height;
    private Paint arc1_paint, arc2_paint, txt_paint;
    private RectF oval1 = new RectF();
    private RectF oval2 = new RectF();
    private float sweepAngle;
    private String clean_count;
    private String total_count;
    private int size;
    private String txt_color;


    public BinsCustomViewClass(Context context) {
        super(context);
        //super(context);
        arc1_paint = new Paint();
        arc1_paint.setColor(Color.parseColor("#ffffff"));
        arc1_paint.setStyle(Paint.Style.STROKE);
        arc1_paint.setStrokeCap(Paint.Cap.ROUND);
        arc1_paint.setDither(true);
        arc1_paint.setAntiAlias(true);


        arc2_paint = new Paint();
        arc2_paint.setColor(Color.parseColor("#ffffff"));
        arc2_paint.setAntiAlias(true);
        arc2_paint.setDither(true);
        //arc2_paint.setStrokeWidth(10);
        arc2_paint.setStrokeCap(Paint.Cap.ROUND);
        arc2_paint.setStyle(Paint.Style.STROKE);


        txt_color = "#f28019";

        size = 20;

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        canvas_width = w;
        canvas_height = h;


        arc1_paint.setStrokeWidth(canvas_width / 32);
        arc2_paint.setStrokeWidth(canvas_width / 20);

        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        oval1.set(canvas_width / size, canvas_width / size, canvas_height - canvas_width / size, canvas_width - canvas_width / size);
        canvas.drawArc(oval1, 220, 360, false, arc1_paint);

        oval2.set(canvas_width / size, canvas_width / size, canvas_height - canvas_width / size, canvas_width - canvas_width / size);
        canvas.drawArc(oval2, 220, sweepAngle, false, arc2_paint);

        txt_paint = new Paint();
        txt_paint.setColor(Color.parseColor(txt_color));
        txt_paint.setAntiAlias(true);
        txt_paint.setDither(true);
        txt_paint.setTextSize(canvas_width / 6);
        txt_paint.setTextAlign(Paint.Align.CENTER);

        canvas.drawText("" + /*((int) ((sweepAngle / 360) * 100)) + */(total_count), canvas_width / 2 + canvas_width / 40, canvas_height / 2 + canvas_height / 30, txt_paint);
    }

    public float getAngle() {
        return sweepAngle;
    }

    public void setAngle(float angle, String clean, String total) {
        this.sweepAngle = angle;
        clean_count = clean;
        total_count = total;

    }
}
