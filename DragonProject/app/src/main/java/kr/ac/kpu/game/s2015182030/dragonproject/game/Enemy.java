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
    private float bossY;
    private float speed;
    private int hp,maxHp;
    private static final String TAG = Enemy.class.getSimpleName();
    private boolean isDead;
    private boolean isMakeItem;

    private float fireTime;



    private Enemy() {
        //Log.d(TAG,"Enemy constructor");
    }

    public static Enemy get(int level, int x, int y, float speed, int wave) {
        MainGame game = MainGame.get();
        Enemy enemy = (Enemy)game.get(Enemy.class);
        if(enemy == null){
            enemy = new Enemy();
            enemy.init(level, x, y, speed, wave);
            return enemy;
        }

        enemy.init(level, x, y, speed, wave);
        return enemy;
    }

    private void init(int level, int x, int y, float speed, int wave) {
        this.x = x;
        this.y = y;
        this.bossY = y;
        this.speed = speed;
        this.level = level;
        this.fireTime = 0.0f;

        this.hpBitmap= new AnimationGameBitmap(R.mipmap.monhp, FRAMES_PER_SECOND, 15);
        this.isDead = false;
        this.isMakeItem = false;

        // Normal Monster
        if(level == 1) {
            this.hp = 250 * wave;
            this.maxHp = 250 * wave;
        }
        else if(level == 2){
            this.hp = 1050 * wave;
            this.maxHp = 1050 * wave;
        }
        else if(level == 3){
            this.hp = 850 * wave;
            this.maxHp = 850 * wave;
        }
        // First Boss
       else if(level >= 4) {
           this.y = 0;
            this.hp = 30000 * wave;
            this.maxHp = 30000 * wave;
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

    private void fireBullet(int level) {
        if(level == 0) {
            MonsterBullet bullet = MonsterBullet.get(this.x, this.y, BULLET_SPEED,90);
            MainGame game = MainGame.get();
            game.add(MainGame.Layer.monsterBullet, bullet);
        }

        else if(level == 1){
            for(int i = 1; i < 4; ++i){
                MonsterBullet bullet = MonsterBullet.get(this.x, this.y, BULLET_SPEED,45*i);
                MainGame game = MainGame.get();
                game.add(MainGame.Layer.monsterBullet, bullet);
            }
        }

        else if(level == 2){
            for(int i = 0; i < 10; ++i){
                MonsterBullet bullet = MonsterBullet.get(this.x, this.y, 900,25*i);
                MainGame game = MainGame.get();
                game.add(MainGame.Layer.monsterBullet, bullet);
            }
        }
    }

    private void createItem() {
        Random randCreate = new Random();
        int isCreate = randCreate.nextInt(5)+1;
        if(isCreate < 4) return;

        Random r = new Random();
        int level = r.nextInt(5) + 1;

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
            if(y < bossY)
                y += speed * 0.3 * game.frameTime;
            else {
                x += speed * 0.3 * game.frameTime;

                if (x <= 200){
                    speed *= -1.0;
                    if(speed < 0)
                        speed *= -1.0;
                }
                else if(x > GameView.view.getWidth()-250){
                    speed *= -1.0;
                    if(speed > 0)
                        speed *= -1.0;
                }
//                if (x <= 200 || x > GameView.view.getWidth()-250) {
//                    speed *= -1.0;
//                }
            }

            fireTime += game.frameTime;
            if (fireTime >= FIRE_INTERVAL) {
                Random randLev = new Random();
                int lev=0;

                if((float)hp/(float)maxHp*100.f < 80 && (float)hp/(float)maxHp * 100.f > 50)
                    lev=randLev.nextInt(2);
                else if((float)hp/(float)maxHp * 100.f < 50)
                    lev=randLev.nextInt(3);

                fireBullet(lev);
                
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

            hpBitmap.drawMonsterHP(canvas,x,y+280,2,this.hp,this.maxHp);
            bitmap.drawSize(canvas,x,y,2);
        }
        // Normal Monster
        else {

            hpBitmap.drawMonsterHP(canvas,x,y+150,1,this.hp,this.maxHp);
            bitmap.drawSize(canvas, x, y,3);
        }
    }

    @Override
    public void recycle() {

    }
}
