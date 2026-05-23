package com.thaivantrung.littleenglishhero;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;

public class StudyActivity extends AppCompatActivity {

    FrameLayout frameContent;

    // FLASHCARD
    ProgressBar progressWord;

    TextView txtEnglish;
    TextView txtMeaning;
    TextView txtProgress;

    ImageView imgWord;
    ImageView btnSpeak;
    ImageView btnMic;

    Button btnNext;

    // QUIZ
    TextView tvQuestionWord;
    TextView tvFeedbackTitle;
    TextView tvFeedbackSub;
    TextView tvSpeechBubble;
    TextView tvQuestionPrompt;

    ImageView tvQuestionImage;

    Button btnAnswer1;
    Button btnAnswer2;
    Button btnAnswer3;
    Button btnAnswer4;

    Button btnContinue;

    LinearLayout feedbackPanel;

    ProgressBar progressQuiz;

    TextView tvQuestionCount;
    TextView tvProgressPct;

    // SPEECH
    TextToSpeech textToSpeech;

    SpeechRecognizer speechRecognizer;
    Intent speechIntent;

    // DATA
    ArrayList<VocabularyModel> list;
    ArrayList<VocabularyModel> quizList;

    int currentIndex = 0;

    boolean isImageQuiz = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);

        frameContent = findViewById(R.id.frameContent);

        // DATABASE
        DBHelper dbHelper = new DBHelper(this);

        int lessonId = getIntent().getIntExtra(
                "lessonId",
                1
        );

        list = dbHelper.getVocabularyByLesson(lessonId);

        // RANDOM QUIZ LIST
        quizList = new ArrayList<>(list);
        Collections.shuffle(quizList);

        quizList = new ArrayList<>();
        ArrayList<VocabularyModel> tempList = new ArrayList<>(list);
        Collections.shuffle(tempList);
        while(quizList.size() < 10){
            Collections.shuffle(tempList);
            for(VocabularyModel vocab : tempList){
                quizList.add(vocab);
                if(quizList.size() == 10){
                    break;
                }
            }
        }

        // FLASHCARD
        showFlashcard();

        loadWord();

        // NEXT
        btnNext.setOnClickListener(v -> {

            currentIndex++;

            if(currentIndex < list.size()){

                loadWord();

            } else {

                currentIndex = 0;

                showRandomQuiz();

            }

        });

        // TEXT TO SPEECH
        textToSpeech = new TextToSpeech(this, status -> {

            if(status != TextToSpeech.ERROR){

                textToSpeech.setLanguage(Locale.US);

            }

        });

        // SPEECH
        setupSpeechRecognizer();

        // PERMISSION
        if(ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
        ) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    1
            );
        }
    }

    // =====================================================
    // FLASHCARD
    // =====================================================

    private void showFlashcard(){

        View view = getLayoutInflater().inflate(
                R.layout.item_flashcard,
                null
        );

        ImageView btnBack = view.findViewById(R.id.btn_back_flashcard);
        btnBack.setOnClickListener(v -> finish());

        frameContent.addView(view);

        txtEnglish = view.findViewById(R.id.txtEnglish);
        txtMeaning = view.findViewById(R.id.txtMeaning);

        txtProgress = view.findViewById(R.id.txtProgress);

        imgWord = view.findViewById(R.id.imgWord);

        btnSpeak = view.findViewById(R.id.btnSpeak);
        btnMic = view.findViewById(R.id.btnMic);

        btnNext = view.findViewById(R.id.btnNext);

        progressWord = view.findViewById(R.id.progressWord);


    }

    private void loadWord(){

        VocabularyModel vocab = list.get(currentIndex);

        txtEnglish.setText(vocab.getWord());

        txtMeaning.setText(vocab.getMeaning());

        int imageId = getResources().getIdentifier(
                vocab.getImage(),
                "drawable",
                getPackageName()
        );

        imgWord.setImageResource(imageId);

        progressWord.setMax(list.size());

        progressWord.setProgress(currentIndex + 1);

        txtProgress.setText(
                (currentIndex + 1)
                        + " / "
                        + list.size()
                        + " WORDS"
        );

        // SPEAK
        btnSpeak.setOnClickListener(v -> {

            textToSpeech.speak(
                    vocab.getWord(),
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    null
            );

        });

        // MIC
        btnMic.setOnClickListener(v -> {

            btnMic.clearColorFilter();

            speechRecognizer.startListening(
                    speechIntent
            );

        });

    }

    // =====================================================
    // RANDOM QUIZ
    // =====================================================

    private void showRandomQuiz(){

        isImageQuiz = new Random().nextBoolean();

        if(isImageQuiz){

            showQuizImage();

        } else {

            showQuizTranslate();

        }

    }

    // =====================================================
    // QUIZ TRANSLATE
    // =====================================================

    private void showQuizTranslate(){

        frameContent.removeAllViews();

        View view = getLayoutInflater().inflate(
                R.layout.item_quiz_translate,
                null
        );

        ImageView btnBack = view.findViewById(R.id.btn_back_translate);
        btnBack.setOnClickListener(v -> finish());

        frameContent.addView(view);

        tvQuestionWord = view.findViewById(R.id.tv_question_word);
        tvFeedbackTitle = view.findViewById(R.id.tv_feedback_title);
        tvFeedbackSub = view.findViewById(R.id.tv_feedback_sub);
        tvQuestionCount = view.findViewById(R.id.tv_question_count);
        tvProgressPct = view.findViewById(R.id.tv_progress_pct);
        tvSpeechBubble = view.findViewById(R.id.tv_speech_bubble);

        btnAnswer1 = view.findViewById(R.id.btn_answer_1);
        btnAnswer2 = view.findViewById(R.id.btn_answer_2);
        btnAnswer3 = view.findViewById(R.id.btn_answer_3);
        btnAnswer4 = view.findViewById(R.id.btn_answer_4);
        btnContinue = view.findViewById(R.id.btn_continue);

        feedbackPanel = view.findViewById(R.id.feedback_panel);

        progressQuiz = view.findViewById(R.id.progress_quiz);

        loadQuizTranslate();

    }

    private void loadQuizTranslate(){

        VocabularyModel vocab =
                quizList.get(currentIndex);

        tvQuestionWord.setText(vocab.getMeaning());

        ArrayList<String> answers =
                new ArrayList<>();

        answers.add(vocab.getWord());

        while(answers.size() < 4){

            VocabularyModel randomVocab =
                    list.get(
                            new Random().nextInt(list.size())
                    );

            if(!answers.contains(randomVocab.getWord())){

                answers.add(randomVocab.getWord());

            }

        }

        Collections.shuffle(answers);

        btnAnswer1.setText(answers.get(0));
        btnAnswer2.setText(answers.get(1));
        btnAnswer3.setText(answers.get(2));
        btnAnswer4.setText(answers.get(3));

        btnAnswer1.setOnClickListener(v ->
                checkAnswer(
                        btnAnswer1.getText().toString(),
                        vocab
                ));

        btnAnswer2.setOnClickListener(v ->
                checkAnswer(
                        btnAnswer2.getText().toString(),
                        vocab
                ));

        btnAnswer3.setOnClickListener(v ->
                checkAnswer(
                        btnAnswer3.getText().toString(),
                        vocab
                ));

        btnAnswer4.setOnClickListener(v ->
                checkAnswer(
                        btnAnswer4.getText().toString(),
                        vocab
                ));

        tvSpeechBubble.setText("Chọn nghĩa đúng cho từ này nhé!");

        updateQuizProgress();

    }

    // =====================================================
    // QUIZ IMAGE
    // =====================================================

    private void showQuizImage(){

        frameContent.removeAllViews();

        View view = getLayoutInflater().inflate(
                R.layout.item_quiz_image,
                null
        );

        //btn_back
        ImageView btnBack = view.findViewById(R.id.btn_back_image);
        btnBack.setOnClickListener(v -> finish());

        frameContent.addView(view);

        tvQuestionImage = view.findViewById(R.id.tv_question_image);
        tvFeedbackTitle = view.findViewById(R.id.tv_feedback_title);
        tvFeedbackSub = view.findViewById(R.id.tv_feedback_sub);
        tvQuestionPrompt = view.findViewById(R.id.tv_question_prompt);
        tvQuestionCount = view.findViewById(R.id.tv_question_count);
        tvProgressPct = view.findViewById(R.id.tv_progress_pct);

        btnAnswer1 = view.findViewById(R.id.btn_answer_1);
        btnAnswer2 = view.findViewById(R.id.btn_answer_2);
        btnAnswer3 = view.findViewById(R.id.btn_answer_3);
        btnAnswer4 = view.findViewById(R.id.btn_answer_4);
        btnContinue = view.findViewById(R.id.btn_continue);

        feedbackPanel = view.findViewById(R.id.feedback_panel);

        progressQuiz = view.findViewById(R.id.progress_quiz);

        loadQuizImage();

    }

    private void loadQuizImage(){

        VocabularyModel vocab =
                quizList.get(currentIndex);

        int imageId = getResources().getIdentifier(
                vocab.getImage(),
                "drawable",
                getPackageName()
        );

        tvQuestionImage.setImageResource(imageId);

        ArrayList<String> answers =
                new ArrayList<>();

        answers.add(vocab.getWord());

        while(answers.size() < 4){

            VocabularyModel randomVocab =
                    list.get(
                            new Random().nextInt(list.size())
                    );

            if(!answers.contains(randomVocab.getWord())){

                answers.add(randomVocab.getWord());

            }

        }

        Collections.shuffle(answers);

        btnAnswer1.setText(answers.get(0));
        btnAnswer2.setText(answers.get(1));
        btnAnswer3.setText(answers.get(2));
        btnAnswer4.setText(answers.get(3));

        btnAnswer1.setOnClickListener(v ->
                checkAnswer(
                        btnAnswer1.getText().toString(),
                        vocab
                ));

        btnAnswer2.setOnClickListener(v ->
                checkAnswer(
                        btnAnswer2.getText().toString(),
                        vocab
                ));

        btnAnswer3.setOnClickListener(v ->
                checkAnswer(
                        btnAnswer3.getText().toString(),
                        vocab
                ));

        btnAnswer4.setOnClickListener(v ->
                checkAnswer(
                        btnAnswer4.getText().toString(),
                        vocab
                ));

        tvQuestionPrompt.setText("Hình này tiếng Anh là gì?");

        updateQuizProgress();

    }

    // =====================================================
    // CHECK ANSWER
    // =====================================================

    private void checkAnswer(String selectedAnswer,
                             VocabularyModel vocab){

        feedbackPanel.setVisibility(View.VISIBLE);

        btnContinue.setVisibility(View.VISIBLE);

        boolean isCorrect =
                selectedAnswer.equals(vocab.getWord());

        if(isCorrect){

            tvFeedbackTitle.setText("Xuất sắc!");

            tvFeedbackSub.setText(
                    vocab.getMeaning()
                            + " = "
                            + vocab.getWord()
            );

            textToSpeech.speak(
                    "Correct",
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    null
            );

        } else {

            tvFeedbackTitle.setText("Sai rồi!");

            tvFeedbackSub.setText(
                    "Đáp án đúng là "
                            + vocab.getWord()
            );

            textToSpeech.speak(
                    "Wrong answer",
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    null
            );

        }

        btnAnswer1.setEnabled(false);
        btnAnswer2.setEnabled(false);
        btnAnswer3.setEnabled(false);
        btnAnswer4.setEnabled(false);

        btnContinue.setOnClickListener(v -> {

            currentIndex++;

            if(currentIndex < quizList.size()){

                showRandomQuiz();

            } else {

                finish();

            }

        });

    }

    // =====================================================
    // PROGRESS
    // =====================================================

    private void updateQuizProgress(){

        progressQuiz.setMax(quizList.size());

        progressQuiz.setProgress(currentIndex + 1);

        tvQuestionCount.setText(
                "Câu "
                        + (currentIndex + 1)
                        + " / "
                        + quizList.size()
        );

        int percent =
                (currentIndex + 1) * 100
                        / quizList.size();

        tvProgressPct.setText(percent + "%");

    }

    // =====================================================
    // SPEECH RECOGNIZER
    // =====================================================

    private void setupSpeechRecognizer(){

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        speechIntent = new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH
        );

        speechIntent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        );

        speechIntent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
                "en-US"
        );

        speechRecognizer.setRecognitionListener(
                new RecognitionListener() {

                    @Override
                    public void onReadyForSpeech(Bundle params) {

                        btnMic.setColorFilter(
                                android.graphics.Color.YELLOW
                        );

                    }

                    @Override
                    public void onResults(Bundle results) {

                        ArrayList<String> data =
                                results.getStringArrayList(
                                        SpeechRecognizer.RESULTS_RECOGNITION
                                );

                        if(data != null && data.size() > 0){

                            String answer = list.get(currentIndex)
                                    .getWord()
                                    .toLowerCase();

                            boolean isCorrect = false;

                            for(String text : data){

                                String spokenText =
                                        text.toLowerCase();

                                if(spokenText.equals(answer)
                                        || spokenText.contains(answer)){

                                    isCorrect = true;

                                    break;

                                }

                            }

                            if(isCorrect){

                                btnMic.setColorFilter(
                                        android.graphics.Color.GREEN
                                );

                                textToSpeech.speak(
                                        "Correct",
                                        TextToSpeech.QUEUE_FLUSH,
                                        null,
                                        null
                                );

                            } else {

                                btnMic.setColorFilter(
                                        android.graphics.Color.RED
                                );

                                textToSpeech.speak(
                                        "Try again",
                                        TextToSpeech.QUEUE_FLUSH,
                                        null,
                                        null
                                );

                            }

                        }

                    }

                    @Override
                    public void onError(int error) {

                        btnMic.setColorFilter(
                                android.graphics.Color.GRAY
                        );

                    }

                    @Override public void onBeginningOfSpeech() {}
                    @Override public void onRmsChanged(float rmsdB) {}
                    @Override public void onBufferReceived(byte[] buffer) {}
                    @Override public void onEndOfSpeech() {}
                    @Override public void onPartialResults(Bundle partialResults) {}
                    @Override public void onEvent(int eventType, Bundle params) {}

                });

    }

    // =====================================================
    // MUSIC
    // =====================================================

    @Override
    protected void onStart() {
        super.onStart();

        MusicManager.pauseMusic();

    }

    @Override
    protected void onStop() {
        super.onStop();

        MusicManager.resumeMusic();

    }

    // =====================================================
    // DESTROY
    // =====================================================

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(speechRecognizer != null){

            speechRecognizer.destroy();

        }

        if(textToSpeech != null){

            textToSpeech.stop();

            textToSpeech.shutdown();

        }

    }

}