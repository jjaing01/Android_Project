package kr.ac.kpu.game.s2015182030.dragonproject.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Choreographer;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import kr.ac.kpu.game.s2015182030.dragonproject.R;
import kr.ac.kpu.game.s2015182030.dragonproject.framework.GameObject;
import kr.ac.kpu.game.s2015182030.dragonproject.framework.Sound;
import kr.ac.kpu.game.s2015182030.dragonproject.game.MainGame;

public class GameView extends View {
    private static final String TAG = GameView.class.getSimpleName();

    public static float MULTIPLIER = 2;
    private boolean running;

    private long lastFrame;
    public static GameView view;
    public MediaPlayer mediaPlayer;

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        GameView.view = this;
        Sound.init(context);
        running = true;
        mediaPlayer= MediaPlayer.create(context, R.raw.bgm);
//        startUpdating();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        MainGame game = MainGame.get();
        boolean justInitialized = game.initResources();
        if (justInitialized) {
            requestCallback();
        }
    }

    private void update() {
        MainGame game = MainGame.get();
        game.update();
        invalidate();
    }

    private void requestCallback() {
        if (!running) {
            //Log.d(TAG, "Not running. Not calling Choreographer.postFrameCallback()");
            return;
        }
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long time) {
                if (lastFrame == 0) {
                    lastFrame = time;
                }
                MainGame game = MainGame.get();
                game.frameTime = (float) (time - lastFrame) / 1_000_000_000;
                update();
                lastFrame = time;
                requestCallback();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        MainGame game = MainGame.get();
        game.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        MainGame game = MainGame.get();

        if(running==false) {
            game.resetPlayer();

            ArrayList<GameObject> ending = game.getEnding();
            for (GameObject o : ending) {
                game.remove(o);
            }

            resumeGame();
        }

        return game.onTouchEvent(event);
    }

    public void pauseGame() {
        running = false;
    }

    public void resumeGame() {
        if (!running) {
            running = true;
            lastFrame = 0;
            requestCallback();
        }
    }
}
