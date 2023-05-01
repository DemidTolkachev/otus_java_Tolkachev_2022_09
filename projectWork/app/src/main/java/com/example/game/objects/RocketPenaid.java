package com.example.game.objects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.example.game.Destruction;
import com.example.game.objectCollections.Explosions;

import java.util.Random;

//ракеты с комплексом средств преодоления противоракетной обороны (КСП ПРО)
public class RocketPenaid {
    private final int speed = 10; //максимальная скорость полёта ракеты c КСП ПРО float maxSpeed
    private final int xMinDispersion = 20; //минимально возможное случайное отклонение ракеты c КСП ПРО по x-кординате
    private final int xMaxDispersion = 280; //максимально возможное случайное отклонение ракеты c КСП ПРО по x-кординате
    private final int yMinDispersion = 105; //минимально возможное случайное отклонение ракеты c КСП ПРО по y-кординате
    private final int yMaxDispersion = 420; //максимально возможное случайное отклонение ракеты c КСП ПРО по y-кординате
    private final float maxDegreesDelta = 6; //максимально возможное изменение угла за одно обновление
    private final Random random = new Random();
    private Bitmap image;
    private Explosions explosions;
    private Explosions groundExplosions;
    private Destruction destruction;
    private float x, y; //координаты ракеты c КСП ПРО
    private float xTempDestiny, yTempDestiny; //временная координаты, куда стремится ракета c КСП ПРО
    private float xCorrection, yCorrection; //корректировка координат ракеты c КСП ПРО в зависимости от её размеров
    private float degrees; //градус поворта ракеты c КСП ПРО
    private float degreesInRad; //угол полёта ракеты c КСП ПРО в радианах
    private float tempDestinyDegrees; //временный целевой градус поворта ракеты c КСП ПРО
    private Matrix matrix; //матрица преобразований для ракеты c КСП ПРО
    private boolean readyToExplode; //признак готовности ракеты c КСП ПРО к взрыву
    private boolean visibility; //признак видимости ракеты c КСП ПРО
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels; //высота экрана
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels; //ширина экрана

    public RocketPenaid(Bitmap bmp, Explosions explosions, Explosions groundExplosions, Destruction destruction) {
        image = bmp;
        this.explosions = explosions;
        this.groundExplosions = groundExplosions;
        this.destruction = destruction;
        clear();
        degrees = 0;
        degreesInRad = 0;
        tempDestinyDegrees = 0;
        matrix = new Matrix();
        matrix.setTranslate(x, y); //устанавливает координаты ракеты c КСП ПРО
        xCorrection = bmp.getWidth() / 2;
        yCorrection = bmp.getHeight() / 2;
    }

    //приводит ракету c КСП ПРО к первоначальному состоянию
    public void clear() {
        x = -1000;
        y = -1000;
        xTempDestiny = x;
        yTempDestiny = y;
        readyToExplode = false;
        visibility = false;
    }

    public void draw(Canvas canvas) {
        if (visibility) {
            canvas.drawBitmap(image, matrix, null);
            /*Paint redPaint = new Paint();
            redPaint.setColor(Color.RED);
            Paint greenPaint = new Paint();
            greenPaint.setColor(Color.GREEN);
            canvas.drawCircle(xTempDestiny, yTempDestiny, 3, redPaint);
            canvas.drawCircle(getXCenterCoord(), getYCenterCoord(), 3, greenPaint);*/
        }
    }

    public void update() {
        if (visibility) {
            //if ((getXCenterCoord() < 0) || (getXCenterCoord() > screenWidth) || (getYCenterCoord() < 0) || (getYCenterCoord() > screenHeight)) {
                //System.out.println("x, y: " + getXCenterCoord() + ", " + getYCenterCoord());
            //}
            float distance; //расстояние между точкой взрыва и текущим расположением ракеты c КСП ПРО
            //если ракета c КСП ПРО ещё не готова взорваться
            if (readyToExplode == false) {
                setNewDirection();
                x -= (float) Math.sin(degreesInRad) * speed;
                y += (float) Math.cos(degreesInRad) * speed;
                setNewTempDestinyDegrees();
                distance = (float) Math.abs(screenHeight - getYCenterCoord());

                //если расстояние между точкой взрыва и текущим расположением ракеты c КСП ПРО меньше либо равно расстояния, пролетаемого ракетой за одно обновление
                if (distance <= speed) {
                    readyToExplode = true;
                }
            } else {
                groundExplosions.newExplosion(getXCenterCoord(), screenHeight - groundExplosions.getHeight() / 2);
                destruction.doDestruction(getXCenterCoord());
                clear();
            }
            matrix.reset(); //обнуляет расположение ракеты c КСП ПРО
            matrix.setTranslate(x, y); //устанавливает координаты ракеты c КСП ПРО
            matrix.postRotate(degrees, x, y); //вращает ракету c КСП ПРО
        }
    }

