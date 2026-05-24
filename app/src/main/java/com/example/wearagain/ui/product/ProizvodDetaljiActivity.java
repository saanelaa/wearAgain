package com.example.wearagain.ui.product;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.wearagain.R;
import com.example.wearagain.databinding.ActivityProizvodDetaljiBinding;
import com.example.wearagain.data.local.entity.KorpaEntity;
import com.example.wearagain.viewmodel.KorpaViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProizvodDetaljiActivity extends AppCompatActivity {

    private ActivityProizvodDetaljiBinding vezanje;
    private FirebaseAuth auth;
    public static final String KLJUC_PROIZVOD_ID = "proizvod_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vezanje = ActivityProizvodDetaljiBinding.inflate(getLayoutInflater());
        setContentView(vezanje.getRoot());

        auth = FirebaseAuth.getInstance();

        String proizvodId = getIntent().getStringExtra(KLJUC_PROIZVOD_ID);
        if (proizvodId != null) {
            ucitajProizvod(proizvodId);
        }

        KorpaViewModel korpaViewModel = new ViewModelProvider(this).get(KorpaViewModel.class);

        vezanje.btnDodajUKorpu.setOnClickListener(v -> {
            KorpaEntity stavka = new KorpaEntity();
            stavka.setProizvodId(proizvodId);
            stavka.setNaziv(vezanje.tvNaziv.getText().toString());
            stavka.setCijena(Double.parseDouble(vezanje.tvCijena.getText().toString().replace(" KM", "")));
            stavka.setVelicina(vezanje.tvVelicina.getText().toString().replace("Veličina: ", ""));
            stavka.setSlikaUrl(vezanje.ivSlika.getTag() != null ? vezanje.ivSlika.getTag().toString() : "");
            korpaViewModel.dodajUKorpu(stavka);
            Toast.makeText(this, "Dodano u korpu!", Toast.LENGTH_SHORT).show();
        });

        vezanje.btnNazad.setOnClickListener(v -> finish());

        vezanje.btnMojaObjava.setOnClickListener(v -> {
            Intent namjera = new Intent(ProizvodDetaljiActivity.this, UredjivanjeObjaveActivity.class);
            namjera.putExtra(UredjivanjeObjaveActivity.KLJUC_PROIZVOD_ID, proizvodId);
            startActivity(namjera);
        });
    }

    private void ucitajProizvod(String proizvodId) {
        FirebaseDatabase.getInstance("https://wearagain-4f746-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("proizvodi")
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
                            vezanje.ivSlika.setTag(slikaUrl);
                        }

                        String vlasnikId = snapshot.child("korisnikId").getValue(String.class);
                        ucitajKorisnika(vlasnikId);

                        String trenutniKorisnikId = auth.getCurrentUser() != null ?
                                auth.getCurrentUser().getUid() : "";

                        if (vlasnikId != null && vlasnikId.equals(trenutniKorisnikId)) {
                            vezanje.btnDodajUKorpu.setVisibility(android.view.View.GONE);
                            vezanje.btnMojaObjava.setVisibility(android.view.View.VISIBLE);
                        } else {
                            vezanje.btnDodajUKorpu.setVisibility(android.view.View.VISIBLE);
                            vezanje.btnMojaObjava.setVisibility(android.view.View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(ProizvodDetaljiActivity.this, "Greška pri učitavanju", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void ucitajKorisnika(String korisnikId) {
        if (korisnikId == null) return;

        FirebaseDatabase.getInstance("https://wearagain-4f746-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("korisnici")
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