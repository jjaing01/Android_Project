package kr.ac.kpu.game.s2015182030.dragonproject.game;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.HashMap;

import kr.ac.kpu.game.s2015182030.dragonproject.R;
import kr.ac.kpu.game.s2015182030.dragonproject.framework.BoxCollidable;
import kr.ac.kpu.game.s2015182030.dragonproject.framework.GameObject;
import kr.ac.kpu.game.s2015182030.dragonproject.framework.Recyclable;
import kr.ac.kpu.game.s2015182030.dragonproject.ui.view.GameView;
import kr.ac.kpu.game.s2015182030.dragonproject.utils.CollisionHelper;

public class MainGame {
    private static final String TAG = MainGame.class.getSimpleName();

    // singleton
    private static MainGame instance;
    private Player player;
    private Score score;

    public static MainGame get() {
        if (instance == null) {
            instance = new MainGame();
        }
        return instance;
    }

    public float frameTime;
    private boolean initialized;

    ArrayList<ArrayList<GameObject>> layers;
    private static HashMap<Class, ArrayList<GameObject>> recycleBin = new HashMap<>();

    public void recycle(GameObject object) {
        Class clazz = object.getClass();
        ArrayList<GameObject> array = recycleBin.get(clazz);
        if (array == null) {
            array = new ArrayList<>();
            recycleBin.put(clazz, array);
        }
        array.add(object);
    }

    public GameObject get(Class clazz) {
        ArrayList<GameObject> array = recycleBin.get(clazz);
        if (array == null || array.isEmpty()) return null;
        return array.remove(0);
    }

    public enum Layer {
        bg1, enemy, bullet, player, monsterBullet, item, ui, controller, ENEMY_COUNT
    }
    public boolean initResources() {
        if (initialized) {
            return false;
        }
        int w = GameView.view.getWidth();
        int h = GameView.view.getHeight();

        initLayers(Layer.ENEMY_COUNT.ordinal());

        player = new Player(w/2, h - 300);

        add(Layer.player, player);
        add(Layer.controller, new EnemyGenerator());

        int margin = (int) (20 * GameView.MULTIPLIER);
        score = new Score(w - margin, margin);
        score.setScore(0);
        add(Layer.ui, score);

        HorizontalScrollBackground bg = new HorizontalScrollBackground(R.mipmap.scene01, 100);
        add(Layer.bg1, bg);

        initialized = true;
        return true;
    }

    private void initLayers(int layerCount) {
        layers = new ArrayList<>();
        for (int i = 0; i < layerCount; i++) {
            layers.add(new ArrayList<>());
        }
    }

    public void update() {
        for (ArrayList<GameObject> objects : layers) {
            for (GameObject o : objects) {
                o.update();
            }
        }

        ArrayList<GameObject> enemies = layers.get(Layer.enemy.ordinal());
        ArrayList<GameObject> bullets = layers.get(Layer.bullet.ordinal());
        ArrayList<GameObject> monsterBullets = layers.get(Layer.monsterBullet.ordinal());
        ArrayList<GameObject> items = layers.get(Layer.item.ordinal());

        // Collide Enemy - Bullet
        for (GameObject o1 : enemies) {
            Enemy enemy = (Enemy) o1;
            boolean collided = false;

            for (GameObject o2 : bullets) {
                Bullet bullet = (Bullet) o2;

                if (CollisionHelper.collides(enemy, bullet)) {
                    if(enemy.getDead() == false) {
                        enemy.decreaseHp(10);
                    }
                    else {
                        remove(enemy, false);
                    }

                    remove(bullet, false);
                    score.addScore(10);
                    collided = true;
                    break;
                }
            }
            if (collided) {
                break;
            }
        }

        // Collide Player - Item
        for (GameObject o1 : items) {
            Item item = (Item) o1;
            boolean collided = false;

            if (CollisionHelper.collides(item, player)) {
                // 0: coin, 1:DualShot, 2:bullet Level, 3: Life
                int itemLev = item.getLevel();

                switch (itemLev) {
                    case 0:
                        player.setCoin(1);
                        break;
                    case 1:
                        player.setBulletNum(1);
                        break;
                    case 2:
                        player.setBulletLevel(1);
                        break;
                    case 3:
                        player.setLife(1);
                        break;
                }
                remove(item, false);
                collided = true;
                break;
            }

            if (collided) {
                break;
            }
        }


    }

    public void draw(Canvas canvas) {
        for (ArrayList<GameObject> objects: layers) {
            for (GameObject o : objects) {
                o.draw(canvas);
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
            player.moveTo(event.getX(), event.getY());
            return true;
        }
        return false;
    }

    public void add(Layer layer, GameObject gameObject) {
        GameView.view.post(new Runnable() {
            @Override
            public void run() {
                ArrayList<GameObject> objects = layers.get(layer.ordinal());
                objects.add(gameObject);
            }
        });
    }

    public void remove(GameObject gameObject) {
        remove(gameObject, true);
    }

    public void remove(GameObject gameObject, boolean delayed) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (ArrayList<GameObject> objects: layers) {
                    boolean removed = objects.remove(gameObject);
                    if (removed) {
                        if (gameObject instanceof Recyclable) {
                            ((Recyclable) gameObject).recycle();
                            recycle(gameObject);
                        }
                        break;
                    }
                }
            }
        };

        if (delayed) {
            GameView.view.post(runnable);
        }
        else {
            runnable.run();
        }
    }
}
