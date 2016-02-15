package com.abhinav.dylan.umainetrade;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import BCrypt.BCrypt;
import email.GmailSender;

public class Signup extends AppCompatActivity {
    public static Context context;
    public static Signup instance = null;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        context = getApplicationContext();
        instance = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Sign Up");

        final EditText firstNameET = (EditText) findViewById(R.id.FirstNameET);
        final EditText lastNameET = (EditText) findViewById(R.id.LastNameET);
        final EditText emailET = (EditText) findViewById(R.id.EmailET);
        final EditText passwordET = (EditText) findViewById(R.id.PasswordET);

        Button signUpButton = (Button) findViewById(R.id.SignUpButton);


        signUpButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                setFirstName(firstNameET.getText().toString());

                setLastName(lastNameET.getText().toString());


                setEmail(emailET.getText().toString());


                String unhashedPassword = passwordET.getText().toString();

                String hashedPassword = BCrypt.hashpw(unhashedPassword, BCrypt.gensalt(12));
                setPassword(hashedPassword);

                if (!getFirstName().isEmpty() && !getLastName().isEmpty() && !getEmail().isEmpty() && !getPassword().isEmpty()) {

                    if (LoginActivity.validate(getEmail())) {
                        DBLogin login = new DBLogin();
                        login.signUp(getFirstName(), getLastName(), getEmail(), getPassword());
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    String verifyURL = "http://10.0.3.2:8080/UMaineTrade/VerifyEmail?emailaddress="+getEmail()+"&firstname="+getFirstName();
                                    GmailSender sender = new GmailSender("umainetrade@gmail.com", "capstone");
                                    sender.sendMail("UMaineTrade Verification Email",
                                            "<HTML><HEAD><TITLE>UMaineTrade Verification</TITLE><BODY>" +
                                            "<P>Please click on the Activation Link below to activate your account</P>"+
                                            "<a href="+verifyURL+">Activation link</a></BODY></HTML>", "umainetrade@gmail.com", getEmail());
                                    Toast.makeText(context, "gmailsender", Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Log.e("Sendmail", e.getMessage(), e);
                                }

                            }
                        }).start();
                        AlertDialog.Builder alert = new AlertDialog.Builder(Signup.this);
                        alert.setTitle("Thank you for signing up.");
                        alert.setMessage("A validation email has been sent to the email provided.");
                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        alert.show();
                    } else {
                        Toast.makeText(context, "Please enter a valid University of Maine email.", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(context, "Please fill out all fields to sign up.", Toast.LENGTH_SHORT).show();

                }


            }


        });


    }


}
