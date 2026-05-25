package com.thaivantrung.littleenglishhero;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    ImageView imgSplash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imgSplash =  findViewById(R.id.img_icon_splash);
        Animation floatingAnim = AnimationUtils.loadAnimation(this, R.anim.up_down);
        imgSplash.startAnimation(floatingAnim);

        MusicManager.playMusic(this);

        new Handler().postDelayed(() -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(user != null){
                // ĐÃ LOGIN
                startActivity(new Intent(SplashActivity.this, MainMenuActivity.class));
            } else {
                // CHƯA LOGIN
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            finish();
        }, 6000);
    }
}