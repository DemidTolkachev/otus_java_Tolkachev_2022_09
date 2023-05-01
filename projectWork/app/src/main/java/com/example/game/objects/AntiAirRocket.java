package com.example.game.objects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.example.game.objectCollections.Bombs;
import com.example.game.objectCollections.Explosions;
import com.example.game.objectCollections.Gbus;
import com.example.game.objectCollections.RocketPenaids;
import com.example.game.objectCollections.Rockets;

import java.util.Map;

//ракета ЗРК (зенитная ракета)
public class AntiAirRocket {
    private final int speed = 10; //максимальная скорость полёта ракеты ЗРК
    private final float maxDegreesDelta = 6; //максимально возможное изменение угла за одно обновление
    private Bitmap image;
    private Explosions explosions;
    private Bombs bombs;
    private Gbus gbus;
    private Rockets rockets;
    private RocketPenaids rocketPenaids;
    private float x, y; //координаты ракеты ЗРК
    private float xTempDestiny, yTempDestiny; //временная координаты, куда стремится ракета ЗРК
    private float xCorrection, yCorrection; //корректировка координат ракеты ЗРК в зависимости от её размеров
    private float degrees; //градус поворта ракеты ЗРК
    private float degreesInRad; //угол полёта ракеты ЗРК в радианах
    private float tempDestinyDegrees; //временный целевой градус поворта ракеты ЗРК
    private float antiAirRocketExplosionRadius; //усреднённый радиус взрыва ракеты ЗРК
    private Matrix matrix; //матрица преобразований для ракеты ЗРК
    private int antiAircraftSystemNumber; //номер ЗРК, который выпустил ракету
    private boolean launched; //признак запуска ракеты ЗРК
    private boolean visibility; //признак видимости ракеты ЗРК
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels; //высота экрана
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels; //ширина экрана

    public AntiAirRocket(Bitmap bmp, Explosions explosions, Bombs bombs, Gbus gbus, Rockets rockets, RocketPenaids rocketPenaids, float antiAirRocketExplosionRadius) {
        image = bmp;
        this.explosions = explosions;
        this.bombs = bombs;
        this.gbus = gbus;
        this.rockets = rockets;
        this.rocketPenaids = rocketPenaids;
        this.antiAirRocketExplosionRadius = antiAirRocketExplosionRadius;
        clear();
        degrees = 0;
        degreesInRad = 0;
        tempDestinyDegrees = 0;
        matrix = new Matrix();
        matrix.setTranslate(x, y); //устанавливает координаты ракеты c КСП ПРО
        xCorrection = bmp.getWidth() / 2;
        yCorrection = bmp.getHeight() / 2;
    }

    //приводит ракету ЗРК к первоначальному состоянию
    public void clear() {
        x = -1000;
        y = -1000;
        xTempDestiny = x;
        yTempDestiny = y;
        antiAircraftSystemNumber = -1;
        launched = false;
        visibility = false;
    }

    public void draw(Canvas canvas) {
        if (visibility) {
            canvas.drawBitmap(image, matrix, null);
        }
    }

