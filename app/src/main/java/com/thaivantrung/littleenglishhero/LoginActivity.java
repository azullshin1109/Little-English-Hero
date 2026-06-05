package com.thaivantrung.littleenglishhero;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.*;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin, btnCreate, btnGoogle;
    private TextView tvForgotPassword;
    private ImageView ivTogglePassword, ivMascot;

    // Firebase
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    // Google Sign In
    private GoogleSignInClient googleClient;

    // State
    private boolean isPasswordVisible = false;

    // Google Launcher
    private final ActivityResultLauncher<Intent> googleLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        try {
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            firebaseAuthWithGoogle(account.getIdToken());
                        } catch (ApiException e) {
                            showToast("Lỗi Google Sign-In");
                            e.printStackTrace();
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        initFirebase();
        initGoogleSignIn();
        setupAnimation();
        setupListeners();
    }


    private void initViews() {
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnCreate = findViewById(R.id.btn_create_account);
        btnGoogle = findViewById(R.id.btn_google_signin);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        ivTogglePassword = findViewById(R.id.iv_toggle_password);
        ivMascot = findViewById(R.id.iv_mascot);
    }

    private void initFirebase() {
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    private void initGoogleSignIn() {
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(
                        GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(
                                getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
        googleClient = GoogleSignIn.getClient(this, gso);
    }

    private void setupAnimation() {
        Animation floatingAnim = AnimationUtils.loadAnimation(this, R.anim.up_down);
        ivMascot.startAnimation(floatingAnim);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> {
            SoundManager.playClick(this);
            loginUser();
        });
        btnCreate.setOnClickListener(v -> {
            SoundManager.playClick(this);
            registerUser();
        });
        btnGoogle.setOnClickListener(v -> {
            SoundManager.playClick(this);
            signInWithGoogle();
        });
        tvForgotPassword.setOnClickListener(v -> resetPassword());
        ivTogglePassword.setOnClickListener(v -> togglePasswordVisibility());
    }

    private void loginUser() {
        String email = getEmail();
        String password = getPassword();
        if (!isValidInput(email, password)) return;
        //Xac thuc tk dang nhap
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showToast("Đăng nhập thành công");
                        checkUser();
                    } else {
                        showToast(task.getException().getMessage());
                    }
                });
    }

    private void registerUser() {
        String email = getEmail();
        String password = getPassword();
        if (!isValidInput(email, password)) return;
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showToast("Đăng kí thành công");
                        checkUser();
                    } else {
                        showToast(task.getException().getMessage());
                    }
                });
    }


    private void signInWithGoogle() {
        Intent intent = googleClient.getSignInIntent();
        googleLauncher.launch(intent);
    }

    private void firebaseAuthWithGoogle(String token) {
        AuthCredential credential = GoogleAuthProvider.getCredential(token, null);
        auth.signInWithCredential(credential).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showToast("Đăng nhập Google thành công");
                        checkUser();
                    } else {
                        showToast("Đăng nhập Google thất bại");
                    }
                });
    }


    private void resetPassword() {
        String email = getEmail();
        if (email.isEmpty()) {
            showToast("Vui lòng nhập email để khôi phục mật khẩu");
            return;
        }

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showToast("Đã gửi email khôi phục");
                    } else {
                        showToast(task.getException().getMessage());
                    }
                });
    }


    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            ivTogglePassword.setImageResource(R.drawable.ic_eye_shut_one);
        } else {
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            ivTogglePassword.setImageResource(R.drawable.ic_eye_open);
        }
        isPasswordVisible = !isPasswordVisible;
        etPassword.setSelection(etPassword.getText().length());
    }


    private void checkUser() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) return;
        String uid = currentUser.getUid();
        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(this::handleUserData);
    }

    private void handleUserData(
            @NonNull DocumentSnapshot document) {
        if (document.exists()) {
            saveUserToPreferences(document);
            startActivity(new Intent(this, MainMenuActivity.class));
        } else {
            startActivity(new Intent(this, ChooseAvatarActivity.class));
        }

        finish();
    }


    private void saveUserToPreferences(DocumentSnapshot document) {
        SharedPreferences prefs = getSharedPreferences("LEH_DATA", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String name = document.getString("name");
        Long avatarLong = document.getLong("avatar");
        int avatar = avatarLong != null
                ? avatarLong.intValue()
                : R.drawable.avatar_bear;
        editor.putString(
                        "player_name",
                        name != null && !name.isEmpty()
                                ? name
                                : "Little Hero")

                .putInt("player_avatar", avatar)
                .putInt("total_xp", getIntValue(document, "total_xp"))
                .putInt("quiz_correct", getIntValue(document, "quiz_correct"))
                .putInt("perfect_quiz", getIntValue(document, "perfect_quiz"))
                .putInt("lessons_done", getIntValue(document, "lessons_done"))
                .putInt("streak", getIntValue(document, "streak"))
                .putInt("family_score", getIntValue(document, "family_score"))
                .putInt("animals_score", getIntValue(document, "animals_score"))
                .putBoolean("family_perfect", getBooleanValue(document, "family_perfect"))
                .putBoolean("animals_perfect", getBooleanValue(document, "animals_perfect"))
                .putBoolean("fruits_perfect", getBooleanValue(document, "fruits_perfect"))
                .putBoolean("colors_perfect", getBooleanValue(document, "colors_perfect"))
                .putBoolean("numbers_perfect", getBooleanValue(document, "numbers_perfect"));

        String lastDate = document.getString("last_study_date");

        if (lastDate != null) {
            editor.putString("last_study_date", lastDate);
        }
        editor.apply();
    }


    private String getEmail() {
        return etEmail.getText().toString().trim();
    }

    private String getPassword() {
        return etPassword.getText().toString().trim();
    }

    private boolean isValidInput(
            String email,
            String password) {

        if (email.isEmpty() || password.isEmpty()) {

            showToast("Vui lòng nhập email và mật khẩu");
            return false;
        }

        return true;
    }

    private int getIntValue(
            DocumentSnapshot document,
            String key) {

        Long value = document.getLong(key);
        return value != null ? value.intValue() : 0;
    }

    private boolean getBooleanValue(
            DocumentSnapshot document,
            String key) {

        Boolean value = document.getBoolean(key);
        return value != null && value;
    }

    private void showToast(String message) {
        Toast.makeText(this,
                message,
                Toast.LENGTH_SHORT).show();
    }
}