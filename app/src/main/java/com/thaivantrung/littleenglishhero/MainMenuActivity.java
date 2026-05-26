package com.thaivantrung.littleenglishhero;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainMenuActivity extends AppCompatActivity {
    TextView txtName;
    ImageView imgAvatar;
    ProgressBar progressXP;
    TextView txtXP;
    TextView txtLevel;
    LinearLayout btnLearn, btnScore, btnSettings, btnUpdate;
    Animation scaleAnimation;
    TextView txtStreak;
    boolean isLoggingOut = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_menu);
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        finishAffinity();
                    }
                }
        );

        txtName = findViewById(R.id.txtName);
        txtXP = findViewById(R.id.txtXP);
        txtLevel = findViewById(R.id.txtLevel);
        txtStreak = findViewById(R.id.txtStreak);

        imgAvatar = findViewById(R.id.imgAvatar);

        btnLearn = findViewById(R.id.btnLearn);
        btnScore = findViewById(R.id.btnScore);
        btnSettings = findViewById(R.id.btnSettings);
        btnUpdate = findViewById(R.id.btnUpdate);

        progressXP = findViewById(R.id.progressXP);

        // animation
        scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_anim);

        // data
        SharedPreferences prefs = getSharedPreferences("LEH_DATA", MODE_PRIVATE);

        String name = prefs.getString("player_name", "Little Hero");

        int avatar = prefs.getInt("player_avatar", R.drawable.avatar_bear);

        txtName.setText(name + "!");
        imgAvatar.setImageResource(avatar);
        loadPlayerProgress();

        // BUTTON EFFECTS
        setButtonEffect(btnLearn, LearnActivity.class);

        setButtonEffect(btnScore, ScoreActivity.class);


        // Cài Đặt Popup
        btnSettings.setOnClickListener(v -> {
            v.startAnimation(scaleAnimation);
            SoundManager.playClick(this);
            showSettingsDialog();
        });
    }

    // BUTTON EFFECT + SOUND + CHUYỂN MÀN
    private void setButtonEffect(
            LinearLayout button,
            Class<?> targetActivity
    ) {
        button.setOnClickListener(v -> {
            // animation
            v.startAnimation(scaleAnimation);
            // sound
            SoundManager.playClick(this);
            // delay nhẹ cho mượt
            v.postDelayed(() -> {
                Intent intent = new Intent(MainMenuActivity.this, targetActivity);
                startActivity(intent);
            }, 150);
        });
    }

    // SETTINGS POPUP
    // SETTINGS POPUP
    private void showSettingsDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_settings);

        dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));

        SharedPreferences prefs = getSharedPreferences("LEH_DATA", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Switch switchMusic = dialog.findViewById(R.id.switchMusic);
        Switch switchEffect = dialog.findViewById(R.id.switchEffect);
        Button btnLogout = dialog.findViewById(R.id.btnLogout);
        Button btnClose = dialog.findViewById(R.id.btnClose);

        // ===== MUSIC STATE =====
        boolean isMusicOn = prefs.getBoolean("music_on", true);
        switchMusic.setOnCheckedChangeListener(null);
        switchMusic.setChecked(isMusicOn);
        switchMusic.setText(isMusicOn ? "Music ON" : "Music OFF");

        switchMusic.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("music_on", isChecked);
            editor.apply();
            switchMusic.setText(isChecked ? "Music ON" : "Music OFF");
            if (isChecked) {
                MusicManager.resumeMusic();
            } else {
                MusicManager.pauseMusic();
            }
        });

        // ===== EFFECT STATE =====
        boolean isEffectOn = prefs.getBoolean("effect_on", true);
        switchEffect.setChecked(isEffectOn);
        switchEffect.setText(isEffectOn ? "Effect ON" : "Effect OFF");
        SoundManager.setEffectOn(isEffectOn);
        switchEffect.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("effect_on", isChecked);
            editor.apply();
            switchEffect.setText(isChecked ? "Effect ON" : "Effect OFF");
            SoundManager.setEffectOn(isChecked);
        });

        // ===== LOGOUT BUTTON =====
        btnLogout.setOnClickListener(v -> {
            isLoggingOut = true;
            // 1. Đăng xuất Firebase
            FirebaseAuth.getInstance().signOut();

            // 3. FIX LỖI MẤT NHẠC: Giữ lại cấu hình Settings trước khi clear data
            boolean currentMusic = prefs.getBoolean("music_on", true);
            boolean currentEffect = prefs.getBoolean("effect_on", true);

            editor.clear(); // Xóa data tiến độ (XP, name, avatar...)
            editor.putBoolean("music_on", currentMusic); // Ghi lại cấu hình nhạc
            editor.putBoolean("effect_on", currentEffect);
            editor.apply();

            // Bỏ dòng MusicManager.stopMusic() nếu bạn muốn màn hình Login vẫn có nhạc nền.

            Intent intent = new Intent(MainMenuActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            dialog.dismiss();
        });

        // ===== CLOSE BUTTON =====
        btnClose.setOnClickListener(v -> {
            SoundManager.playClick(this);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void loadPlayerProgress() {

        SharedPreferences prefs =
                getSharedPreferences(
                        "LEH_DATA",
                        MODE_PRIVATE
                );

        int totalXP =
                prefs.getInt(
                        "total_xp",
                        0
                );
        int streak =
                prefs.getInt(
                        "streak",
                        0
                );

        // LEVEL
        int level = (totalXP / 100) + 1;

        // XP hiện tại trong level
        int currentLevelXP = totalXP % 100;

        // progress
        progressXP.setMax(100);

        progressXP.setProgress(currentLevelXP);

        // text xp
        txtXP.setText(
                currentLevelXP
                        + " / 100 XP"
        );

        // level
        txtLevel.setText(
                "Level " + level
        );

        txtStreak.setText("Day " + streak + " streak!");

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPlayerProgress();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isLoggingOut) {
            MusicManager.stopMusic();
        }

    }
}