package kr.ac.kpu.game.s2015182030.dragonproject.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import kr.ac.kpu.game.s2015182030.dragonproject.R;
import kr.ac.kpu.game.s2015182030.dragonproject.framework.GameBitmap;
import kr.ac.kpu.game.s2015182030.dragonproject.framework.GameObject;
import kr.ac.kpu.game.s2015182030.dragonproject.ui.view.GameView;

public class Score implements GameObject {
    private final Bitmap bitmap;
    private final int right;
    private final int top;

    public void setScore(int score) {
        this.score = score;
        this.displayScore = score;
    }

    public void addScore(int amount) {
        this.score += amount;
    }

    private int score, displayScore;

    public Score(int right, int top) {
        bitmap = GameBitmap.load(R.mipmap.number);
        this.right = right;
        this.top = top;
    }
    @Override
    public void update() {
        if(displayScore < score){
            displayScore++;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        int value = this.displayScore;
        int nw = bitmap.getWidth() / 10;
        int nh = bitmap.getHeight();
        Rect src = new Rect();
        Rect dst = new Rect();
        int x = right;
        int dw = (int)(nw * GameView.MULTIPLIER);
        int dh = (int)(nh * GameView.MULTIPLIER);

        while (value>0) {
            int digit = value % 10;
            src.set(digit * nw, 0, (digit + 1) * nw, nh);
            x -= dw;
            dst.set(x,top,x + dw, top + dh);
            canvas.drawBitmap(bitmap,src,dst,null);

            value /= 10;
        }
    }
}