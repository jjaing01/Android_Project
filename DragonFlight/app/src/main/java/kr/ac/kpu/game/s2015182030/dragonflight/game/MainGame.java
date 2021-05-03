package kr.ac.kpu.game.s2015182030.dragonflight.game;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.HashMap;

import kr.ac.kpu.game.s2015182030.dragonflight.framework.BoxCollidable;
import kr.ac.kpu.game.s2015182030.dragonflight.framework.GameObject;
import kr.ac.kpu.game.s2015182030.dragonflight.framework.Recyclable;
import kr.ac.kpu.game.s2015182030.dragonflight.ui.view.GameView;
import kr.ac.kpu.game.s2015182030.dragonflight.utils.CollisionHelper;

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

    //ArrayList<GameObject> objects = new ArrayList<>();
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

    public enum Layer{
        enemy, bullet, player, ui, controller, ENEMY_COUNT;
    }
    public boolean initResources() {
        if (initialized) {
            return false;
        }
        int w = GameView.view.getWidth();
        int h = GameView.view.getHeight();

        initLayers(Layer.ENEMY_COUNT.ordinal());

        player = new Player(w/2, h - 300);
        //objects.add(player);
        //layers.get(Layer.player.ordinal()).add(player);
        add(Layer.player,player);

        //objects.add(new EnemyGenerator());
        add(Layer.controller,new EnemyGenerator());

        int margin = (int)(20 * GameView.MULTIPLIER);
        score = new Score(w - margin, margin);
        score.setScore(0);
        add(Layer.ui, score);

        initialized = true;
        return true;
    }

    private void initLayers(int layerCount) {
        layers = new ArrayList<>();
        for(int i = 0; i<layerCount; ++i){
            layers.add(new ArrayList<>());
        }
    }

    public void update() {
        //if (!initialized) return;
        for(ArrayList<GameObject> objects : layers){
            for (GameObject o : objects) {
                o.update();
            }
        }

        ArrayList<GameObject> enemies = layers.get(Layer.enemy.ordinal());
        ArrayList<GameObject> bullets = layers.get(Layer.bullet.ordinal());
        for(GameObject o1 : enemies) {
            Enemy enemy = (Enemy) o1;
            boolean collided = false;
            for(GameObject o2 : bullets) {
                Bullet bullet = (Bullet) o2;
                if (CollisionHelper.collides(enemy, bullet)){
                    remove(enemy);
                    remove(bullet);
                    score.addScore(10);
                    collided = true;
                    break;
                }
            }
            if(collided){
                break;
            }
        }

//        for (GameObject o1 : objects) {
//            if (!(o1 instanceof Enemy)) {
//                continue;
//            }
//            Enemy enemy = (Enemy) o1;
//            boolean removed = false;
//            for (GameObject o2 : objects) {
//                if (!(o2 instanceof Bullet)) {
//                    continue;
//                }
//                Bullet bullet = (Bullet) o2;
//
//                if (CollisionHelper.collides(enemy, bullet)) {
//                    //Log.d(TAG, "Collision!" + o1 + " - " + o2);
//                    remove(enemy);
//                    remove(bullet);
//                    //bullet.recycle();
//                    //recycle(bullet);
//                    removed = true;
//                    break;
//                }
//            }
//            if (removed) {
//                continue;
//            }
//            if (CollisionHelper.collides(enemy, player)) {
//                Log.d(TAG, "Collision: Enemy - Player");
//            }
//        }
    }

    public void draw(Canvas canvas) {
        //if (!initialized) return;
        for(ArrayList<GameObject> objects : layers){
            for (GameObject o : objects) {
                o.draw(canvas);
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
//        if (action == MotionEvent.ACTION_DOWN) {
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
//        Log.d(TAG, "<A> object count = " + objects.size());
    }

    public void remove(GameObject gameObject) {
        if (gameObject instanceof Recyclable) {
            ((Recyclable) gameObject).recycle();
            recycle(gameObject);
        }
        GameView.view.post(new Runnable() {
            @Override
            public void run() {
                for(ArrayList<GameObject> objects : layers){
                    boolean removed = objects.remove(gameObject);
                    if(removed){
                        break;
                    }
                }
            }
        });
    }
}
