package com.example.n3psion;

import androidx.annotation.NonNull;
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

import com.example.n3psion.Admin.AdminHomePage;
import com.example.n3psion.Objects.Users;
import com.example.n3psion.shared.Home_Page;
import com.example.n3psion.user.UserHomePage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText username_editbox;
    private EditText password_editbox;
    private CheckBox remember_me_checkbox;
    private Button login_button;
    private TextView signup_textview;
    private TextView forgotpassword_textview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(MainActivity.this);
        // retrieve saved user account if avaliable;
        logPreviousUser();

        login_button = findViewById(R.id.login_button);
        signup_textview = findViewById(R.id.signup_linkText); // clickable text
        forgotpassword_textview = findViewById(R.id.forgotpassword_linkText); // // clickable text

        remember_me_checkbox = findViewById(R.id.remberme_checkbox);
        username_editbox = findViewById(R.id.username_edittext);
        password_editbox = findViewById(R.id.password_edittext);

        remember_me_checkbox.setChecked(false);
        login_button.setOnClickListener(this);
        signup_textview.setOnClickListener(this);
        forgotpassword_textview.setOnClickListener(this);
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
            // with remeber me we can either directly log them or simply put their email and password in editBox
             login(userEmail, userPassword); // username and password is editbox and is empty

            // in below case don't destroy Paper when logout
            /*username.setText(userEmail);
            password.setText(userPassword);*/
        }
    }

    public void verifyAndLogin(){
       final String emailInput = username_editbox.getText().toString();
       final String passwordInput = password_editbox.getText().toString();
       Boolean empty = false;

        if (TextUtils.isEmpty(emailInput) ) { // name error
            username_editbox.setError("Username can't be empty!");
            username_editbox.requestFocus();
            empty = true;
        }

        if (TextUtils.isEmpty(passwordInput) ) { // name error
            password_editbox.setError("Password can't be empty!");
            password_editbox.requestFocus();
            empty = true;
        }

        // if email and pass not empty check email and password and login them
        if (!empty) {

            final ProgressDialog loadingpage = new ProgressDialog(MainActivity.this);
            loadingpage.setTitle("Log In");
            loadingpage.setTitle("PLease wait while we verify you!");
            loadingpage.setCanceledOnTouchOutside(true);
            loadingpage.show();

            if (emailInput.equals("admin@gmail.com")){
                adminVerification(emailInput, passwordInput);
            }else {
                userVerification(emailInput, passwordInput);
            }
        }
    }

    public void adminVerification(final  String email, final String password){

    }

    public void userVerification(final  String email, final String password){

    }


    public void login(final String email, final String password, final String dbname){

        final DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child(dbname);
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

                            Intent intent = new Intent(MainActivity.this, Home_Page.class);

                            if (currentUser.getEmail().equals(ADMIN)){
                                Intent homeIntent = new Intent(MainActivity.this, AdminHomePage.class); // home page
                                startActivity(homeIntent);
                            }else{
                                Intent homeIntent = new Intent(MainActivity.this, UserHomePage.class); // home page
                                startActivity(homeIntent);
                            }


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
