package com.example.aubre.hornet;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.Timer;

public class View_Image_Acttivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__image__acttivity);
    }

    ImageView imageView = (ImageView)findViewById(R.id.imageView);

    Uri imageUri = getIntent().getData();

    Picasso.with(this).l

    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
        @Override
        public void run() {
            finish();
        }
    }, 10*1000);



}
