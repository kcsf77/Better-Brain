package com.example.betterbrain;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
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
import java.util.Locale;
import java.util.Random;

public class MatchingGameActivity extends AppCompatActivity implements View.OnClickListener{
    private int countPair = 1;
    private Button retry, next;

    private int numberOfElements;
    private MemoryButton[] buttons;

    private int [] buttonGraphicLocations;
    private int [] buttonGraphics;

    private MemoryButton selectedButton1;
    private MemoryButton selectedButton2;

    private boolean isBusy = false;
    private TextView mg_Hint;
    private TextView mg_level;
    private TextView mg_highScore;

    int mg_LevelCounter =1;
    int matching_game_data;
    GridLayout gridLayout;
    int numColumns;
    int numRows;

    //timer variables
    TextView timer;
    private static long START_TIME_IN_MILLIS = 300000;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private static long mTimeLeftInMillis = START_TIME_IN_MILLIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_game);

        FrameLayout frameLayout = findViewById(R.id.matching_menu_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) frameLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();


        gridLayout = findViewById(R.id.gridLayout);
        mg_Hint = findViewById(R.id.mg_hint);
        timer = findViewById(R.id.timerId);
        mg_level = findViewById(R.id.mg_level);
        next = findViewById(R.id.nextSet);
        retry = (Button) findViewById(R.id.retry);
        mg_highScore = findViewById(R.id.mg_highScore);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("HighScores").child("Matching_Game");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.getValue() != null) {
                    matching_game_data = Integer.parseInt(snapshot.getValue().toString());
                    mg_highScore.setText("Best Level: " + snapshot.getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        numColumns = gridLayout.getColumnCount();
        numRows = gridLayout.getRowCount();
        numberOfElements = numColumns * numRows;

        buttons = new MemoryButton[numberOfElements];

        buttonGraphics = new int[numberOfElements /2];
        buttonGraphics [0] = R.drawable.matchblack;
        buttonGraphics [1] = R.drawable.matchbrown;
        buttonGraphics [2] = R.drawable.matchblue;
        buttonGraphics [3] = R.drawable.matchgreen;
        buttonGraphics [4] = R.drawable.matchyellow;
        buttonGraphics [5] = R.drawable.matchpurple;
        buttonGraphics [6] = R.drawable.matchorange;
        buttonGraphics [7] = R.drawable.matchred;

        buttonGraphicLocations = new int[numberOfElements];

        shuffleButtonGraphics();
        displayButtonGraphics();
        startTimer();
        mg_level.setText("Level :" + mg_LevelCounter);
        mg_Hint.setText("Starting time :  5 Minutes" );




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

    protected void shuffleButtonGraphics()
    {
        Random rand = new Random();

        for (int i = 0; i < numberOfElements; i++)
        {
            buttonGraphicLocations[i]= i % (numberOfElements / 2);
        }

        for (int i = 0; i < numberOfElements; i++)
        {
            int temp = buttonGraphicLocations[i];

            int swapIndex = rand.nextInt(16);

            buttonGraphicLocations[i] = buttonGraphicLocations[swapIndex];

            buttonGraphicLocations[swapIndex] = temp;
        }
    }

    protected void displayButtonGraphics(){
        for (int r = 0; r < numRows; r++)
        {
            for(int c= 0; c < numColumns; c++)
            {
                MemoryButton tempButton = new MemoryButton(this, r, c,buttonGraphics[buttonGraphicLocations[r * numColumns + c]]);
                tempButton.setId(View.generateViewId());
                tempButton.setOnClickListener(this);
                buttons [r * numColumns + c] = tempButton;
                gridLayout.addView(tempButton);
            }
        }

    }


    @Override
    public void onClick(View view) {
        MemoryButton button = (MemoryButton) view;

        if(isBusy)
            return;



        if (button.isMatched)
            return;


        if (selectedButton1 == null)
        {
            selectedButton1 = button;
            selectedButton1.flip();
            return;
        }


        if (selectedButton1.getId() == button.getId())
        {
            return;
        }

        if (countPair == 8){

            mg_LevelCounter++;
            winSfx();
            Toast toast =  Toast.makeText(MatchingGameActivity.this, "Good Job!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();


            if (mg_LevelCounter > matching_game_data) {
                FirebaseDatabase.getInstance().getReference("users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("HighScores").child("Matching_Game")
                        .setValue(mg_LevelCounter);
            }


            next.setVisibility(View.VISIBLE);
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    newLevel();
                }
            });



        }

        if (selectedButton1.getFrontDrawableId() == button.getFrontDrawableId()){
            button.flip();
            button.setMatched(true);
            selectedButton1.setMatched(true);
            selectedButton1.setEnabled(false);
            button.setEnabled(false);
            selectedButton1 = null;
            countPair++;
            MatchGameMainSfx();
            return;
        }
        else {
            selectedButton2 = button;
            selectedButton2.flip();
            isBusy = true;

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    selectedButton2.flip();
                    selectedButton1.flip();
                    selectedButton2 = null;
                    selectedButton1 = null;
                    isBusy = false;

                }
            }, 400);


        }
    }

    public void newGame(){
        resetTimer();
        startTimer();
        startActivity(getIntent());
        finish();

        overridePendingTransition(0, 0);
    }

    private void newLevel(){
        next.setVisibility(View.INVISIBLE);
        mg_level.setText("Level :" + mg_LevelCounter);
        countPair =1;
        mCountDownTimer.cancel();
        resetTimer();


        mTimeLeftInMillis = ((mTimeLeftInMillis) - mg_LevelCounter *30000)  ;


        if(mTimeLeftInMillis < 30000){
            mTimeLeftInMillis = 30000;
            mg_Hint.setText("Match the colors in " +mTimeLeftInMillis/1000 + " seconds");
        }else {
            mg_Hint.setText("Starting time reduced by " + (mg_LevelCounter * 30000) / 1000 + " seconds");
        }
        startTimer();

        shuffleButtonGraphics();
        displayButtonGraphics();



    }

    //timer

    private void startTimer(){
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis,1000) {
            @Override
            public void onTick(long l) {

                mTimeLeftInMillis = l;
                updateCountdownText();
            }

            @Override
            public void onFinish() {


                if (mg_LevelCounter > matching_game_data) {
                    FirebaseDatabase.getInstance().getReference("users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("HighScores").child("Matching_Game")
                            .setValue(mg_LevelCounter);
                }


                mTimerRunning = false;
                retry.setVisibility(View.VISIBLE);
                gridLayout.setVisibility(View.INVISIBLE);
                mg_Hint.setText("TIME IS UP!!");
                retry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        newGame();

                    }
                });

            }
        }.start();
        mTimerRunning = true;
    }

    private void resetTimer(){
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountdownText();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mTimerRunning) {
            resetTimer();
            mCountDownTimer.cancel();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(mTimerRunning) {
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
        int minutes = (int) mTimeLeftInMillis /1000/60;
        int seconds = (int) mTimeLeftInMillis /1000 % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes , seconds);
        timer.setText(timeLeftFormatted);
    }
    public void MatchGameMainSfx(){
        MediaPlayer matchMainSfx = MediaPlayer.create(this, R.raw.correct);
        if (isServiceRunning(getApplication(), BgMusic.class)) {
            matchMainSfx.start();
        } else {
            matchMainSfx.release();
        }
    }
    public void winSfx(){
        MediaPlayer nextLevelSfx = MediaPlayer.create(this, R.raw.win);
        if (isServiceRunning(getApplication(), BgMusic.class)) {
            nextLevelSfx.start();
        } else {
            nextLevelSfx.release();
        }
    }
}