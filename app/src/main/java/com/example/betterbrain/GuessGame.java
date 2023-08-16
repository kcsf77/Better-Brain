package com.example.betterbrain;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Random;

public class GuessGame extends AppCompatActivity implements View.OnClickListener {
    public static int MAX_NUMBER = 0;
    public static final Random RANDOM = new Random();
    private TextView hint;
    private TextView level;
    private EditText input;
    private Button guess;
    private TextView gg_highScore;
    int glevel = 1;
    int guess_game_data;
    private int numberToFind, numberTries=0;
    Button guessBtn1,guessBtn2,guessBtn3,guessBtn4,guessBtn5,guessBtn6,guessBtn7,guessBtn8,guessBtn9,guessBtn0,
            guessBtnPeriod;
    ImageButton guessBtnDel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guess_game);

        FrameLayout frameLayout = findViewById(R.id.guessLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) frameLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        hint = findViewById(R.id.hint);
        input = findViewById(R.id.input);
        guess = findViewById(R.id.guess);
        level = findViewById(R.id.levIndicator);
        gg_highScore = findViewById(R.id.gg_highScore);

        guessBtn1 = findViewById(R.id.guessBtn1);
        guessBtn2 = findViewById(R.id.guessBtn2);
        guessBtn3 = findViewById(R.id.guessBtn3);
        guessBtn4 = findViewById(R.id.guessBtn4);
        guessBtn5 = findViewById(R.id.guessBtn5);
        guessBtn6 = findViewById(R.id.guessBtn6);
        guessBtn7 = findViewById(R.id.guessBtn7);
        guessBtn8 = findViewById(R.id.guessBtn8);
        guessBtn9 = findViewById(R.id.guessBtn9);
        guessBtn0 = findViewById(R.id.guessBtn0);
        guessBtnPeriod = findViewById(R.id.guessBtnPeriod);
        guessBtnDel = findViewById(R.id.guessBtnDel);

        guess.setOnClickListener(this);
        guessBtn1.setOnClickListener(this);
        guessBtn2.setOnClickListener(this);
        guessBtn3.setOnClickListener(this);
        guessBtn4.setOnClickListener(this);
        guessBtn5.setOnClickListener(this);
        guessBtn6.setOnClickListener(this);
        guessBtn7.setOnClickListener(this);
        guessBtn8.setOnClickListener(this);
        guessBtn9.setOnClickListener(this);
        guessBtn0.setOnClickListener(this);
        guessBtnPeriod.setOnClickListener(this);
        guessBtnDel.setOnClickListener(this);

        newGame();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("HighScores").child("Guess_Game");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.getValue() != null) {
                    guess_game_data = Integer.parseInt(snapshot.getValue().toString());
                    gg_highScore.setText("Best Level: " + snapshot.getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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



    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        if (view == guess){
            guess();
        }
        if (view == guessBtn1){
            input.setText(input.getText()+"1");
            btnGuessSfx();
        }
        if (view == guessBtn2){
            input.setText(input.getText()+"2");
            btnGuessSfx();
        }
        if (view == guessBtn3){
            input.setText(input.getText()+"3");
            btnGuessSfx();
        }
        if (view == guessBtn4){
            input.setText(input.getText()+"4");
            btnGuessSfx();
        }
        if (view == guessBtn5){
            input.setText(input.getText()+"5");
            btnGuessSfx();
        }
        if (view == guessBtn6){
            input.setText(input.getText()+"6");
            btnGuessSfx();
        }
        if (view == guessBtn7){
            input.setText(input.getText()+"7");
            btnGuessSfx();
        }
        if (view == guessBtn8){
            input.setText(input.getText()+"8");
            btnGuessSfx();
        }
        if (view == guessBtn9){
            input.setText(input.getText()+"9");
            btnGuessSfx();
        }
        if (view == guessBtn0){
            input.setText(input.getText()+"0");
            btnGuessSfx();
        }
        if (view == guessBtnPeriod){
            input.setText(input.getText()+".");
            btnGuessSfx();
        }
        if (view == guessBtnDel){

            int textLength = input.getText().length();
            if(textLength != 0){
                SpannableStringBuilder select = (SpannableStringBuilder) input.getText();
                select.replace(textLength-1, textLength,"");
                input.setText(select);
                input.setSelection(textLength-1);
                btnGuessSfx();
            }
        }

    }

    public void guess (){

        try {
            int n = Integer.parseInt(input.getText().toString());
            numberTries++;
            if (numberTries <= 5) {
                if (numberTries == 4 && n != numberToFind) {
                    Toast toast =  Toast.makeText(GuessGame.this, "You have one last chance to guess!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                if (n == numberToFind) {

                    if (numberTries == 1 && n == numberToFind) {
                        Toast.makeText(this, "Nice Guess! You guessed " + numberToFind + " in " + numberTries
                                + " try", Toast.LENGTH_SHORT).show();
                        glevel++;
                        nextLevel();
                        guessGameSfx1();
                    }else{
                        Toast.makeText(this, "Nice Guess! You guessed " + numberToFind + " in " + numberTries
                                + " tries!", Toast.LENGTH_SHORT).show();
                        glevel++;
                        nextLevel();
                        guessGameSfx1();
                    }
                } else if (n > numberToFind) {
                    hint.setText("❝ " + n + " is too high ❞");
                    input.setText("");
                    guessGameSfx2();
                } else if (n < numberToFind) {
                    hint.setText("❝ " + n + " is too low ❞");
                    input.setText("");
                    guessGameSfx2();
                }
            }
            if(numberTries >=5 ){
                Toast.makeText(this, "You lost! The number is " + numberToFind, Toast.LENGTH_SHORT).show();
                guessGameSfx2();

                if(glevel > guess_game_data){
                    FirebaseDatabase.getInstance().getReference("users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("HighScores").child("Guess_Game")
                            .setValue(glevel);
                }

                MAX_NUMBER = 5;
                glevel = 1;
                newGame();
            }
        }catch (Exception e){
            e.printStackTrace();
            hint.setText("input an integer");
        }
    }

    private void nextLevel(){
        MAX_NUMBER = MAX_NUMBER + 5;
        numberToFind = RANDOM.nextInt(MAX_NUMBER );
        hint.setText("Guess the number from 0 to " + MAX_NUMBER);
        level.setText("Level " + glevel);
        input.setText("");
        numberTries=0;

    }

    private void newGame(){
        numberToFind = RANDOM.nextInt(MAX_NUMBER=5);
        hint.setText("Guess the number from 0 to " + MAX_NUMBER);
        level.setText("Level " + glevel);
        input.setText("");
        numberTries=0;
    }

    public void guessGameSfx1(){
        MediaPlayer guessSfx1 = MediaPlayer.create(this, R.raw.win);
        if (isServiceRunning(getApplication(), BgMusic.class)) {
            guessSfx1.start();
        } else {
            guessSfx1.release();
        }
    }
    public void guessGameSfx2(){
        MediaPlayer guessSfx2 = MediaPlayer.create(this, R.raw.wrong);
        if (isServiceRunning(getApplication(), BgMusic.class)) {
            guessSfx2.start();
        } else {
            guessSfx2.release();
        }
    }

    public void btnGuessSfx(){
        MediaPlayer btnSfx = MediaPlayer.create(this, R.raw.btn_sound);
        if (isServiceRunning(getApplication(), BgMusic.class)) {
            btnSfx.start();
        } else {
            btnSfx.release();
        }
    }
}