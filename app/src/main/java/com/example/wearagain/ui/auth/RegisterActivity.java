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
        FirebaseDatabase baza = FirebaseDatabase.getInstance("https://wearagain-4f746-default-rtdb.europe-west1.firebasedatabase.app/");
        bazaPodataka = baza.getReference("korisnici");

        vezanje.btnRegistracija.setOnClickListener(v -> registrujKorisnika());
        vezanje.tvPrijava.setOnClickListener(v -> finish());
    }

    private void registrujKorisnika() {
        String ime = vezanje.etIme.getText().toString().trim();
        String username = vezanje.etUsername.getText().toString().trim();
        String email = vezanje.etEmail.getText().toString().trim();
        String lozinka = vezanje.etLozinka.getText().toString().trim();

        if (ime.isEmpty() || username.isEmpty() || email.isEmpty() || lozinka.isEmpty()) {
            Toast.makeText(this, "Popunite sva polja", Toast.LENGTH_SHORT).show();
            return;
        }

        if (lozinka.length() < 6) {
            Toast.makeText(this, "Lozinka mora imati najmanje 6 karaktera", Toast.LENGTH_SHORT).show();
            return;
        }

        bazaPodataka.orderByChild("username").equalTo(username)
                .addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                        android.util.Log.d("REGISTER", "onDataChange pozvan, postoji: " + snapshot.exists());
                        if (snapshot.exists()) {
                            Toast.makeText(RegisterActivity.this, "Username je već zauzet", Toast.LENGTH_SHORT).show();
                        } else {
                            kreirajNalog(ime, username, email, lozinka);
                        }
                    }

                    @Override
                    public void onCancelled(com.google.firebase.database.DatabaseError error) {}
                });
    }

    private void kreirajNalog(String ime, String username, String email, String lozinka) {
        auth.createUserWithEmailAndPassword(email, lozinka)
                .addOnSuccessListener(authResult -> {
                    String korisnikId = auth.getCurrentUser().getUid();
                    bazaPodataka.child(korisnikId).child("ime").setValue(ime);
                    bazaPodataka.child(korisnikId).child("username").setValue(username);
                    bazaPodataka.child(korisnikId).child("email").setValue(email);

                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Greška: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}