package com.example.n3psion.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.n3psion.MainActivity;
import com.example.n3psion.Pages.SliderAdapter;
import com.example.n3psion.Pages.examplefragment;
import com.example.n3psion.R;
import com.google.android.material.navigation.NavigationView;

import io.paperdb.Paper;



public class UserHomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // for drawer
    private DrawerLayout drawer;
    LinearLayout linearLayout;

    // for view slider
    private LinearLayout dotHolder;
    private ViewPager viewSlider;
    private SliderAdapter sliderAdapter;
    private TextView[] contentDots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);


        // setting up drawer
        linearLayout = findViewById(R.id.homepage_linearlayout);
        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer);
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(UserHomePage.this, drawer, toolbar, R.string.navigation_open_drawer, R.string.navigation_close_drawer){
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float slidex = drawerView.getWidth()*slideOffset;
                linearLayout.setTranslationX(slidex);
            }
        };

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toogle.getDrawerArrowDrawable().setColor(getColor(R.color.black));
        toogle.getDrawerArrowDrawable().setBarThickness(5);
        drawer.addDrawerListener(toogle);
        toogle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(UserHomePage.this);
        //selecting navigation item when welcome
        if(savedInstanceState == null){ // when our app is rotated is system manages resource it sometimes refresh our oncreate thus reloading page
            // if something is already saved don't override and change their page/fragment
            navigationView.setCheckedItem(R.id.nav_1);
            getSupportFragmentManager().beginTransaction().replace(R.id.homepage_framelayout, new examplefragment()).commit();
        }





        // setting up view slider
        viewSlider = findViewById(R.id.view_pager);
        dotHolder = findViewById(R.id.dot_holder);

        sliderAdapter = new SliderAdapter(UserHomePage.this);
        viewSlider.setAdapter(sliderAdapter);
        addDotForSlider(0);
        viewSlider.addOnPageChangeListener(viewListener);
       }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
           @Override
           public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

           }

           @Override
           public void onPageSelected(int position) {
                addDotForSlider(position);
           }

           @Override
           public void onPageScrollStateChanged(int state) {

           }
       };

    public void addDotForSlider(int viewPosition){
            dotHolder.removeAllViews();
            contentDots = new TextView[sliderAdapter.getCount()];
            for (int i = 0 ; i< contentDots.length; i++){
                contentDots[i] = new TextView(UserHomePage.this);
                contentDots[i].setText(Html.fromHtml("&#8226;"));
                contentDots[i].setTextSize(25);
                contentDots[i].setTextColor(getColor(R.color.colorAccent));
                dotHolder.addView(contentDots[i]);
            }
            contentDots[viewPosition].setTextColor(getColor(R.color.whiteColor));
       }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        logOut();
    }

    public void logOut(){
        Paper.book().destroy(); // destroy
        Intent loginPage = new Intent(UserHomePage.this, MainActivity.class);
        startActivity(loginPage);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_1:
                getSupportFragmentManager().beginTransaction().replace(R.id.homepage_framelayout, new examplefragment()).commit();
                break;
            case R.id.nav_2:
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

