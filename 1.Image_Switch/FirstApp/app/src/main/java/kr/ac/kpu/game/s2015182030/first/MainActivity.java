package kr.ac.kpu.game.s2015182030.first;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mainTextView;
    private ImageView mainImageView;
    private int imageCount = 0;
    private int picture[] = { R.mipmap.cat1,R.mipmap.cat2,R.mipmap.cat3,R.mipmap.cat4,R.mipmap.cat5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainTextView = findViewById(R.id.mainTextView);
        mainTextView.setText("1/5");

        mainImageView=findViewById(R.id.mainImageView);
    }

    public void onButtonPrev(View view) {
        if(imageCount>0)
            imageCount-=1;
        else return;
        mainTextView.setText(imageCount+1+"/5");
        mainImageView.setImageResource(picture[imageCount]);
    }

    public void onButtonNext(View view) {
        if(imageCount<4)
            imageCount+=1;
        else return;
        mainTextView.setText(imageCount+1+"/5");
        mainImageView.setImageResource(picture[imageCount]);
    }
}