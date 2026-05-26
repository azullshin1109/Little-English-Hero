package com.thaivantrung.littleenglishhero;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ChooseAvatarActivity extends AppCompatActivity {

    LinearLayout av1, av2, av3, av4, av5, av6;
    EditText edtName;

    int selectedAvatar = -1;

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

        // GỌI HÀM NÀY ĐỂ LẮNG NGHE SỰ KIỆN CHỌN AVATAR
        avatarClick();

        findViewById(R.id.btnLetsGo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = edtName.getText().toString().trim();

                // 1. Kiểm tra avatar
                if(selectedAvatar == -1){
                    edtName.setError(null);
                    android.widget.Toast.makeText(
                            ChooseAvatarActivity.this,
                            "Siêu anh hùng vui lòng chọn ảnh đại diện nhé!",
                            android.widget.Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                // 2. Kiểm tra tên
                if(name.isEmpty()){
                    edtName.setError("Biệt danh của siêu anh hùng là gì?");
                    edtName.requestFocus();
                    return;
                }

                // 3. Lưu vào SharedPreferences (Cục bộ)
                SharedPreferences prefs = getSharedPreferences("LEH_DATA", MODE_PRIVATE);
                prefs.edit()
                        .putString("player_name", name)
                        .putInt("player_avatar", selectedAvatar)
                        .apply();

                // 4. Lưu dữ liệu lên Firestore (Đám mây)
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    Map<String, Object> userProfile = new HashMap<>();
                    userProfile.put("name", name);
                    userProfile.put("avatar", selectedAvatar);
                    userProfile.put("total_xp", 0);

                    db.collection("users").document(uid)
                            .set(userProfile)
                            .addOnSuccessListener(aVoid -> {
                                Intent intent = new Intent(ChooseAvatarActivity.this, MainMenuActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                android.widget.Toast.makeText(
                                        ChooseAvatarActivity.this,
                                        "Lỗi lưu dữ liệu: " + e.getMessage(),
                                        android.widget.Toast.LENGTH_SHORT
                                ).show();
                            });
                } else {
                    android.widget.Toast.makeText(
                            ChooseAvatarActivity.this,
                            "Lỗi: Chưa đăng nhập tài khoản!",
                            android.widget.Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
    } // <-- ĐÃ THÊM DẤU NGOẶC ĐÓNG HÀM onCreate() TẠI ĐÂY

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