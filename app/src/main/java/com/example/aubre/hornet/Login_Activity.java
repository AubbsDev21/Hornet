package com.example.aubre.hornet;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


public class Login_Activity extends AppCompatActivity {

    protected TextView mSignupText;
    protected EditText mUsername;
    protected EditText mPassword;
    protected Button mLoginbutton;
    protected ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mProgressBar = (ProgressBar) findViewById(R.id.LoginprogressBar);
        mProgressBar.setVisibility(View.INVISIBLE);
        mSignupText = (TextView) findViewById(R.id.Signuptext);
        mSignupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Refreshwheel();
                Intent intent = new Intent(Login_Activity.this, Signup_Activity.class);
                startActivity(intent);

            }
        });
        mUsername = (EditText) findViewById(R.id.usernameLog);
        mPassword = (EditText) findViewById(R.id.PasswordLog);
        mLoginbutton = (Button) findViewById(R.id.Loginbutton);


        //When Login button is click....
        mLoginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Refreshwheel();
                //getting the string value from editText field
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();


                //trimming white spaces
                username = username.trim();
                password = password.trim();
                //Error for empty fields
                if (username.isEmpty() || password.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Login_Activity.this);
                    builder.setMessage(R.string.Signup_error_message)
                            .setTitle(R.string.Signup_error_title)
                            .setPositiveButton(R.string.ok_label, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();

                } else {
                    //Login

                    ParseUser.logInInBackground(username, password, new LogInCallback() {
                        public void done(ParseUser user, ParseException e) {


                            if (user != null) {
                                // Hooray! The user is logged in.
                                Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                // Login failed. Look at the ParseException to see what happened.
                                AlertDialog.Builder builder = new AlertDialog.Builder(Login_Activity.this);
                                builder.setMessage(R.string.Login_error_message)
                                        .setTitle(R.string.Login_error_title)
                                        .setPositiveButton(R.string.ok_label, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    });

                }


            }
        });
    }

    private void Refreshwheel() {
        if (mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}
