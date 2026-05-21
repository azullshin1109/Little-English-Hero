package com.thaivantrung.littleenglishhero;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainMenuActivity extends AppCompatActivity {
    TextView txtName;
    ImageView imgAvatar;

    LinearLayout btnLearn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_menu);
        txtName = findViewById(R.id.txtName);
        imgAvatar = findViewById(R.id.imgAvatar);
        btnLearn = findViewById(R.id.btnLearn);
        String name = getIntent().getStringExtra("name");
        int avatar = getIntent().getIntExtra(
                "avatar",
                R.drawable.avatar_bear
        );

        txtName.setText(name + "! 🎉");

        imgAvatar.setImageResource(avatar);

        // Mở Learn activity
        btnLearn.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainMenuActivity.this,
                    LearnActivity.class
            );

            startActivity(intent);
        });

    }
}