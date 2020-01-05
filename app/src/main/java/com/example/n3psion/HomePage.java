package com.example.n3psion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import io.paperdb.Paper;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        logOut();
    }

    public void logOut(){
        Paper.book().destroy(); // destroy
        Intent loginPage = new Intent(HomePage.this, MainActivity.class);
        startActivity(loginPage);
    }
}

