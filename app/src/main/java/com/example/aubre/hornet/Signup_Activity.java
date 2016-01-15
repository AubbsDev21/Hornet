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

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class Signup_Activity extends AppCompatActivity {

    protected EditText mUsername;
    protected EditText mEmail;
    protected EditText mPassword;
    protected Button mSignupbutton;
    protected ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mUsername = (EditText)findViewById(R.id.UsernameSU);
        mEmail = (EditText)findViewById(R.id.emailSUText);
        mPassword = (EditText)findViewById(R.id.PasswordSU);
        mSignupbutton = (Button)findViewById(R.id.Signupbutton);
        mProgressBar = (ProgressBar)findViewById(R.id.SUprogressbar);
        mProgressBar.setVisibility(View.INVISIBLE);

        //signup button actions
        mSignupbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //shows work is being done
                Refreshwheel();
                //getting the string value from editText field
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();
                String email = mEmail.getText().toString();

                //trimming white spaces
                username = username.trim();
                password = password.trim();
                email = email.trim();

                if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                    AlertDialog.Builder  builder = new AlertDialog.Builder(Signup_Activity.this);
                    builder.setMessage(R.string.Signup_error_message)
                    .setTitle(R.string.Signup_error_title)
                    .setPositiveButton(R.string.ok_label, null);
                   AlertDialog dialog = builder.create();
                    dialog.show();

                } else {
                    //if  user checks out good sign them in
                    ParseUser user = new ParseUser();
                    user.setUsername(username);
                    user.setPassword(password);
                    user.setEmail(email);

// other fields can be set just like with ParseObject
                    user.put("phone", "650-555-0000");

                    user.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {


                            if (e == null) {
                                // Hooray! Let them use the app now.
                                Intent intent = new Intent(Signup_Activity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                // Sign up didn't succeed. Look at the ParseException
                                // to figure out what went wrong
                                //Error message
                                AlertDialog.Builder  builder = new AlertDialog.Builder(Signup_Activity.this);
                                builder.setMessage(R.string.Signup_error_message)
                                        .setTitle(R.string.Signup_error_title)
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
        if (mProgressBar.getVisibility() == View.INVISIBLE ) {
            mProgressBar.setVisibility(View.VISIBLE);

        }else {
            mProgressBar.setVisibility(View.INVISIBLE);

        }
    }
}
