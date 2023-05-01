package com.example.game.objectCollections;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.game.Destruction;
import com.example.game.Statistics;
import com.example.game.objects.Gbu;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//коллекция корректируемых авиационных бомб (КАБ)
public class Gbus {
    private final int probability = 4000; //вероятность появления КАБ за одно обновление
    private final int maxProbability = 1000000; //максимальная вероятность, принятая за 100%
    private final Random random = new Random();
    private Bitmap image;
    private ArrayList<Gbu> gbus = new ArrayList<>();
    private Explosions explosions;
    private Explosions groundExplosions;
    private Statistics statistics;
    private Destruction destruction;
    private int xMin, xMax; //минимальная и максимальная возможные x-координаты точек появления КАБ
    private float gbuRadius; //усреднённый радиус КАБ
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels; //ширина экрана


    public Gbus(Bitmap bmp, Explosions explosions, Explosions groundExplosions, Statistics statistics, Destruction destruction) {
        image = bmp;
        xMin = bmp.getWidth() / 2;
        xMax = screenWidth - bmp.getWidth() / 2;
        gbuRadius = (bmp.getWidth() + bmp.getHeight()) / 4;
        this.explosions = explosions;
        this.groundExplosions = groundExplosions;
        this.statistics = statistics;
        this.destruction = destruction;
    }

    public void draw(Canvas canvas) {
        for(Gbu g : gbus) {
            if (g.getVisibility()) {
                g.draw(canvas);
            }
        }
    }

    public void update() {
        //если генерируем новую КАБ
        if (isGbuGenerate()) {
            newGbu();
        }

        for(Gbu g : gbus) {
            if (g.getVisibility()) {
                g.update();
            }
        }
    }

    //уничтожаем КАБ, попавшие в радиус взрыва зенитного снаряда или ракеты
    public void gbusDestruction(float x, float y, float radius) {
        //используем примитивный алгоритм из серии сферического коня в вакууме
        float destructionDistance = radius + gbuRadius; //расстояние от центра КАБ до центра взрыва, на котором КАБ будет уничтожена
        float realDistance; //текущее расстояние между центром КАБ и центром взрыва

        for(Gbu g : gbus) {
            if (g.getVisibility()) {
                realDistance = (float)Math.sqrt(Math.pow(x - g.getXCenterCoord(), 2) + Math.pow(y - g.getYCenterCoord(), 2));

                //если КАБ попала в радиус поражения
                if (realDistance < destructionDistance) {
                    //уничтожаем КАБ
                    g.destroy();
                    //увеличиваем статистику сбитых КАБ
                    statistics.increaseGbusShotDown();
                }
            }
        }
    }

    //возвращает координаты ближайшей КАБ и расстояние до неё, а также признак, будет ли КАБ уничтожена ракетой ЗРК
    public Map<String, Float> getClosestGbu(float x, float y, float radius) {
        //используем примитивный алгоритм из серии сферического коня в вакууме
        float destructionDistance = radius + gbuRadius; //расстояние от центра КАБ до центра взрыва, на котором КАБ будет уничтожена
        float realDistance; //текущее расстояние между центром КАБ и центром взрыва
        float minDistance = 999999999; //минимальное расстояние между центром КАБ и центром взрыва
        float xCoord = 0, yCoord = 0; //координаты ближайшей КАБ
        float willBeDestroyed = 0; //будет ли КАБ уничтожена ракетой ЗРК: 0 - не будет, 1 - будет

        for(Gbu g : gbus) {
            if (g.getVisibility()) {
                realDistance = (float)Math.sqrt(Math.pow(x - g.getXCenterCoord(), 2) + Math.pow(y - g.getYCenterCoord(), 2));

                if (realDistance < minDistance) {
                    minDistance = realDistance;
                    xCoord = g.getXCenterCoord();
                    yCoord = g.getYCenterCoord();

                    //если КАБ попала в радиус поражения
                    if (realDistance < destructionDistance) {
                        willBeDestroyed = 1;
                    }
                }
            }
        }

        //если ни одну КАБ не нашли
        if (minDistance == 999999999) {
            return null;
        }
        else {
            Map<String, Float> collect = Stream.of(
                            new AbstractMap.SimpleEntry<>("minDistance", minDistance),
                            new AbstractMap.SimpleEntry<>("xCoord", xCoord),
                            new AbstractMap.SimpleEntry<>("yCoord", yCoord),
                            new AbstractMap.SimpleEntry<>("willBeDestroyed", willBeDestroyed)
                    )
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            return collect;
        }
    }

    //приводит КАБ к первоначальному состоянию
    public void clear() {
        for(Gbu g : gbus) {
            g.clear();
        }
    }

    //генерировать ли новую КАБ
    private boolean isGbuGenerate() {
        if (random.nextInt(maxProbability) < probability) {
            return true;
        }

        return false;
    }

    //генерируем новую КАБ
    private void newGbu() {
        boolean find = false;
        float xCoord, yCoord; //координаты точки появления КАБ

        xCoord = random.nextInt(xMax - xMin) + xMin;
        yCoord = 0;

        for(Gbu g : gbus) {
            if (g.getVisibility() == false) {
                g.relocate(xCoord, yCoord);
                find = true;
                break;
            }
        }

        if (find == false) {
            Gbu gbu = new Gbu(image, explosions, groundExplosions, destruction);
            gbu.relocate(xCoord, yCoord);
            gbus.add(gbu);
        }
    }
}
