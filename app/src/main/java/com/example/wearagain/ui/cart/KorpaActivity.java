package com.example.wearagain.ui.cart;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.wearagain.databinding.ActivityKorpaBinding;
import com.example.wearagain.viewmodel.KorpaViewModel;

public class KorpaActivity extends AppCompatActivity {

    private ActivityKorpaBinding vezanje;
    private KorpaAdapter adapter;
    private KorpaViewModel korpaViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vezanje = ActivityKorpaBinding.inflate(getLayoutInflater());
        setContentView(vezanje.getRoot());

        adapter = new KorpaAdapter(stavka -> {
            korpaViewModel.ukloniIzKorpe(stavka);
            Toast.makeText(this, "Uklonjeno iz korpe", Toast.LENGTH_SHORT).show();
        });

        vezanje.recyclerKorpa.setLayoutManager(new LinearLayoutManager(this));
        vezanje.recyclerKorpa.setAdapter(adapter);

        korpaViewModel = new ViewModelProvider(this).get(KorpaViewModel.class);
        korpaViewModel.dohvatiKorpu().observe(this, stavke -> {
            adapter.postaviListu(stavke);
            double ukupno = 0;
            for (var stavka : stavke) {
                ukupno += stavka.getCijena();
            }
            vezanje.tvUkupno.setText("Ukupno: " + String.format("%.2f", ukupno) + " KM");
        });

        vezanje.btnNazad.setOnClickListener(v -> finish());
        vezanje.btnKupi.setOnClickListener(v -> {
            if (adapter.getItemCount() == 0) {
                Toast.makeText(this, "Korpa je prazna", Toast.LENGTH_SHORT).show();
            } else {
                korpaViewModel.isprazniKorpu();
                Toast.makeText(this, "Narudžba uspješno poslana!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
