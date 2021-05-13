package kr.ac.kpu.game.s2015182030.dragonproject.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import kr.ac.kpu.game.s2015182030.dragonproject.R;
import kr.ac.kpu.game.s2015182030.dragonproject.framework.GameBitmap;
import kr.ac.kpu.game.s2015182030.dragonproject.framework.GameObject;
import kr.ac.kpu.game.s2015182030.dragonproject.ui.view.GameView;

public class Money implements GameObject {
    private final Bitmap bitmap;
    private final GameBitmap uiBitmap;
    private final int right;
    private final int top;

    private int money, displayMoney;
    private Rect src = new Rect();
    private RectF dst = new RectF();

    private int ShowDigit=0;

    public void setMoney(int score) {
        this.money = score;
        this.displayMoney = score;

    }

    public void addMoney(int amount) {
        this.money += amount;
    }


    public Money(int right, int top) {
        this.bitmap = GameBitmap.load(R.mipmap.number);
        this.uiBitmap =  new GameBitmap(R.mipmap.itemcoin);
        this.right = right;
        this.top = top;
    }
    @Override
    public void update() {
        MainGame game = MainGame.get();

        if (displayMoney < money) {
            displayMoney++;
        }

        int value = this.displayMoney;

        int TempD=0;

        while (value > 0) {
            value /= 10;
            TempD++;
        }
        ShowDigit=TempD;
    }

    @Override
    public void draw(Canvas canvas) {
        uiBitmap.drawSize(canvas,50,50,3);

        int value = this.displayMoney;
        int nw = bitmap.getWidth() / 10;
        int nh = bitmap.getHeight();
        int x = right;
        int dw = (int) (nw * GameView.MULTIPLIER);
        int dh = (int) (nh * GameView.MULTIPLIER);

        while (value > 0) {
            int digit = value % 10;
            src.set(digit * nw, 0, (digit + 1) * nw, nh);
            x -= dw;
            dst.set(x +(ShowDigit*50) , top, x + dw+(ShowDigit*50), top + dh);
            canvas.drawBitmap(bitmap, src, dst, null);
            value /= 10;
        }
    }
}