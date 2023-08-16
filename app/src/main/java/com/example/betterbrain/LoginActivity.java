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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FrameLayout frameLayout = findViewById(R.id.LoginActLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) frameLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();



        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() !=null){
            finish();
            return;
        }
        Button btnLogin = findViewById(R.id.RegBtn);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginSfx();
                authenticateUser();
            }
        });
        Button btnRegister = findViewById(R.id.Loginbtn);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginSfx();
                switchToRegister();
            }
        });


    }

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


    private void authenticateUser(){
        EditText LgEmail = findViewById(R.id.LgName);
        EditText LgPassword = findViewById(R.id.LgPassword);
        AudioManager BgMusic = (AudioManager)getSystemService(this.AUDIO_SERVICE);

        String Lgemail = LgEmail.getText().toString();
        String Lgpassword = LgPassword.getText().toString();

        if (Lgemail.isEmpty() || Lgpassword.isEmpty()){
            Toast.makeText(this,"Fill up all the fields", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(Lgemail, Lgpassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (BgMusic.isMusicActive()){
                                Toast.makeText(LoginActivity.this,"Logged in successfully",Toast.LENGTH_SHORT).show();
                                showMainActivity_withSFX();

                            }
                            else {
                                showMainActivity_withoutSFX();
                                Toast.makeText(LoginActivity.this,"Logged in successfully",Toast.LENGTH_SHORT).show();

                            }

                        } else {
                            Toast.makeText(LoginActivity.this,"Authentication Failed",Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }



    private void showMainActivity_withSFX(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    private void showMainActivity_withoutSFX(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        Intent BgMusic = new Intent(this, BgMusic.class);
        startService(BgMusic);
        finish();
    }
    private void switchToRegister(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    public void LoginSfx(){
        MediaPlayer loginSfx = MediaPlayer.create(this, R.raw.btn_sound);
        if (isServiceRunning(getApplication(), BgMusic.class)) {
            loginSfx.start();
        } else {
            loginSfx.release();
        }
    }
}