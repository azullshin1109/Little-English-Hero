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

        loadPlayerInfo();
        loadScoreData();
        checkBadge();

        btnBack.setOnClickListener(v -> {
            SoundManager.playClick(this);
            startActivity(new Intent(ScoreActivity.this, MainMenuActivity.class));
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
        String name = prefs.getString("player_name", "Little Hero");
        int avatar = prefs.getInt("player_avatar", R.drawable.avatar_bear);
        txtName.setText(name + "!");
        imgAvatar.setImageResource(avatar);
    }

    // SCORE
    private void loadScoreData() {
        int xp = prefs.getInt("total_xp", 0);
        int quizCorrect = prefs.getInt("quiz_correct", 0);
        int perfectQuiz = prefs.getInt("perfect_quiz", 0);
        int lessonsDone = prefs.getInt("lessons_done", 0);

        // Tính level dựa trên tổng XP
        int level = 1;
        int xpNeed = 100;   // XP cần để lên level đầu tiên
        int remainXP = xp;

        while(remainXP >= xpNeed){
            remainXP -= xpNeed;  // trừ XP đã dùng để lên level
            level++;             // tăng level
            xpNeed += 100;       // level sau cần nhiều XP hơn
        }

        int currentXP = remainXP;
        int nextLevelXP = xpNeed;
        int needXP = nextLevelXP - currentXP;

        //TEXT
        tvXP.setText(String.valueOf(xp));
        tvQuiz.setText(String.valueOf(quizCorrect));
        tvPerfect.setText(String.valueOf(perfectQuiz));
        tvLesson.setText(String.valueOf(lessonsDone));
        tvLevel.setText(String.valueOf(level));

        // LEVEL TEXT
        tvNextLevel.setText("Cần " + needXP + " XP để lên cấp!");
        tvCurrentXP.setText(currentXP + " / " + nextLevelXP + " XP");
        tvLevelLeft.setText("Level " + level);
        tvLevelRight.setText("Level " + (level + 1));

        // PROGRESS BAR
        progressXP.setMax(nextLevelXP);
        progressXP.setProgress(currentXP);

        //STREAK
        int streak = prefs.getInt("streak", 0);

        // Cập nhật giao diện Streak
        if(streak <= 0){
            tvStreak.setText("Chưa học bài nào!");
        }
        else{
            tvStreak.setText(streak + " Days");
        }
    }

    // STREAK
    private static int calculateStreak(SharedPreferences prefs){
        // Lấy ngày học cuối cùng được lưu trong máy.
        String lastStudyDate = prefs.getString("last_study_date", "");
        if (lastStudyDate.equals("")) return 1;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Calendar lastCal = Calendar.getInstance();
            lastCal.setTime(sdf.parse(lastStudyDate));
            Calendar todayCal = Calendar.getInstance();
            //Kiêm tra ngay gần nhất
            // Lấy thời điểm hiện tại trừ đi thời điểm học lần trước
            long diff = todayCal.getTimeInMillis() - lastCal.getTimeInMillis();
            long days = diff / (1000 * 60 * 60 * 24);

            int streak = prefs.getInt("streak", 0);

            if (days == 1) return streak + 1; // đúng 1 +
            else if (days > 1) return 1;// lớn hơn 1 chuỗi về 0
            else return streak; // giữ nguyên chuỗi

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 1;
    }

    // BADGE
    private void checkBadge() {
        int familyScore = prefs.getInt("family_score", 0);
        int animalScore = prefs.getInt("animals_score", 0);

        // PERFECT LESSON
        boolean familyPerfect = prefs.getBoolean("family_perfect", false);
        boolean animalPerfect = prefs.getBoolean("animals_perfect", false);
        boolean fruitsPerfect = prefs.getBoolean("fruits_perfect", false);
        boolean colorPerfect = prefs.getBoolean("colors_perfect", false);
        boolean jobsPerfect = prefs.getBoolean("numbers_perfect", false);

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
        if(
                familyPerfect
                        && animalPerfect
                        && fruitsPerfect
                        && colorPerfect
                        && jobsPerfect
        ) {
            badgeQuiz.setAlpha(1f);
        } else {
            badgeQuiz.setAlpha(0.4f);
        }
    }

    public static void addXP(
            AppCompatActivity activity,
            int addXP,
            boolean perfectQuiz,
            String lessonType,
            int lessonScore
    ){
        SharedPreferences prefs = activity.getSharedPreferences("LEH_DATA", MODE_PRIVATE);

        int xp = prefs.getInt("total_xp", 0);
        int quizCorrect = prefs.getInt("quiz_correct", 0);
        int perfect = prefs.getInt("perfect_quiz", 0);
        int lessonsDone = prefs.getInt("lessons_done", 0);

        xp += addXP;
        quizCorrect++;
        lessonsDone++;

        SharedPreferences.Editor editor = prefs.edit();
        if(perfectQuiz) {
            perfect++;
            if(lessonType.equals("family")) editor.putBoolean("family_perfect", true);
            if(lessonType.equals("animals")) editor.putBoolean("animals_perfect", true);
            if(lessonType.equals("fruits")) editor.putBoolean("fruits_perfect", true);
            if(lessonType.equals("colors")) editor.putBoolean("colors_perfect", true);
            if(lessonType.equals("numbers")) editor.putBoolean("numbers_perfect", true);
        }
        editor.putInt("total_xp", xp);
        editor.putInt("quiz_correct", quizCorrect);
        editor.putInt("perfect_quiz", perfect);
        editor.putInt("lessons_done", lessonsDone);

        if(lessonType.equals("family")) {
            editor.putInt("family_score", lessonScore);
        }
        if(lessonType.equals("animals")) {
            editor.putInt("animals_score", lessonScore);
        }

        String today = new SimpleDateFormat("dd/MM/yyyy",
                Locale.getDefault())
                .format(Calendar.getInstance().getTime());

        int streak = calculateStreak(prefs);

        editor.putInt("streak", streak);
        editor.putString("last_study_date", today);

        editor.apply();

        com.google.firebase.auth.FirebaseUser user = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            com.google.firebase.firestore.FirebaseFirestore db = com.google.firebase.firestore.FirebaseFirestore.getInstance();

            java.util.Map<String, Object> progressData = new java.util.HashMap<>();

            progressData.put("total_xp", prefs.getInt("total_xp", 0));
            progressData.put("quiz_correct", prefs.getInt("quiz_correct", 0));
            progressData.put("perfect_quiz", prefs.getInt("perfect_quiz", 0));
            progressData.put("lessons_done", prefs.getInt("lessons_done", 0));
            progressData.put("streak", prefs.getInt("streak", 0));
            progressData.put("last_study_date", prefs.getString("last_study_date", ""));

            progressData.put("family_score", prefs.getInt("family_score", 0));
            progressData.put("animals_score", prefs.getInt("animals_score", 0));

            progressData.put("family_perfect", prefs.getBoolean("family_perfect", false));
            progressData.put("animals_perfect", prefs.getBoolean("animals_perfect", false));
            progressData.put("fruits_perfect", prefs.getBoolean("fruits_perfect", false));
            progressData.put("colors_perfect", prefs.getBoolean("colors_perfect", false));
            progressData.put("numbers_perfect", prefs.getBoolean("numbers_perfect", false));

            db.collection("users")
                    .document(uid).set(progressData,
                            com.google.firebase.firestore
                                    .SetOptions.merge());
        }
    }
}