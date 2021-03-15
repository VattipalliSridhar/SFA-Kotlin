package com.apps.sfaapp.view.base;

import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.apps.sfaapp.view.ui.viewclass.ToiletCustomViewClass;


/**
 * Created by Sridhar.
 */

public class ToiletCustomViewAnimation extends Animation
{
    private ToiletCustomViewClass customViewClass;
    private float oldAngle;
    private float newAngle;
    private String clean_count;
    private String clean_total;

    public ToiletCustomViewAnimation(ToiletCustomViewClass customViewClass0, float newAngle, String count, String total)
    {
        customViewClass=customViewClass0;
        this.oldAngle = customViewClass.getAngle();
        this.newAngle = newAngle;

        clean_count = count;
        clean_total = total;

    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t)
    {
        super.applyTransformation(interpolatedTime, t);
        float angle =  (oldAngle + ((newAngle - oldAngle) * interpolatedTime));
        customViewClass.setAngle(angle,clean_count,clean_total);
        customViewClass.requestLayout();
    }
}
