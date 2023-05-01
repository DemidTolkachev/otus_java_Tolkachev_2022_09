package com.example.game.objects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.game.Destruction;
import com.example.game.objectCollections.Explosions;

import java.util.Random;

//корректируемая авиационная бомба (КАБ)
public class Gbu {
    private final int xSpeed = 2; //скорость корректировки полёта КАБ по горизонтали
    private final int ySpeed = 6; //скорость полёта КАБ вниз
    private final int xMinDispersion = 10; //минимально возможное случайное отклонение КАБ в одну сторону
    private final int xMaxDispersion = 200; //максимально возможное случайное отклонение КАБ в одну сторону
    private final Random random = new Random();
    private Bitmap image;
    private Explosions explosions;
    private Explosions groundExplosions;
    private Destruction destruction;
    private float x, y; //координаты КАБ
    private float xCorrection, yCorrection; //корректировка координат КАБ в зависимости от её размеров
    private float xTempDestiny; //временная x-координата, куда стремится КАБ
    private boolean readyToExplode; //признак готовности КАБ к взрыву
    private boolean visibility; //признак видимости КАБ
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels; //высота экрана
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels; //ширина экрана

    public Gbu (Bitmap bmp, Explosions explosions, Explosions groundExplosions, Destruction destruction) {
        image = bmp;
        this.explosions = explosions;
        this.groundExplosions = groundExplosions;
        this.destruction = destruction;
        xCorrection = bmp.getWidth() / 2;
        yCorrection = bmp.getHeight() / 2;
        clear();
    }

    //приводит КАБ к первоначальному состоянию
    public void clear() {
        x = -1000;
        y = -1000;
        xTempDestiny = x;
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
            float distance; //расстояние между точкой взрыва и текущим расположением КАБ
            //если КАБ ещё не готова взорваться
            if (readyToExplode == false) {
                setNewDirection();

                //если текущая x-координата центра КАБ больше, чем временная x-координата, куда КАБ стремится
                if (getXCenterCoord() > xTempDestiny) {
                    x -= xSpeed;
                }
                else {
                    x += xSpeed;
                }
                y += ySpeed;
                distance = (float)Math.abs(screenHeight - getYCenterCoord());

                //если расстояние между точкой взрыва и текущим расположением КАБ меньше либо равно расстояния, пролетаемого КАБ за одно обновление
                if (distance <= ySpeed) {
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

    //перемещает КАБ в начальную точку полёта
    public void relocate(float xCoord, float yCoord) {
        float distance; //расстояние между точкой взрыва и текущим расположением КАБ

        readyToExplode = false;
        visibility = true;

        x = xCoord - xCorrection;
        y = yCoord - yCorrection;
        xTempDestiny = xCoord;
        distance = (float)Math.abs(screenHeight - yCoord);

        //если расстояние между точкой взрыва и текущим расположением КАБ меньше либо равно расстояния, пролетаемого КАБ за одно обновление
        if (distance <= ySpeed) {
            readyToExplode = true;
        }
    }

    public boolean getVisibility() {
        return visibility;
    }

    //возвращает трекущую x-координату центра КАБ
    public float getXCenterCoord() {
        return x + xCorrection;
    }

    //возвращает трекущую y-координату центра КАБ
    public float getYCenterCoord() {
        return y + yCorrection;
    }

    //уничтожает КАБ
    public void destroy() {
        explosions.newExplosion(getXCenterCoord(), getYCenterCoord());
        clear();
    }

    //устанавливает новое случайное отклонение КАБ при необходимости
    private void setNewDirection() {
        float xCenterCoord = getXCenterCoord();

        //если КАБ приблизилась к временной x-координате, куда КАБ стремится
        if (Math.abs(xTempDestiny - xCenterCoord) < xSpeed) {
            if (((random.nextInt(2) == 0) && (x > xMinDispersion)) || (xCenterCoord + xCorrection + xMinDispersion >= screenWidth)) {
                //движение КАБ влево
                //определяем новую временную x-координату, куда стремится КАБ
                xTempDestiny = xCenterCoord - (random.nextInt(xMaxDispersion - xMinDispersion) + xMinDispersion);
            }
            else {
                //движение КАБ вправо
                //определяем новую временную x-координату, куда стремится КАБ
                xTempDestiny = xCenterCoord + (random.nextInt(xMaxDispersion - xMinDispersion) + xMinDispersion);
            }

            if (xTempDestiny < 0) {
                xTempDestiny = 0;
            }

            if (xTempDestiny > screenWidth) {
                xTempDestiny = screenWidth;
            }
        }
    }
}
