package com.example.betterbrain;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import me.relex.circleindicator.CircleIndicator;

public class TutorialActivity extends AppCompatActivity {
    private Button skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        ConstraintLayout constraintLayout = findViewById(R.id.tutorialLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();


        ViewPager viewPager = findViewById(R.id.viewPager);
        ImageAdapterTutorial imageAdapterTutorial = new ImageAdapterTutorial(this);
        viewPager.setAdapter(imageAdapterTutorial);

        ViewPager viewpager = (ViewPager) findViewById(R.id.viewPager);
        viewpager.setAdapter(imageAdapterTutorial);

        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewpager);

        skip = (Button) findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                returnMainMenu();
            }
        });
    }
    private void returnMainMenu(){
        Intent intent = new Intent(this,    MainMenu.class);
        startActivity(intent);
    }
}