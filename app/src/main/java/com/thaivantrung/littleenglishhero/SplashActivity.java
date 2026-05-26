package com.thaivantrung.littleenglishhero;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    ImageView imgSplash;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imgSplash = findViewById(R.id.img_icon_splash);
        progressBar = findViewById(R.id.progressBar);

        Animation floatingAnim = AnimationUtils.loadAnimation(this, R.anim.up_down);
        imgSplash.startAnimation(floatingAnim);

        MusicManager.playMusic(this);

        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", 0, 100);
        animation.setDuration(6000);
        animation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    startActivity(new Intent(SplashActivity.this, MainMenuActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                finish();
            }
        });
        animation.start();
    }
}