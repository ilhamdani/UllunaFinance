package com.ulluna.ullunafinance;

import android.graphics.drawable.Drawable;

import java.util.Random;

/**
 * Created by Tomasz on 1/29/2015.
 */
public class Stock {
    public float value;
    public float change;
    public String name;
    public String description = "For now there is no description";
    public Drawable sIcon;

    public Stock(float value, float change, String name, String description, Drawable sIcon){
        this.value = value;
        this.change = change;
        this.name = name;
        this.description = description;
        this.sIcon = sIcon;
    }

    public Stock(float value, float change, String name, Drawable sIcon){
        this.value = value;
        this.change = change;
        this.name = name;
        this.sIcon = sIcon;
    }

    public static float[] modifyValue(float currentValue){
        Random rand = new Random();
        float curCopy = currentValue;
        float result[] = new float[2];
        float change = rand.nextFloat();
        change-=0.5;
        if(Math.abs(change)<0.45)
            change/=10;

        currentValue *=change+1;
        currentValue = (float)Math.round(currentValue * 1000) / 1000;
        if (currentValue<=1 || currentValue>=30)
            currentValue=10;
        result[0]=currentValue;
        result[1]=(currentValue - curCopy)/currentValue*100;
        result[1]= roundPrice(result[1]);
        return result;
    }

    public static float roundPrice(float number){
        number = (float)Math.round(number*100)/100;
        return number;
    }

    public static float roundPrice2(float number){
        number = (float)Math.round(number*1000)/1000;
        return number;
    }
}
