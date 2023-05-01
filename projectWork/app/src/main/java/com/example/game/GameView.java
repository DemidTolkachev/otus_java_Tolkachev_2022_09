package com.example.game;

import static android.util.DisplayMetrics.DENSITY_420;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

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

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    private Statistics statistics;
    private Explosions antiAirExplosions; //коллекция взрывов зенитных снарядов
    private Explosions explosions; //коллекция взрывов ракет
    private Explosions groundExplosions; //коллекция наземных взрывов
    private Explosions destructionExplosions; //коллекция взрывов от разрушения наземных объектов
    private Destruction destruction;
    private BarrelFires barrelFires;
    private Bombs bombs;
    private Gbus gbus;
    private Rockets rockets;
    private RocketPenaids rocketPenaids;
    private AntiAirRockets antiAirRockets;
    private AntiAircraftSystems antiAircraftSystems;
    private Houses houses;
    private Projectiles projectiles;
    private StartButton startButton;
    private GameOver gameOver;
    private AntiAircraftGun antiAircraftGun;
    private int gameStatus; //1 - игра идёт, 0 - игра не началась, 2 - игра закончилась
    private boolean automaticFire; //автоматический огонь из зенитной пушки
    private float xForAntiAircraftGun, yForAntiAircraftGun; //координаты области, в которую зенитная пушка будет вести автоматический огонь
    private float buildingsLevel; //y-координата, ниже которой располагаются строения и пушка с ЗРК
    private Paint skyPaint; //для рисования неба
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels; //высота экрана
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels; //ширина экрана

    public GameView(Context context) {
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);

        setFocusable(true);
        gameStatus = 0;
        automaticFire = false;
        xForAntiAircraftGun = 0;
        yForAntiAircraftGun = 0;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //игра идёт
        if (gameStatus == 1) {
            //если касание экрана выше уровня зенитной пушки + высота зенитного снаряда
            if (event.getY() < buildingsLevel) {
                xForAntiAircraftGun = event.getX();
                yForAntiAircraftGun = event.getY();

                //если автоматический огонь выключен
                if (automaticFire == false) {
                    antiAircraftGun.rotateBarrel(event.getX(), event.getY());
                    barrelFires.newBarrelFire(antiAircraftGun.getBarrelBaseX(), antiAircraftGun.getBarrelBaseY(), antiAircraftGun.getBarrelHeight(), antiAircraftGun.getDegrees());
                    projectiles.newProjectile(antiAircraftGun.getBarrelBaseX(), antiAircraftGun.getBarrelBaseY(), antiAircraftGun.getBarrelHeight(), antiAircraftGun.getDegrees(), event.getX(), event.getY());
                    statistics.increaseShellsSpent();
                }
            }
            //если касание экране в области зенитной пушки
            else if (antiAircraftGun.xInMounting(event.getX())) {
                //если автоматический огонь выключен - включим его, иначе выключим
                if (automaticFire == false) {
                    automaticFire = true;
                } else {
                    automaticFire = false;
                }
            }
            //иначе проверяем, не коснулись ли ЗРК, и запускаем зенитную ракету, если коснулись и можем её запустить
            else {
                antiAircraftSystems.launch(event.getX());
            }
        }
        //если игра не началась
        else if (gameStatus == 0) {
            //если кнопка начала игры нажата
            if (startButton.isButtonClicked(event)) {
                gameStatus = 1;
            }
        }
        //если игра закончена
        else if (gameStatus == 2) {
            //если кнопка ОК нажата
            if (gameOver.isButtonClicked(event)) {
                gameStatus = 0;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        statistics = new Statistics();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDensity = DENSITY_420;
        antiAirExplosions = new Explosions(BitmapFactory.decodeResource(getResources(), R.drawable.antiairexplosion, options));
        explosions = new Explosions(BitmapFactory.decodeResource(getResources(), R.drawable.explosion, options));
        groundExplosions = new Explosions(BitmapFactory.decodeResource(getResources(), R.drawable.groundexplosion, options));
        destructionExplosions = new Explosions(BitmapFactory.decodeResource(getResources(), R.drawable.destructionexplosion, options));
        destruction = new Destruction(destructionExplosions);
        startButton = new StartButton(BitmapFactory.decodeResource(getResources(), R.drawable.startbutton, options));
        antiAircraftGun = new AntiAircraftGun(BitmapFactory.decodeResource(getResources(), R.drawable.mounting, options), BitmapFactory.decodeResource(getResources(), R.drawable.barrel, options));
        barrelFires = new BarrelFires(BitmapFactory.decodeResource(getResources(), R.drawable.barrelfire, options));
        bombs = new Bombs(BitmapFactory.decodeResource(getResources(), R.drawable.bomb, options), explosions, groundExplosions, statistics, destruction);
        gbus = new Gbus(BitmapFactory.decodeResource(getResources(), R.drawable.gbu, options), explosions, groundExplosions, statistics, destruction);
        rockets = new Rockets(BitmapFactory.decodeResource(getResources(), R.drawable.rocket, options), explosions, groundExplosions, statistics, destruction);
        rocketPenaids = new RocketPenaids(BitmapFactory.decodeResource(getResources(), R.drawable.rocketpenaid, options), explosions, groundExplosions, statistics, destruction);
        antiAirRockets = new AntiAirRockets(BitmapFactory.decodeResource(getResources(), R.drawable.antiairrocket, options), explosions, bombs, gbus, rockets, rocketPenaids);
        antiAircraftSystems = new AntiAircraftSystems(BitmapFactory.decodeResource(getResources(), R.drawable.antiaircraftsystem, options));
        houses = new Houses(BitmapFactory.decodeResource(getResources(), R.drawable.house, options), BitmapFactory.decodeResource(getResources(), R.drawable.destroyedhouse, options), antiAircraftSystems.getWidth(), antiAircraftGun.getMountingWidth());
        projectiles = new Projectiles(BitmapFactory.decodeResource(getResources(), R.drawable.projectile, options), antiAirExplosions, bombs, gbus, rockets, rocketPenaids, antiAirRockets);
        gameOver = new GameOver(BitmapFactory.decodeResource(getResources(), R.drawable.okbutton, options), statistics, antiAircraftSystems, antiAirRockets, barrelFires, bombs, antiAirExplosions, explosions, groundExplosions, destructionExplosions, gbus, houses, projectiles, rocketPenaids, rockets);

        destruction.init(antiAircraftGun, antiAircraftSystems, houses, gameOver);
        antiAircraftSystems.init(BitmapFactory.decodeResource(getResources(), R.drawable.destroyedantiaircraftsystem, options), antiAirRockets, statistics, houses.getWidth());

        buildingsLevel = antiAircraftGun.getBarrelBaseY() - antiAircraftGun.getBarrelHeight() - projectiles.getProjectileHeight();

        //создаём градиент для неба
        LinearGradient skyGradient = new LinearGradient(0, buildingsLevel, 0, 0, 0xFFFFFFFF, 0xFF7FD2FC, android.graphics.Shader.TileMode.CLAMP);
        skyPaint = new Paint();
        skyPaint.setDither(true);
        skyPaint.setShader(skyGradient);

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    public void update() {
        //проверяем, не закончена ли игра
        if (gameOver.getIsGameOver()) {
            automaticFire = false;
            gameStatus = 2;
        }
        //игра идёт
        if (gameStatus == 1) {
            //если автоматический огонь включён
            if (automaticFire) {
                automaticFireDone();
            }
            barrelFires.update();
            bombs.update();
            gbus.update();
            rockets.update();
            rocketPenaids.update();
            antiAirRockets.update();
            antiAircraftSystems.update();
            projectiles.update();
            antiAirExplosions.update();
            explosions.update();
            groundExplosions.update();
            destructionExplosions.update();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            //игра не началась
            if (gameStatus == 0) {
                canvas.drawColor(Color.WHITE);
                startButton.draw(canvas);
            }
            //игра идёт
            else if (gameStatus == 1) {
                canvas.drawRect(0, 0, screenWidth, screenHeight, skyPaint);
                statistics.draw(canvas);
                antiAircraftGun.draw(canvas);
                barrelFires.draw(canvas);
                bombs.draw(canvas);
                gbus.draw(canvas);
                rockets.draw(canvas);
                rocketPenaids.draw(canvas);
                antiAirRockets.draw(canvas);
                antiAircraftSystems.draw(canvas);
                houses.draw(canvas);
                projectiles.draw(canvas);
                antiAirExplosions.draw(canvas);
                explosions.draw(canvas);
                groundExplosions.draw(canvas);
                destructionExplosions.draw(canvas);
            }
            //игра закончена
            else if (gameStatus == 2) {
                canvas.drawColor(Color.WHITE);
                gameOver.draw(canvas);
            }
        }
    }

    //ведём автоматический огонь из зенитной пушки
    private void automaticFireDone() {
        float x = projectiles.getNewXCoordForAutomaticFire(xForAntiAircraftGun);
        float y = projectiles.getNewYCoordForAutomaticFire(yForAntiAircraftGun, buildingsLevel);
        antiAircraftGun.rotateBarrel(x, y);
        barrelFires.newBarrelFire(antiAircraftGun.getBarrelBaseX(), antiAircraftGun.getBarrelBaseY(), antiAircraftGun.getBarrelHeight(), antiAircraftGun.getDegrees());
        projectiles.newProjectile(antiAircraftGun.getBarrelBaseX(), antiAircraftGun.getBarrelBaseY(), antiAircraftGun.getBarrelHeight(), antiAircraftGun.getDegrees(), x, y);
        statistics.increaseShellsSpent();
    }
}
