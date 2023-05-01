package com.example.game.objectCollections;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.game.Destruction;
import com.example.game.Statistics;
import com.example.game.objects.RocketPenaid;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//коллекция ракет с комплексом средств преодоления противоракетной обороны (КСП ПРО)
public class RocketPenaids {
    private final int probability = 2000; //вероятность появления ракеты c КСП ПРО за одно обновление
    private final int maxProbability = 1000000; //максимальная вероятность, принятая за 100%
    private final Random random = new Random();
    private Bitmap image;
    private ArrayList<RocketPenaid> rocketPenaids = new ArrayList<>();
    private Explosions explosions;
    private Explosions groundExplosions;
    private Statistics statistics;
    private Destruction destruction;
    private int xMin, xMax; //минимальная и максимальная возможные x-координаты точек появления ракет c КСП ПРО
    private float rocketPenaidRadius; //усреднённый радиус ракеты c КСП ПРО
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels; //ширина экрана

    public RocketPenaids(Bitmap bmp, Explosions explosions, Explosions groundExplosions, Statistics statistics, Destruction destruction) {
        image = bmp;
        xMin = bmp.getWidth() / 2;
        xMax = screenWidth - bmp.getWidth() / 2;
        rocketPenaidRadius = (bmp.getWidth() + bmp.getHeight()) / 4;
        this.explosions = explosions;
        this.groundExplosions = groundExplosions;
        this.statistics = statistics;
        this.destruction = destruction;
    }

    public void draw(Canvas canvas) {
        for(RocketPenaid rp : rocketPenaids) {
            if (rp.getVisibility()) {
                rp.draw(canvas);
            }
        }
    }

    public void update() {
        //если генерируем новую ракету c КСП ПРО
        if (isRocketPenaidGenerate()) {
            newRocketPenaid();
        }

        for(RocketPenaid rp : rocketPenaids) {
            if (rp.getVisibility()) {
                rp.update();
            }
        }
    }

    //уничтожаем ракеты c КСП ПРО, попавшие в радиус взрыва зенитного снаряда или ракеты
    public void rocketPenaidsDestruction(float x, float y, float radius) {
        //используем примитивный алгоритм из серии сферического коня в вакууме
        float destructionDistance = radius + rocketPenaidRadius; //расстояние от центра ракеты c КСП ПРО до центра взрыва, на котором ракета будет уничтожена
        float realDistance; //текущее расстояние между центром ракеты c КСП ПРО и центром взрыва

        for(RocketPenaid rp : rocketPenaids) {
            if (rp.getVisibility()) {
                realDistance = (float)Math.sqrt(Math.pow(x - rp.getXCenterCoord(), 2) + Math.pow(y - rp.getYCenterCoord(), 2));

                //если ракета c КСП ПРО попала в радиус поражения
                if (realDistance < destructionDistance) {
                    //уничтожаем ракету c КСП ПРО
                    rp.destroy();
                    //увеличиваем статистику сбитых ракет c КСП ПРО
                    statistics.increaseRocketPenaidsShotDown();
                }
            }
        }
    }

    //возвращает координаты, угол полёта ближайшей ракеты c КСП ПРО и расстояние до неё, а также признак, будет ли ракета уничтожена ракетой ЗРК
    public Map<String, Float> getClosestRocketPenaids(float x, float y, float radius) {
        //используем примитивный алгоритм из серии сферического коня в вакууме
        float destructionDistance = radius + rocketPenaidRadius; //расстояние от центра ракеты c КСП ПРО до центра взрыва, на котором ракета будет уничтожена
        float realDistance; //текущее расстояние между центром ракеты c КСП ПРО и центром взрыва
        float minDistance = 999999999; //минимальное расстояние между центром ракеты c КСП ПРО и центром взрыва
        float xCoord = 0, yCoord = 0; //координаты ближайшей ракеты c КСП ПРО
        float degreesInRad = 0; //угол полёта ближайшей ракеты c КСП ПРО в радианах
        float willBeDestroyed = 0; //будет ли ракета c КСП ПРО уничтожена ракетой ЗРК: 0 - не будет, 1 - будет

        for(RocketPenaid rp : rocketPenaids) {
            if (rp.getVisibility()) {
                realDistance = (float)Math.sqrt(Math.pow(x - rp.getXCenterCoord(), 2) + Math.pow(y - rp.getYCenterCoord(), 2));

                if (realDistance < minDistance) {
                    minDistance = realDistance;
                    xCoord = rp.getXCenterCoord();
                    yCoord = rp.getYCenterCoord();
                    degreesInRad = rp.getDegreesInRad();

                    //если ракета c КСП ПРО попала в радиус поражения
                    if (realDistance < destructionDistance) {
                        willBeDestroyed = 1;
                    }
                }
            }
        }

        //если ни одну ракету c КСП ПРО не нашли
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

    //приводит ракеты c КСП ПРО к первоначальному состоянию
    public void clear() {
        for(RocketPenaid rp : rocketPenaids) {
            rp.clear();
        }
    }

    //генерировать ли новую ракету c КСП ПРО
    private boolean isRocketPenaidGenerate() {
        if (random.nextInt(maxProbability) < probability) {
            return true;
        }

        return false;
    }

    //генерируем новую ракету c КСП ПРО
    private void newRocketPenaid() {
        boolean find = false;
        float xCoord, yCoord; //координаты точки появления ракеты c КСП ПРО

        xCoord = random.nextInt(xMax - xMin) + xMin;
        yCoord = 0;

        for(RocketPenaid rp : rocketPenaids) {
            if (rp.getVisibility() == false) {
                rp.relocate(xCoord, yCoord);
                find = true;
                break;
            }
        }

        if (find == false) {
            RocketPenaid rocketPenaid = new RocketPenaid(image, explosions, groundExplosions, destruction);
            rocketPenaid.relocate(xCoord, yCoord);
            rocketPenaids.add(rocketPenaid);
        }
    }
}
