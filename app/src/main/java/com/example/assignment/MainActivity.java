package com.example.assignment;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    ImageView[] arrCloud= new ImageView[3];
    ConstraintLayout main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        main = findViewById(R.id.main);
        for (int i = 0; i < arrCloud.length; i++) {
            arrCloud[i] = findViewById(getResources().getIdentifier("cloud" + (i + 1), "id", getPackageName()));
        }
        ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(10000L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // all width of cloud are same
                final float width = main.getWidth();
                for (int i = 0; i < arrCloud.length; i++) {
                    arrCloud[i].setTranslationX((arrCloud[i].getTranslationX()-(width/1000)));
                    if(arrCloud[i].getTranslationX()<= 0-arrCloud[i].getWidth()){
                        arrCloud[i].setTranslationX(width+100+((float) (Math.random()*100)));
                        arrCloud[i].setTranslationY((float) (Math.random()*100));
                    }
                }

            }
        });
        animator.start();
    }


    public void clickPlay(View v){
        Intent intent = new Intent(this, PlayActivity.class);
        startActivity(intent);
    }

    public void clickRanking(View v){
        Intent intent = new Intent(this, RankingActivity.class);
        startActivity(intent);
    }
    public void clickRecord(View v){
        Intent intent = new Intent(this, RecordActivity.class);
        startActivity(intent);
    }
    public void clickSettings(View v){
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }
    public void clickClose(View v){
        finish();
    }

}