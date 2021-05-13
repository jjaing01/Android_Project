package kr.ac.kpu.game.s2015182030.dragonproject.game;

import android.graphics.Canvas;
import android.graphics.RectF;

import kr.ac.kpu.game.s2015182030.dragonproject.R;
import kr.ac.kpu.game.s2015182030.dragonproject.framework.AnimationGameBitmap;
import kr.ac.kpu.game.s2015182030.dragonproject.framework.BoxCollidable;
import kr.ac.kpu.game.s2015182030.dragonproject.framework.GameBitmap;
import kr.ac.kpu.game.s2015182030.dragonproject.framework.GameObject;

public class Player implements GameObject, BoxCollidable {
    private static final String TAG = Player.class.getSimpleName();
    private static final int BULLET_SPEED = 1500;
    private static final float FIRE_INTERVAL = 1.0f / 7.5f;
    private static final float LASER_DURATION = FIRE_INTERVAL / 3;
    private static final float FRAMES_PER_SECOND = 8.0f;

    private GameBitmap planeBitmap;
    private GameBitmap fireBitmap;
    private GameBitmap fireBitmap2;
    private GameBitmap fireBitmap3;
    private GameBitmap fireBitmap4;
    private GameBitmap fireBitmap5;

    private float fireTime;

    private float speed;
    private float tx,ty;
    private float x,y;

    private int bulletLevel;
    private int life;
    private int bulletNum;
    private int coin;

    public Player(float x, float y) {
        this.x = x;
        this.y = y;
        this.tx = x;
        this.ty = 0;
        this.speed = 2000;

        this.life = 3;
        this.bulletLevel = 1;
        this.bulletNum = 1;
        this.coin = 0;

        this.planeBitmap = new AnimationGameBitmap(R.mipmap.player,FRAMES_PER_SECOND,4);
        this.fireTime = 0.0f;
    }

    public void moveTo(float x, float y) {
        this.tx = x;
    }

    public void setCoin(int n) {
        this.coin += n;
    }

    public void setLife(int n) {
        this.life += n;

        if(this.life > 3)
            this.life = 3;
        if(this.life < 1)
            this.life = 0;
    }

    public void setBulletLevel(int n) {
        this.bulletLevel += n;

        if(this.bulletLevel > 5)
            this.bulletLevel = 5;
        if(this.bulletLevel < 1)
            this.bulletLevel = 1;
    }

    public void setBulletNum(int n) {
        this.bulletNum += n;

        if(this.bulletNum > 2)
            this.bulletNum = 2;
        if(this.bulletNum < 1)
            this.bulletNum = 1;
    }

    @Override
    public void getBoundingRect(RectF rect) {
        planeBitmap.getBoundingRect(x, y, rect);
    }

    @Override
    public void update() {
        MainGame game = MainGame.get();
        float dx = speed * game.frameTime;

        if (tx < x) { // move left
            dx = -dx;
        }

        x += dx;
        if ((dx > 0 && x > tx) || (dx < 0 && x < tx)) {
            x = tx;
        }

        fireTime += game.frameTime;

        if (fireTime >= FIRE_INTERVAL) {
            fireBullet();
            fireTime -= FIRE_INTERVAL;
        }
    }

    private void fireBullet() {
        MainGame game = MainGame.get();

        if(this.bulletNum > 1) {
            Bullet bullet1 = Bullet.get(this.bulletLevel, this.x - 50, this.y, BULLET_SPEED);
            game.add(MainGame.Layer.bullet, bullet1);

            Bullet bullet2 = Bullet.get(this.bulletLevel, this.x + 50, this.y, BULLET_SPEED);
            game.add(MainGame.Layer.bullet, bullet2);
        }
        else {
            Bullet bullet = Bullet.get(this.bulletLevel, this.x, this.y, BULLET_SPEED);
            game.add(MainGame.Layer.bullet, bullet);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        planeBitmap.drawSize(canvas, x, y,3);
    }
}
