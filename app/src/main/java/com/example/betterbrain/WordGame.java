package com.example.betterbrain;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class WordGame extends AppCompatActivity {
    private static long START_TIME_IN_MILLIS = 120000;
    TextView wordInfo, wordMain, scoreCount, levelTimer, wg_highScore, wordInfo2;
    EditText wordInput;
    Button checkAnswer, newWord, startGame;
    int scoreCounter = 0;
    int word_game_data;
    Random r;
    String currentWord;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private static long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    private String[] dictionary = {"data", "internet", "computer", "software", "database", "system", "hardware",
            "programming", "application", "technology", "programmer", "developer", "object", "network", "code",
            "program", "file", "device", "mobile", "website", "binary", "user", "client"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_game);

        FrameLayout frameLayout = findViewById(R.id.wordLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) frameLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();


        levelTimer = findViewById(R.id.wordTimer);
        wordInfo = findViewById(R.id.info);
        wordInfo2 = findViewById(R.id.info2);
        wordMain = findViewById(R.id.theWord);
        wordInput = findViewById(R.id.answerInput);
        checkAnswer = findViewById(R.id.answerBtn);
        newWord = findViewById(R.id.newBtn);
        startGame = findViewById(R.id.startGameBtn);
        scoreCount = findViewById(R.id.score);
        wg_highScore = findViewById(R.id.wg_highScore);


        r = new Random();
        levelTimer.setVisibility(View.INVISIBLE);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("HighScores").child("Word_Game");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.getValue() != null) {
                    word_game_data = Integer.parseInt(snapshot.getValue().toString());
                    wg_highScore.setText("High Score: " + snapshot.getValue());
                } else {

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                newGame();
                startTimer();
                scoreCount.setText("");
                wordInput.setVisibility(View.VISIBLE);
                startGame.setVisibility(View.INVISIBLE);
                btnWordSfx();
            }
        });


        checkAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (wordInput.getText().toString().equalsIgnoreCase(currentWord)) {
                    wordInfo.setText(" ❝ EXCELLENT !! ❞ ");
                    checkAnswer.setVisibility(View.INVISIBLE);
                    newWord.setVisibility(View.VISIBLE);
                    scoreCounter++;
                    wordGameSfx1();
                    scoreCount.setText("Score:" + scoreCounter);
                } else {
                    wordInfo.setText("〝 INCORRECT !! 〞");
                    wordInfo2.setText("The word was : " + currentWord);
                    newWord.setVisibility(View.VISIBLE);
                    checkAnswer.setVisibility(View.INVISIBLE);
                    wordGameSfx2();
                }

            }
        });

        newWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newGame();
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

    private String shuffleWord(String word) {
        List<String> letters = Arrays.asList(word.split(""));
        Collections.shuffle(letters);
        String shuffled = "";
        for (String letter : letters) {
            shuffled += letter;
        }
        return shuffled;

    }

    private void newGame() {
        levelTimer.setVisibility(View.VISIBLE);
        wordMain.setVisibility(View.VISIBLE);
        currentWord = dictionary[r.nextInt(dictionary.length)];
        wordMain.setText(shuffleWord(currentWord));
        wordInput.setText("");
        wordInfo.setText("");
        wordInfo2.setText("");
        newWord.setVisibility(View.INVISIBLE);
        checkAnswer.setVisibility(View.VISIBLE);

    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long l) {

                mTimeLeftInMillis = l;
                updateCountdownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;

                wordInfo.setText("Time is up!");

                scoreCount.setText("Total score in 2 minutes: " + scoreCounter);

                if (scoreCounter > word_game_data) {
                    FirebaseDatabase.getInstance().getReference("users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("HighScores").child("Word_Game")
                            .setValue(scoreCounter);
                }

                scoreCounter = 0;
                resetTimer();
                wordGameSfx3();
                wordInfo2.setText("");
                startGame.setVisibility(View.VISIBLE);
                newWord.setVisibility(View.INVISIBLE);
                checkAnswer.setVisibility(View.INVISIBLE);
                wordInput.setVisibility(View.INVISIBLE);
                wordMain.setVisibility(View.INVISIBLE);
                startGame.setText("New Game");


            }
        }.start();
        mTimerRunning = true;
    }


    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountdownText();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mTimerRunning) {
            resetTimer();
            mCountDownTimer.cancel();
        }

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        if (mTimerRunning) {
            resetTimer();
            mCountDownTimer.cancel();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }


    private void updateCountdownText() {
        int minutes = (int) mTimeLeftInMillis / 1000 / 60;
        int seconds = (int) mTimeLeftInMillis / 1000 % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        levelTimer.setText(timeLeftFormatted);
    }


    public void wordGameSfx1() {
        MediaPlayer wordSfx1 = MediaPlayer.create(this, R.raw.win);
        if (isServiceRunning(getApplication(), BgMusic.class)) {
            wordSfx1.start();
        } else {
            wordSfx1.release();
        }
    }

    public void wordGameSfx2() {
        MediaPlayer wordSfx2 = MediaPlayer.create(this, R.raw.wrong);
        if (isServiceRunning(getApplication(), BgMusic.class)) {
            wordSfx2.start();
        } else {
            wordSfx2.release();
        }
    }

    public void wordGameSfx3() {
        MediaPlayer wordSfx3 = MediaPlayer.create(this, R.raw.no_time);
        if (isServiceRunning(getApplication(), BgMusic.class)) {
            wordSfx3.start();
        } else {
            wordSfx3.release();
        }
    }

    public void btnWordSfx() {
        MediaPlayer btnSfx = MediaPlayer.create(this, R.raw.btn_sound);
        if (isServiceRunning(getApplication(), BgMusic.class)) {
            btnSfx.start();
        } else {
            btnSfx.release();
        }
    }
}