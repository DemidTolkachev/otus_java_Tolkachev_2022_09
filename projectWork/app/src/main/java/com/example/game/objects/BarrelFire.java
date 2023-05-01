package com.example.game.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

//огонь выстрела зенитной пушки
public class BarrelFire {
    private final int maxTransparency = 255; //максимальное значение непрозрачности
    private final int transparencyDelta = 10; //изменение значения прозрачности за одно обновление
    private final Paint transparentPaint = new Paint();
    private Bitmap image;
    private float x, y; //координаты выстрела
    private float xCorrection, yCorrection; //корректировка координат огня от выстрела в зависимости от его размеров
    private int transparency; //значение текущей прозрачности огня от выстрела
    private Matrix matrix; //матрица преобразований для огня от выстрела
    private boolean visibility; //признак видимости объекта

    public BarrelFire (Bitmap bmp) {
        image = bmp;
        matrix = new Matrix();
        xCorrection = bmp.getWidth() / 2;
        yCorrection = bmp.getHeight() / 2;
        clear();
        matrix.setTranslate(x, y); //устанавливает координаты огня от выстрела
    }

    //приводит огонь выстрела зенитной пушки к первоначальному состоянию
    public void clear() {
        x = -1000;
        y = -1000;
        transparency = maxTransparency;
        visibility = false;
    }

    public void draw(Canvas canvas) {
        if (visibility) {
            transparentPaint.setAlpha(transparency);
            canvas.drawBitmap(image, matrix, transparentPaint);
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

    //перемещает огонь выстрела зенитной пушки в нужную точку
    //barrelBaseX, barrelBaseY - координаты основания ствола пушки, относительно которых осуществляется поворот
    //barrelHeight - высота ствола пушки
    //degrees - градус поворта ствола пушки
    public void relocate(float barrelBaseX, float barrelBaseY, int barrelHeight, float degrees) {
        float degreesInRad = (90 - degrees) * (float)Math.PI / 180;

        transparency = maxTransparency;
        visibility = true;

        x = barrelBaseX + (float)Math.cos(degreesInRad) * (barrelHeight + yCorrection) - (float)Math.sin(degreesInRad) * xCorrection;
        y = barrelBaseY - (float)Math.sin(degreesInRad) * (barrelHeight + yCorrection) - (float)Math.cos(degreesInRad) * xCorrection;

        matrix.reset(); //обнуляет расположение огня от выстрела
        matrix.setTranslate(x, y); //устанавливает координаты огня от выстрела
        matrix.postRotate(degrees, x, y); //вращает огонь от выстрела
    }

    public boolean getVisibility() {
        return visibility;
    }
}
