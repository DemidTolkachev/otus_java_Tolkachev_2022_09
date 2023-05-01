package com.example.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.example.game.objectCollections.AntiAirRockets;
import com.example.game.objectCollections.AntiAircraftSystems;
import com.example.game.objectCollections.BarrelFires;
import com.example.game.objectCollections.Bombs;
import com.example.game.objectCollections.Explosions;
import com.example.game.objectCollections.Gbus;
import com.example.game.objectCollections.Houses;
import com.example.game.objectCollections.Projectiles;
import com.example.game.objectCollections.RocketPenaids;
import com.example.game.objectCollections.Rockets;

//окончание игры
public class GameOver {
    private final String youLose = "Игра окончена!";
    private final String youResults = "Ваши достижения:";
    private Bitmap image;
    private Statistics statistics;
    private AntiAircraftSystems antiAircraftSystems;
    private AntiAirRockets antiAirRockets;
    private BarrelFires barrelFires;
    private Bombs bombs;
    private Explosions antiAirExplosions; //коллекция взрывов зенитных снарядов
    private Explosions explosions; //коллекция взрывов ракет
    private Explosions groundExplosions; //коллекция наземных взрывов
    private Explosions destructionExplosions; //коллекция взрывов от разрушения наземных объектов
    private Gbus gbus;
    private Houses houses;
    private Projectiles projectiles;
    private RocketPenaids rocketPenaids;
    private Rockets rockets;
    private Paint fontPaintYouLose; //шрифт для "Игра окончена!"
    private Paint fontPaintYouResults; //шрифт для "Ваши достижения:"
    private float x, y; //координаты расположения кнопки ОК
    private float xYouLose, yYouLose; //координаты расположения текста "Игра окончена!"
    private float xYouResults, yYouResults; //координаты расположения текста "Ваши достижения:"
    private int width, height; //ширина и высота кнопки ОК
    private boolean isGameOver; //флаг, обозначающий, закончена ли игра
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels; //ширина экрана
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels; //высота экрана

    public GameOver(Bitmap bmp, Statistics statistics, AntiAircraftSystems antiAircraftSystems, AntiAirRockets antiAirRockets, BarrelFires barrelFires, Bombs bombs, Explosions antiAirExplosions, Explosions explosions, Explosions groundExplosions, Explosions destructionExplosions, Gbus gbus, Houses houses, Projectiles projectiles, RocketPenaids rocketPenaids, Rockets rockets) {
        image = bmp;
        this.statistics = statistics;
        this.antiAircraftSystems = antiAircraftSystems;
        this.antiAirRockets = antiAirRockets;
        this.barrelFires = barrelFires;
        this.bombs = bombs;
        this.antiAirExplosions = antiAirExplosions;
        this.explosions = explosions;
        this.groundExplosions = groundExplosions;
        this.destructionExplosions = destructionExplosions;
        this.gbus = gbus;
        this.houses = houses;
        this.projectiles = projectiles;
        this.rocketPenaids = rocketPenaids;
        this.rockets = rockets;
        fontPaintYouLose = new Paint(Paint.ANTI_ALIAS_FLAG);
        fontPaintYouLose.setTextSize(100);
        fontPaintYouLose.setStyle(Paint.Style.FILL);
        fontPaintYouLose.setColor(Color.RED);
        fontPaintYouResults = new Paint(Paint.ANTI_ALIAS_FLAG);
        fontPaintYouResults.setTextSize(50);
        fontPaintYouResults.setStyle(Paint.Style.FILL);
        fontPaintYouResults.setColor(Color.BLACK);
        width = bmp.getWidth();
        height = bmp.getHeight();
        x = screenWidth / 2 - width / 2;
        y = screenHeight - 10 - height;
        xYouLose = screenWidth / 2 - fontPaintYouLose.measureText(youLose) / 2;
        yYouLose = 110;
        xYouResults = 10;
        yYouResults = 170;
        isGameOver = false;
    }

    //инициализирует конец игры
    public void initGameOver() {
        isGameOver = true;
        statistics.setStaticticLevel(210);
    }

    //рисует всё, что нужно
    public void draw(Canvas canvas) {
        canvas.drawText(youLose, xYouLose, yYouLose, fontPaintYouLose);
        canvas.drawText(youResults, xYouResults, yYouResults, fontPaintYouResults);
        statistics.draw(canvas);
        canvas.drawBitmap(image, x, y, null);
    }

    //проверяет, нажата ли кнопка ОК, и выполняет необходимые действия
    public boolean isButtonClicked(MotionEvent event) {
        if (((event.getX() > x) && (event.getX() < x + width)) && ((event.getY() > y) && (event.getY() < y + height))) {
            isGameOver = false;
            statistics.setStaticticLevel(40);
            statistics.clear();
            antiAircraftSystems.clear();
            antiAirRockets.clear();
            barrelFires.clear();
            bombs.clear();
            antiAirExplosions.clear();
            explosions.clear();
            groundExplosions.clear();
            destructionExplosions.clear();
            gbus.clear();
            houses.clear();
            projectiles.clear();
            rocketPenaids.clear();
            rockets.clear();
            //кнопка нажата
            return true;
        }
        //кнопка не нажата
        return false;
    }

    //возвращает значение флага, обозначающего, закончена ли игра
    public boolean getIsGameOver() {
        return isGameOver;
    }
}
