package com.example.wearagain.ui.product;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.wearagain.data.remote.ImgBBOdgovor;
import com.example.wearagain.data.remote.ImgBBServis;
import com.example.wearagain.databinding.ActivityAddProizvodBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddProizvodActivity extends AppCompatActivity {

    private ActivityAddProizvodBinding vezanje;
    private DatabaseReference bazaPodataka;
    private FirebaseAuth auth;
    private Uri odabranaSlikaUri;
    private static final String IMGBB_API_KEY = "818fba9b3c987c13bbece9b24c79ce85";

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
        String grad = vezanje.etGrad.getText().toString().trim();
        String drzava = vezanje.etDrzava.getText().toString().trim();

        if (naziv.isEmpty() || opis.isEmpty() || cijenaStr.isEmpty() || grad.isEmpty() || drzava.isEmpty()) {
            Toast.makeText(this, "Popunite sva polja", Toast.LENGTH_SHORT).show();
            return;
        }

        if (odabranaSlikaUri == null) {
            Toast.makeText(this, "Odaberite sliku", Toast.LENGTH_SHORT).show();
            return;
        }

        vezanje.btnDodajProizvod.setEnabled(false);
        Toast.makeText(this, "Uploadujem sliku:)...", Toast.LENGTH_SHORT).show();

        double cijena = Double.parseDouble(cijenaStr);
        String korisnikId = auth.getCurrentUser().getUid();
        String proizvodId = bazaPodataka.push().getKey();

        try {
            InputStream inputStream = getContentResolver().openInputStream(odabranaSlikaUri);
            byte[] bajtovi = inputStream.readAllBytes();

            RequestBody tijelo = RequestBody.create(MediaType.parse("image/*"), bajtovi);
            MultipartBody.Part dio = MultipartBody.Part.createFormData("image", "slika.jpg", tijelo);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.imgbb.com/1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ImgBBServis servis = retrofit.create(ImgBBServis.class);
            servis.uploadujSliku(IMGBB_API_KEY, dio).enqueue(new Callback<ImgBBOdgovor>() {
                @Override
                public void onResponse(Call<ImgBBOdgovor> call, Response<ImgBBOdgovor> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String slikaUrl = response.body().data.url;
                        sacuvajProizvod(proizvodId, naziv, opis, cijena, kategorija, velicina, grad, drzava, slikaUrl, korisnikId);
                    } else {
                        vezanje.btnDodajProizvod.setEnabled(true);
                        Toast.makeText(AddProizvodActivity.this, "Greška pri uploadu slike", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ImgBBOdgovor> call, Throwable t) {
                    vezanje.btnDodajProizvod.setEnabled(true);
                    Toast.makeText(AddProizvodActivity.this, "Greška: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            vezanje.btnDodajProizvod.setEnabled(true);
            Toast.makeText(this, "Greška pri čitanju slike", Toast.LENGTH_SHORT).show();
        }
    }

    private void sacuvajProizvod(String proizvodId, String naziv, String opis, double cijena,
                                 String kategorija, String velicina, String grad, String drzava,
                                 String slikaUrl, String korisnikId) {
        Map<String, Object> proizvod = new HashMap<>();
        proizvod.put("id", proizvodId);
        proizvod.put("naziv", naziv);
        proizvod.put("opis", opis);
        proizvod.put("cijena", cijena);
        proizvod.put("kategorija", kategorija);
        proizvod.put("velicina", velicina);
        proizvod.put("grad", grad);
        proizvod.put("drzava", drzava);
        proizvod.put("slikaUrl", slikaUrl);
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
    }
}