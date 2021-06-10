package kr.ac.kpu.game.s2015182030.dragonproject.game;

import android.graphics.Canvas;
import android.graphics.RectF;

import kr.ac.kpu.game.s2015182030.dragonproject.R;
import kr.ac.kpu.game.s2015182030.dragonproject.framework.AnimationGameBitmap;
import kr.ac.kpu.game.s2015182030.dragonproject.framework.BoxCollidable;
import kr.ac.kpu.game.s2015182030.dragonproject.framework.GameBitmap;
import kr.ac.kpu.game.s2015182030.dragonproject.framework.GameObject;
import kr.ac.kpu.game.s2015182030.dragonproject.framework.Recyclable;
import kr.ac.kpu.game.s2015182030.dragonproject.ui.view.GameView;

public class Item implements GameObject, BoxCollidable, Recyclable {
    private static final String TAG = Item.class.getSimpleName();

    private static final int[] RESOURCE_IDS = {
            R.mipmap.itemcoin, R.mipmap.itemdualshot, R.mipmap.itemfinal, R.mipmap.itemhp, R.mipmap.itembomb
    };

    private float x,y;
    private int speed;
    private int level;
    private GameBitmap bitmap;

    private Item(int lev, float x, float y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = -speed;
        this.level = lev;

        int resId = RESOURCE_IDS[level - 1];
        this.bitmap = new GameBitmap(resId);
    }

    public static Item get(int lev, float x, float y, int speed) {
        MainGame game = MainGame.get();
        Item item = (Item) game.get(Item.class);

        if (item == null) {
            return new Item(lev, x, y, speed);
        }

        item.init(lev, x, y, speed);
        return item;
    }

    private void init(int lev, float x, float y, int speed) {
        this.x = x;
        this.y = y;
        this.speed = -speed;
        this.level = lev;

        // 0: coin, 1:DualShot, 2:bullet Level, 3: Life 4: SKill
        int resId = RESOURCE_IDS[level - 1];
        this.bitmap = new GameBitmap(resId);
    }

    public int getLevel() {
        return level - 1;
    }

    @Override
    public void getBoundingRect(RectF rect) {
        bitmap.getBoundingRect(x, y, rect);
    }

    @Override
    public void update() {
        MainGame game = MainGame.get();
        y -= speed * game.frameTime;

        if (y > GameView.view.getHeight() || y < 0
                || x > GameView.view.getWidth() || x < 0) {
            game.remove(this);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if(this.level-1 == 4)
            bitmap.drawSize(canvas, x, y,1);
        else
            bitmap.drawSize(canvas, x, y,3);
    }

    @Override
    public void recycle() {

    }
}
