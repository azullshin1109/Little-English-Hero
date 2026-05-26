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
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

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

        scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale_anim);

        SharedPreferences prefs = getSharedPreferences("LEH_DATA", MODE_PRIVATE);
        String name = prefs.getString("player_name", "Little Hero");
        int avatar = prefs.getInt("player_avatar", R.drawable.avatar_bear);

        txtName.setText(name + "!");
        imgAvatar.setImageResource(avatar);
        loadPlayerProgress();

        setButtonEffect(btnLearn, LearnActivity.class);
        setButtonEffect(btnScore, ScoreActivity.class);

        btnSettings.setOnClickListener(v -> {
            v.startAnimation(scaleAnimation);
            SoundManager.playClick(this);
            showSettingsDialog();
        });
    }

    private void setButtonEffect(LinearLayout button, Class<?> targetActivity) {
        button.setOnClickListener(v -> {
            v.startAnimation(scaleAnimation);
            SoundManager.playClick(this);
            v.postDelayed(() -> {
                Intent intent = new Intent(MainMenuActivity.this, targetActivity);
                startActivity(intent);
            }, 150);
        });
    }

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
                // ĐÃ SỬA: Gọi playMusic để khởi tạo lại nếu máy vừa bị thoát ra
                MusicManager.playMusic(MainMenuActivity.this);
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
            SoundManager.playClick(this);
            isLoggingOut = true;

            FirebaseAuth.getInstance().signOut();

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            GoogleSignInClient googleClient = GoogleSignIn.getClient(MainMenuActivity.this, gso);

            boolean currentMusic = prefs.getBoolean("music_on", true);
            boolean currentEffect = prefs.getBoolean("effect_on", true);

            editor.clear();
            editor.putBoolean("music_on", currentMusic);
            editor.putBoolean("effect_on", currentEffect);
            editor.apply();

            googleClient.signOut().addOnCompleteListener(task -> {
                Intent intent = new Intent(MainMenuActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                dialog.dismiss();
            });
        });

        // ===== CLOSE BUTTON =====
        btnClose.setOnClickListener(v -> {
            SoundManager.playClick(this);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void loadPlayerProgress() {
        SharedPreferences prefs = getSharedPreferences("LEH_DATA", MODE_PRIVATE);
        int totalXP = prefs.getInt("total_xp", 0);
        int streak = prefs.getInt("streak", 0);

        int level = (totalXP / 100) + 1;
        int currentLevelXP = totalXP % 100;

        progressXP.setMax(100);
        progressXP.setProgress(currentLevelXP);
        txtXP.setText(currentLevelXP + " / 100 XP");
        txtLevel.setText("Level " + level);
        txtStreak.setText("Day " + streak + " streak!");
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPlayerProgress();

        // CHỐT CHẶN BẢO MẬT: Ép hệ thống cập nhật đúng ý muốn của người dùng mỗi khi ra trang chủ
        SharedPreferences prefs = getSharedPreferences("LEH_DATA", MODE_PRIVATE);
        boolean isMusicOn = prefs.getBoolean("music_on", true);
        boolean isEffectOn = prefs.getBoolean("effect_on", true);

        SoundManager.setEffectOn(isEffectOn);

        if (isMusicOn) {
            MusicManager.playMusic(this);
            MusicManager.resumeMusic();
        } else {
            MusicManager.pauseMusic();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isLoggingOut) {
            MusicManager.stopMusic();
        }
    }
}