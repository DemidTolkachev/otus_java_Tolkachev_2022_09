package com.example.game.objectCollections;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.game.Destruction;
import com.example.game.Statistics;
import com.example.game.objects.Rocket;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//коллекция обычных ракет воздух-земля
public class Rockets {
    private final int probability = 4000; //вероятность появления ракеты за одно обновление
    private final int maxProbability = 1000000; //максимальная вероятность, принятая за 100%
    private final int xMinDeviation = 100; //минимальное отклонение x-координаты назначения ракеты от x-координаты её появления
    private final Random random = new Random();
    private Bitmap image;
    private ArrayList<Rocket> rockets = new ArrayList<>();
    private Explosions explosions;
    private Explosions groundExplosions;
    private Statistics statistics;
    private Destruction destruction;
    private int xMin, xMax; //минимальная и максимальная возможные x-координаты точек появления ракет
    private float rocketRadius; //усреднённый радиус ракеты
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels; //высота экрана
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels; //ширина экрана


    public Rockets(Bitmap bmp, Explosions explosions, Explosions groundExplosions, Statistics statistics, Destruction destruction) {
        image = bmp;
        xMin = bmp.getWidth() / 2;
        xMax = screenWidth - bmp.getWidth() / 2;
        rocketRadius = (bmp.getWidth() + bmp.getHeight()) / 4;
        this.explosions = explosions;
        this.groundExplosions = groundExplosions;
        this.statistics = statistics;
        this.destruction = destruction;
    }

    public void draw(Canvas canvas) {
        for(Rocket r : rockets) {
            if (r.getVisibility()) {
                r.draw(canvas);
            }
        }
    }

    public void update() {
        //если генерируем новую ракету
        if (isRocketGenerate()) {
            newRocket();
        }

        for(Rocket r : rockets) {
            if (r.getVisibility()) {
                r.update();
            }
        }
    }

    //уничтожаем ракеты, попавшие в радиус взрыва зенитного снаряда или ракеты
    public void rocketsDestruction(float x, float y, float radius) {
        //используем примитивный алгоритм из серии сферического коня в вакууме
        float destructionDistance = radius + rocketRadius; //расстояние от центра ракеты до центра взрыва, на котором ракета будет уничтожена
        float realDistance; //текущее расстояние между центром ракеты и центром взрыва

        for(Rocket r : rockets) {
            if (r.getVisibility()) {
                realDistance = (float)Math.sqrt(Math.pow(x - r.getXCenterCoord(), 2) + Math.pow(y - r.getYCenterCoord(), 2));

                //если ракета попала в радиус поражения
                if (realDistance < destructionDistance) {
                    //уничтожаем ракету
                    r.destroy();
                    //увеличиваем статистику сбитых ракет
                    statistics.increaseRocketsShotDown();
                }
            }
        }
    }

    //возвращает координаты, угол полёта ближайшей ракеты и расстояние до неё, а также признак, будет ли ракета уничтожена ракетой ЗРК
    public Map<String, Float> getClosestRocket(float x, float y, float radius) {
        //используем примитивный алгоритм из серии сферического коня в вакууме
        float destructionDistance = radius + rocketRadius; //расстояние от центра ракеты до центра взрыва, на котором ракета будет уничтожена
        float realDistance; //текущее расстояние между центром ракеты и центром взрыва
        float minDistance = 999999999; //минимальное расстояние между центром ракеты и центром взрыва
        float xCoord = 0, yCoord = 0; //координаты ближайшей ракеты
        float degreesInRad = 0; //угол полёта ближайшей ракеты в радианах
        float willBeDestroyed = 0; //будет ли ракета уничтожена ракетой ЗРК: 0 - не будет, 1 - будет

        for(Rocket r : rockets) {
            if (r.getVisibility()) {
                realDistance = (float)Math.sqrt(Math.pow(x - r.getXCenterCoord(), 2) + Math.pow(y - r.getYCenterCoord(), 2));

                if (realDistance < minDistance) {
                    minDistance = realDistance;
                    xCoord = r.getXCenterCoord();
                    yCoord = r.getYCenterCoord();
                    degreesInRad = r.getDegreesInRad();

                    //если ракета попала в радиус поражения
                    if (realDistance < destructionDistance) {
                        willBeDestroyed = 1;
                    }
                }
            }
        }

        //если ни одну ракету не нашли
        if (minDistance == 999999999) {
            return null;
        }
        else {
            Map<String, Float> collect = Stream.of(
                            new AbstractMap.SimpleEntry<>("minDistance", minDistance),
                            new AbstractMap.SimpleEntry<>("xCoord", xCoord),
                            new AbstractMap.SimpleEntry<>("yCoord", yCoord),
                            new AbstractMap.SimpleEntry<>("degreesInRad", degreesInRad),
                            new AbstractMap.SimpleEntry<>("willBeDestroyed", willBeDestroyed)
                    )
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            return collect;
        }
    }

    //приводит ракеты к первоначальному состоянию
    public void clear() {
        for(Rocket r : rockets) {
            r.clear();
        }
    }

    //генерировать ли новую ракету
    private boolean isRocketGenerate() {
        if (random.nextInt(maxProbability) < probability) {
            return true;
        }

        return false;
    }

    //генерируем новую ракету
    private void newRocket() {
        boolean find = false;
        float xCoord, yCoord; //координаты точки появления ракеты
        float xDestiny, yDestiny; //координаты точки назначения ракеты

        xCoord = random.nextInt(xMax - xMin) + xMin;
        yCoord = 0;
        xDestiny = random.nextInt(xMax - xMin) + xMin;

        //будем генерировать новую x-координату точки назначения ракеты, пока она не станет достаточно отклонена от начальной точки
        while (Math.abs(xCoord - xDestiny) < xMinDeviation) {
            xDestiny = random.nextInt(xMax - xMin) + xMin;
        }
        yDestiny = screenHeight;

        for(Rocket r : rockets) {
            if (r.getVisibility() == false) {
                r.relocate(xCoord, yCoord, xDestiny, yDestiny);
                find = true;
                break;
            }
        }

        if (find == false) {
            Rocket rocket = new Rocket(image, explosions, groundExplosions, destruction);
            rocket.relocate(xCoord, yCoord, xDestiny, yDestiny);
            rockets.add(rocket);
        }
    }
}
