package com.example.game.objectCollections;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.example.game.objects.Projectile;

import java.util.ArrayList;
import java.util.Random;

//коллекция зенитных снарядов
public class Projectiles {
    private final float powerCoef = 1; //коэффициент увеличения эффективного радиуса поражения зенитного снаряда
    private final float dispersionCoef = (float)1.5; //коэффициент разброса зенитных снарядов при автоматическом огне
    private final Random random = new Random();
    private Bitmap image;
    private ArrayList<Projectile> projectiles = new ArrayList<>();
    private Explosions antiAirExplosions;
    private Bombs bombs;
    private Gbus gbus;
    private Rockets rockets;
    private RocketPenaids rocketPenaids;
    private AntiAirRockets antiAirRockets;
    private float projectileExplosionRadius; //усреднённый радиус взрыва зенитного снаряда
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels; //ширина экрана

    public Projectiles(Bitmap bmp, Explosions antiAirExplosions, Bombs bombs, Gbus gbus, Rockets rockets, RocketPenaids rocketPenaids, AntiAirRockets antiAirRockets) {
        image = bmp;
        this.antiAirExplosions = antiAirExplosions;
        this.bombs = bombs;
        this.gbus = gbus;
        this.rockets = rockets;
        this.rocketPenaids = rocketPenaids;
        this.antiAirRockets = antiAirRockets;
        projectileExplosionRadius = (antiAirExplosions.getWidth() + antiAirExplosions.getHeight()) / 4 * powerCoef;
    }

    public void newProjectile(float barrelBaseX, float barrelBaseY, int barrelHeight, float degrees, float xDestiny, float yDestiny) {
        boolean find = false;

        for(Projectile bf : projectiles) {
            if (bf.getVisibility() == false) {
                bf.relocate(barrelBaseX, barrelBaseY, barrelHeight, degrees, xDestiny, yDestiny);
                find = true;
                break;
            }
        }

        if (find == false) {
            Projectile projectile = new Projectile(image, antiAirExplosions, bombs, gbus, rockets, rocketPenaids, antiAirRockets, projectileExplosionRadius);
            projectile.relocate(barrelBaseX, barrelBaseY, barrelHeight, degrees, xDestiny, yDestiny);
            projectiles.add(projectile);
        }
    }

    public void draw(Canvas canvas) {
        for(Projectile p : projectiles) {
            if (p.getVisibility()) {
                p.draw(canvas);
            }
        }
    }

    public void update() {
        for(Projectile p : projectiles) {
            if (p.getVisibility()) {
                p.update();
            }
        }
    }

    //возвращает высоту зенитного снаряда
    public int getProjectileHeight() {
        return image.getHeight();
    }

    //возвращает новую случайаную x-координату для автоматической стрельбы
    public int getNewXCoordForAutomaticFire(float xCoord) {
        int xBegin = (int)(xCoord - projectileExplosionRadius / dispersionCoef); // начальная x-координата области
        int xEnd = (int)(xCoord + projectileExplosionRadius / dispersionCoef); // конечная x-координата области

        if (xBegin < 0) {
            xBegin = 0;
        }

        if (xEnd > screenWidth) {
            xEnd = screenWidth;
        }

        return random.nextInt(xEnd - xBegin + 1) + xBegin;
    }

    //возвращает новую случайаную y-координату для автоматической стрельбы
    public int getNewYCoordForAutomaticFire(float yCoord, float buildingsLevel) {
        int yBegin = (int)(yCoord - projectileExplosionRadius / dispersionCoef); // начальная y-координата области
        int yEnd = (int)(yCoord + projectileExplosionRadius / dispersionCoef); // конечная y-координата области

        if (yBegin < 0) {
            yBegin = 0;
        }

        if (yEnd > (int)buildingsLevel) {
            yEnd = (int)buildingsLevel;
        }

        return random.nextInt(yEnd - yBegin + 1) + yBegin;
    }

    //приводит зенитные снаряды к первоначальному состоянию
    public void clear() {
        for(Projectile p : projectiles) {
            p.clear();
        }
    }
}
