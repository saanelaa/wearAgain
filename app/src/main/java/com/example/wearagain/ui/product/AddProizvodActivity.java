package com.example.wearagain.ui.product;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.wearagain.R;
import com.example.wearagain.databinding.ActivityAddProizvodBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddProizvodActivity extends AppCompatActivity {

    private ActivityAddProizvodBinding vezanje;
    private DatabaseReference bazaPodataka;
    private FirebaseAuth auth;
    private StorageReference storage;
    private Uri odabranaSlikaUri;

    private ActivityResultLauncher<String> odabirSlike = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    odabranaSlikaUri = uri;
                    Glide.with(this).load(uri).into(vezanje.ivPreviewSlike);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vezanje = ActivityAddProizvodBinding.inflate(getLayoutInflater());
        setContentView(vezanje.getRoot());

        auth = FirebaseAuth.getInstance();
        bazaPodataka = FirebaseDatabase.getInstance("https://wearagain-4f746-default-rtdb.europe-west1.firebasedatabase.app/").getReference("proizvodi");
        storage = FirebaseStorage.getInstance().getReference("slike_proizvoda");

        postaviSpinnere();

        vezanje.btnOdaberiSliku.setOnClickListener(v -> odabirSlike.launch("image/*"));
        vezanje.btnDodajProizvod.setOnClickListener(v -> dodajProizvod());
        vezanje.btnNazad.setOnClickListener(v -> finish());
    }

    private void postaviSpinnere() {
        String[] kategorije = {"Majice", "Pantalone", "Haljine", "Jakne", "Obuća", "Dodaci"};
        String[] velicine = {"XS", "S", "M", "L", "XL", "XXL"};

        vezanje.spinnerKategorija.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, kategorije));
        vezanje.spinnerVelicina.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, velicine));
    }

    private void dodajProizvod() {
        String naziv = vezanje.etNaziv.getText().toString().trim();
        String opis = vezanje.etOpis.getText().toString().trim();
        String cijenaStr = vezanje.etCijena.getText().toString().trim();
        String kategorija = vezanje.spinnerKategorija.getSelectedItem().toString();
        String velicina = vezanje.spinnerVelicina.getSelectedItem().toString();

        if (naziv.isEmpty() || opis.isEmpty() || cijenaStr.isEmpty()) {
            Toast.makeText(this, "Popunite sva polja", Toast.LENGTH_SHORT).show();
            return;
        }

        if (odabranaSlikaUri == null) {
            Toast.makeText(this, "Odaberite sliku", Toast.LENGTH_SHORT).show();
            return;
        }

        vezanje.btnDodajProizvod.setEnabled(false);
        double cijena = Double.parseDouble(cijenaStr);
        String korisnikId = auth.getCurrentUser().getUid();
        String proizvodId = bazaPodataka.push().getKey();

        StorageReference slikaRef = storage.child(UUID.randomUUID().toString());
        slikaRef.putFile(odabranaSlikaUri)
                .addOnSuccessListener(taskSnapshot -> {
                    slikaRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        Map<String, Object> proizvod = new HashMap<>();
                        proizvod.put("id", proizvodId);
                        proizvod.put("naziv", naziv);
                        proizvod.put("opis", opis);
                        proizvod.put("cijena", cijena);
                        proizvod.put("kategorija", kategorija);
                        proizvod.put("velicina", velicina);
                        proizvod.put("slikaUrl", uri.toString());
                        proizvod.put("korisnikId", korisnikId);

                        bazaPodataka.child(proizvodId).setValue(proizvod)
                                .addOnSuccessListener(a -> {
                                    Toast.makeText(this, "Oglas uspješno objavljen!", Toast.LENGTH_SHORT).show();
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    vezanje.btnDodajProizvod.setEnabled(true);
                                    Toast.makeText(this, "Greška: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    });
                })
                .addOnFailureListener(e -> {
                    vezanje.btnDodajProizvod.setEnabled(true);
                    Toast.makeText(this, "Greška pri uploadu slike: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}