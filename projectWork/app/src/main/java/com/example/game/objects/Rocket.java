package com.example.game.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.example.game.Destruction;
import com.example.game.objectCollections.Explosions;

//обычные ракеты воздух-земля
public class Rocket {
    private final int speed = 10; //скорость полёта ракеты
    private Bitmap image;
    private Explosions explosions;
    private Explosions groundExplosions;
    private Destruction destruction;
    private float x, y; //координаты ракеты
    private float xDestiny, yDestiny; //координаты, куда должна прилететь ракета
    private float xCorrection, yCorrection; //корректировка координат ракеты в зависимости от её размеров
    private float degrees; //градус поворта ракеты
    private float degreesInRad; //угол полёта ракеты в радианах
    private Matrix matrix; //матрица преобразований для ракеты
    private boolean readyToExplode; //признак готовности ракеты к взрыву
    private boolean visibility; //признак видимости ракеты

    public Rocket (Bitmap bmp, Explosions explosions, Explosions groundExplosions, Destruction destruction) {
        image = bmp;
        this.explosions = explosions;
        this.groundExplosions = groundExplosions;
        this.destruction = destruction;
        clear();
        degrees = 0;
        degreesInRad = 0;
        xDestiny = 0;
        yDestiny = 0;
        matrix = new Matrix();
        matrix.setTranslate(x, y); //устанавливает координаты ракеты
        xCorrection = bmp.getWidth() / 2;
        yCorrection = bmp.getHeight() / 2;
    }

    //приводит ракету к первоначальному состоянию
    public void clear() {
        x = -1000;
        y = -1000;
        readyToExplode = false;
        visibility = false;
    }

    public void draw(Canvas canvas) {
        if (visibility) {
            canvas.drawBitmap(image, matrix, null);
        }
    }

    public void update() {
        if (visibility) {
            float distance; //расстояние между точкой взрыва и текущим расположением ракеты
            //если ракета ещё не готова взорваться
            if (readyToExplode == false) {
                x -= (float) Math.sin(degreesInRad) * speed;
                y += (float) Math.cos(degreesInRad) * speed;
                distance = (float)Math.sqrt(Math.pow(xDestiny - getXCenterCoord(), 2) + Math.pow(yDestiny - getYCenterCoord(), 2));

                //если расстояние между точкой взрыва и текущим расположением ракеты меньше либо равно расстояния, пролетаемого ракетой за одно обновление
                if (distance <= speed) {
                    readyToExplode = true;
                }
            }
            else {
                groundExplosions.newExplosion(xDestiny, yDestiny - groundExplosions.getHeight() / 2);
                destruction.doDestruction(xDestiny);
                clear();
            }
            matrix.reset(); //обнуляет расположение ракеты
            matrix.setTranslate(x, y); //устанавливает координаты ракеты
            matrix.postRotate(degrees, x, y); //вращает ракету
        }
    }

    //перемещает ракету в начальную точку полёта
    //xStarting, yStarting - координаты, откуда ракета стартует
    //xDestiny, yDestiny - координаты, куда ракета прилетит
    public void relocate(float xStarting, float yStarting, float xDestiny, float yDestiny) {
        float distance; //расстояние между точкой взрыва и текущим расположением ракеты
        this.xDestiny = xDestiny;
        this.yDestiny = yDestiny;

        if (xDestiny == xStarting) {
            degrees = 0;
        }
        else {
            if (xDestiny > xStarting) {
                degrees = (float) Math.toDegrees(Math.atan((yDestiny - yStarting) / (xDestiny - xStarting)) - Math.PI / 2);
            }
            else {
                degrees = (float) Math.toDegrees(Math.atan((yDestiny - yStarting) / (xDestiny - xStarting)) + Math.PI / 2);
            }
        }
        degreesInRad = degrees * (float)Math.PI / 180;

        readyToExplode = false;
        visibility = true;

        x = xStarting - (float)Math.cos(degreesInRad) * xCorrection + (float)Math.sin(degreesInRad) * yCorrection;
        y = yStarting - (float)Math.cos(degreesInRad) * yCorrection - (float)Math.sin(degreesInRad) * xCorrection;
        distance = (float)Math.sqrt(Math.pow(xDestiny - xStarting, 2) + Math.pow(yDestiny - yStarting, 2));

        //если расстояние между точкой взрыва и текущим расположением ракеты меньше либо равно расстояния, пролетаемого ракетой за одно обновление
        if (distance <= speed) {
            readyToExplode = true;
        }

        matrix.reset(); //обнуляет расположение ракеты
        matrix.setTranslate(x, y); //устанавливает координаты ракеты
        matrix.postRotate(degrees, x, y); //вращает ракету
    }

    public boolean getVisibility() {
        return visibility;
    }

    //возвращает трекущую x-координату центра ракеты
    public float getXCenterCoord() {
        return x + (float)Math.cos(degreesInRad) * xCorrection - (float)Math.sin(degreesInRad) * yCorrection;
    }

    //возвращает трекущую y-координату центра ракеты
    public float getYCenterCoord() {
        return y + (float)Math.cos(degreesInRad) * yCorrection + (float)Math.sin(degreesInRad) * xCorrection;
    }

    //уничтожает ракету
    public void destroy() {
        explosions.newExplosion(getXCenterCoord(), getYCenterCoord());
        clear();
    }

    //возвращает угол полёта ракеты в радианах
    public float getDegreesInRad() {
        return degreesInRad;
    }
}
