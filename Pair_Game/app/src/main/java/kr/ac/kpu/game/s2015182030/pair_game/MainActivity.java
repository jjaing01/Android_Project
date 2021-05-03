package kr.ac.kpu.game.s2015182030.pair_game;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import org.w3c.dom.Text;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int[] buttonIds = {
            R.id.card_00, R.id.card_01, R.id.card_02, R.id.card_03,
            R.id.card_10, R.id.card_11, R.id.card_12, R.id.card_13,
            R.id.card_20, R.id.card_21, R.id.card_22, R.id.card_23,
            R.id.card_30, R.id.card_31, R.id.card_32, R.id.card_33,
            R.id.card_40, R.id.card_41, R.id.card_42, R.id.card_43
    };
    private int[] cards = {
            R.mipmap.card_braum, R.mipmap.card_braum, R.mipmap.card_darius, R.mipmap.card_darius,
            R.mipmap.card_fortune, R.mipmap.card_fortune, R.mipmap.card_jinx, R.mipmap.card_jinx,
            R.mipmap.card_lux, R.mipmap.card_lux, R.mipmap.card_renekton, R.mipmap.card_renekton,
            R.mipmap.card_sol, R.mipmap.card_sol, R.mipmap.card_thresh, R.mipmap.card_thresh,
            R.mipmap.card_yasuo, R.mipmap.card_yasuo, R.mipmap.card_yone, R.mipmap.card_yone
    };
    private ImageButton prevButton;
    private int visibleCardCount;
    private TextView scoreTextView;

    private int score;

    public void setScore(int score) {
        this.score = score;
        scoreTextView.setText("Flips:" + score);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreTextView = findViewById(R.id.scoreTextView);
        setScore(0);

        startGame();
    }

    public void onBtnCard(View view) {
        if (view == prevButton) {
            return;
        }

        int prevCard = 0;
        if (prevButton != null) {
            prevButton.setImageResource(R.mipmap.card_back);
            prevCard = (Integer) prevButton.getTag();
        }

        int buttonIndex = getButtonIndex(view.getId());
        Log.d(TAG, "onBtnCard() has been called. buttonIndex = " + buttonIndex);

        int card = cards[buttonIndex];
        ImageButton imageButton = (ImageButton)view;
        imageButton.setImageResource(card);

        if(card == prevCard) {
            imageButton.setVisibility(View.INVISIBLE);
            prevButton.setVisibility(View.INVISIBLE);
            prevButton = null;
            visibleCardCount -= 2;
            if(0 == visibleCardCount) {
                askRestart();
            }
            return;
        }

        if (prevButton != null) {
            setScore(score + 1);
        }
        prevButton = imageButton;
    }

    private int getButtonIndex(int resID) {
        for(int i =0; i < buttonIds.length; ++i){
            if (buttonIds[i] == resID){
                return i;
            }
        }

        return -1;
    }

    public void onBtnRestart(View view) {
        askRestart();
    }

    private void askRestart() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Restart");
        builder.setMessage("Do you really want to restart the game?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startGame();
            }
        });
        builder.setNegativeButton("No", null);
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void startGame() {
        Random random = new Random();
        for (int i = 0; i  < cards.length; ++i) {
            int ri = random.nextInt(cards.length);
            int t = cards[i];
            cards[i] = cards[ri];
            cards[ri] = t;
        }

        for(int i = 0; i < buttonIds.length; ++i) {
            ImageButton b = findViewById(buttonIds[i]);
            b.setTag(cards[i]);
            b.setVisibility(View.VISIBLE);
            b.setImageResource(R.mipmap.card_back);
        }

        prevButton = null;
        visibleCardCount = cards.length;
        setScore(0);
    }
}