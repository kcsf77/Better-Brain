package com.example.betterbrain;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;
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

public class NumberGame extends AppCompatActivity {

    TextView level_text, the_number, ng_highScore, theInfo, theNumberInfo;
    EditText number_input;
    Button confirm_button;
    Random r;
    int currentLevel = 1;
    int number_game_bestLevel ;
    int number_game_data ;
    String generatedNumber;
    Button numberBtn1,numberBtn2,numberBtn3,numberBtn4,numberBtn5,numberBtn6,numberBtn7,numberBtn8,numberBtn9,numberBtn0,
            numberBtnPeriod;
    ImageButton numberBtnDelete;
    TableLayout myTableLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.number_game);

        FrameLayout frameLayout = findViewById(R.id.numberLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) frameLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();



        level_text = findViewById(R.id.tv_level1);
        the_number = findViewById(R.id.tv_number0);
        number_input = findViewById(R.id.et_number);
        confirm_button = findViewById(R.id.b_confirm );
        ng_highScore = findViewById(R.id.ng_highScore);
        myTableLayout = findViewById(R.id.btnLayoutId);
        theInfo = findViewById(R.id.tv_info);
        theNumberInfo = findViewById(R.id.tv_numberInfo);
        r = new Random();

        number_input.setVisibility(View.GONE);
        confirm_button.setVisibility(View.INVISIBLE);
        myTableLayout.setVisibility(View.INVISIBLE);
        the_number.setVisibility(View.VISIBLE);



        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("HighScores").child("Number_Game");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.getValue() != null) {
                    number_game_data = Integer.parseInt(snapshot.getValue().toString());
                    ng_highScore.setText("Best Level: " + snapshot.getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        level_text.setText("Level : " + currentLevel);

        generatedNumber= (generatedNumber(currentLevel));
        the_number.setText(generatedNumber);

        numberBtn1 = findViewById(R.id.numBtn1);
        numberBtn2 = findViewById(R.id.numBtn2);
        numberBtn3 = findViewById(R.id.numBtn3);
        numberBtn4 = findViewById(R.id.numBtn4);
        numberBtn5 = findViewById(R.id.numBtn5);
        numberBtn6 = findViewById(R.id.numBtn6);
        numberBtn7 = findViewById(R.id.numBtn7);
        numberBtn8 = findViewById(R.id.numBtn8);
        numberBtn9 = findViewById(R.id.numBtn9);
        numberBtn0 = findViewById(R.id.numBtn0);
        numberBtnPeriod = findViewById(R.id.numBtnPeriod);
        numberBtnDelete = findViewById(R.id.numBtnDel);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                number_input.setVisibility(View.VISIBLE);
                confirm_button.setVisibility(View.VISIBLE);
                the_number.setVisibility(View.GONE);
                myTableLayout.setVisibility(View.VISIBLE);
                number_input.requestFocus();
            }
        },2000);

        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // check  if the number  are the same
                if (generatedNumber.equals(number_input.getText().toString())) {

                    // hide the input and the button and show the number

                    number_input.setVisibility(View.GONE);
                    confirm_button.setVisibility(View.INVISIBLE);
                    myTableLayout.setVisibility(View.INVISIBLE);
                    the_number.setVisibility(View.VISIBLE);
                    numberGameSfx1();

                    //remove text from input
                    number_input.setText("");

                    // increase the level

                    currentLevel++;

                    // display the current level
                    level_text.setText("Level : " + currentLevel);

                    //display random numbers according to the level

                    generatedNumber = (generatedNumber(currentLevel));
                    the_number.setText(generatedNumber);

                    //display the elements after two seconds and hide the number
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            number_input.setVisibility(View.VISIBLE);
                            confirm_button.setVisibility(View.VISIBLE);
                            myTableLayout.setVisibility(View.VISIBLE);
                            the_number.setVisibility(View.GONE);
                            number_input.requestFocus();
                        }
                    }, 2000);
                } else {
                    level_text.setText("-- GAME OVER --");
                    theInfo.setText("The number was :");
                    theNumberInfo.setText(" ❝ " + generatedNumber + " ❞ ");

                    numberGameSfx2();
                    confirm_button.setText("New Game");

                    number_game_bestLevel = currentLevel;
                    if (number_game_bestLevel > number_game_data) {
                        FirebaseDatabase.getInstance().getReference("users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("HighScores").child("Number_Game")
                                .setValue(number_game_bestLevel);
                    }
                    confirm_button = (Button) findViewById(R.id.b_confirm);
                    confirm_button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            btnNumberSfx();
                            newGame();
                        }
                    });
                }
            }
        });

        // Number buttons

        numberBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number_input.setText(number_input.getText()+"1");
                btnNumberSfx();
            }
        });
        numberBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number_input.setText(number_input.getText()+"2");
                btnNumberSfx();
            }
        });
        numberBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number_input.setText(number_input.getText()+"3");
                btnNumberSfx();
            }
        });
        numberBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number_input.setText(number_input.getText()+"4");
                btnNumberSfx();
            }
        });
        numberBtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number_input.setText(number_input.getText()+"5");
                btnNumberSfx();
            }
        });
        numberBtn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number_input.setText(number_input.getText()+"6");
                btnNumberSfx();
            }
        });
        numberBtn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number_input.setText(number_input.getText()+"7");
                btnNumberSfx();
            }
        });
        numberBtn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number_input.setText(number_input.getText()+"8");
                btnNumberSfx();
            }
        });
        numberBtn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number_input.setText(number_input.getText()+"9");
                btnNumberSfx();
            }
        });
        numberBtn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number_input.setText(number_input.getText()+"0");
                btnNumberSfx();
            }
        });
        numberBtnPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                number_input.setText(number_input.getText()+".");
                btnNumberSfx();
            }
        });
        numberBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnNumberSfx();

                int textLen = number_input.getText().length();
                if(textLen != 0){
                    SpannableStringBuilder select = (SpannableStringBuilder) number_input.getText();
                    select.replace(textLen-1, textLen,"");
                    number_input.setText(select);
                    number_input.setSelection(textLen-1);
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


    private String generatedNumber(int digits) {
        String output ="";
        for (int i= 0; i < digits; i++){
            int randomDigits = r.nextInt(10);
            output = output + "" + randomDigits;
        }
        return output;
    }
    private void newGame(){
        startActivity(getIntent());
        finish();
        overridePendingTransition(0, 0);
    }

    public void numberGameSfx1(){
        MediaPlayer numberSfx1 = MediaPlayer.create(this, R.raw.win);
        if (isServiceRunning(getApplication(), BgMusic.class)) {
            numberSfx1.start();
        } else {
            numberSfx1.release();
        }
    }
    public void numberGameSfx2(){
        MediaPlayer numberSfx2 = MediaPlayer.create(this, R.raw.wrong);
        if (isServiceRunning(getApplication(), BgMusic.class)) {
            numberSfx2.start();
        } else {
            numberSfx2.release();
        }
    }

    public void btnNumberSfx(){
        MediaPlayer btnSfx = MediaPlayer.create(this, R.raw.btn_sound);
        if (isServiceRunning(getApplication(), BgMusic.class)) {
            btnSfx.start();
        } else {
            btnSfx.release();
        }
    }

}