    public void update() {
        if ((visibility) && (launched)) {
            float xCenterCoord = getXCenterCoord();
            float yCenterCoord = getYCenterCoord();
            boolean willBeDestroyed = false;
            float minDistance = 999999999; //минимальное расстояние между ракетой ЗРК и целью

            Map<String, Float> bombInfo = bombs.getClosestBomb(xCenterCoord, yCenterCoord, antiAirRocketExplosionRadius);
            Map<String, Float> gbuInfo = gbus.getClosestGbu(xCenterCoord, yCenterCoord, antiAirRocketExplosionRadius);
            Map<String, Float> rocketInfo = rockets.getClosestRocket(xCenterCoord, yCenterCoord, antiAirRocketExplosionRadius);
            Map<String, Float> rocketPenaidInfo = rocketPenaids.getClosestRocketPenaids(xCenterCoord, yCenterCoord, antiAirRocketExplosionRadius);

            if (bombInfo != null) {
                //если ракета ЗРК может уничтожить ближайшую бомбу
                if (bombInfo.get("willBeDestroyed") == 1) {
                    willBeDestroyed = true;
                }
            }

            if (gbuInfo != null) {
                //если ракета ЗРК может уничтожить ближайшую КАБ
                if (gbuInfo.get("willBeDestroyed") == 1) {
                    willBeDestroyed = true;
                }
            }

            if (rocketInfo != null) {
                //если ракета ЗРК может уничтожить ближайшую ракету
                if (rocketInfo.get("willBeDestroyed") == 1) {
                    willBeDestroyed = true;
                }
            }

            if (rocketPenaidInfo != null) {
                //если ракета ЗРК может уничтожить ближайшую ракету c КСП ПРО
                if (rocketPenaidInfo.get("willBeDestroyed") == 1) {
                    willBeDestroyed = true;
                }
            }

            //если ракета ЗРК залетает за пределы экрана, её также взрываем
            if ((xCenterCoord < 0) || (xCenterCoord > screenWidth) || (yCenterCoord < 0) || (yCenterCoord > screenHeight)) {
                willBeDestroyed = true;
            }

            //если ракета ЗРК может что-нибудь уничтожить
            if (willBeDestroyed) {
                //если ракета ЗРК может что-нибудь уничтожить - взрываем её
                explosions.newExplosion(xCenterCoord, yCenterCoord);
                clear();
                bombs.bombsDestruction(xCenterCoord, yCenterCoord, antiAirRocketExplosionRadius);
                gbus.gbusDestruction(xCenterCoord, yCenterCoord, antiAirRocketExplosionRadius);
                rockets.rocketsDestruction(xCenterCoord, yCenterCoord, antiAirRocketExplosionRadius);
                rocketPenaids.rocketPenaidsDestruction(xCenterCoord, yCenterCoord, antiAirRocketExplosionRadius);
            } else {
                //иначе наводим ракету ЗРК на ближайшую цель
                if (bombInfo != null) {
                    if (bombInfo.get("minDistance") < minDistance) {
                        //наводим ракету ЗРК на ближайшую бомбу
                        minDistance = bombInfo.get("minDistance");
                        xTempDestiny = bombInfo.get("xCoord");
                        yTempDestiny = bombInfo.get("yCoord");
                    }
                }

                if (gbuInfo != null) {
                    if (gbuInfo.get("minDistance") < minDistance) {
                        //наводим ракету ЗРК на ближайшую КАБ
                        minDistance = gbuInfo.get("minDistance");
                        xTempDestiny = gbuInfo.get("xCoord");
                        yTempDestiny = gbuInfo.get("yCoord");
                    }
                }

                if (rocketInfo != null) {
                    if (rocketInfo.get("minDistance") < minDistance) {
                        //наводим ракету ЗРК на ближайшую ракету
                        minDistance = rocketInfo.get("minDistance");
                        //наводим на координаты с опережением, учитывая радиус поражения ракеты ЗРК
                        xTempDestiny = rocketInfo.get("xCoord") - (float) Math.sin(rocketInfo.get("degreesInRad")) * antiAirRocketExplosionRadius;
                        yTempDestiny = rocketInfo.get("yCoord") + (float) Math.cos(rocketInfo.get("degreesInRad")) * antiAirRocketExplosionRadius;
                    }
                }

                if (rocketPenaidInfo != null) {
                    if (rocketPenaidInfo.get("minDistance") < minDistance) {
                        //наводим ракету ЗРК на ближайшую ракету c КСП ПРО
                        //наводим на координаты с опережением, учитывая радиус поражения ракеты ЗРК
                        xTempDestiny = rocketPenaidInfo.get("xCoord") - (float) Math.sin(rocketPenaidInfo.get("degreesInRad")) * antiAirRocketExplosionRadius;
                        yTempDestiny = rocketPenaidInfo.get("yCoord") + (float) Math.cos(rocketPenaidInfo.get("degreesInRad")) * antiAirRocketExplosionRadius;
                    }
                }
                setNewDirection();
                x += (float) Math.sin(degreesInRad) * speed;
                y -= (float) Math.cos(degreesInRad) * speed;
            }

            matrix.reset(); //обнуляет расположение ракеты ЗРК
            matrix.setTranslate(x, y); //устанавливает координаты ракеты ЗРК
            matrix.postRotate(degrees, x, y); //вращает ракету ЗРК
        }
    }

