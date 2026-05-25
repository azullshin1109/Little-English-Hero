package com.thaivantrung.littleenglishhero;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ScoreActivity extends AppCompatActivity {

    // HEADER
    ImageView btnBack;
    ImageView imgAvatar;

    TextView txtName;

    // SCORE
    TextView tvXP;
    TextView tvQuiz;
    TextView tvPerfect;
    TextView tvLesson;

    // LEVEL
    TextView tvLevel;
    TextView tvNextLevel;
    TextView tvCurrentXP;
    TextView tvLevelLeft;
    TextView tvLevelRight;

    // STREAK
    TextView tvStreak;

    // PROGRESS
    ProgressBar progressXP;

    // BADGE
    LinearLayout badgeFamily;
    LinearLayout badgeAnimal;
    LinearLayout badgeQuiz;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        initViews();

        prefs = getSharedPreferences("LEH_DATA", MODE_PRIVATE);

        // LOAD ALL DATA
        loadPlayerInfo();

        loadScoreData();

        calculateStreak();

        checkBadge();

        btnBack.setOnClickListener(v -> {

            startActivity(
                    new Intent(
                            ScoreActivity.this,
                            MainMenuActivity.class
                    )
            );

            finish();
        });
    }

    private void initViews() {
        btnBack = findViewById(R.id.btn_back);
        imgAvatar = findViewById(R.id.imgAvatar);
        txtName = findViewById(R.id.txtName);
        // SCORE
        tvXP = findViewById(R.id.tv_total_xp);
        tvQuiz = findViewById(R.id.tv_quiz_correct);
        tvPerfect = findViewById(R.id.tv_perfect_quiz);
        tvLesson = findViewById(R.id.tv_lessons_done);
        // LEVEL
        tvLevel = findViewById(R.id.tv_level);
        tvNextLevel = findViewById(R.id.tv_next_level);
        tvCurrentXP = findViewById(R.id.tv_current_xp);
        tvLevelLeft = findViewById(R.id.tv_level_left);
        tvLevelRight = findViewById(R.id.tv_level_right);
        // STREAK
        tvStreak = findViewById(R.id.tv_streak);
        // PROGRESS
        progressXP = findViewById(R.id.progressXP);
        // BADGE
        badgeFamily = findViewById(R.id.badge_family);
        badgeAnimal = findViewById(R.id.badge_animal);
        badgeQuiz = findViewById(R.id.badge_quiz);
    }
    private void loadPlayerInfo() {
        // NAME
        String playerName = prefs.getString("player_name", "Little Hero");
        txtName.setText(playerName);
        // AVATAR
        int avatar = prefs.getInt("player_avatar", R.drawable.avatar_bear);
        imgAvatar.setImageResource(avatar);
    }
    // SCORE
    private void loadScoreData() {
        int xp = prefs.getInt("xp", 0);
        int quizCorrect = prefs.getInt("quiz_correct", 0);
        int perfectQuiz = prefs.getInt("perfect_quiz", 0);
        int lessonsDone = prefs.getInt("lessons_done", 0);

        // LEVEL SYSTEM
        int level = (xp / 100) + 1;
        int currentXP = xp % 100;
        int needXP = 100 - currentXP;

        // SET TEXT
        tvXP.setText(String.valueOf(xp));
        tvQuiz.setText(String.valueOf(quizCorrect));
        tvPerfect.setText(String.valueOf(perfectQuiz));
        tvLesson.setText(String.valueOf(lessonsDone));
        tvLevel.setText(String.valueOf(level));

        // LEVEL TEXT
        tvNextLevel.setText("Only " + needXP + " XP to Level Up!");
        tvCurrentXP.setText(currentXP + " / 100 XP");
        tvLevelLeft.setText("Level " + level);
        tvLevelRight.setText("Level " + (level + 1));

        // PROGRESS BAR
        progressXP.setMax(100);
        progressXP.setProgress(currentXP);
    }

    // STREAK
    private void calculateStreak() {
        String lastStudyDate = prefs.getString("last_study_date", "");
        int streak = prefs.getInt("streak", 0);
        String today = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime());
        if(lastStudyDate.equals("")) {
            streak = 1;
            prefs.edit()
                    .putString(
                            "last_study_date",
                            today
                    )
                    .putInt(
                            "streak",
                            streak
                    )
                    .apply();

        } else {
            try {
                SimpleDateFormat sdf =
                        new SimpleDateFormat(
                                "dd/MM/yyyy",
                                Locale.getDefault()
                        );
                Calendar lastCal =
                        Calendar.getInstance();
                lastCal.setTime(
                        sdf.parse(lastStudyDate)
                );
                Calendar todayCal =
                        Calendar.getInstance();
                long diff =
                        todayCal.getTimeInMillis()
                                - lastCal.getTimeInMillis();
                long days =
                        diff / (1000 * 60 * 60 * 24);
                // HỌC LIÊN TIẾP
                if(days == 1) {
                    streak++;
                    prefs.edit()
                            .putInt(
                                    "streak",
                                    streak
                            )
                            .putString(
                                    "last_study_date",
                                    today
                            )
                            .apply();
                }

                // NGHỈ QUÁ 1 NGÀY
                else if(days > 1) {

                    streak = 1;

                    prefs.edit()
                            .putInt(
                                    "streak",
                                    streak
                            )
                            .putString(
                                    "last_study_date",
                                    today
                            )
                            .apply();
                }

            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        tvStreak.setText(streak + " Days");
    }

    // =========================
    // BADGE
    // =========================
    private void checkBadge() {

        int familyScore =
                prefs.getInt(
                        "family_score",
                        0
                );

        int animalScore =
                prefs.getInt(
                        "animal_score",
                        0
                );

        int perfectQuiz =
                prefs.getInt(
                        "perfect_quiz",
                        0
                );

        // FAMILY MASTER
        if(familyScore >= 100) {

            badgeFamily.setAlpha(1f);

        } else {

            badgeFamily.setAlpha(0.4f);
        }

        // ANIMAL MASTER
        if(animalScore >= 100) {

            badgeAnimal.setAlpha(1f);

        } else {

            badgeAnimal.setAlpha(0.4f);
        }

        // QUIZ CHAMPION
        if(perfectQuiz >= 5) {

            badgeQuiz.setAlpha(1f);

        } else {

            badgeQuiz.setAlpha(0.4f);
        }
    }

    // =========================
    // ADD XP AFTER QUIZ
    // =========================
    public static void addXP(
            AppCompatActivity activity,
            int addXP,
            boolean perfectQuiz,
            String lessonType,
            int lessonScore
    ) {

        SharedPreferences prefs =
                activity.getSharedPreferences(
                        "LEH_DATA",
                        MODE_PRIVATE
                );

        // CURRENT DATA
        int xp =
                prefs.getInt("xp", 0);

        int quizCorrect =
                prefs.getInt(
                        "quiz_correct",
                        0
                );

        int perfect =
                prefs.getInt(
                        "perfect_quiz",
                        0
                );

        int lessonsDone =
                prefs.getInt(
                        "lessons_done",
                        0
                );

        // ADD
        xp += addXP;

        quizCorrect++;

        lessonsDone++;

        if(perfectQuiz) {

            perfect++;
        }

        SharedPreferences.Editor editor =
                prefs.edit();

        // SAVE
        editor.putInt("xp", xp);

        editor.putInt(
                "quiz_correct",
                quizCorrect
        );

        editor.putInt(
                "perfect_quiz",
                perfect
        );

        editor.putInt(
                "lessons_done",
                lessonsDone
        );

        // SAVE LESSON SCORE
        if(lessonType.equals("family")) {

            editor.putInt(
                    "family_score",
                    lessonScore
            );
        }

        if(lessonType.equals("animal")) {

            editor.putInt(
                    "animal_score",
                    lessonScore
            );
        }

        // SAVE STREAK DATE
        String today =
                new SimpleDateFormat(
                        "dd/MM/yyyy",
                        Locale.getDefault()
                ).format(
                        Calendar.getInstance().getTime()
                );

        editor.putString(
                "last_study_date",
                today
        );

        editor.apply();
    }
}