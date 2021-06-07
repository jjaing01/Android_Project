package kr.ac.kpu.game.s2015182030.dragonproject.game;

import android.graphics.Canvas;
import android.graphics.RectF;

import java.util.Random;

import kr.ac.kpu.game.s2015182030.dragonproject.R;
import kr.ac.kpu.game.s2015182030.dragonproject.framework.AnimationGameBitmap;
import kr.ac.kpu.game.s2015182030.dragonproject.framework.BoxCollidable;
import kr.ac.kpu.game.s2015182030.dragonproject.framework.GameBitmap;
import kr.ac.kpu.game.s2015182030.dragonproject.framework.GameObject;
import kr.ac.kpu.game.s2015182030.dragonproject.framework.Recyclable;
import kr.ac.kpu.game.s2015182030.dragonproject.ui.view.GameView;

public class Enemy implements GameObject, BoxCollidable, Recyclable {
    private static final int BULLET_SPEED = 1500;
    private static final float FIRE_INTERVAL = 1.0f / 1.5f;
    private static final float LASER_DURATION = FIRE_INTERVAL / 3;
    private static final float FRAMES_PER_SECOND = 8.0f;

    private static final int[] RESOURCE_IDS = {
            R.mipmap.enemy01, R.mipmap.enemy02, R.mipmap.enemy03,
            R.mipmap.boss01, R.mipmap.boss02, R.mipmap.boss03,
    };

    private GameBitmap bitmap;
    private AnimationGameBitmap hpBitmap;
    //private GameBitmap fireBitmap;

    private int level;
    private float x,y;
    private int speed;
    private int hp,maxHp;
    private static final String TAG = Enemy.class.getSimpleName();
    private boolean isDead;
    private boolean isMakeItem;

    private float fireTime;



    private Enemy() {
        //Log.d(TAG,"Enemy constructor");
    }

    public static Enemy get(int level, int x, int y, int speed) {
        MainGame game = MainGame.get();
        Enemy enemy = (Enemy)game.get(Enemy.class);
        if(enemy == null){
            enemy = new Enemy();
            enemy.init(level, x, y, speed);
            return enemy;
        }

        enemy.init(level, x, y, speed);
        return enemy;
    }

    private void init(int level, int x, int y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.level = level;
        this.fireTime = 0.0f;
        //this.fireBitmap = new GameBitmap(R.mipmap.monbullet);
        this.hpBitmap= new AnimationGameBitmap(R.mipmap.monhp, FRAMES_PER_SECOND, 15);
        this.isDead = false;
        this.isMakeItem = false;

        // Normal Monster
        if(level < 4) {
            this.hp = 249;
            this.maxHp = 250;
        }
        // First Boss
       else if(level >= 4) {
            this.hp = 250;
            this.maxHp = 250;
        }
        int resId = RESOURCE_IDS[level - 1];
        this.bitmap = new AnimationGameBitmap(resId, FRAMES_PER_SECOND, 4);
    }

    public void decreaseHp(int hp) {
        this.hp -= hp;
        if(this.hp <= 0) {
            this.hp = 0;
            isDead = true;
        }
    }

    public boolean getDead() {
        return isDead;
    }

    private void fireBullet() {
        MonsterBullet bullet = MonsterBullet.get(this.x, this.y, BULLET_SPEED);
        MainGame game = MainGame.get();
        game.add(MainGame.Layer.monsterBullet, bullet);
    }

    private void createItem() {
        Random r = new Random();
        int level = r.nextInt(4) + 1;

        Item item = Item.get(level, this.x, this.y, 1500);

        MainGame game = MainGame.get();
        game.add(MainGame.Layer.item, item);
    }

    @Override
    public void getBoundingRect(RectF rect) {
        bitmap.getBoundingRect(x, y, rect);
    }

    @Override
    public void update() {
        MainGame game = MainGame.get();

        if(isDead) {
            if(isMakeItem == false){
                createItem();
                isMakeItem = true;
            }

        }

        // First Boss
        if(level == 4) {
            x += speed * game.frameTime;

            if ( x <= 0 || x > GameView.view.getWidth()) {
                speed *= -1.0;
            }

            fireTime += game.frameTime;
            if (fireTime >= FIRE_INTERVAL) {
                fireBullet();
                
                fireTime -= FIRE_INTERVAL;
            }
        }
        // Normal Monster
        else {
            y += speed * game.frameTime;

            if (y > GameView.view.getHeight()) {
                game.remove(this);
            }
        }

    }

    @Override
    public void draw(Canvas canvas) {
        if(level == 4) {

            hpBitmap.drawMonsterHP(canvas,x,y+500,2,this.hp,this.maxHp);
            bitmap.drawSize(canvas,x,y,2);
        }
        // Normal Monster
        else {

            hpBitmap.drawMonsterHP(canvas,x,y+150,2,this.hp,this.maxHp);
            bitmap.drawSize(canvas, x, y,4);
        }
    }

    @Override
    public void recycle() {

    }
}
