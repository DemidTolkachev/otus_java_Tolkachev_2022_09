package com.example.game.objectCollections;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.game.objects.BarrelFire;

import java.util.ArrayList;

//коллекция огней выстрела зенитной пушки
public class BarrelFires {
    private Bitmap image;
    private ArrayList<BarrelFire> barrelFires = new ArrayList<>();

    public BarrelFires(Bitmap bmp) {
        image = bmp;
    }

    public void newBarrelFire(float barrelBaseX, float barrelBaseY, int barrelHeight, float degrees) {
        boolean find = false;

        for(BarrelFire bf : barrelFires) {
            if (bf.getVisibility() == false) {
                bf.relocate(barrelBaseX, barrelBaseY, barrelHeight, degrees);
                find = true;
                break;
            }
        }

        if (find == false) {
            BarrelFire barrelFire = new BarrelFire(image);
            barrelFire.relocate(barrelBaseX, barrelBaseY, barrelHeight, degrees);
            barrelFires.add(barrelFire);
        }
    }

    public void draw(Canvas canvas) {
        for(BarrelFire bf : barrelFires) {
            if (bf.getVisibility()) {
                bf.draw(canvas);
            }
        }
    }

    public void update() {
        for(BarrelFire bf : barrelFires) {
            if (bf.getVisibility()) {
                bf.update();
            }
        }
    }

    //приводит огни выстрела зенитной пушки к первоначальному состоянию
    public void clear() {
        for (BarrelFire bf : barrelFires) {
            bf.clear();
        }
    }
}
