package com.example.aubre.hornet;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by Aubre on 1/3/2016.
 */
public class HornetAppication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "W86IrK7hJtYabh8nMMl9yToBLVYKbYIhuxQMKLya", "iA7cSkps4QdaVWKNy5IFjusAzoV3voSRScUIbBJy");

        ParseUser user = new ParseUser();
        user.setUsername("my name");
        user.setPassword("my pass");
        user.setEmail("email@example.com");

// other fields can be set just like with ParseObject
        user.put("phone", "650-555-0000");

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }
            }
        });
    }
}
