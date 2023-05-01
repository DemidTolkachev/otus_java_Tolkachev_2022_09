package com.example.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

//Класс для сбора игровой статистики
public class Statistics {
    private Paint fontPaint; //шрифт для текста статистики
    private int shellsSpent; //потрачено снарядов
    private int rocketsSpent; //потрачено ракет ЗРК
    private int bombsShotDown; //сбито бомб
    private int gbusShotDown; //сбито КАБ
    private int rocketsShotDown; //сбито ракет
    private int rocketPenaidsShotDown; //сбито ракет c КСП ПРО
    private int scores; //игровые очки
    private String spentString; //строка для отображения снарядов и ракет, потраченных игроком
    private String shotDownString; //строка для отображения того, что игрок сбил
    private String scoresString; //строка для отображения игровых очков
    private int staticticLevel; //y-координата, с которой начинается отображение статистики

    public Statistics() {
        fontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fontPaint.setTextSize(30);
        fontPaint.setStyle(Paint.Style.FILL);
        fontPaint.setColor(Color.BLACK);
        staticticLevel = 40;
        clear();
    }

    //обнуляет статистику
    public void clear() {
        shellsSpent = 0;
        rocketsSpent = 0;
        bombsShotDown = 0;
        gbusShotDown = 0;
        rocketsShotDown = 0;
        rocketPenaidsShotDown = 0;
        scores = 0;
        updateSpentString();
        updateShotDownString();
        updateScoresString();
    }

    //тратим очередной снаряд зенитной пушки
    public void increaseShellsSpent() {
        shellsSpent++;
        updateSpentString();
        updateScoresString();
    }

    //тратим очередную ракету ЗРК
    public void increaseRocketsSpent() {
        rocketsSpent++;
        updateSpentString();
        updateScoresString();
    }

    //сбиваем очередную бомбу
    public void increaseBombsShotDown() {
        bombsShotDown++;
        updateShotDownString();
        updateScoresString();
    }

    //сбиваем очередную КАБ
    public void increaseGbusShotDown() {
        gbusShotDown++;
        updateShotDownString();
        updateScoresString();
    }

    //сбиваем очередную ракету
    public void increaseRocketsShotDown() {
        rocketsShotDown++;
        updateShotDownString();
        updateScoresString();
    }

    //сбиваем очередную ракету c КСП ПРО
    public void increaseRocketPenaidsShotDown() {
        rocketPenaidsShotDown++;
        updateShotDownString();
        updateScoresString();
    }

    //отображаем статистику на экране
    public void draw(Canvas canvas) {
        canvas.drawText(spentString, 10, staticticLevel, fontPaint);
        canvas.drawText(shotDownString, 10, staticticLevel + 40, fontPaint);
        canvas.drawText(scoresString, 10, staticticLevel + 80, fontPaint);
    }

    //устанавливаем y-координату, с которой начинается отображение статистики
    public void setStaticticLevel(int staticticLevel) {
        this.staticticLevel = staticticLevel;
    }

    //обновляем строку для отображения снарядов и ракет, потраченных игроком
    private void updateSpentString() {
        spentString = "Потрачено: снарядов - " + shellsSpent + ", ракет - " + rocketsSpent;
    }

    //обновляем строку для отображения того, что игрок сбил
    private void updateShotDownString() {
        shotDownString = "Сбито: бомб - " + bombsShotDown + ", КАБ - " + gbusShotDown + ", ракет - " + rocketsShotDown + ", ракет c КСП ПРО - " + rocketPenaidsShotDown;
    }

    //обновляем строку для отображения игровых очков
    private void updateScoresString() {
        if ((shellsSpent == 0) && (rocketsSpent == 0)) {
            scores = 0;
        }
        else {
            scores = (int)Math.round((float) (10 * bombsShotDown + 25 * gbusShotDown + 25 * rocketsShotDown + 50 * rocketPenaidsShotDown) / Math.pow(shellsSpent + 10 * rocketsSpent, 0.2));
        }
        scoresString = "Очки: " + scores;
    }
}
