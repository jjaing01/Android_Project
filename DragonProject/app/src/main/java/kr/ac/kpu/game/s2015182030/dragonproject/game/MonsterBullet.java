package kr.ac.kpu.game.s2015182030.dragonproject.game;

import android.graphics.Canvas;
import android.graphics.RectF;

import kr.ac.kpu.game.s2015182030.dragonproject.R;
import kr.ac.kpu.game.s2015182030.dragonproject.framework.BoxCollidable;
import kr.ac.kpu.game.s2015182030.dragonproject.framework.GameBitmap;
import kr.ac.kpu.game.s2015182030.dragonproject.framework.GameObject;
import kr.ac.kpu.game.s2015182030.dragonproject.framework.Recyclable;
import kr.ac.kpu.game.s2015182030.dragonproject.ui.view.GameView;

public class MonsterBullet implements GameObject, BoxCollidable, Recyclable {
    private static final String TAG = MonsterBullet.class.getSimpleName();
    private float x;
    private final GameBitmap bitmap;
    private float y;
    private int speed;
    private float angle;

    private MonsterBullet(float x, float y, int speed,float angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.speed = -speed;

        this.bitmap = new GameBitmap(R.mipmap.monbullet);
    }

    public static MonsterBullet get(float x, float y, int speed, float angle) {
        MainGame game = MainGame.get();
        MonsterBullet bullet = (MonsterBullet) game.get(MonsterBullet.class);

        if (bullet == null) {
            return new MonsterBullet(x, y, speed, angle);
        }

        bullet.init(x, y, speed, angle);
        return bullet;
    }

    private void init(float x, float y, int speed, float angle) {
        this.x = x;
        this.y = y;
        this.speed = -speed;
        this.angle = angle;
    }

    @Override
    public void getBoundingRect(RectF rect) {
        bitmap.getBoundingRect(x, y, rect);
    }

    @Override
    public void update() {
        MainGame game = MainGame.get();

        x += Math.cos(this.angle* 3.141592 / 180.f) * speed * game.frameTime;
        y -= Math.sin(this.angle* 3.141592 / 180.f) * speed * game.frameTime;

        if (y > GameView.view.getHeight() || y < 0
                || x > GameView.view.getWidth() || x < 0) {
            game.remove(this);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        bitmap.draw(canvas, x, y);
    }

    @Override
    public void recycle() {

    }
}
