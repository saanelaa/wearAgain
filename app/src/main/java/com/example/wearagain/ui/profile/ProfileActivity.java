package com.example.wearagain.ui.profile;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wearagain.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.wearagain.ui.auth.LoginActivity;
import android.content.Intent;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding vezanje;
    private FirebaseAuth auth;
    private DatabaseReference bazaPodataka;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vezanje = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(vezanje.getRoot());

        auth = FirebaseAuth.getInstance();
        bazaPodataka = FirebaseDatabase.getInstance("https://wearagain-4f746-default-rtdb.europe-west1.firebasedatabase.app/").getReference("korisnici");

        ucitajPodatkeKorisnika();

        vezanje.btnSacuvaj.setOnClickListener(v -> sacuvajPromjene());
        vezanje.btnPromijeniLozinku.setOnClickListener(v -> promijeniLozinku());
        vezanje.btnOdjava.setOnClickListener(v -> odjavi());
    }

    private void ucitajPodatkeKorisnika() {
        FirebaseUser korisnik = auth.getCurrentUser();
        if (korisnik == null) return;

        bazaPodataka.child(korisnik.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        vezanje.etIme.setText(snapshot.child("ime").getValue(String.class));
                        vezanje.etUsername.setText(snapshot.child("username").getValue(String.class));
                        vezanje.etEmail.setText(korisnik.getEmail());
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });
    }

    private void sacuvajPromjene() {
        String novoIme = vezanje.etIme.getText().toString().trim();
        String noviUsername = vezanje.etUsername.getText().toString().trim();

        if (novoIme.isEmpty() || noviUsername.isEmpty()) {
            Toast.makeText(this, "Polja ne mogu biti prazna", Toast.LENGTH_SHORT).show();
            return;
        }

        String korisnikId = auth.getCurrentUser().getUid();

        bazaPodataka.orderByChild("username").equalTo(noviUsername)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        boolean usernameZauzet = false;
                        for (DataSnapshot dijete : snapshot.getChildren()) {
                            if (!dijete.getKey().equals(korisnikId)) {
                                usernameZauzet = true;
                                break;
                            }
                        }

                        if (usernameZauzet) {
                            Toast.makeText(ProfileActivity.this, "Username je već zauzet", Toast.LENGTH_SHORT).show();
                        } else {
                            bazaPodataka.child(korisnikId).child("ime").setValue(novoIme);
                            bazaPodataka.child(korisnikId).child("username").setValue(noviUsername)
                                    .addOnSuccessListener(a ->
                                            Toast.makeText(ProfileActivity.this, "Podaci uspješno sačuvani", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e ->
                                            Toast.makeText(ProfileActivity.this, "Greška: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });
    }

    private void promijeniLozinku() {
        FirebaseUser korisnik = auth.getCurrentUser();
        if (korisnik == null || korisnik.getEmail() == null) return;

        auth.sendPasswordResetEmail(korisnik.getEmail())
                .addOnSuccessListener(a ->
                        Toast.makeText(this, "Email za reset lozinke je poslan", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Greška: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void odjavi() {
        auth.signOut();
        Intent namjera = new Intent(ProfileActivity.this, LoginActivity.class);
        namjera.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(namjera);
        finish();
    }
}