package kr.ac.kpu.game.s2015182030.dragonproject.game;

import android.graphics.Canvas;
import android.graphics.RectF;

import kr.ac.kpu.game.s2015182030.dragonproject.R;
import kr.ac.kpu.game.s2015182030.dragonproject.framework.BoxCollidable;
import kr.ac.kpu.game.s2015182030.dragonproject.framework.GameBitmap;
import kr.ac.kpu.game.s2015182030.dragonproject.framework.GameObject;
import kr.ac.kpu.game.s2015182030.dragonproject.framework.Recyclable;

public class Skill implements GameObject, BoxCollidable, Recyclable {
    private static final String TAG = Skill.class.getSimpleName();
    private float x,y;
    private GameBitmap bitmap;
    private int speed;
    private int power;

    private Skill(float x, float y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = -speed;
        this.power = 150;

        this.bitmap = new GameBitmap(R.mipmap.skill0);
    }

    public static Skill get(float x, float y, int speed) {
        MainGame game = MainGame.get();
        Skill skill = (Skill) game.get(Skill.class);

        if (skill == null) {
            return new Skill(x, y, speed);
        }

        skill.init(x, y, speed);
        return skill;
    }

    private void init(float x, float y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = -speed;
        this.power = 150;

        this.bitmap = new GameBitmap(R.mipmap.skill0);
    }

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
        bitmap.drawSize(canvas, x, y,5);
    }

    @Override
    public void recycle() {

    }
}
