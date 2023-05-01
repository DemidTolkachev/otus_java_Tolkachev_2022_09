package com.example.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

//зенитная пушка
public class AntiAircraftGun {
    private final int barrelHeightCorr = 7; //поправка по высоте ствола пушки, чтобы он сливался с основанием
    private Bitmap mounting; //основание пушки
    private Bitmap barrel; //ствол пушки
    private float mountingX, mountingY; //координаты расположения основания пушки
    private float barrelX, barrelY; //координаты расположения ствола пушки
    private float barrelBaseX, barrelBaseY; //координаты основания ствола пушки, относительно которых осуществляется поворот
    private int barrelWidth, barrelHeight; //ширирна и высота ствола пушки
    private float degrees; //градус поворта ствола пушки
    private Matrix matrix; //матрица преобразований для ствола пушки
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels; //ширина экрана
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels; //высота экрана

    public AntiAircraftGun (Bitmap mounting, Bitmap barrel) {
        this.mounting = mounting;
        this.barrel = barrel;
        barrelWidth = barrel.getWidth();
        barrelHeight = barrel.getHeight();
        mountingX = screenWidth / 2 - mounting.getWidth() / 2;
        mountingY = screenHeight - mounting.getHeight();
        barrelX = screenWidth / 2 - barrelWidth / 2;
        barrelY = screenHeight - mounting.getHeight() - barrelHeight + barrelHeightCorr;
        barrelBaseX = barrelX + barrelWidth / 2;
        barrelBaseY = barrelY + barrelHeight;
        matrix = new Matrix();
        matrix.setTranslate(barrelX, barrelY); //устанавливает координаты ствола пушки
    }

    // рисует основание и пушку
    public void draw(Canvas canvas) {
        canvas.drawBitmap(mounting, mountingX, mountingY, null);
        canvas.drawBitmap(barrel, matrix, null);
    }

    //вращает пушку по направлению к точке выстрела
    public void rotateBarrel(float x, float y) {
        if (x == barrelBaseX) {
            degrees = 0;
        }
        else {
            if (x > barrelBaseX) {
                degrees = (float) Math.toDegrees(Math.atan((y - barrelBaseY) / (x - barrelBaseX)) + Math.PI / 2);
            }
            else {
                degrees = (float) Math.toDegrees(Math.atan((y - barrelBaseY) / (x - barrelBaseX)) - Math.PI / 2);
            }
        }
        matrix.reset(); //обнуляет расположение ствола пушки
        matrix.setTranslate(barrelX, barrelY); //устанавливает координаты ствола пушки
        matrix.postRotate(degrees, barrelBaseX, barrelBaseY); //вращает ствол пушки
    }

    //определяет, находится ли x-координата внутри основания зенитной пушки
    public boolean xInMounting(float x) {
        if ((x >= mountingX) && (x < mountingX + mounting.getWidth())) {
            return true;
        }

        return false;
    }

    public float getBarrelBaseX() {
        return barrelBaseX;
    }

    public float getBarrelBaseY() {
        return barrelBaseY;
    }

    public int getBarrelHeight() {
        return barrelHeight;
    }

    public float getDegrees() {
        return degrees;
    }

    public int getMountingWidth() {
        return mounting.getWidth();
    }
}
