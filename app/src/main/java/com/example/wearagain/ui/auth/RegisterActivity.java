package com.example.wearagain.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wearagain.databinding.ActivityRegisterBinding;
import com.example.wearagain.ui.home.HomeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding vezanje;
    private FirebaseAuth auth;
    private DatabaseReference bazaPodataka;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vezanje = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(vezanje.getRoot());

        auth = FirebaseAuth.getInstance();
        bazaPodataka = FirebaseDatabase.getInstance().getReference("korisnici");

        vezanje.btnRegistracija.setOnClickListener(v -> registrujKorisnika());
        vezanje.tvPrijava.setOnClickListener(v -> finish());
    }

    private void registrujKorisnika() {
        String ime = vezanje.etIme.getText().toString().trim();
        String email = vezanje.etEmail.getText().toString().trim();
        String lozinka = vezanje.etLozinka.getText().toString().trim();

        if (ime.isEmpty() || email.isEmpty() || lozinka.isEmpty()) {
            Toast.makeText(this, "Popunite sva polja", Toast.LENGTH_SHORT).show();
            return;
        }

        if (lozinka.length() < 6) {
            Toast.makeText(this, "Lozinka mora imati najmanje 6 karaktera", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(email, lozinka)
                .addOnSuccessListener(authResult -> {
                    String korisnikId = auth.getCurrentUser().getUid();
                    bazaPodataka.child(korisnikId).child("ime").setValue(ime);
                    bazaPodataka.child(korisnikId).child("email").setValue(email);

                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Greška: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
