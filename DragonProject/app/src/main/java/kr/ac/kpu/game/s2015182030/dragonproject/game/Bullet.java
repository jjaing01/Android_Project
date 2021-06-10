package kr.ac.kpu.game.s2015182030.dragonproject.game;

import android.graphics.Canvas;
import android.graphics.RectF;

import kr.ac.kpu.game.s2015182030.dragonproject.R;
import kr.ac.kpu.game.s2015182030.dragonproject.framework.BoxCollidable;
import kr.ac.kpu.game.s2015182030.dragonproject.framework.GameBitmap;
import kr.ac.kpu.game.s2015182030.dragonproject.framework.GameObject;
import kr.ac.kpu.game.s2015182030.dragonproject.framework.Recyclable;

public class Bullet implements GameObject, BoxCollidable, Recyclable {
    private static final String TAG = Bullet.class.getSimpleName();
    private float x,y;
    private GameBitmap bitmap;
    private int level;
    private int speed;
    private int power;
    private int size;

    private static final int[] RESOURCE_IDS = {
            R.mipmap.bullet01, R.mipmap.bullet02, R.mipmap.bullet03, R.mipmap.bullet04, R.mipmap.bullet05,
    };

    private Bullet(int level, float x, float y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = -speed;
        this.level = level;

        if(this.level == 1) {
            this.power = 170;
            this.size = 4;
        }
        else if(this.level == 2) {
            this.power = 230;
            this.size = 4;
        }
        else if(this.level == 3) {
            this.power = 280;
            this.size = 4;
        }
        else if(this.level == 4) {
            this.power = 330;
            this.size = 5;
        }
        else if(this.level == 5) {
            this.power = 400;
            this.size = 5;
        }

        //this.bitmap = new GameBitmap(R.mipmap.bullet01);
        int resId = RESOURCE_IDS[level - 1];
        this.bitmap = new GameBitmap(resId);
    }

    public static Bullet get(int level, float x, float y, int speed) {
        MainGame game = MainGame.get();
        Bullet bullet = (Bullet) game.get(Bullet.class);

        if (bullet == null) {
            return new Bullet(level, x, y, speed);
        }

        bullet.init(level, x, y, speed);
        return bullet;
    }

    private void init(int level, float x, float y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = -speed;
        this.level = level;

        if(this.level == 1) {
            this.power = 50;
            this.size = 4;
        }
        else if(this.level == 2) {
            this.power = 80;
            this.size = 4;
        }
        else if(this.level == 3) {
            this.power = 150;
            this.size = 4;
        }
        else if(this.level == 4) {
            this.power = 220;
            this.size = 5;
        }
        else if(this.level == 5) {
            this.power = 300;
            this.size = 5;
        }

        int resId = RESOURCE_IDS[level - 1];
        this.bitmap = new GameBitmap(resId);
    }

    public int getBulletDamage() {return this.power;}

    @Override
    public void getBoundingRect(RectF rect) {
        bitmap.getBoundingRect(x, y, rect);
    }

    @Override
    public void update() {
        MainGame game = MainGame.get();
        y += speed * game.frameTime;

        if (y < 0) {
            game.remove(this);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        bitmap.draw(canvas, x, y);

        if(this.level == 1 || this.level == 2) {
            bitmap.drawSize(canvas, x, y,2);
        }
        else if(this.level == 3) {
            bitmap.drawSize(canvas, x, y,3);
        }
        else if(this.level == 4) {
            bitmap.drawSize(canvas, x, y,4);
        }
        else if(this.level == 5) {
            bitmap.drawSize(canvas, x, y,4);
        }
    }

    @Override
    public void recycle() {

    }
}
