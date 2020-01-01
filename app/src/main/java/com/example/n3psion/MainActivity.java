package com.example.n3psion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button login = findViewById(R.id.login_button);
        TextView signup = findViewById(R.id.signup_linkText);


        EditText username = findViewById(R.id.username_edittext);
        EditText password = findViewById(R.id.password_edittext);


        login.setOnClickListener(this);
        signup.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_button:
                break;
            case R.id.signup_linkText:
                Toast.makeText(MainActivity.this, "Signup page", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
