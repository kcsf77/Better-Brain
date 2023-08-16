package com.example.betterbrain;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class GameSelection extends AppCompatActivity {
    private Button vGames, nGames, tGames, wGames, gGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_selection);
        FrameLayout frameLayout = findViewById(R.id.gameSelectionLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) frameLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();


        vGames = (Button) findViewById(R.id.vGames);
        vGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectionSfx();
                openMatchingGame();
            }
        });
        tGames = (Button) findViewById(R.id.tGame);
        tGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectionSfx();
                openTimeGame();
            }
        });
        wGames = (Button) findViewById(R.id.wGame);
        wGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectionSfx();
                openWordGame();
            }
        });
        nGames = (Button) findViewById(R.id.nGame);
        nGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectionSfx();
                openNumberGame();
            }
        });
        gGames = (Button) findViewById(R.id.gGame);
        gGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectionSfx();
                openGuessGame();
            }
        });
    }
    //This is for knowing if the music is running which is vital for mute/unmute sounds
    public boolean isServiceRunning(Context c, Class<?> serviceClass) {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
            if (runningServiceInfo.service.getClassName().equals(serviceClass.getName())) {
                return true;
            }
        }
        return false;
    }

    public void openMatchingGame() {
        Intent intent = new Intent(this, MatchingGame.class);
        startActivity(intent);
    }
    public void  openTimeGame() {
        Intent intent = new Intent(this, TimeGame.class);
        startActivity(intent);
    }
    public void  openWordGame() {
        Intent intent = new Intent(this, WordGame.class);
        startActivity(intent);
    }
    public void openNumberGame() {
        Intent intent = new Intent(this, NumberGame.class);
        startActivity(intent);
    }
    public void openGuessGame() {
        Intent intent = new Intent(this, GuessGame.class);
        startActivity(intent);
    }
    public void SelectionSfx(){
        MediaPlayer SelectSfx= MediaPlayer.create(this, R.raw.btn_sound);
        if (isServiceRunning(getApplication(), BgMusic.class)) {
            SelectSfx.start();
        } else {
            SelectSfx.release();
        }
    }
}