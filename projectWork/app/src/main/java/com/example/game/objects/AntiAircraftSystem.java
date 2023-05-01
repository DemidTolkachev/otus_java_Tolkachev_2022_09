package com.example.game.objects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.game.Statistics;
import com.example.game.objectCollections.AntiAirRockets;

//зенитно-ракетный комплекс (ЗРК)
public class AntiAircraftSystem {
    private final int timer = 150; //количество обновлений, через которое появляется ракета ЗРК
    private final int xRocketCorrection = -5; //корректировка x-координаты появления зенитной ракеты относительно ЗРК
    private final int yRocketCorrection = 28; //корректировка y-координаты появления зенитной ракеты относительно ЗРК
    private final int xInsideDispersion = 20; //дополнительный разброс x-координат для упрощения нажатия на ЗРК с целью пуска ракеты
    private Bitmap enabledImage; //изображение рабочего ЗРК
    private Bitmap disabledImage; //изображение уничтоженного ЗРК
    private AntiAirRockets antiAirRockets;
    private Statistics statistics;
    private float x, y; //координаты расположения ЗРК
    private int number; //номер ЗРК
    private int timeLeft; //количество обновлений, оставшееся до появления ракеты ЗРК
    private boolean enabled; //целостность ЗРК: true - если цел, false - если разрушен
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels; //высота экрана

    public AntiAircraftSystem (Bitmap enabledImage, Bitmap disabledImage, AntiAirRockets antiAirRockets, Statistics statistics, float x, int number) {
        this.enabledImage = enabledImage;
        this.disabledImage = disabledImage;
        this.antiAirRockets = antiAirRockets;
        this.statistics = statistics;
        this.x = x;
        this.y = screenHeight - enabledImage.getHeight();
        this.number = number;
        clear();
    }

    //приводит ЗРК к первоначальному состоянию
    public void clear() {
        timeLeft = 1;
        enabled = true;
    }

    // рисует ЗРК
    public void draw(Canvas canvas) {
        //если ЗРК цел
        if (enabled) {
            canvas.drawBitmap(enabledImage, x, y, null);
        }
        else {
            canvas.drawBitmap(disabledImage, x, y, null);
        }
    }

    public void update() {
        //если ЗРК цел
        if (enabled) {
            //новая ракета ЗРК ещё не загрузилась в установку
            if (timeLeft > 0) {
                timeLeft--;
            }
            //новую ракету ЗРК надо загрузить в установку
            else if (timeLeft == 0) {
                antiAirRockets.newAntiAirRockets(x - xRocketCorrection, y - yRocketCorrection, number);
                timeLeft = -1;
            }
        }
    }

    //определяет по горизонтали, было ли нажатие на ЗРК
    public boolean wasPressed(float x) {
        //если ЗРК цел
        if (enabled) {
            if ((x >= this.x - xInsideDispersion) && (x <= this.x + enabledImage.getWidth() + xInsideDispersion)) {
                return true;
            }
        }

        return false;
    }

    //определяет, находится ли x-координата внутри ЗРК
    public boolean xInside(float x) {
        if ((x >= this.x) && (x < this.x + enabledImage.getWidth())) {
            return true;
        }

        return false;
    }

    //запускает ракету ЗРК из установки
    public void launch() {
        if (antiAirRockets.launch(number)) {
            timeLeft = timer;
            //увеличиваем количество потраченных ракет ЗРК
            statistics.increaseRocketsSpent();
        }
    }

    //возвращает x-координату центра ЗРК
    public float getXCenterCoord() {
        return x + enabledImage.getWidth() / 2;
    }

    //уничтожает ЗРК
    public void destroyAntiAircraftSystem() {
        //напоследок выпускаем ракету, если можем
        launch();
        enabled = false;
    }

    //возвращает признак целостности ЗРК
    public boolean getEnabled() {
        return enabled;
    }
}
