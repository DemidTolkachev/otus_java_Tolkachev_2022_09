package com.example.game;

import android.content.res.Resources;

import com.example.game.objectCollections.AntiAircraftSystems;
import com.example.game.objectCollections.Explosions;
import com.example.game.objectCollections.Houses;

//разрушения наземных объектов
public class Destruction {
    private AntiAircraftGun antiAircraftGun;
    private AntiAircraftSystems antiAircraftSystems;
    private Houses houses;
    private Explosions destructionExplosions;
    private GameOver gameOver;
    private int yCoord; //y-координата взрывов
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels; //высота экрана

    public Destruction(Explosions destructionExplosions) {
        this.destructionExplosions = destructionExplosions;
        yCoord = screenHeight - destructionExplosions.getHeight() / 2;
    }

    //инициализация используемых объектов
    public void init(AntiAircraftGun antiAircraftGun, AntiAircraftSystems antiAircraftSystems, Houses houses, GameOver gameOver) {
        this.antiAircraftGun = antiAircraftGun;
        this.antiAircraftSystems = antiAircraftSystems;
        this.houses = houses;
        this.gameOver = gameOver;
    }

    //делаем разрушение какого-нибудь объекта, если он ещё не разрушен
    public void doDestruction(float x) {
        //если попали в зенитную пушку
        if (antiAircraftGun.xInMounting(x)) {
            destructionExplosions.newExplosion(antiAircraftGun.getBarrelBaseX(), yCoord);
            gameOver.initGameOver();
        }
        else {
            Float xCoord = antiAircraftSystems.isAnyDestruction(x);

            //если попали в ЗРК
            if (xCoord != null) {
                destructionExplosions.newExplosion(xCoord, yCoord);
            }
            else {
                xCoord = houses.isAnyDestruction(x);

                //если попали в дом
                if (xCoord != null) {
                    destructionExplosions.newExplosion(xCoord, yCoord);

                    if (houses.isAllHousesDestroyed()) {
                        gameOver.initGameOver();
                    }
                }
            }
        }
    }
}
