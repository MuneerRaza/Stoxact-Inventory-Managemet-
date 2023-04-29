package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.project.Helper.SliderAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class OnBoarding extends AppCompatActivity {
    ViewPager2 viewPager;
    ImageView[] dots;
    Button get_started_btn;
    FloatingActionButton next_btn;
    private static final int NO_OF_SLIDES = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.on_boarding);

        viewPager = findViewById(R.id.slider);
        get_started_btn = findViewById(R.id.get_start_btn);
        next_btn = findViewById(R.id.next_btn);
        dots = new ImageView[NO_OF_SLIDES];
//        TO-DO when slides increases
        dots[0] = findViewById(R.id.dot1);
        dots[1] = findViewById(R.id.dot2);
        dots[2] = findViewById(R.id.dot3);
        dots[3] = findViewById(R.id.dot4);
        dots[4] = findViewById(R.id.dot5);
        dots[0].setImageResource(R.drawable.dot_colored);
        viewPager.setAdapter(new SliderAdapter(getApplicationContext()));

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position+1 == NO_OF_SLIDES){
                    get_started_btn.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), androidx.navigation.ui.R.anim.nav_default_enter_anim));
                    get_started_btn.setVisibility(View.VISIBLE);
//                    next_btn.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), androidx.navigation.ui.R.anim.nav_default_exit_anim));
                    next_btn.setVisibility(View.INVISIBLE);

                }
                else {
                    get_started_btn.setVisibility(View.INVISIBLE);
                    next_btn.setVisibility(View.VISIBLE);
                }
                dots[position].setImageResource(R.drawable.dot_colored);
                for (int i = 0; i < dots.length; i++) {
                    if(i==position){
                        continue;
                    }
                    dots[i].setImageResource(R.drawable.dot);
                }
            }
        });
    }


    public void onClick_next(View view) {
        viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
    }

    public void onClick_get_started(View view) {
        Intent signup = new Intent(getApplicationContext(), Login.class);
        finish();
        startActivity(signup);

    }
}