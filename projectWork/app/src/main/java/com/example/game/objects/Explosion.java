package com.example.game.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

//взрыв
public class Explosion {
    private final int maxTransparency = 255; //максимальное значение непрозрачности
    private final int transparencyDelta = 5; //изменение значения прозрачности за одно обновление
    private final Paint transparentPaint = new Paint();
    private Bitmap image;
    private float x, y;
    private float xCorrection, yCorrection;
    private int transparency;
    private boolean visibility;

    public Explosion (Bitmap bmp) {
        image = bmp;
        xCorrection = bmp.getWidth() / 2;
        yCorrection = bmp.getHeight() / 2;
        clear();
    }

    //приводит взрыв к первоначальному состоянию
    public void clear() {
        x = -1000;
        y = -1000;
        transparency = maxTransparency;
        visibility = false;
    }

    public void draw(Canvas canvas) {
        if (visibility) {
            transparentPaint.setAlpha(transparency);
            canvas.drawBitmap(image, x, y, transparentPaint);
        }
    }

    public void update() {
        if (visibility) {
            if (transparency <= 0) {
                visibility = false;
            }
            else {
                transparency -= transparencyDelta;

                if (transparency < 0){
                    transparency = 0;
                }
            }
        }
    }

    public void relocate(float xCoord, float yCoord) {
        transparency = maxTransparency;
        visibility = true;
        x = xCoord - xCorrection;
        y = yCoord - yCorrection;
    }

    public boolean getVisibility() {
        return visibility;
    }
}
