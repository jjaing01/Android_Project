package kr.ac.kpu.game.s2015182030.dragonproject.game;

import android.graphics.Canvas;
import android.util.Log;

import java.util.Random;

import kr.ac.kpu.game.s2015182030.dragonproject.framework.GameObject;
import kr.ac.kpu.game.s2015182030.dragonproject.ui.view.GameView;

public class EnemyGenerator implements GameObject {
    private static final float INITIAL_SPAWN_INTERVAL = 2.0f;
    private static final String TAG = EnemyGenerator.class.getSimpleName();
    private float time;
    private float spawnInterval;
    private int wave;
    private int bossWave;

    public EnemyGenerator() {
        time = INITIAL_SPAWN_INTERVAL;
        spawnInterval = INITIAL_SPAWN_INTERVAL;
        wave = 0;
    }

    @Override
    public void update() {
        MainGame game = MainGame.get();
        time += game.frameTime;
        if (time >= spawnInterval) {
            generate();
            time -= spawnInterval;
        }
    }

    public void initGenerate() {wave=0; bossWave = 0;}

    private void generate() {
        wave++;
        bossWave++;
        //Log.d(TAG,"wave:"+wave);

        int monsetting = bossWave / 10;
        if(monsetting < 1)
            monsetting = 1;

        MainGame game = MainGame.get();
        int tenth = GameView.view.getWidth() / 10;

        Random r = new Random();
        for (int i = 1; i <= 9; i += 2) {
            int x = tenth * i;
            int y = 0;

            int level = wave / 5 - r.nextInt(3);

            if (level < 1) level = 1;
            if (level > 3) level = 3;

            float spd = (float)level/2*1300;
            if(spd < 1000)
                spd = 1000.f;
            else if(spd > 2000.f)
                spd = 1500.f;

            Enemy enemy = Enemy.get(level, x, y, spd, monsetting);
            game.add(MainGame.Layer.enemy, enemy);
        }

        if(wave > 18)
            wave = 11;

        // First Boss
        if(bossWave % 10 == 0) {
            Enemy enemy = Enemy.get(4, 5 * tenth, 400, 1000, monsetting);
            game.add(MainGame.Layer.enemy, enemy);
        }
    }

    @Override
    public void draw(Canvas canvas) {

    }
}
