package com.thaivantrung.littleenglishhero;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.Manifest;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class SplashActivity extends AppCompatActivity {
    ImageView imgIconSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    1
            );

        }


        MusicManager.playMusic(this);

        // Ánh xạ ImageView
        imgIconSplash = findViewById(R.id.img_icon_splash);

        // Load animation
        Animation animation = AnimationUtils.loadAnimation(
                this,
                R.anim.up_down
        );

        // Bắt đầu animation
        imgIconSplash.startAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(
                        SplashActivity.this, ChooseAvatarActivity.class
                );
                startActivity(intent);
                finish();
            }
        }, 6000);
    }
}