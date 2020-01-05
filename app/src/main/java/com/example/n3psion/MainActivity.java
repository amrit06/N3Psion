package com.example.n3psion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.n3psion.Objects.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final static int EMAILERRORCODE = 1;
    private final static int PASSWORDERRORCODE = 2;
    private final static int EMAILPASSWORDERRORCODE = 3;


    private EditText username;
    private EditText password;
    private CheckBox rememberMe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button login = findViewById(R.id.login_button);
        TextView signup = findViewById(R.id.signup_linkText); // clickable text
        TextView forgotpassword = findViewById(R.id.forgotpassword_linkText); // // clickable text
        rememberMe = findViewById(R.id.remberme_checkbox);

        username = findViewById(R.id.username_edittext);
        password = findViewById(R.id.password_edittext);

        rememberMe.setChecked(false);
        Paper.init(MainActivity.this);
        // retrieve saved user account if avaliable;
        logPreviousUser();




        login.setOnClickListener(this);
        signup.setOnClickListener(this);
        forgotpassword.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                verifyAndLogin();
                break;

            case R.id.signup_linkText:
                Intent signupPageIntent = new Intent(MainActivity.this, SignupPage.class);
                startActivity(signupPageIntent);
                break;

            case R.id.forgotpassword_linkText:
                Toast.makeText(MainActivity.this, "Forgot password", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void logPreviousUser(){
        String userEmail = Paper.book().read("currentUserEmail");
        String userPassword = Paper.book().read("password");

        if(userEmail != null && userPassword != null){
            Log.i("data: ", userEmail + userPassword);
            // with remeber me we can either directly login them or simply put their email and password in editBox
             login(userEmail, userPassword, username, password); // username and password is editbox and is empty

            // in below case don't destroy Paper when logout
            /*username.setText(userEmail);
            password.setText(userPassword);*/
        }
    }

    public void verifyAndLogin(){
       final String emailInput = username.getText().toString();
       final String passwordInput = password.getText().toString();
       Boolean noError = true;

        if (TextUtils.isEmpty(emailInput) ) { // name error
            username.setError("Username can't be empty!");
            username.requestFocus();
            noError = false;
        }

        if (TextUtils.isEmpty(passwordInput) ) { // name error
            password.setError("Password can't be empty!");
            password.requestFocus();
            noError = false;
        }

        // if email and pass not empty check email and password and login them
        if (noError) {
            login(emailInput, passwordInput, username, password);
            /*{
                loadingpage.setTitle("Loging");
                loadingpage.setTitle("PLease wait while we verify you!");
                loadingpage.setCanceledOnTouchOutside(true);
                loadingpage.show();

                final DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Users");
                Query query = dbref.orderByChild("email").equalTo(emailInput);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) { // email is found
                            loadingpage.dismiss();
                            for (DataSnapshot user : dataSnapshot.getChildren()) {
                                Users currentUser = user.getValue(Users.class);
                                if (currentUser.getEmail().equals(emailInput) && currentUser.getPassword().equals(passwordInput)) {
                                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    if (rememberMe.isChecked()) {
                                        setRememberMe(emailInput, passwordInput);
                                    }
                                    Intent homeintnet = new Intent(MainActivity.this, HomePage.class);
                                    startActivity(homeintnet);
                                } else {
                                    Toast.makeText(MainActivity.this, "Password didn't match", Toast.LENGTH_SHORT).show();
                                    password.setText("");
                                }
                            }

                        } else {
                            username.setText("");
                            password.setText("");
                            loadingpage.dismiss();
                            Toast.makeText(MainActivity.this, "Account with " + emailInput + " doesn't exist ", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });
            }*/
        }
    }

    public void login(final String email, final String password, final EditText emailBox, final EditText passwordBox){
        final ProgressDialog loadingpage = new ProgressDialog(MainActivity.this);
        loadingpage.setTitle("Log In");
        loadingpage.setTitle("PLease wait while we verify you!");
        loadingpage.setCanceledOnTouchOutside(true);
        loadingpage.show();


        final DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Users");
        Query query = dbref.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) { // email is found
                    for (DataSnapshot user:dataSnapshot.getChildren()){
                        Users currentUser = user.getValue(Users.class);
                        if (currentUser.getEmail().equals(email) && currentUser.getPassword().equals(password)){
                            loadingpage.dismiss();
                            Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                            if (rememberMe.isChecked()){
                                setRememberMe(email, password);
                            }
                            Intent homeIntent = new Intent(MainActivity.this, HomePage.class);
                            startActivity(homeIntent);
                        }else {
                            loadingpage.dismiss();
                            passwordBox.setText("");
                            Toast.makeText(MainActivity.this, "Password didn't match", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    loadingpage.dismiss();
                    emailBox.setText("");
                    passwordBox.setText("");
                    Toast.makeText(MainActivity.this, "Account with " + email + " doesn't exist ", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    public void setRememberMe(String email, String password ) {
            Paper.book().write("currentUserEmail", email);
            Paper.book().write("password", password);
    }

}
