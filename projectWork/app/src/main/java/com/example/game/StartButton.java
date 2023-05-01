package com.example.game;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

// класс для кнопки начала игры, а также для вывода названия игры
public class StartButton {
    private final String gameName = "Зенитчик";
    private Bitmap image;
    private Paint fontPaint; //шрифт для названия игры
    private float x, y; //координаты расположения кнопки начала игры
    private float xGameName, yGameName; //координаты расположения текста названия игры
    private int width, height; //ширина и высота кнопки
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels; //ширина экрана
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels; //высота экрана

    public StartButton (Bitmap bmp) {
        image = bmp;
        fontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fontPaint.setTextSize(120);
        fontPaint.setStyle(Paint.Style.FILL);
        fontPaint.setColor(0xFF033400);
        width = bmp.getWidth();
        height = bmp.getHeight();
        x = screenWidth / 2 - width / 2;
        y = screenHeight / 2 - height / 2;
        xGameName = screenWidth / 2 - fontPaint.measureText(gameName) / 2;
        yGameName = screenHeight / 4;
    }

    //рисует название игры и кнопку
    public void draw(Canvas canvas) {
        canvas.drawText(gameName, xGameName, yGameName, fontPaint);
        canvas.drawBitmap(image, x, y, null);
    }

    //проверяет, нажата ли кнопка
    public boolean isButtonClicked(MotionEvent event) {
        if (((event.getX() > x) && (event.getX() < x + width)) && ((event.getY() > y) && (event.getY() < y + height))) {
            //кнопка нажата
            return true;
        }
        //кнопка не нажата
        return false;
    }
}