    //перемещает ракету ЗРК в начальную точку полёта
    //xStarting, yStarting - координаты, откуда ракета ЗРК стартует
    //antiAircraftSystemNumber - номер ЗРК, который выпустил ракету
    public void relocate(float xStarting, float yStarting, int antiAircraftSystemNumber) {
        this.antiAircraftSystemNumber = antiAircraftSystemNumber;
        degrees = 0;
        degreesInRad = degrees * (float) Math.PI / 180;
        tempDestinyDegrees = 0;
        launched = false;
        visibility = true;

        x = xStarting;
        y = yStarting;
        xTempDestiny = getXCenterCoord();
        yTempDestiny = 0;

        matrix.reset(); //обнуляет расположение ракеты ЗРК
        matrix.setTranslate(x, y); //устанавливает координаты ракеты ЗРК
        matrix.postRotate(degrees, x, y); //вращает ракету ЗРК
    }

    public boolean getVisibility() {
        return visibility;
    }

    //возвращает трекущую x-координату центра ракеты ЗРК
    public float getXCenterCoord() {
        return x + (float)Math.cos(degreesInRad) * xCorrection - (float)Math.sin(degreesInRad) * yCorrection;
    }

    //возвращает трекущую y-координату центра ракеты ЗРК
    public float getYCenterCoord() {
        return y + (float)Math.cos(degreesInRad) * yCorrection + (float)Math.sin(degreesInRad) * xCorrection;
    }

    //уничтожает ракету ЗРК
    public void destroy() {
        if (launched) {
            explosions.newExplosion(getXCenterCoord(), getYCenterCoord());
            clear();
        }
    }

    //запускает ракету ЗРК
    public void launch() {
        launched = true;
    }

    //возвращает, запущена ли ракета ЗРК
    public boolean getLaunched() {
        return launched;
    }

    //возвращает номер установки, из которой возникла ракета ЗРК
    public int getAntiAircraftSystemNumber() {
        return antiAircraftSystemNumber;
    }

    //устанавливает новое направление ракеты ЗРК
    private void setNewDirection() {
        if (Math.abs(degrees) > 360) {
            degrees = degrees % 360;
        }

        setNewTempDestinyDegrees();

        //если текущий угол поворота не равен временному целевому
        if (degrees != tempDestinyDegrees) {
            //если можем сразу повернуть на нужный угол
            if (Math.abs(degreesDistance(degrees, tempDestinyDegrees)) <= maxDegreesDelta) {
                degrees = tempDestinyDegrees;
            } else {
                //определяем, куда поворачивать
                if (degreesDistance(degrees + maxDegreesDelta, tempDestinyDegrees) < degreesDistance(degrees, tempDestinyDegrees)) {
                    degrees += maxDegreesDelta;
                } else {
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
                tempDestinyDegrees = (float) Math.toDegrees(Math.atan((yTempDestiny - yCenterCoord) / (xTempDestiny - xCenterCoord)) + Math.PI / 2);
            } else {
                tempDestinyDegrees = (float) Math.toDegrees(Math.atan((yTempDestiny - yCenterCoord) / (xTempDestiny - xCenterCoord)) - Math.PI / 2);
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
