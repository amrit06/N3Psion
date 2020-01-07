package com.example.n3psion.Pages;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.n3psion.R;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater inflater;

    public int[] slide_images = {
            R.drawable.pic_boys_clothing,
            R.drawable.pic_men_clothing,
            R.drawable.pic_girls_clothing,
            R.drawable.pic_women_clothing
    };

    public String[] slide_Title = {
            "Boys",
            "Men",
            "Girls",
            "Women"
    };

    public int[] slide_bg = {
            R.color.boys_bg_color,
            R.color.men_bg_color,
            R.color.girls_bg_color,
            R.color.women_bg_color
    };


    public SliderAdapter(Context context1) {
        this.context = context1;
    }

    @Override
    public int getCount() {
        return slide_Title.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
     inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
     View view = inflater.inflate(R.layout.men_women_slide, container, false);

        ImageView slideImage = view.findViewById(R.id.slide_imageview);
        TextView slideTitle = view.findViewById(R.id.slide_title);
        LinearLayout layout = view.findViewById(R.id.slide_layout);

        slideImage.setImageResource(slide_images[position]);
        slideTitle.setText(slide_Title[position]);
        layout.setBackgroundColor(slide_bg[position]);

        container.addView(view);
     return view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
