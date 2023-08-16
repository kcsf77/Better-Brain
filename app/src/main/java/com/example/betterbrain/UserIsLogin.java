package com.example.betterbrain;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserIsLogin extends AppCompatActivity {
    private Button start;
    TextView Username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_is_login);

        //Background Animation
        FrameLayout frameLayout = findViewById(R.id.userLogin);
        AnimationDrawable animationDrawable = (AnimationDrawable) frameLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        //For Background Music
        AudioManager BgMusic = (AudioManager)getSystemService(this.AUDIO_SERVICE);

        //Firebase things
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        Username = findViewById(R.id.UserName);
        Button btnLogout = findViewById(R.id.Loginbtn);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserLoginSfx();
                logoutUser();
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users").child(currentUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users user = snapshot.getValue(Users.class);
                if (user != null){
                    Username.setText(user.firstName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
        start = (Button) findViewById(R.id.Play);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BgMusic.isMusicActive()){
                    openMainMenu_withSFX();
                    UserLoginSfx();
                }
                else {
                    openMainMenu_withoutSFX();
                    UserLoginSfx();
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


    public void openMainMenu_withSFX(){
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
        finish();
    }
    public void openMainMenu_withoutSFX(){
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
        Intent BgMusic = new Intent(this, BgMusic.class);
        startService(BgMusic);
        finish();
    }
    private void logoutUser(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    public void UserLoginSfx(){
        MediaPlayer Uloginsfx = MediaPlayer.create(this, R.raw.btn_sound);
        if (isServiceRunning(getApplication(), BgMusic.class)) {
            Uloginsfx.start();
        } else {
            Uloginsfx.release();
        }
    }
}