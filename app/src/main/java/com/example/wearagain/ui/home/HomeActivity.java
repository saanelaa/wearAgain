package com.example.wearagain.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.wearagain.databinding.ActivityHomeBinding;
import com.example.wearagain.ui.profile.ProfileActivity;
import com.example.wearagain.ui.settings.SettingsActivity;
import com.example.wearagain.viewmodel.ProizvodViewModel;

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
    }

    private void postaviRecyclerView() {
        adapter = new ProizvodAdapter();
        vezanje.recyclerProizvodi.setLayoutManager(new GridLayoutManager(this, 2));
        vezanje.recyclerProizvodi.setAdapter(adapter);
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
    }
}
