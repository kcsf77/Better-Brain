package com.example.betterbrain;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

import java.util.List;

public class Settings extends AppCompatActivity {

    Boolean isSwitchOn = false;
    Switch sfx, musBtn;
    AudioManager volume;
    LottieAnimationView lottie;


    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        lottie = findViewById(R.id.lottieVolume);

        FrameLayout frameLayout = findViewById(R.id.settingLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) frameLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();


        AudioManager audioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);


        SeekBar volume = (SeekBar)findViewById(R.id.seekBar);
        volume.setMax(maxVolume);
        volume.setProgress(currentVol);

        final TextView volProgress = (TextView)findViewById(R.id.volBar);
        volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress , 0);
                volProgress.setText("Volume " + progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        SharedPreferences sharedPreferences = getSharedPreferences("save", MODE_PRIVATE);


        if (isServiceRunning(getApplication(), BgMusic.class)) {
            lottie.setMinAndMaxProgress(0.5f, 1.0f);
            lottie.playAnimation();
            isSwitchOn = false;
        } else {
            lottie.setMinAndMaxProgress(0.0f, 0.5f);
            lottie.playAnimation();
            isSwitchOn = true;
        }


        lottie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSwitchOn){
                    lottie.setMinAndMaxProgress(0.5f, 1.0f);
                    lottie.playAnimation();
                    isSwitchOn = false;
                    play();
                } else {
                    lottie.setMinAndMaxProgress(0.0f, 0.5f);
                    lottie.playAnimation();
                    isSwitchOn = true;
                    stop();
                }

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



    private void play() {
        Intent intent = new Intent(this, BgMusic.class);
        startService(intent);


    }

    private void stop() {
        Intent intent1 = new Intent(this, BgMusic.class);
        stopService(intent1);
    }
}