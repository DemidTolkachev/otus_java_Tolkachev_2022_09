package com.example.game.objectCollections;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.game.Statistics;
import com.example.game.objects.AntiAircraftSystem;

import java.util.ArrayList;

//коллекция зенитно-ракетных комплексов (ЗРК)
public class AntiAircraftSystems {
    private Bitmap enabledImage; //изображение рабочего ЗРК
    private ArrayList<AntiAircraftSystem> antiAircraftSystems = new ArrayList<>();
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels; //ширина экрана

    public AntiAircraftSystems(Bitmap enabledImage) {
        this.enabledImage = enabledImage;
    }

    public void init(Bitmap disabledImage, AntiAirRockets antiAirRockets, Statistics statistics, int houseWidth) {
        antiAircraftSystems.add(new AntiAircraftSystem(enabledImage, disabledImage, antiAirRockets, statistics, houseWidth, 1));
        antiAircraftSystems.add(new AntiAircraftSystem(enabledImage, disabledImage, antiAirRockets, statistics, screenWidth - houseWidth - getWidth(), 2));
    }

    public void draw(Canvas canvas) {
        for (AntiAircraftSystem aas : antiAircraftSystems) {
            aas.draw(canvas);
        }
    }

    public void update() {
        for (AntiAircraftSystem aas : antiAircraftSystems) {
            aas.update();
        }
    }

    //запускает ракету ЗРК из установки, в области которой было касание экрана
    public void launch(float x) {
        for (AntiAircraftSystem aas : antiAircraftSystems) {
            if (aas.wasPressed(x)) {
                aas.launch();
                break;
            }
        }
    }

    //возвращает ширину ЗРК
    public int getWidth() {
        return enabledImage.getWidth();
    }

    //проверяет, будет ли при попадании по переданной x-координате уничтожен какой-либо ЗРК
    public Float isAnyDestruction(float x) {
        for (AntiAircraftSystem aas : antiAircraftSystems) {
            if (aas.getEnabled()) {
                if (aas.xInside(x)) {
                    aas.destroyAntiAircraftSystem();
                    return aas.getXCenterCoord();
                }
            }
        }

        return null;
    }

    //приводит ЗРК к первоначальному состоянию
    public void clear() {
        for (AntiAircraftSystem aas : antiAircraftSystems) {
            aas.clear();
        }
    }
}
