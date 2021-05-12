package kr.ac.kpu.game.s2015182030.dragonproject.framework;

import android.graphics.Canvas;
import android.graphics.Rect;

public class AnimationGameBitmap extends GameBitmap {
    private final int imageWidth;
    private final int imageHeight;
    private final int frameWidth;
    private final long createdOn;
    private int frameIndex;
    private final float framesPerSecond;
    private final int frameCount;

    protected Rect srcRect = new Rect();

    public AnimationGameBitmap(int resId, float framesPerSecond, int frameCount) {
        super(resId);
        imageWidth = bitmap.getWidth();
        imageHeight = bitmap.getHeight();

        if (frameCount == 0) {
            frameCount = imageWidth / imageHeight;
        }

        frameWidth = imageWidth / frameCount;

        this.framesPerSecond = framesPerSecond;
        this.frameCount = frameCount;

        createdOn = System.currentTimeMillis();
        frameIndex = 0;
    }

    public void draw(Canvas canvas, float x, float y) {
        int elapsed = (int)(System.currentTimeMillis() - createdOn);
        frameIndex = Math.round(elapsed * 0.001f * framesPerSecond) % frameCount;

        int fw = frameWidth;
        int h = imageHeight;
//        float hw = fw / 2 * GameView.MULTIPLIER;
//        float hh = h / 2 * GameView.MULTIPLIER;
        float hw = fw / 2 * 4;
        float hh = h / 2 * 4;

        srcRect.set(fw * frameIndex, 0, fw * frameIndex + fw, h);
        dstRect.set(x - hw, y - hh, x + hw, y + hh);
        canvas.drawBitmap(bitmap, srcRect, dstRect, null);
    }

    public void drawSize(Canvas canvas, float x, float y, int size) {
        int elapsed = (int)(System.currentTimeMillis() - createdOn);
        frameIndex = Math.round(elapsed * 0.001f * framesPerSecond) % frameCount;

        int fw = frameWidth;
        int h = imageHeight;

        float hw = fw / 2 * size;
        float hh = h / 2 * size;

        srcRect.set(fw * frameIndex, 0, fw * frameIndex + fw, h);
        dstRect.set(x - hw, y - hh, x + hw, y + hh);
        canvas.drawBitmap(bitmap, srcRect, dstRect, null);
    }

    public int getWidth() {
        return frameWidth;
    }

    public int getHeight() {
        return imageHeight;
    }
}