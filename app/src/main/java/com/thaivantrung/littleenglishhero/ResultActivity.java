package com.thaivantrung.littleenglishhero;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ResultActivity extends AppCompatActivity {

    TextView tvScoreCurrent;
    TextView tvScoreTotal;

    TextView tvStatAccuracyValue;
    TextView tvStatTimeValue;

    TextView tvRatingText;

    TextView tvTitleExcellent;
    TextView tvSubtitle;

    TextView tvMascotCheer;
    TextView tvMascotMessage;

    ImageView ivStar1;
    ImageView ivStar2;
    ImageView ivStar3;
    ImageView imgTrophy;
    ImageView imgConfettiLeft, imgConfettiRight;

    CardView cardReplay;
    CardView cardAnotherLesson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        imgTrophy = findViewById(R.id.iv_trophy);
        imgConfettiRight = findViewById(R.id.tv_confetti_right);
        imgConfettiLeft = findViewById(R.id.tv_confetti_left);

        Animation trophyAnim = AnimationUtils.loadAnimation(this, R.anim.snake_anim);
        Animation rotateAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_anim);

        imgTrophy.startAnimation(rotateAnim);
        imgConfettiRight.startAnimation(trophyAnim);
        imgConfettiLeft.startAnimation(trophyAnim);
        // FIND VIEW
        tvScoreCurrent = findViewById(R.id.tv_score_current);
        tvScoreTotal = findViewById(R.id.tv_score_total);
        tvStatAccuracyValue = findViewById(R.id.tv_stat_accuracy_value);
        tvStatTimeValue = findViewById(R.id.tv_stat_time_value);
        tvRatingText = findViewById(R.id.tv_rating_text);
        tvTitleExcellent = findViewById(R.id.tv_title_excellent);
        tvSubtitle = findViewById(R.id.tv_subtitle);
        tvMascotCheer = findViewById(R.id.tv_mascot_cheer);
        tvMascotMessage = findViewById(R.id.tv_mascot_message);

        ivStar1 = findViewById(R.id.iv_star_1);
        ivStar2 = findViewById(R.id.iv_star_2);
        ivStar3 = findViewById(R.id.iv_star_3);

        cardReplay = findViewById(R.id.cardView_btn_replay);

        cardAnotherLesson = findViewById(R.id.cardView_btn_next_lesson);

        // GET DATA


        int score =
                getIntent().getIntExtra(
                        "score",
                        0
                );

        int total =
                getIntent().getIntExtra(
                        "total",
                        10
                );

        long timeMillis =
                getIntent().getLongExtra(
                        "time",
                        0
                );

        int lessonId =
                getIntent().getIntExtra(
                        "lessonId",
                        1
                );

        // =========================
        // CALCULATE
        // =========================

        int accuracy =
                (score * 100) / total;

        long totalSeconds =
                timeMillis / 1000;

        long minutes =
                totalSeconds / 60;

        long seconds =
                totalSeconds % 60;

        // =========================
        // SCORE
        // =========================

        tvScoreCurrent.setText(
                String.valueOf(score)
        );

        tvScoreTotal.setText(
                String.valueOf(total)
        );

        // =========================
        // ACCURACY
        // =========================

        tvStatAccuracyValue.setText(
                accuracy + "%"
        );

        // =========================
        // TIME
        // =========================

        String timeText =
                String.format(
                        "%02d:%02d",
                        minutes,
                        seconds
                );

        tvStatTimeValue.setText(timeText);

        // =========================
        // STAR + TEXT
        // =========================

        ivStar1.setAlpha(0.2f);
        ivStar2.setAlpha(0.2f);
        ivStar3.setAlpha(0.2f);

        // 3 STAR
        if(accuracy == 100){

            ivStar1.setAlpha(1f);
            ivStar2.setAlpha(1f);
            ivStar3.setAlpha(1f);

            tvRatingText.setText(
                    "Perfect Hero!"
            );

            tvTitleExcellent.setText(
                    "Xuất sắc!"
            );

            tvSubtitle.setText(
                    "Bé đã trả lời đúng tất cả!"
            );

            tvMascotCheer.setText(
                    "Tuyệt vời!"
            );

            tvMascotMessage.setText(
                    "Bé thật sự là một Little English Hero!"
            );

        }

        // 2 STAR
        else if(accuracy >= 50){

            ivStar1.setAlpha(1f);
            ivStar2.setAlpha(1f);

            tvRatingText.setText(
                    "Great Job!"
            );

            tvTitleExcellent.setText(
                    "Rất tốt!"
            );

            tvSubtitle.setText(
                    "Bé làm rất tốt rồi!"
            );

            tvMascotCheer.setText(
                    "Giỏi lắm!"
            );

            tvMascotMessage.setText(
                    "Cố thêm chút nữa là đạt 3 sao rồi!"
            );

        }

        // 1 STAR
        else {

            ivStar1.setAlpha(1f);

            tvRatingText.setText(
                    "Keep Trying!"
            );

            tvTitleExcellent.setText(
                    "Cố lên nhé!"
            );

            tvSubtitle.setText(
                    "Luyện tập thêm sẽ giỏi hơn!"
            );

            tvMascotCheer.setText(
                    "Không sao đâu!"
            );

            tvMascotMessage.setText(
                    "Bé hãy thử lại lần nữa nhé!"
            );

        }

        // CHOI LAI
        cardReplay.setOnClickListener(v -> {
            Intent intent =new Intent(ResultActivity.this, StudyActivity.class);
            intent.putExtra("lessonId", lessonId);
            intent.putExtra("startQuizOnly", true);
            startActivity(intent);
            finish();
        });

        // BAI HOC KHAC
        cardAnotherLesson.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, LearnActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }
}