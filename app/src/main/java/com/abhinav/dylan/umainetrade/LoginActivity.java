package com.abhinav.dylan.umainetrade;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import BCrypt.BCrypt;
import email.GmailSender;
import com.R;
import com.example.dylan.MainActivity;

public class LoginActivity extends AppCompatActivity {
    private String email;
    private String password;
    public static int userId;


    public static Context context;
    //private Pattern pattern;
    //private Matcher matcher;

    private static final String EMAIL_PATTERN =

            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*(@maine.edu|@umit.maine.edu)$";
           // "^[_A-Za-z0-9-\\\\+]+(\\\\.[_A-Za-z0-9-]+)*(@maine.edu|@umit.maine.edu)";


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
        setContentView(R.layout.activity_login);
        context = getApplicationContext();
        //Override policy to make app able to connect to internet
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("UMaineTrade");


        final EditText emailET = (EditText) findViewById(R.id.EmailEditText);
        final EditText passwordET = (EditText) findViewById(R.id.PasswordEditText);


        Button loginButton = (Button) findViewById(R.id.LoginButton);
        Button forgotButton = (Button) findViewById(R.id.ForgotButton);
        Button signUpButton = (Button) findViewById(R.id.SignUpButton);
        Button scanButton = (Button) findViewById(R.id.scan_button);
        Button testing = (Button) findViewById(R.id.TestListingPageButton);
        Button tabbed = (Button) findViewById(R.id.TestTabbedButton);



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setEmail(emailET.getText().toString());

                setPassword(passwordET.getText().toString());


                //connect();

                if (!getEmail().isEmpty() && !getPassword().isEmpty()) {

                    if (validate(getEmail())) {
                        DBLogin login = new DBLogin();
                        if (login.authenticate(getEmail(), getPassword())) {
                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();

                            Intent listingsPageIntent = new Intent(LoginActivity.this, ListingsPage.class);
                            startActivity(listingsPageIntent);

                        } else {
                            Toast.makeText(getApplicationContext(), "Wrong Username and/or Password", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter a valid University of Maine email address.", Toast.LENGTH_SHORT).show();
                    }
                } else {

                    Toast.makeText(getApplicationContext(), "Please enter your email and password.", Toast.LENGTH_SHORT).show();

                }


            }
        });

        forgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent signUpIntent = new Intent(LoginActivity.this, Signup.class);
                startActivity(signUpIntent);


            }
        });
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent signUpIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(signUpIntent);


            }
        });
        testing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent signUpIntent = new Intent(LoginActivity.this, ListingsPage.class);
                startActivity(signUpIntent);


            }
        });

        tabbed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent signUpIntent = new Intent(LoginActivity.this, TabbedListings.class);
                startActivity(signUpIntent);


            }
        });



    }

    protected void showInputDialog() {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(LoginActivity.this);
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText dialogEditText = (EditText) promptView.findViewById(R.id.dialogEditText);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Toast.makeText(context, "hi", Toast.LENGTH_SHORT).show();
                        if (validate(dialogEditText.getText().toString())) {
                            DBLogin login = new DBLogin();
                            if (login.registeredEmail(dialogEditText.getText().toString())) {


                                AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                                alertDialog.setTitle("Password Reset");
                                alertDialog.setMessage("Password reset instructions have been sent to your email.");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();

                            } else {
                                Toast.makeText(context, "Couldn't find registration. Please register.", Toast.LENGTH_SHORT).show();

                            }

                        } else {
                            Toast.makeText(context, "Please enter a valid University of Maine email address", Toast.LENGTH_SHORT).show();

                        }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public void sendForgotEmail(String email){
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    String verifyURL = "http://10.0.3.2:8080/UMaineTrade/ForgotPassword?emailaddress="+getEmail();
                    GmailSender sender = new GmailSender("umainetrade@gmail.com", "capstone");
                    sender.sendMail("UMaineTrade Password Reset Instructions",
                            "<HTML><HEAD><TITLE>UMaineTrade Password Reset</TITLE><BODY>" +
                                    "<P>Please click on the following link to reset your password</P>"+
                                    "<a href="+verifyURL+">Reset Password</a></BODY></HTML>", "umainetrade@gmail.com", getEmail());
                    Toast.makeText(context, "gmailsender", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("Sendmail", e.getMessage(), e);
                }

            }
        }).start();

    }

    public static boolean validate(final String hex) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(hex);
        return matcher.matches();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
