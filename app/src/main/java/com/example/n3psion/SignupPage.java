package com.example.n3psion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.n3psion.Objects.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class SignupPage extends AppCompatActivity implements View.OnClickListener {


    private EditText name;
    private EditText lastname;
    private EditText email;
    private EditText password;
    private EditText retryPassword;
    private EditText phone;

    private TextView nameerrormsg;
    private TextView lastnameerrormsg;
    private TextView emailerrormsg;
    private TextView passworderrormsg;
    private TextView retrypassworderrormsg;
    private TextView displayAge;
    private TextView gendererrormsg;
    private TextView completeformerrormsg;

    private ProgressDialog loadingpage;
    private Users userOne;
    // instead of age we save these value
    private int year = 0;
    private int month = 0;
    private int day = 0;
    // instead of text gender we save int 0= non selected, 1 = male and so on
    private String gender = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);


        Button signup = findViewById(R.id.signup_button);
        RadioGroup radioGroup = findViewById(R.id.gender_radiogroup);
        ImageButton calenderPick = findViewById(R.id.calender_imagebutton);


        name = findViewById(R.id.name_edittext);
        lastname = findViewById(R.id.lastname_edittext);
        email = findViewById(R.id.email_edittext);
        password = findViewById(R.id.password_edittext);
        retryPassword = findViewById(R.id.retrypassword_edittext);
        phone = findViewById(R.id.phone_edittext);

        nameerrormsg = findViewById(R.id.name_errormsg);
        lastnameerrormsg = findViewById(R.id.lastname_errormsg);
        emailerrormsg = findViewById(R.id.email_errormsg);
        passworderrormsg = findViewById(R.id.password_errormsg);
        retrypassworderrormsg = findViewById(R.id.retrypassword_errormsg);
        displayAge = findViewById(R.id.age_textview);
        gendererrormsg = findViewById(R.id.gender_errormsg);
        completeformerrormsg = findViewById(R.id.compleform_text);

        loadingpage = new ProgressDialog(SignupPage.this);
        userOne = new Users();

        // selecting gender
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.male_radiobutton:
                        gender = "Male";
                        break;
                    case R.id.female_radiobutton:
                        gender = "Female";
                        break;
                    case R.id.unspecified_radiobutton:
                        gender = "Unspecified";
                        break;
                }
            }
        });
        calenderPick.setOnClickListener(this); // picking date
        signup.setOnClickListener(this); // submitting
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup_button:
                registerUser();
                break;
            case R.id.calender_imagebutton:
                retrieveDate();
                break;

        }
    }

    private void retrieveDate() {
        DatePickerDialog.OnDateSetListener datePickerListener;
        datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int localyear, int localmonth, int dayOfMonth) {
                localmonth = localmonth + 1; // 0 = jan
                day = dayOfMonth;
                month = localmonth;
                year = localyear;

                displayAge.setText("DOB: " + day + "/" + month + "/" + year);
                displayAge.setTextColor(getColor(R.color.black));
            }
        };

        DatePickerDialog datePicker = new DatePickerDialog(
                SignupPage.this,
                android.R.style.Theme_DeviceDefault_Dialog,
                datePickerListener,
                year, month, day);
        datePicker.show();
    }


    private void registerUser() {
        Boolean noError = true;

        userOne.setUserName(name.getText().toString());
        userOne.setLastName(lastname.getText().toString());
        userOne.setEmail(email.getText().toString());
        userOne.setPassword(password.getText().toString());
        userOne.setPhone(phone.getText().toString());
        userOne.setDob(day + "/" + month + "/" + year);
        userOne.setGender(gender);


        String stringretrypassword = retryPassword.getText().toString();
        Pattern regex = Pattern.compile("[0-9$&+,:;=\\\\?@#|/'<>.^*()%!-]");

        // error checking
        {
            if (TextUtils.isEmpty(userOne.getUserName()) || regex.matcher(userOne.getUserName()).find()) { // name error
                displayErrorMSG(name, nameerrormsg, "Must contain alphabets inputs!");
                noError = false;
            } else {
                neutralstate(name, nameerrormsg);
            }

            if (TextUtils.isEmpty(userOne.getLastName()) || regex.matcher(userOne.getLastName()).find()) { // lastname error
                displayErrorMSG(lastname,lastnameerrormsg, "Must contain alphabets inputs!");
                noError = false;
            } else {
                neutralstate(lastname,lastnameerrormsg);
            }

            if (TextUtils.isEmpty(userOne.getEmail()) || !Patterns.EMAIL_ADDRESS.matcher(userOne.getEmail()).matches()) { // email error
                displayErrorMSG(email,emailerrormsg, "*Must contain valid email inputs!");
                noError = false;
            } else {
                neutralstate(email, emailerrormsg);
            }

            if (TextUtils.isEmpty(userOne.getPassword())) { // password error
                displayErrorMSG(password, passworderrormsg, "*Password can't be empty!");
                noError = false;
            } else {
                neutralstate(password, passworderrormsg);
                if (!userOne.getPassword().equals(stringretrypassword)) {
                    displayErrorMSG(retryPassword, retrypassworderrormsg, "*Password must be same as above!");
                    noError = false;
                } else {
                    neutralstate(retryPassword, retrypassworderrormsg);
                }
            }

            if ( year < 1 ) { // meaning DOB is empty
                displayAge.setText("Please set DOB from calender!");
                displayAge.setTextColor(getColor(R.color.errorColor));
                noError = false;
            } else {
                displayAge.setTextColor(getColor(R.color.black));
            }

            if (TextUtils.isEmpty(userOne.getGender())) {
                gendererrormsg.setVisibility(View.VISIBLE);
                noError = false;
            } else {
                gendererrormsg.setVisibility(View.INVISIBLE);
            }

        }

        if (noError) {
            completeformerrormsg.setVisibility(View.INVISIBLE);
            loadingpage.setTitle("Creating Account");
            loadingpage.setTitle("PLease wait while we verify and register you!");
            loadingpage.setCanceledOnTouchOutside(true);
            loadingpage.show();
            validateemail();
        }else {
            completeformerrormsg.setVisibility(View.VISIBLE);
        }

    }

    private void validateemail() {
        Log.i("Email: ", userOne.getEmail());
        final DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Users");
        Query query = dbref.orderByChild("email").equalTo(userOne.getEmail());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(SignupPage.this, "Email Exists! Try Another", Toast.LENGTH_LONG).show();
                    loadingpage.dismiss();
                    email.setText("");
                    password.setText("");
                    retryPassword.setText("");
                } else {


                    String useroneid = dbref.push().getKey();
                    userOne.setUserID(useroneid);
                    //.child("User") is not required because we are reading form it above so we are already inside it
                    dbref.child(userOne.getUserID()).setValue(userOne, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            Toast.makeText(SignupPage.this, "account created", Toast.LENGTH_SHORT).show();
                            loadingpage.dismiss();
                            Intent signupPageIntent = new Intent(SignupPage.this, MainActivity.class);
                            startActivity(signupPageIntent);
                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    private void displayErrorMSG(EditText inputBox,TextView textView, String errorMsg){
        inputBox.setText("");
        textView.setVisibility(View.VISIBLE);
        textView.setText(errorMsg);
        inputBox.setBackground(getDrawable(R.drawable.errrorinputbox));
        inputBox.setHintTextColor(getColor(R.color.errorColor));
    }

    private void neutralstate(EditText inputBox,TextView textView){
        textView.setVisibility(View.INVISIBLE);
        inputBox.setBackground(getDrawable(R.drawable.textinput));
        inputBox.setHintTextColor(getColor(R.color.black));
    }


}
