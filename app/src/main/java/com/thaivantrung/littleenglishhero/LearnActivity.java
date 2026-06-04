package com.thaivantrung.littleenglishhero;

import android.os.Bundle;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LearnActivity extends AppCompatActivity {

    RecyclerView rcvLesson;
    ImageView btnBack;

    ArrayList<LessonModel> list;
    LessonAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        AnhXa();

        btnBack.setOnClickListener(v -> {
            SoundManager.playClick(this);
            finish();});
        list = new ArrayList<>();


        list.add(new LessonModel(
                R.drawable.img_animals,
                "Bài học 1",
                "Động Vật",
                "8 bài tập",
                1
        ));

        list.add(new LessonModel(
                R.drawable.img_fruits,
                "Bài học 2",
                "Trái Cây",
                "7 bài tập",
                2
        ));

        list.add(new LessonModel(

                R.drawable.img_family,
                "Bài học 3",
                "Gia Đình",
                "7 bài tập",
                3
        ));

        list.add(new LessonModel(
                R.drawable.img_color,
                "Bài học 4",
                "Màu Sắc",
                "7 bài tập",
                4
        ));

        list.add(new LessonModel(
                R.drawable.img_number,
                "Bài học 5",
                "Các Con Số",
                "10 bài tập",
                5
        ));

        adapter = new LessonAdapter(this, list);
        rcvLesson.setLayoutManager(new LinearLayoutManager(this));
        rcvLesson.setAdapter(adapter);
    }

    private void AnhXa() {
        rcvLesson = findViewById(R.id.rcvLesson);
        btnBack = findViewById(R.id.btnBack);
    }
}