    //перемещает ракету c КСП ПРО в начальную точку полёта
    //xStarting, yStarting - координаты, откуда ракета c КСП ПРО стартует
    public void relocate(float xStarting, float yStarting) {
        float distance; //расстояние между точкой взрыва и текущим расположением ракеты c КСП ПРО

        degrees = 0;
        degreesInRad = degrees * (float) Math.PI / 180;
        tempDestinyDegrees = 0;
        readyToExplode = false;
        visibility = true;

        x = xStarting - xCorrection;
        y = yStarting - yCorrection;
        xTempDestiny = xStarting;
        yTempDestiny = yStarting;
        distance = (float) Math.abs(screenHeight - yStarting);

        //если расстояние между точкой взрыва и текущим расположением ракеты c КСП ПРО меньше либо равно расстояния, пролетаемого ракетой за одно обновление
        if (distance <= speed) {
            readyToExplode = true;
        }

        matrix.reset(); //обнуляет расположение ракеты c КСП ПРО
        matrix.setTranslate(x, y); //устанавливает координаты ракеты c КСП ПРО
        matrix.postRotate(degrees, x, y); //вращает ракету c КСП ПРО
    }

    public boolean getVisibility() {
        return visibility;
    }

    //возвращает трекущую x-координату центра ракеты c КСП ПРО
    public float getXCenterCoord() {
        return x + (float)Math.cos(degreesInRad) * xCorrection - (float)Math.sin(degreesInRad) * yCorrection;
    }

    //возвращает трекущую y-координату центра ракеты c КСП ПРО
    public float getYCenterCoord() {
        return y + (float)Math.cos(degreesInRad) * yCorrection + (float)Math.sin(degreesInRad) * xCorrection;
    }

    //уничтожает ракету c КСП ПРО
    public void destroy() {
        explosions.newExplosion(getXCenterCoord(), getYCenterCoord());
        clear();
    }

    //возвращает угол полёта ракеты c КСП ПРО в радианах
    public float getDegreesInRad() {
        return degreesInRad;
    }

    //устанавливает новое случайное отклонение ракеты c КСП ПРО при необходимости
    private void setNewDirection() {
        float xCenterCoord = getXCenterCoord();
        float yCenterCoord = getYCenterCoord();

        //если ракета c КСП ПРО приблизилась к временным координатам, куда она стремится, или угол поворота вышел за допустимые значения
        if ((Math.sqrt(Math.pow(xTempDestiny - xCenterCoord, 2) + Math.pow(yTempDestiny - yCenterCoord, 2)) < speed) || Math.abs(degrees) > 360) {
            if (((random.nextInt(2) == 0) && (x > xMaxDispersion)) || (xCenterCoord + xCorrection + xMaxDispersion >= screenWidth)) {
                //движение ракеты c КСП ПРО влево
                //определяем новую временную x-координату, куда стремится ракета c КСП ПРО
                xTempDestiny = xCenterCoord - (random.nextInt(xMaxDispersion - xMinDispersion) + xMinDispersion);
            } else {
                //движение ракеты c КСП ПРО вправо
                //определяем новую временную x-координату, куда стремится ракета c КСП ПРО
                xTempDestiny = xCenterCoord + (random.nextInt(xMaxDispersion - xMinDispersion) + xMinDispersion);
            }
            //определяем новую временную y-координату, куда стремится ракета c КСП ПРО
            yTempDestiny = yCenterCoord + (random.nextInt(yMaxDispersion - yMinDispersion) + yMinDispersion);

            if (xTempDestiny < 0) {
                xTempDestiny = 0;
            }

            if (xTempDestiny > screenWidth) {
                xTempDestiny = screenWidth;
            }

            if (yTempDestiny < 0) {
                yTempDestiny = 0;
            }

            if (yTempDestiny > screenHeight) {
                yTempDestiny = screenHeight;
            }

            if (Math.abs(degrees) > 360) {
                degrees = degrees % 360;
            }

            setNewTempDestinyDegrees();
        }

        //если текущий угол поворота не равен временному целевому
        if (degrees != tempDestinyDegrees) {
            //если можем сразу повернуть на нужный угол
            if (Math.abs(degreesDistance(degrees, tempDestinyDegrees)) <= maxDegreesDelta) {
                degrees = tempDestinyDegrees;
            }
            else {
                //определяем, куда поворачивать
                if (degreesDistance(degrees + maxDegreesDelta, tempDestinyDegrees) < degreesDistance(degrees, tempDestinyDegrees)) {
                    degrees += maxDegreesDelta;
                }
                else {
                    degrees -= maxDegreesDelta;
                }
            }
            degreesInRad = degrees * (float) Math.PI / 180;
        }
    }

    //устанавливает новый временный целевой угол поворота
    private void setNewTempDestinyDegrees() {
        float xCenterCoord = getXCenterCoord();
        float yCenterCoord = getYCenterCoord();

        if (xTempDestiny == xCenterCoord) {
            tempDestinyDegrees = 0;
        } else {
            if (xTempDestiny > xCenterCoord) {
                tempDestinyDegrees = (float) Math.toDegrees(Math.atan((yTempDestiny - yCenterCoord) / (xTempDestiny - xCenterCoord)) - Math.PI / 2);
            } else {
                tempDestinyDegrees = (float) Math.toDegrees(Math.atan((yTempDestiny - yCenterCoord) / (xTempDestiny - xCenterCoord)) + Math.PI / 2);
            }
        }
    }

    //считает разницу между двумя углами
    private float degreesDistance(float a, float b) {
        float r1, r2;

        if (a > b) {
            r1 = a - b;
            r2 = b - a + 360;
        } else {
            r1 = b - a;
            r2 = a - b + 360;
        }

        return Math.min(r1, r2);
    }
}
