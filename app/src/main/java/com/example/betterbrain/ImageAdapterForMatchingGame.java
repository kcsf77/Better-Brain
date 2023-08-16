package com.example.betterbrain;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageAdapterForMatchingGame extends BaseAdapter {

    private Context context;

    public ImageAdapterForMatchingGame(Context context)
    {
        this.context = context;
    }
    @Override
    public int getCount() {
        return 16;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;
        if(convertView == null)
        {
            imageView = new ImageView(this.context);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(225,225));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }



        else imageView = (ImageView) convertView;

        imageView.setImageResource(R.drawable.newquestion);
        return imageView;
    }
}

