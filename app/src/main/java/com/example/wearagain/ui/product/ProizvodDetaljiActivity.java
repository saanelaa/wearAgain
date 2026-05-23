package com.example.wearagain.ui.product;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.wearagain.R;
import com.example.wearagain.databinding.ActivityProizvodDetaljiBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProizvodDetaljiActivity extends AppCompatActivity {

    private ActivityProizvodDetaljiBinding vezanje;
    public static final String KLJUC_PROIZVOD_ID = "proizvod_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vezanje = ActivityProizvodDetaljiBinding.inflate(getLayoutInflater());
        setContentView(vezanje.getRoot());

        String proizvodId = getIntent().getStringExtra(KLJUC_PROIZVOD_ID);
        if (proizvodId != null) {
            ucitajProizvod(proizvodId);
        }

        vezanje.btnNazad.setOnClickListener(v -> finish());
    }

    private void ucitajProizvod(String proizvodId) {
        FirebaseDatabase.getInstance("https://wearagain-4f746-default-rtdb.europe-west1.firebasedatabase.app/").getReference("proizvodi")
                .child(proizvodId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        vezanje.tvNaziv.setText(snapshot.child("naziv").getValue(String.class));
                        vezanje.tvOpis.setText(snapshot.child("opis").getValue(String.class));
                        vezanje.tvKategorija.setText("Kategorija: " + snapshot.child("kategorija").getValue(String.class));
                        vezanje.tvVelicina.setText("Veličina: " + snapshot.child("velicina").getValue(String.class));

                        Double cijena = snapshot.child("cijena").getValue(Double.class);
                        if (cijena != null) {
                            vezanje.tvCijena.setText(cijena + " KM");
                        }

                        String grad = snapshot.child("grad").getValue(String.class);
                        String drzava = snapshot.child("drzava").getValue(String.class);
                        if (grad != null && drzava != null) {
                            vezanje.tvLokacija.setText("📍 " + grad + ", " + drzava);
                        }

                        String slikaUrl = snapshot.child("slikaUrl").getValue(String.class);
                        if (slikaUrl != null && !slikaUrl.isEmpty()) {
                            Glide.with(ProizvodDetaljiActivity.this)
                                    .load(slikaUrl)
                                    .placeholder(R.drawable.ic_launcher_foreground)
                                    .into(vezanje.ivSlika);
                        }

                        ucitajKorisnika(snapshot.child("korisnikId").getValue(String.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(ProizvodDetaljiActivity.this, "Greška pri učitavanju", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void ucitajKorisnika(String korisnikId) {
        if (korisnikId == null) return;

        FirebaseDatabase.getInstance("https://wearagain-4f746-default-rtdb.europe-west1.firebasedatabase.app/").getReference("korisnici")
                .child(korisnikId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        String username = snapshot.child("username").getValue(String.class);
                        if (username != null) {
                            vezanje.tvProdavac.setText("@" + username);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });
    }
}
