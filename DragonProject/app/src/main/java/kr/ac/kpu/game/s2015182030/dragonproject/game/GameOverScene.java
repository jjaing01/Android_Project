package kr.ac.kpu.game.s2015182030.dragonproject.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;

import kr.ac.kpu.game.s2015182030.dragonproject.R;
import kr.ac.kpu.game.s2015182030.dragonproject.framework.GameBitmap;
import kr.ac.kpu.game.s2015182030.dragonproject.framework.GameObject;
import kr.ac.kpu.game.s2015182030.dragonproject.ui.view.GameView;

public class GameOverScene implements GameObject {
    private final GameBitmap bitmap;
    private static final String TAG = Player.class.getSimpleName();
    private Rect srcRect = new Rect();
    private RectF dstRect = new RectF();

    public GameOverScene(int resId, int speed) {
        this.bitmap = new GameBitmap(R.mipmap.end);
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {
        int tenth = GameView.view.getWidth()/10;
        bitmap.drawSize(canvas,tenth*5,tenth*8,5);

        GameView.view.pauseGame();
    }

}
