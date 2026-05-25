package com.thaivantrung.littleenglishhero;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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

    GoogleSignInClient googleClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // INIT VIEW
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);

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
                        .requestEmail()
                        .build();

        googleClient = GoogleSignIn.getClient(this, gso);

        // LOGIN
        btnLogin.setOnClickListener(v -> loginUser());

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
                    "Please enter email and password",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {

                    if(task.isSuccessful()) {

                        Toast.makeText(this,
                                "Login success",
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
                    "Please enter email and password",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(task -> {

                    if(task.isSuccessful()) {

                        Toast.makeText(this,
                                "Register success",
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
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100) {

            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(data);

            try {

                GoogleSignInAccount account =
                        task.getResult(ApiException.class);

                firebaseAuthWithGoogle(account.getIdToken());

            } catch (Exception e) {
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
                                "Google Login Success",
                                Toast.LENGTH_SHORT).show();

                        checkUser();

                    } else {

                        Toast.makeText(this,
                                "Google Login Failed",
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

                    if(documentSnapshot.exists()) {

                        // ĐÃ CÓ PROFILE
                        startActivity(
                                new Intent(this,
                                        MainMenuActivity.class)
                        );

                    } else {

                        // CHƯA CÓ PROFILE
                        startActivity(
                                new Intent(this,
                                        ChooseAvatarActivity.class)
                        );
                    }

                    finish();
                });
    }
}