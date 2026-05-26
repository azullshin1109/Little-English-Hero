package com.thaivantrung.littleenglishhero;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.*;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    EditText etEmail, etPassword;
    Button btnLogin;
    Button btnCreate;
    Button btnGoogle;

    FirebaseAuth auth;
    FirebaseFirestore db;
    ImageView imgMascost;
    TextView tvForgotPassword;
    ImageView ivTogglePassword;
    boolean isPasswordVisible = false;

    GoogleSignInClient googleClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        imgMascost =  findViewById(R.id.iv_mascot);
        Animation floatingAnim = AnimationUtils.loadAnimation(this, R.anim.up_down);
        imgMascost.startAnimation(floatingAnim);

        // INIT VIEW
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);

        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        ivTogglePassword = findViewById(R.id.iv_toggle_password);

        btnLogin = findViewById(R.id.btn_login);
        btnCreate = findViewById(R.id.btn_create_account);
        btnGoogle = findViewById(R.id.btn_google_signin);

        // FIREBASE
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // GOOGLE SIGN IN
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(
                        GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

        googleClient = GoogleSignIn.getClient(this, gso);

        // LOGIN
        btnLogin.setOnClickListener(v -> loginUser());

        ivTogglePassword.setOnClickListener(v -> {
            if (isPasswordVisible) {
                etPassword.setTransformationMethod(android.text.method.PasswordTransformationMethod.getInstance());
                ivTogglePassword.setImageResource(R.drawable.ic_eye_shut_one);
            } else {
                etPassword.setTransformationMethod(android.text.method.HideReturnsTransformationMethod.getInstance());
                ivTogglePassword.setImageResource(R.drawable.ic_eye_open);
            }
            isPasswordVisible = !isPasswordVisible;
            etPassword.setSelection(etPassword.getText().length());
        });
        tvForgotPassword.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email của bạn vào ô trên để khôi phục mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Đã gửi hướng dẫn khôi phục vào email: " + email, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, "Lỗi: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // REGISTER
        btnCreate.setOnClickListener(v -> registerUser());

        // GOOGLE LOGIN
        btnGoogle.setOnClickListener(v -> {
            Intent intent = googleClient.getSignInIntent();
            startActivityForResult(intent, 100);
        });
    }

    // LOGIN EMAIL
    private void loginUser() {

        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();

        if(email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this,
                    "Vui lòng nhập email và mật khẩu",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {

                    if(task.isSuccessful()) {

                        Toast.makeText(this,
                                "Đăng nhập thành công",
                                Toast.LENGTH_SHORT).show();

                        checkUser();

                    } else {

                        Toast.makeText(this,
                                task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // REGISTER EMAIL
    private void registerUser() {

        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();

        if(email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this,
                    "Vui lòng nhập email và mật khẩu",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {

                    if(task.isSuccessful()) {

                        Toast.makeText(this,
                                "Đăng kí thành công",
                                Toast.LENGTH_SHORT).show();

                        checkUser();

                    } else {

                        Toast.makeText(this,
                                task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // GOOGLE RESULT
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // HIỂN THỊ MÃ LỖI ĐỂ BIẾT CHÍNH XÁC NGUYÊN NHÂN
                Toast.makeText(this, "Lỗi Google Sign-In: " + e.getStatusCode(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    // FIREBASE GOOGLE LOGIN
    private void firebaseAuthWithGoogle(String token) {

        AuthCredential credential =
                GoogleAuthProvider.getCredential(token, null);

        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {

                    if(task.isSuccessful()) {

                        Toast.makeText(this,
                                "Đăng nhập Google thành công",
                                Toast.LENGTH_SHORT).show();

                        checkUser();

                    } else {

                        Toast.makeText(this,
                                "Đăng nhập Google thất bại",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // CHECK USER FIRESTORE
    private void checkUser() {

        String uid = auth.getCurrentUser().getUid();

        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    if (documentSnapshot.exists()) {

                        SharedPreferences prefs = getSharedPreferences("LEH_DATA", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();

                        // 1. Lấy thông tin cơ bản
                        String name = documentSnapshot.getString("name");
                        Long avatarLong = documentSnapshot.getLong("avatar");
                        int avatar = (avatarLong != null) ? avatarLong.intValue() : R.drawable.avatar_bear;

                        // 2. Lấy các chỉ số XP và Score (xử lý an toàn tránh Null)
                        Long xp = documentSnapshot.getLong("total_xp");
                        Long quiz = documentSnapshot.getLong("quiz_correct");
                        Long perf = documentSnapshot.getLong("perfect_quiz");
                        Long lesson = documentSnapshot.getLong("lessons_done");
                        Long streak = documentSnapshot.getLong("streak");

                        Long famScore = documentSnapshot.getLong("family_score");
                        Long aniScore = documentSnapshot.getLong("animals_score");

                        // 3. Lấy Badge (Boolean)
                        Boolean famPerf = documentSnapshot.getBoolean("family_perfect");
                        Boolean aniPerf = documentSnapshot.getBoolean("animals_perfect");
                        Boolean fruPerf = documentSnapshot.getBoolean("fruits_perfect");
                        Boolean colPerf = documentSnapshot.getBoolean("colors_perfect");
                        Boolean numPerf = documentSnapshot.getBoolean("numbers_perfect");

                        // 4. Lưu toàn bộ vào SharedPreferences
                        editor.putString("player_name", (name != null && !name.isEmpty()) ? name : "Little Hero")
                                .putInt("player_avatar", avatar)
                                .putInt("total_xp", (xp != null) ? xp.intValue() : 0)
                                .putInt("quiz_correct", (quiz != null) ? quiz.intValue() : 0)
                                .putInt("perfect_quiz", (perf != null) ? perf.intValue() : 0)
                                .putInt("lessons_done", (lesson != null) ? lesson.intValue() : 0)
                                .putInt("streak", (streak != null) ? streak.intValue() : 0)
                                .putInt("family_score", (famScore != null) ? famScore.intValue() : 0)
                                .putInt("animals_score", (aniScore != null) ? aniScore.intValue() : 0)
                                .putBoolean("family_perfect", famPerf != null && famPerf)
                                .putBoolean("animals_perfect", aniPerf != null && aniPerf)
                                .putBoolean("fruits_perfect", fruPerf != null && fruPerf)
                                .putBoolean("colors_perfect", colPerf != null && colPerf)
                                .putBoolean("numbers_perfect", numPerf != null && numPerf);

                        String lastDate = documentSnapshot.getString("last_study_date");
                        if (lastDate != null) editor.putString("last_study_date", lastDate);

                        editor.apply();

                        // Chuyển sang màn hình chính
                        startActivity(new Intent(LoginActivity.this, MainMenuActivity.class));
                        finish();

                    } else {
                        // CHƯA CÓ PROFILE -> Chuyển sang màn hình chọn Avatar
                        startActivity(new Intent(LoginActivity.this, ChooseAvatarActivity.class));
                        finish();
                    }
                });
    }
}