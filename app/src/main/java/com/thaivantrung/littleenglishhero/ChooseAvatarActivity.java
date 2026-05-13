package com.thaivantrung.littleenglishhero;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ChooseAvatarActivity extends AppCompatActivity {

    LinearLayout av1, av2, av3, av4, av5, av6;
    EditText edtName;

    int selectedAvatar = R.drawable.avatar_bear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choose_avatar);

        av1 = findViewById(R.id.av1);
        av2 = findViewById(R.id.av2);
        av3 = findViewById(R.id.av3);
        av4 = findViewById(R.id.av4);
        av5 = findViewById(R.id.av5);
        av6 = findViewById(R.id.av6);

        edtName = findViewById(R.id.edtName);
        avatarClick();
        findViewById(R.id.btnLetsGo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString();

                Intent intent = new Intent(
                        ChooseAvatarActivity.this,
                        MainMenuActivity.class
                );

                intent.putExtra("name",name);
                intent.putExtra("avatar",selectedAvatar);

                startActivity(intent);

            }
        });
    }
    private void avatarClick(){

        av1.setOnClickListener(v -> {
            selectedAvatar = R.drawable.avatar_tiger;
            resetAvatar();
            av1.setBackgroundResource(R.drawable.bg_avatar_selected);
        });

        av2.setOnClickListener(v -> {
            selectedAvatar = R.drawable.avatar_bear;
            resetAvatar();
            av2.setBackgroundResource(R.drawable.bg_avatar_selected);
        });

        av3.setOnClickListener(v -> {
            selectedAvatar = R.drawable.avatar_cat;
            resetAvatar();
            av3.setBackgroundResource(R.drawable.bg_avatar_selected);
        });

        av4.setOnClickListener(v -> {
            selectedAvatar = R.drawable.avatar_chick;
            resetAvatar();
            av4.setBackgroundResource(R.drawable.bg_avatar_selected);
        });

        av5.setOnClickListener(v -> {
            selectedAvatar = R.drawable.avatar_lion;
            resetAvatar();
            av5.setBackgroundResource(R.drawable.bg_avatar_selected);
        });

        av6.setOnClickListener(v -> {
            selectedAvatar = R.drawable.avatar_pig;
            resetAvatar();
            av6.setBackgroundResource(R.drawable.bg_avatar_selected);
        });

    }

    private void resetAvatar(){

        av1.setBackgroundResource(R.drawable.bg_avatar_normal);
        av2.setBackgroundResource(R.drawable.bg_avatar_normal);
        av3.setBackgroundResource(R.drawable.bg_avatar_normal);
        av4.setBackgroundResource(R.drawable.bg_avatar_normal);
        av5.setBackgroundResource(R.drawable.bg_avatar_normal);
        av6.setBackgroundResource(R.drawable.bg_avatar_normal);

    }
}