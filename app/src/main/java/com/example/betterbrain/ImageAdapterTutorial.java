package com.example.betterbrain;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ImageAdapterTutorial extends PagerAdapter {
    private Context mContext;
    private int[] mImageIds = new int []{R.drawable.matchtuts1,R.drawable.matchtuts2, R.drawable.reactiontuts2,
            R.drawable.reactiontuts1,R.drawable.reactiontuts3, R.drawable.anagramtuts1, R.drawable.anagramtuts2, R.drawable.anagramtuts3,
            R.drawable.numberstuts1, R.drawable.numberstuts2, R.drawable.numberstuts3,R.drawable.numberstuts4,
            R.drawable.numberstuts5, R.drawable.guesstuts1,R.drawable.guesstuts2, R.drawable.guesstuts3, R.drawable.guesstuts4,
            R.drawable.guesstuts5};

    ImageAdapterTutorial(Context context){
        mContext = context;
    }
    @Override
    public int getCount() {
        return mImageIds.length;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.FIT_START);
        imageView.setImageResource(mImageIds[position]);
        container.addView(imageView,0);
        return imageView;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView)object);
    }
}