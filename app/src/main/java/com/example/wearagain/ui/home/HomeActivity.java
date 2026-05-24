package com.example.wearagain.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.wearagain.databinding.ActivityHomeBinding;
import com.example.wearagain.ui.cart.KorpaActivity;
import com.example.wearagain.ui.profile.ProfileActivity;
import com.example.wearagain.ui.settings.SettingsActivity;
import com.example.wearagain.viewmodel.ProizvodViewModel;
import com.example.wearagain.ui.product.ProizvodDetaljiActivity;
import com.example.wearagain.ui.product.AddProizvodActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding vezanje;
    private ProizvodAdapter adapter;
    private ProizvodViewModel proizvodViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vezanje = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(vezanje.getRoot());

        postaviRecyclerView();
        postaviViewModel();
        postaviPretragu();
        postaviNavigaciju();
        dodajTestneProizvode();
    }

    private void postaviRecyclerView() {
        adapter = new ProizvodAdapter();
        vezanje.recyclerProizvodi.setLayoutManager(new GridLayoutManager(this, 2));
        vezanje.recyclerProizvodi.setAdapter(adapter);
        adapter.postaviKlikListener(proizvodId -> {
            Intent namjera = new Intent(HomeActivity.this, ProizvodDetaljiActivity.class);
            namjera.putExtra(ProizvodDetaljiActivity.KLJUC_PROIZVOD_ID, proizvodId);
            startActivity(namjera);
        });
    }

    private void postaviViewModel() {
        proizvodViewModel = new ViewModelProvider(this).get(ProizvodViewModel.class);
        proizvodViewModel.dohvatiProizvode().observe(this, proizvodi -> {
            adapter.postaviListu(proizvodi);
        });
    }

    private void postaviPretragu() {
        vezanje.etPretraga.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filtriraj(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void postaviNavigaciju() {
        vezanje.btnProfil.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class)));

        vezanje.btnPostavke.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, SettingsActivity.class)));

        vezanje.btnDodajOglas.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, AddProizvodActivity.class)));

        vezanje.btnKorpa.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, KorpaActivity.class)));
    }
    private void dodajTestneProizvode() {
        FirebaseDatabase.getInstance("https://wearagain-4f746-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("proizvodi")
                .addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                        if (snapshot.getChildrenCount() < 3) {
                            ubaciTestneProizvode();
                        }
                    }

                    @Override
                    public void onCancelled(com.google.firebase.database.DatabaseError error) {}
                });
    }
    private void ubaciTestneProizvode() {
        DatabaseReference ref = FirebaseDatabase.getInstance("https://wearagain-4f746-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("proizvodi");

        String[][] proizvodi = {
                {"Zara ljetna haljina", "Prekrasna ljetna haljina, nosena samo jednom.", "35.0", "S", "Haljine", "Sarajevo", "Bosna i Hercegovina", "https://i.pinimg.com/1200x/e1/1a/e3/e11ae35310e6a7e234ea2ffa62b81be0.jpg"},
                {"H&M hlače", "Plave baggy fit hlače, odlično stanje.", "25.0", "M", "Pantalone", "Mostar", "Bosna i Hercegovina", "https://i.pinimg.com/736x/81/6b/4a/816b4a5db313482f5510b4b9a41252bd.jpg"},
                {"Nike sportska jakna", "Lagana sportska jakna, idealna za proljeće.", "45.0", "L", "Jakne", "Banja Luka", "Bosna i Hercegovina", "https://i.pinimg.com/1200x/fe/31/50/fe3150a3b1dec2ea91bc7b82469bf66f.jpg"},
                {"Mango bijela majica", "Klasična bijela majica, idealna za svaku priliku.", "15.0", "S", "Majice", "Tuzla", "Bosna i Hercegovina", "https://i.pinimg.com/1200x/75/4a/02/754a02c8e6f0813f60ce747d8784ff4b.jpg"},
                {"Tommy Hilfiger haljina", "Elegantna haljina za posebne prilike.", "60.0", "XS", "Haljine", "Zenica", "Bosna i Hercegovina", "https://i.pinimg.com/736x/9a/92/74/9a9274c741b9023327918f6b7d5d9e52.jpg"}
        };

        for (String[] p : proizvodi) {
            String id = ref.push().getKey();
            java.util.Map<String, Object> proizvod = new java.util.HashMap<>();
            proizvod.put("id", id);
            proizvod.put("naziv", p[0]);
            proizvod.put("opis", p[1]);
            proizvod.put("cijena", Double.parseDouble(p[2]));
            proizvod.put("velicina", p[3]);
            proizvod.put("kategorija", p[4]);
            proizvod.put("grad", p[5]);
            proizvod.put("drzava", p[6]);
            proizvod.put("slikaUrl", p[7]);
            proizvod.put("korisnikId", "testni_korisnik");
            ref.child(id).setValue(proizvod);
        }
    }
}
