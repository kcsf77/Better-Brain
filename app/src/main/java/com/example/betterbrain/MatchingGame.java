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

public class MatchingGame extends AppCompatActivity {
    private Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matching_game);

        FrameLayout frameLayout = findViewById(R.id.matching_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) frameLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        start = (Button) findViewById(R.id.retry);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MatchGameSfx();
                openGame();
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

    public void openGame(){
        Intent intent = new Intent(this, MatchingGameActivity.class);
        startActivity(intent);
    }
    public void MatchGameSfx(){
        MediaPlayer matchSfx = MediaPlayer.create(this, R.raw.btn_sound);
        if (isServiceRunning(getApplication(), BgMusic.class)) {
            matchSfx.start();
        } else {
            matchSfx.release();
        }
    }
}
