package com.example.game.objectCollections;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.game.Destruction;
import com.example.game.Statistics;
import com.example.game.objects.Bomb;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//коллекция свободнопадающих бомб
public class Bombs {
    private final int probability = 10000; //вероятность появления бомбы за одно обновление
    private final int maxProbability = 1000000; //максимальная вероятность, принятая за 100%
    private final Random random = new Random();
    private Bitmap image;
    private ArrayList<Bomb> bombs = new ArrayList<>();
    private Explosions explosions;
    private Explosions groundExplosions;
    private Statistics statistics;
    private Destruction destruction;
    private int xMin, xMax; //минимальная и максимальная возможные x-координаты точек появления бомб
    private float bombRadius; //усреднённый радиус бомбы
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels; //ширина экрана


    public Bombs(Bitmap bmp, Explosions explosions, Explosions groundExplosions, Statistics statistics, Destruction destruction) {
        image = bmp;
        xMin = bmp.getWidth() / 2;
        xMax = screenWidth - bmp.getWidth() / 2;
        bombRadius = (bmp.getWidth() + bmp.getHeight()) / 4;
        this.explosions = explosions;
        this.groundExplosions = groundExplosions;
        this.statistics = statistics;
        this.destruction = destruction;
    }

    public void draw(Canvas canvas) {
        for(Bomb b : bombs) {
            if (b.getVisibility()) {
                b.draw(canvas);
            }
        }
    }

    public void update() {
        //если генерируем новую бомбу
        if (isBombGenerate()) {
            newBomb();
        }

        for(Bomb b : bombs) {
            if (b.getVisibility()) {
                b.update();
            }
        }
    }

    //уничтожаем бомбы, попавшие в радиус взрыва зенитного снаряда или ракеты
    public void bombsDestruction(float x, float y, float radius) {
        //используем примитивный алгоритм из серии сферического коня в вакууме
        float destructionDistance = radius + bombRadius; //расстояние от центра бомбы до центра взрыва, на котором бомба будет уничтожена
        float realDistance; //текущее расстояние между центром бомбы и центром взрыва

        for(Bomb b : bombs) {
            if (b.getVisibility()) {
                realDistance = (float)Math.sqrt(Math.pow(x - b.getXCenterCoord(), 2) + Math.pow(y - b.getYCenterCoord(), 2));

                //если бомба попала в радиус поражения
                if (realDistance < destructionDistance) {
                    //уничтожаем бомбу
                    b.destroy();
                    //увеличиваем статистику сбитых бомб
                    statistics.increaseBombsShotDown();
                }
            }
        }
    }

    //возвращает координаты ближайшей бомбы и расстояние до неё, а также признак, будет ли бомба уничтожена ракетой ЗРК
    public Map<String, Float> getClosestBomb(float x, float y, float radius) {
        //используем примитивный алгоритм из серии сферического коня в вакууме
        float destructionDistance = radius + bombRadius; //расстояние от центра бомбы до центра взрыва, на котором бомба будет уничтожена
        float realDistance; //текущее расстояние между центром бомбы и центром взрыва
        float minDistance = 999999999; //минимальное расстояние между центром бомбы и центром взрыва
        float xCoord = 0, yCoord = 0; //координаты ближайшей бомбы
        float willBeDestroyed = 0; //будет ли бомба уничтожена ракетой ЗРК: 0 - не будет, 1 - будет

        for(Bomb b : bombs) {
            if (b.getVisibility()) {
                realDistance = (float)Math.sqrt(Math.pow(x - b.getXCenterCoord(), 2) + Math.pow(y - b.getYCenterCoord(), 2));

                if (realDistance < minDistance) {
                    minDistance = realDistance;
                    xCoord = b.getXCenterCoord();
                    yCoord = b.getYCenterCoord();

                    //если бомба попала в радиус поражения
                    if (realDistance < destructionDistance) {
                        willBeDestroyed = 1;
                    }
                }
            }
        }

        //если ни одну бомбу не нашли
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
                    .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
            return collect;
        }
    }

    //приводит бомбы к первоначальному состоянию
    public void clear() {
        for(Bomb b : bombs) {
            b.clear();
        }
    }

    //генерировать ли новую бомбу
    private boolean isBombGenerate() {
        if (random.nextInt(maxProbability) < probability) {
            return true;
        }

        return false;
    }

    //генерируем новую бомбу
    private void newBomb() {
        boolean find = false;
        float xCoord, yCoord; //координаты точки появления бомбы

        xCoord = random.nextInt(xMax - xMin) + xMin;
        yCoord = 0;

        for(Bomb b : bombs) {
            if (b.getVisibility() == false) {
                b.relocate(xCoord, yCoord);
                find = true;
                break;
            }
        }

        if (find == false) {
            Bomb bomb = new Bomb(image, explosions, groundExplosions, destruction);
            bomb.relocate(xCoord, yCoord);
            bombs.add(bomb);
        }
    }
}
