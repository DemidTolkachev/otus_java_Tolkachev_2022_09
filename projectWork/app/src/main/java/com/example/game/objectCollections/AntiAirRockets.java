package com.example.game.objectCollections;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.game.objects.AntiAirRocket;

import java.util.ArrayList;

//коллекция ракет ЗРК
public class AntiAirRockets {
    private final float powerCoef = (float)0.6; //коэффициент увеличения эффективного радиуса поражения ракеты ЗРК
    private Bitmap image;
    private ArrayList<AntiAirRocket> antiAirRockets = new ArrayList<>();
    private Explosions explosions;
    private Bombs bombs;
    private Gbus gbus;
    private Rockets rockets;
    private RocketPenaids rocketPenaids;
    private float antiAirRocketExplosionRadius; //усреднённый радиус взрыва ракеты ЗРК
    private float antiAirRocketRadius; //усреднённый радиус ракеты ЗРК

    public AntiAirRockets(Bitmap bmp, Explosions explosions, Bombs bombs, Gbus gbus, Rockets rockets, RocketPenaids rocketPenaids) {
        image = bmp;
        this.explosions = explosions;
        this.bombs = bombs;
        this.gbus = gbus;
        this.rockets = rockets;
        this.rocketPenaids = rocketPenaids;
        antiAirRocketExplosionRadius = (explosions.getWidth() + explosions.getHeight()) / 4 * powerCoef;
        antiAirRocketRadius = (bmp.getWidth() + bmp.getHeight()) / 4;
    }

    public void newAntiAirRockets(float x, float y, int antiAircraftSystemNumber) {
        boolean find = false;

        for(AntiAirRocket aar : antiAirRockets) {
            if (aar.getVisibility() == false) {
                aar.relocate(x, y, antiAircraftSystemNumber);
                find = true;
                break;
            }
        }

        if (find == false) {
            AntiAirRocket antiAirRocket = new AntiAirRocket(image, explosions, bombs, gbus, rockets, rocketPenaids, antiAirRocketExplosionRadius);
            antiAirRocket.relocate(x, y, antiAircraftSystemNumber);
            antiAirRockets.add(antiAirRocket);
        }
    }

    public void draw(Canvas canvas) {
        for(AntiAirRocket aar : antiAirRockets) {
            if (aar.getVisibility()) {
                aar.draw(canvas);
            }
        }
    }

    public void update() {
        for(AntiAirRocket aar : antiAirRockets) {
            if (aar.getVisibility()) {
                aar.update();
            }
        }
    }

    //уничтожаем ракеты ЗРК, попавшие в радиус взрыва зенитного снаряда
    public void antiAirRocketsDestruction(float x, float y, float radius) {
        //используем примитивный алгоритм из серии сферического коня в вакууме
        float destructionDistance = radius + antiAirRocketRadius; //расстояние от центра ракеты ЗРК до центра взрыва, на котором ракета будет уничтожена
        float realDistance; //текущее расстояние между центром ракеты ЗРК и центром взрыва

        for(AntiAirRocket aar : antiAirRockets) {
            if (aar.getVisibility()) {
                realDistance = (float)Math.sqrt(Math.pow(x - aar.getXCenterCoord(), 2) + Math.pow(y - aar.getYCenterCoord(), 2));

                //если ракета ЗРК попала в радиус поражения
                if (realDistance < destructionDistance) {
                    //уничтожаем ракету ЗРК "дружественным огнём"
                    aar.destroy();
                }
            }
        }
    }

    //запускает ракету ЗРК из установки с конкретным номером
    //возвращает true, если удалось запустить, иначе false
    public boolean launch(int number) {
        for(AntiAirRocket aar : antiAirRockets) {
            if (aar.getVisibility()) {
                if (aar.getLaunched() == false) {
                    if (aar.getAntiAircraftSystemNumber() == number) {
                        aar.launch();
                        return true;
                    }
                }
            }
        }

        return false;
    }

    //приводит ракеты ЗРК к первоначальному состоянию
    public void clear() {
        for (AntiAirRocket aar : antiAirRockets) {
            aar.clear();
        }
    }
}
