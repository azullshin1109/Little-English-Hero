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

public class MainMenuActivity extends AppCompatActivity {
    TextView txtName;
    ImageView imgAvatar;
    ProgressBar progressXP;
    TextView txtXP;
    TextView txtLevel;
    LinearLayout btnLearn, btnScore, btnSettings, btnUpdate;
    Animation scaleAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_menu);
        getOnBackPressedDispatcher().addCallback(
                this,
                new OnBackPressedCallback(true) {

                    @Override
                    public void handleOnBackPressed() {

                        finishAffinity();

                    }

                }
        );

        txtName = findViewById(R.id.txtName);
        txtXP = findViewById(R.id.txtXP);
        txtLevel = findViewById(R.id.txtLevel);

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

        String name = prefs.getString(
                        "player_name",
                        "Little Hero"
                );

        int avatar = prefs.getInt(
                        "player_avatar",
                        R.drawable.avatar_bear
                );

        txtName.setText(name + "!");
        imgAvatar.setImageResource(avatar);
        loadPlayerProgress();

        // BUTTON EFFECTS

        setButtonEffect(
                btnLearn,
                LearnActivity.class
        );

        setButtonEffect(
                btnScore,
                ScoreActivity.class
        );


        // SETTINGS POPUP
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

                Intent intent = new Intent(
                        MainMenuActivity.this,
                        targetActivity
                );

                startActivity(intent);

            }, 150);
        });
    }

    // SETTINGS POPUP
    private void showSettingsDialog() {

        Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.dialog_settings);

        // ánh xạ
        SeekBar seekMusic =
                dialog.findViewById(R.id.seekMusic);

        SeekBar seekEffect =
                dialog.findViewById(R.id.seekEffect);

        Switch switchMusic =
                dialog.findViewById(R.id.switchMusic);

        Switch switchEffect =
                dialog.findViewById(R.id.switchEffect);

        Button btnClose =
                dialog.findViewById(R.id.btnClose);

        // volume mặc định
        seekMusic.setProgress(40);
        seekEffect.setProgress(40);

        // bật tắt music
        switchMusic.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    if (isChecked) {

                        MusicManager.resumeMusic();

                    } else {

                        MusicManager.pauseMusic();
                    }
                });

        // close popup
        btnClose.setOnClickListener(v -> {

            SoundManager.playClick(this);

            dialog.dismiss();
        });

        dialog.show();
    }

    private void loadPlayerProgress(){

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

    }
    @Override
    protected void onResume() {
        super.onResume();
        loadPlayerProgress();
    }

}