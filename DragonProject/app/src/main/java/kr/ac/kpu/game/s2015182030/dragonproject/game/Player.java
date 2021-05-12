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
    private float ty;
    private float tx;
    private float x;
    private float y;

    public Player(float x, float y) {
        this.x = x;
        this.y = y;
        this.tx = x;
        this.ty = 0;
        this.speed = 800;

        this.planeBitmap = new AnimationGameBitmap(R.mipmap.player,FRAMES_PER_SECOND,4);
        this.fireBitmap = new GameBitmap(R.mipmap.bullet01);
        this.fireBitmap2 = new GameBitmap(R.mipmap.bullet02);
        this.fireBitmap3 = new GameBitmap(R.mipmap.bullet03);
        this.fireBitmap4 = new GameBitmap(R.mipmap.bullet04);
        this.fireBitmap5 = new GameBitmap(R.mipmap.bullet05);

        this.fireTime = 0.0f;
    }

    public void moveTo(float x, float y) {
        this.tx = x;
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
        Bullet bullet = Bullet.get(this.x, this.y, BULLET_SPEED);
        MainGame game = MainGame.get();
        game.add(MainGame.Layer.bullet, bullet);
    }

    @Override
    public void draw(Canvas canvas) {
        planeBitmap.draw(canvas, x, y);

        if (fireTime < LASER_DURATION) {
            fireBitmap.draw(canvas, x, y - 50);
        }

    }
}
