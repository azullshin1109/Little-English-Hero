package com.thaivantrung.littleenglishhero;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.speech.tts.TextToSpeech;
import java.util.Locale;
import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class StudyActivity extends AppCompatActivity {

    FrameLayout frameContent;
    ProgressBar progressWord;
    TextView txtEnglish, txtMeaning, txtProgress;
    ImageView imgWord, btnSpeak, btnMic;
    Button btnNext;
    TextToSpeech textToSpeech;
    SpeechRecognizer speechRecognizer;
    Intent speechIntent;


    ArrayList<VocabularyModel> list;

    int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);

        frameContent = findViewById(R.id.frameContent);

        showFlashcard();

        createData();

        loadWord();

        btnNext.setOnClickListener(v -> {

            currentIndex++;

            if(currentIndex < list.size()){

                loadWord();

            }

        });

        //Volume
        textToSpeech = new TextToSpeech(this, status -> {

            if(status != TextToSpeech.ERROR) {

                textToSpeech.setLanguage(Locale.US);
            }

        });

        // MIC
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

        speechIntent.putExtra(
                RecognizerIntent.EXTRA_MAX_RESULTS,
                1
        );

        speechIntent.putExtra(
                RecognizerIntent.EXTRA_PARTIAL_RESULTS,
                false
        );

        speechIntent.putExtra(
                RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,
                2000
        );

        speechIntent.putExtra(
                RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS,
                2000
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
                    public void onBeginningOfSpeech() {

                    }

                    @Override
                    public void onRmsChanged(float rmsdB) {

                    }

                    @Override
                    public void onBufferReceived(byte[] buffer) {

                    }

                    @Override
                    public void onEndOfSpeech() {

                    }

                    @Override
                    public void onError(int error) {

                        btnMic.setColorFilter(android.graphics.Color.GRAY);
                        Log.e("SpeechRecognizer", "Lỗi nhận diện giọng nói, mã lỗi: " + error);

                    }

                    @Override
                    public void onResults(Bundle results) {

                        ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                        if(data != null && data.size() > 0){
                            // Lấy từ khóa đáp án và chuyển hết về chữ thường để dễ so sánh
                            String answer = list.get(currentIndex).getWord().toLowerCase();
                            boolean isCorrect = false;

                            // Duyệt qua toàn bộ các từ mà Google "đoán" bé vừa nói
                            for (String text : data) {
                                String spokenText = text.toLowerCase();

                                // Chỉ cần câu bé nói CÓ CHỨA từ khóa, hoặc BẰNG từ khóa là cho qua luôn
                                if (spokenText.equals(answer) || spokenText.contains(answer)) {
                                    isCorrect = true;
                                    break; // Thoát vòng lặp ngay khi tìm thấy kết quả đúng
                                }
                            }

                            if(isCorrect){
                                btnMic.setColorFilter(android.graphics.Color.GREEN);
                                textToSpeech.speak("Correct", TextToSpeech.QUEUE_FLUSH, null, null);
                            } else {
                                btnMic.setColorFilter(android.graphics.Color.RED);
                                textToSpeech.speak("Try again", TextToSpeech.QUEUE_FLUSH, null, null);

                                // Mẹo: In ra Logcat xem bé vừa nói gì mà máy không nhận được
                                // để sau này bạn có thể tinh chỉnh lại
                                android.util.Log.d("SpeechTest", "Bé đã nói: " + data.toString());
                            }
                        }
                    }

                    @Override
                    public void onPartialResults(Bundle partialResults) {

                    }

                    @Override
                    public void onEvent(int eventType, Bundle params) {

                    }


                });
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }

    }

    private void showFlashcard(){

        View view = getLayoutInflater().inflate(R.layout.item_flashcard, null);
        frameContent.addView(view);
        txtEnglish = view.findViewById(R.id.txtEnglish);
        txtMeaning = view.findViewById(R.id.txtMeaning);
        imgWord = view.findViewById(R.id.imgWord);
        btnNext = view.findViewById(R.id.btnNext);
        progressWord = view.findViewById(R.id.progressWord);
        txtProgress = view.findViewById(R.id.txtProgress);
        btnSpeak = view.findViewById(R.id.btnSpeak);
        btnMic = view.findViewById(R.id.btnMic);


    }

    private void createData(){

        list = new ArrayList<>();

        list.add(new VocabularyModel(
                "Dog",
                "Con chó",
                "img_dog"
        ));

        list.add(new VocabularyModel(
                "Cat",
                "Con mèo",
                "img_cat"
        ));

        list.add(new VocabularyModel(
                "Tiger",
                "Con hổ",
                "img_tiger"
        ));

        list.add(new VocabularyModel(
                "Lion",
                "Sư tử",
                "img_lion"
        ));

    }

    private void loadWord(){

        VocabularyModel vocab = list.get(currentIndex);

        txtEnglish.setText(
                vocab.getWord()
        );

        txtMeaning.setText(
                vocab.getMeaning()
        );

        int imageId = getResources().getIdentifier(
                vocab.getImage(),
                "drawable",
                getPackageName()
        );

        imgWord.setImageResource(imageId);

        progressWord.setMax(list.size());

        progressWord.setProgress(currentIndex + 1);

        txtProgress.setText((currentIndex + 1)
                        + " / "
                        + list.size()
                        + " WORDS"
        );

        btnSpeak.setOnClickListener(v -> {
            textToSpeech.speak(
                    vocab.getWord(),
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    null
            );

        });

        btnMic.setOnClickListener(v -> {

            btnMic.clearColorFilter();

            speechRecognizer.startListening(
                    speechIntent
            );

        });


    }

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
