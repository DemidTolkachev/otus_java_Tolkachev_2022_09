package com.example.game.objects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

//дом
public class House {
    private Bitmap enabledImage; //изображение целого дома
    private Bitmap disabledImage; //изображение разрушенного дома
    private float x, y; //координаты расположения дома
    private boolean enabled; //целостность дома: true - если цел, false - если разрушен
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels; //высота экрана

    public House (Bitmap enabledImage, Bitmap disabledImage, float x) {
        this.enabledImage = enabledImage;
        this.disabledImage = disabledImage;
        this.x = x;
        this.y = screenHeight - enabledImage.getHeight();
        clear();
    }

    //приводит дом к первоначальному состоянию
    public void clear() {
        enabled = true;
    }

    // рисует дом
    public void draw(Canvas canvas) {
        //если дом цел
        if (enabled) {
            canvas.drawBitmap(enabledImage, x, y, null);
        }
        else {
            canvas.drawBitmap(disabledImage, x, y, null);
        }
    }

    //определяет, находится ли x-координата внутри дома
    public boolean xInside(float x) {
        if ((x >= this.x) && (x < this.x + enabledImage.getWidth())) {
            return true;
        }

        return false;
    }

    //возвращает x-координату центра дома
    public float getXCenterCoord() {
        return x + enabledImage.getWidth() / 2;
    }

    //уничтожает дом
    public void destroyHouse() {
        enabled = false;
    }

    //возвращает признак целостности дома
    public boolean getEnabled() {
        return enabled;
    }
}
