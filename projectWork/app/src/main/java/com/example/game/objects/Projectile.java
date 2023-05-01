package com.example.game.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.example.game.objectCollections.AntiAirRockets;
import com.example.game.objectCollections.Bombs;
import com.example.game.objectCollections.Explosions;
import com.example.game.objectCollections.Gbus;
import com.example.game.objectCollections.RocketPenaids;
import com.example.game.objectCollections.Rockets;

//зенитный снаряд
public class Projectile {
    private final int speed = 10; //скорость полёта зенитного снаряда
    private Bitmap image;
    private Explosions antiAirExplosions;
    private Bombs bombs;
    private Gbus gbus;
    private Rockets rockets;
    private RocketPenaids rocketPenaids;
    private AntiAirRockets antiAirRockets;
    private float x, y; //координаты зенитного снаряда
    private float xDestiny, yDestiny; //координаты, куда должен прилететь зенитный снаряд
    private float xDestinyCenter, yDestinyCenter; //отцентрованные координаты, куда должен прилететь зенитный снаряд
    private float xCorrection, yCorrection; //корректировка координат зенитного снаряда в зависимости от его размеров
    private float xCenterCorrection, yCenterCorrection; //центровочная корректировка координат зенитного снаряда
    private float degrees; //градус поворта ствола пушки
    private float degreesInRad; //угол полёта снаряяда в радианах
    private float projectileExplosionRadius; //усреднённый радиус взрыва зенитного снаряда
    private Matrix matrix; //матрица преобразований для зенитного снаряда
    private boolean readyToExplode; //признак готовности зенитного снаряда к взрыву
    private boolean visibility; //признак видимости зенитного снаряда

    public Projectile (Bitmap bmp, Explosions antiAirExplosions, Bombs bombs, Gbus gbus, Rockets rockets, RocketPenaids rocketPenaids, AntiAirRockets antiAirRockets, float projectileExplosionRadius) {
        image = bmp;
        this.antiAirExplosions = antiAirExplosions;
        this.bombs = bombs;
        this.gbus = gbus;
        this.rockets = rockets;
        this.rocketPenaids = rocketPenaids;
        this.antiAirRockets = antiAirRockets;
        this.projectileExplosionRadius = projectileExplosionRadius;
        clear();
        degrees = 0;
        degreesInRad = 0;
        xDestiny = 0;
        yDestiny = 0;
        xDestinyCenter = 0;
        yDestinyCenter = 0;
        matrix = new Matrix();
        matrix.setTranslate(x, y); //устанавливает координаты зенитного снаряда
        xCorrection = bmp.getWidth() / 2;
        yCorrection = bmp.getHeight() / 2;
        xCenterCorrection = 0;
        yCenterCorrection = 0;
    }

    //приводит зенитный снаряд к первоначальному состоянию
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
            float distance; //расстояние между точкой взрыва и текущим расположением зенитного снаряда
            //если зенитный снаряд ещё не готов взорваться
            if (readyToExplode == false) {
                x += (float) Math.cos(degreesInRad) * speed;
                y -= (float) Math.sin(degreesInRad) * speed;
                distance = (float)Math.sqrt(Math.pow(xDestinyCenter - getXCenterCoord(), 2) + Math.pow(yDestinyCenter - getYCenterCoord(), 2));

                //если расстояние между точкой взрыва и текущим расположением зенитного снаряда меньше либо равно расстояния, пролетаемого снарядом за одно обновление
                if (distance <= speed) {
                    readyToExplode = true;
                }
            }
            else {
                antiAirExplosions.newExplosion(xDestiny, yDestiny);
                clear();
                bombs.bombsDestruction(xDestiny, yDestiny, projectileExplosionRadius);
                gbus.gbusDestruction(xDestiny, yDestiny, projectileExplosionRadius);
                rockets.rocketsDestruction(xDestiny, yDestiny, projectileExplosionRadius);
                rocketPenaids.rocketPenaidsDestruction(xDestiny, yDestiny, projectileExplosionRadius);
                antiAirRockets.antiAirRocketsDestruction(xDestiny, yDestiny, projectileExplosionRadius);
            }
            matrix.reset(); //обнуляет расположение зенитного снаряда
            matrix.setTranslate(x, y); //устанавливает координаты зенитного снаряда
            matrix.postRotate(degrees, x, y); //вращает зенитный снаряд
        }
    }

    //перемещает зенитный снаряд в начальную точку полёта
    //barrelBaseX, barrelBaseY - координаты основания ствола пушки, относительно которых осуществляется поворот
    //barrelHeight - высота ствола пушки
    //degrees - градус поворта ствола пушки
    //xDestiny, yDestiny - координаты, куда зенитный снаряд должен прилететь
    public void relocate(float barrelBaseX, float barrelBaseY, int barrelHeight, float degrees, float xDestiny, float yDestiny) {
        float distance; //расстояние между точкой взрыва и текущим расположением зенитного снаряда
        this.degrees = degrees;
        this.xDestiny = xDestiny;
        this.yDestiny = yDestiny;
        degreesInRad = (90 - degrees) * (float)Math.PI / 180;

        readyToExplode = false;
        visibility = true;

        x = barrelBaseX + (float)Math.cos(degreesInRad) * (barrelHeight + yCorrection) - (float)Math.sin(degreesInRad) * xCorrection;
        y = barrelBaseY - (float)Math.sin(degreesInRad) * (barrelHeight + yCorrection) - (float)Math.cos(degreesInRad) * xCorrection;
        xDestinyCenter = xDestiny - (float) Math.cos(degreesInRad) * barrelHeight;
        yDestinyCenter = yDestiny + (float) Math.sin(degreesInRad) * barrelHeight;
        xCenterCorrection = (float)Math.sin(degreesInRad) * xCorrection - (float)Math.cos(degreesInRad) * (barrelHeight + yCorrection);
        yCenterCorrection = (float)Math.sin(degreesInRad) * (barrelHeight + yCorrection) + (float)Math.cos(degreesInRad) * xCorrection;
        distance = (float)Math.sqrt(Math.pow(xDestinyCenter - getXCenterCoord(), 2) + Math.pow(yDestinyCenter - getYCenterCoord(), 2));

        //если расстояние между точкой взрыва и текущим расположением зенитного снаряда меньше либо равно расстояния, пролетаемого снарядом за одно обновление
        if (distance <= speed) {
            readyToExplode = true;
        }

        matrix.reset(); //обнуляет расположение зенитного снаряда
        matrix.setTranslate(x, y); //устанавливает координаты зенитного снаряда
        matrix.postRotate(degrees, x, y); //вращает зенитный снаряд
    }

    public boolean getVisibility() {
        return visibility;
    }

    //возвращает текущую отцентрованную x-координату зенитного снаряда
    private float getXCenterCoord() {
        return x + xCenterCorrection;
    }

    //возвращает текущую отцентрованную y-координату зенитного снаряда
    private float getYCenterCoord() {
        return y + yCenterCorrection;
    }
}
