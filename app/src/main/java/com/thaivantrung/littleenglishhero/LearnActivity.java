package com.thaivantrung.littleenglishhero;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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

        btnBack.setOnClickListener(v -> finish());

        list = new ArrayList<>();

        // Lesson 1
        list.add(new LessonModel(R.drawable.img_abc, "Bài học 1", "Bảng Chữ Cái", "8 bài tập"
        ));

        // Lesson 2
        list.add(new LessonModel(R.drawable.img_animals, "Bài học 2", "Động Vật", "8 bài tập"
        ));

        // Lesson 3
        list.add(new LessonModel(R.drawable.img_fruits, "Bài học 3", "Trái Cây", "9 bài tập"
        ));

        // Lesson 4
        list.add(new LessonModel(R.drawable.img_family, "Bài học 4", "Gia Đình", "7 bài tập"
        ));

        // Lesson 5
        list.add(new LessonModel(R.drawable.img_color, "Bài học 5", "Màu Sắc", "11 bài tập"
        ));

        // Lesson 6
        list.add(new LessonModel(R.drawable.img_number, "Bài học 6", "Các Con Số", "12 bài tập"
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
