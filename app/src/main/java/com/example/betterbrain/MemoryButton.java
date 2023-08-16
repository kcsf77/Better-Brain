package com.example.betterbrain;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.GridLayout;

import androidx.appcompat.widget.AppCompatDrawableManager;

public class MemoryButton extends androidx.appcompat.widget.AppCompatButton {
    protected int row;
    protected int column;
    protected int frontDrawableId;

    protected boolean isFlipped = false;
    protected boolean isMatched = false;

    protected Drawable front;
    protected Drawable back;

    @SuppressLint("RestrictedApi")
    public MemoryButton(Context context, int r, int c, int frontImageDrawableId){
        super(context);

        row = r;
        column = c;
        frontDrawableId = frontImageDrawableId;

        front = AppCompatDrawableManager.get().getDrawable(context, frontImageDrawableId);
        back = context.getDrawable(R.drawable.newquestion);

        setBackground(back);
        GridLayout.LayoutParams tempParams = new GridLayout.LayoutParams(GridLayout.spec(r), GridLayout.spec(c));

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        double height = displayMetrics.heightPixels;
        double width = displayMetrics.widthPixels;

        Double cube = (height/width);
        Log.d("success",String.valueOf(cube));

        if(cube > 1.7) {
            tempParams.width = (int) getResources().getDisplayMetrics().density * 80;
            tempParams.height = (int) getResources().getDisplayMetrics().density * 80;
        }else if(cube >=1 &&cube <= 1.7){
            tempParams.width = (int) getResources().getDisplayMetrics().density * 110;
            tempParams.height = (int) getResources().getDisplayMetrics().density * 110;
        }else if(cube < 1){
            tempParams.width = (int) getResources().getDisplayMetrics().density * 140;
            tempParams.height = (int) getResources().getDisplayMetrics().density * 140;
        }

        setLayoutParams(tempParams);
    }

    public boolean isMatched() {
        return isMatched;
    }

    public void setMatched(boolean matched) {
        isMatched = matched;
    }

    public int getFrontDrawableId() {
        return frontDrawableId;
    }

    public void flip(){
        if(isMatched)
            return;

        if(isFlipped){
            setBackground(back);
            isFlipped = false;
        }
        else{
            setBackground(front);
            isFlipped = true;
        }
    }

}