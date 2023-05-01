package com.example.game.objectCollections;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.game.objects.Explosion;

import java.util.ArrayList;

//коллекция взрывов
public class Explosions {
    private Bitmap image;
    private ArrayList<Explosion> explosions = new ArrayList<>();

    public Explosions(Bitmap bmp) {
        image = bmp;
    }

    public void newExplosion(float xCoord, float yCoord) {
        //System.out.println("Взрывов: " + explosions.size());
        boolean find = false;

        for(Explosion e : explosions) {
            if (e.getVisibility() == false) {
                e.relocate(xCoord, yCoord);
                find = true;
                break;
            }
        }

        if (find == false) {
            Explosion explosion = new Explosion(image);
            explosion.relocate(xCoord, yCoord);
            explosions.add(explosion);
        }
    }

    public void draw(Canvas canvas) {
        for(Explosion e : explosions) {
            if (e.getVisibility()) {
                e.draw(canvas);
            }
        }
    }

    public void update() {
        for(Explosion e : explosions) {
            if (e.getVisibility()) {
                e.update();
            }
        }
    }

    //возвращает ширину взрыва
    public int getWidth() {
        return image.getWidth();
    }

    //возвращает высоту взрыва
    public int getHeight() {
        return image.getHeight();
    }

    //приводит взрывы к первоначальному состоянию
    public void clear() {
        for(Explosion e : explosions) {
            e.clear();
        }
    }
}
