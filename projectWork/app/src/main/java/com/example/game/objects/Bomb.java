package com.example.game.objects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.game.Destruction;
import com.example.game.objectCollections.Explosions;

//свободнопадающая бомба
public class Bomb {
    private final int speed = 5; //скорость полёта бомбы
    private Bitmap image;
    private Explosions explosions;
    private Explosions groundExplosions;
    private Destruction destruction;
    private float x, y; //координаты бомбы
    private float xCorrection, yCorrection; //корректировка координат бомбы в зависимости от её размеров
    private boolean readyToExplode; //признак готовности бомбы к взрыву
    private boolean visibility; //признак видимости бомбы
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels; //высота экрана

    public Bomb (Bitmap bmp, Explosions explosions, Explosions groundExplosions, Destruction destruction) {
        image = bmp;
        this.explosions = explosions;
        this.groundExplosions = groundExplosions;
        this.destruction = destruction;
        xCorrection = bmp.getWidth() / 2;
        yCorrection = bmp.getHeight() / 2;
        clear();
    }

    //приводит бомбу к первоначальному состоянию
    public void clear() {
        x = -1000;
        y = -1000;
        readyToExplode = false;
        visibility = false;
    }

    public void draw(Canvas canvas) {
        if (visibility) {
            canvas.drawBitmap(image, x, y, null);
        }
    }

    public void update() {
        if (visibility) {
            float distance; //расстояние между точкой взрыва и текущим расположением бомбы
            //если бомба ещё не готова взорваться
            if (readyToExplode == false) {
                y += speed;
                distance = (float)Math.abs(screenHeight - getYCenterCoord());

                //если расстояние между точкой взрыва и текущим расположением бомбы меньше либо равно расстояния, пролетаемого бомбой за одно обновление
                if (distance <= speed) {
                    readyToExplode = true;
                }
            }
            else {
                groundExplosions.newExplosion(getXCenterCoord(), screenHeight - groundExplosions.getHeight() / 2);
                destruction.doDestruction(getXCenterCoord());
                clear();
            }
        }
    }

    //перемещает бомбу в начальную точку полёта
    public void relocate(float xCoord, float yCoord) {
        float distance; //расстояние между точкой взрыва и текущим расположением бомбы

        readyToExplode = false;
        visibility = true;

        x = xCoord - xCorrection;
        y = yCoord - yCorrection;
        distance = (float)Math.abs(screenHeight - yCoord);

        //если расстояние между точкой взрыва и текущим расположением бомбы меньше либо равно расстояния, пролетаемого бомбой за одно обновление
        if (distance <= speed) {
            readyToExplode = true;
        }
    }

    public boolean getVisibility() {
        return visibility;
    }

    //возвращает трекущую x-координату центра бомбы
    public float getXCenterCoord() {
        return x + xCorrection;
    }

    //возвращает трекущую y-координату центра бомбы
    public float getYCenterCoord() {
        return y + yCorrection;
    }

    //уничтожает бомбу
    public void destroy() {
        explosions.newExplosion(getXCenterCoord(), getYCenterCoord());
        clear();
    }
}
