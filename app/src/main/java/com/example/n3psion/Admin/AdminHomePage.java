package com.example.n3psion.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.n3psion.R;
import com.google.android.material.navigation.NavigationView;

public class AdminHomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_page);

        linearLayout = findViewById(R.id.admin_homepage_linearlayout);
        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer);
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(AdminHomePage.this, drawer, toolbar, R.string.navigation_open_drawer, R.string.navigation_close_drawer){
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float slideVal = drawerView.getWidth()*slideOffset;
                linearLayout.setTranslationX(slideVal);
            }
        };
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toogle.getDrawerArrowDrawable().setColor(getColor(R.color.black));
        toogle.getDrawerArrowDrawable().setBarThickness(5);
        drawer.addDrawerListener(toogle);
        toogle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(AdminHomePage.this);
        if (savedInstanceState == null){
            navigationView.setCheckedItem(R.id.view_all);

        }


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}