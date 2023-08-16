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

public class MainMenu extends AppCompatActivity {
    private Button startBtn, settingBtn, tutorialBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        FrameLayout frameLayout = findViewById(R.id.mainMenuLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) frameLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        startBtn = (Button) findViewById(R.id.startBtn);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainMenuSfx();
                openGameSelection();
            }
        });
        startBtn = (Button) findViewById(R.id.tutorialBtn);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainMenuSfx();
                openTutorial();
            }
        });

        settingBtn = (Button) findViewById(R.id.settingBtn);

        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainMenuSfx();
                openSettings();
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


    public void openGameSelection() {
        Intent intent = new Intent(this, GameSelection.class);
        startActivity(intent);
    }

    public void openTutorial() {
        Intent intent = new Intent(this, TutorialActivity.class);
        startActivity(intent);
    }

    public void openSettings() {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }

    public void MainMenuSfx() {
        MediaPlayer sfxs = MediaPlayer.create(this, R.raw.btn_sound);
        if (isServiceRunning(getApplication(), BgMusic.class)) {
            sfxs.start();
        } else {
            sfxs.release();
        }
    }
}
