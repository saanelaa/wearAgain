package com.example.wearagain.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wearagain.databinding.ActivityLoginBinding;
import com.example.wearagain.ui.home.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding vezanje;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vezanje = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(vezanje.getRoot());

        auth = FirebaseAuth.getInstance();

        vezanje.btnPrijava.setOnClickListener(v -> prijaviKorisnika());
        vezanje.tvRegistracija.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void prijaviKorisnika() {
        String email = vezanje.etEmail.getText().toString().trim();
        String lozinka = vezanje.etLozinka.getText().toString().trim();

        if (email.isEmpty() || lozinka.isEmpty()) {
            Toast.makeText(this, "Unesite email i lozinku", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(email, lozinka)
                .addOnSuccessListener(authResult -> {
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Greška: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
