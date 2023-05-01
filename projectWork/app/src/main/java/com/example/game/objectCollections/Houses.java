package com.example.game.objectCollections;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.game.objects.House;

import java.util.ArrayList;

//коллекция домов
public class Houses {
    private Bitmap enabledImage; //изображение целого дома
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels; //ширина экрана
    private ArrayList<House> houses = new ArrayList<>();

    public Houses(Bitmap enabledImage, Bitmap disabledImage, int antiAircraftSystemWidth, int antiAircraftGunWidth) {
        this.enabledImage = enabledImage;
        int houseWidth = enabledImage.getWidth();
        int halfOfHousesCountInMiddle = (int)Math.floor((screenWidth - 2 * houseWidth - 2 * antiAircraftSystemWidth - antiAircraftGunWidth) / (2 * houseWidth));
        int xCoord = houseWidth + antiAircraftSystemWidth;
        houses.add(new House(enabledImage, disabledImage, 0));

        for (int i = 0; i < halfOfHousesCountInMiddle; i++) {
            houses.add(new House(enabledImage, disabledImage, xCoord));
            xCoord += houseWidth;
        }
        xCoord += antiAircraftGunWidth;

        for (int i = 0; i < halfOfHousesCountInMiddle; i++) {
            houses.add(new House(enabledImage, disabledImage, xCoord));
            xCoord += houseWidth;
        }
        xCoord += antiAircraftSystemWidth;
        houses.add(new House(enabledImage, disabledImage, xCoord));
    }

    public void draw(Canvas canvas) {
        for (House h : houses) {
            h.draw(canvas);
        }
    }

    //возвращает ширину дома
    public int getWidth() {
        return enabledImage.getWidth();
    }

    //проверяет, будет ли при попадании по переданной x-координате уничтожен какой-либо дом
    public Float isAnyDestruction(float x) {
        for (House h : houses) {
            if (h.getEnabled()) {
                if (h.xInside(x)) {
                    h.destroyHouse();
                    return h.getXCenterCoord();
                }
            }
        }

        return null;
    }

    //приводит дома к первоначальному состоянию
    public void clear() {
        for (House h : houses) {
            h.clear();
        }
    }

    //проверяет, все ли дома разрушены
    public boolean isAllHousesDestroyed() {
        for (House h : houses) {
            if (h.getEnabled()) {
                return false;
            }
        }

        return true;
    }
}
