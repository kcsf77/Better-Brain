package com.example.betterbrain;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class TimeGame extends AppCompatActivity {

    Button b_start, b_main;

    TextView tv_info;

    long startTime, endTime, currentTime, temporary = 10000000;
    long time_game_data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_game);

        RelativeLayout relativeLayout = findViewById(R.id.timeLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();


        b_start = findViewById(R.id.b_start);
        b_main = findViewById(R.id.b_main);
        tv_info = findViewById(R.id.tv_info);
        b_start.setEnabled(true);
        b_main.setEnabled(false);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("HighScores").child("Time_Game");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.getValue() != null) {
                    time_game_data = Long.parseLong(snapshot.getValue().toString());
                    tv_info.setText("BEST TIME: " + snapshot.getValue() + "ms");
                } else {
                    time_game_data = temporary;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        b_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b_start.setEnabled(false);
                b_main.setText("");
                startTimeGameSfx();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startTime = System.currentTimeMillis();
                        b_main.setBackgroundColor(
                                ContextCompat.getColor(getApplicationContext(), R.color.green));
                        b_main.setText("PRESS");
                        b_main.setEnabled(true);
                    }
                }, 2000);
            }
        });
        b_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endTime = System.currentTimeMillis();
                currentTime = endTime - startTime;
                b_main.setBackgroundColor(
                        ContextCompat.getColor(getApplicationContext(), R.color.light_purple));
                b_main.setText(currentTime + "ms");
                b_start.setEnabled(true);
                b_main.setEnabled(false);
                TimeGameSfx();

                if (time_game_data == 0) {
                    time_game_data = temporary;
                }


                if (currentTime < time_game_data)
                    FirebaseDatabase.getInstance().getReference("users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("HighScores").child("Time_Game")
                            .setValue(currentTime);

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

    public void TimeGameSfx() {
        MediaPlayer timeSfx = MediaPlayer.create(this, R.raw.win);
        if (isServiceRunning(getApplication(), BgMusic.class)) {
            timeSfx.start();
        } else {
            timeSfx.release();
        }
    }

    public void startTimeGameSfx() {
        MediaPlayer startSfx = MediaPlayer.create(this, R.raw.btn_sound);
        if (isServiceRunning(getApplication(), BgMusic.class)) {
            startSfx.start();
        } else {
            startSfx.release();
        }
    }
}


