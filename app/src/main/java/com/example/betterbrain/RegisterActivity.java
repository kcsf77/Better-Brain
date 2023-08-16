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
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FrameLayout frameLayout = findViewById(R.id.RegActLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) frameLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();



        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser()!= null){
            finish();
            return;
        }

        Button btnReg = findViewById(R.id.RegBtn);
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterSfx();
                registeruser();
            }
        });

        Button btnRegister = findViewById(R.id.Loginbtn);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterSfx();
                switchToLogin();
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





    private void registeruser(){
        EditText FN = findViewById(R.id.FN);
        EditText LN = findViewById(R.id.LN);
        EditText EM = findViewById(R.id.EM);
        EditText PW = findViewById(R.id.PW);
        AudioManager BgMusic = (AudioManager)getSystemService(this.AUDIO_SERVICE);
        String fN = FN.getText().toString();
        String lN = LN.getText().toString();
        String eM = EM.getText().toString();
        String pW = PW.getText().toString();

        if (fN.isEmpty() || lN.isEmpty() || eM.isEmpty() || pW.isEmpty()){
            Toast.makeText(this, "Fill all the fields", Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(eM, pW)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Users users = new Users(fN, lN, eM);
                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (BgMusic.isMusicActive()){
                                                Toast.makeText(RegisterActivity.this,"Registration completed successfully",Toast.LENGTH_SHORT).show();
                                                showMainMenu_withSFX();
                                            }
                                            else {
                                                Toast.makeText(RegisterActivity.this,"Registration completed successfully",Toast.LENGTH_SHORT).show();
                                                showMainMenu_withoutSFX();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(RegisterActivity.this,"Authentication Failed",Toast.LENGTH_LONG).show();

                        }
                    }
                });

    }


    private void showMainMenu_withSFX(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void showMainMenu_withoutSFX(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        Intent BgMusic = new Intent(this, BgMusic.class);
        startService(BgMusic);
        finish();
    }
    private void switchToLogin(){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
    public void RegisterSfx(){
        MediaPlayer regSfx = MediaPlayer.create(this, R.raw.btn_sound);
        if (isServiceRunning(getApplication(), BgMusic.class)) {
            regSfx.start();
        } else {
            regSfx.release();
        }
    }
}