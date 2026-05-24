package com.example.wearagain.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.wearagain.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding vezanje;
    private SharedPreferences postavke;
    private static final String NAZIV_POSTAVKI = "wearagain_postavke";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vezanje = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(vezanje.getRoot());

        postavke = getSharedPreferences(NAZIV_POSTAVKI, MODE_PRIVATE);

        ucitajPostavke();

        vezanje.switchTamnaTema.setOnCheckedChangeListener((buttonView, ukljuceno) -> {
            if (ukljuceno) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            postavke.edit().putBoolean("tamna_tema", ukljuceno).apply();
        });

        vezanje.switchObavijesti.setOnCheckedChangeListener((buttonView, ukljuceno) ->
                postavke.edit().putBoolean("obavijesti", ukljuceno).apply());

        vezanje.btnSacuvaj.setOnClickListener(v -> finish());
    }

    private void ucitajPostavke() {
        boolean tamnaTema = postavke.getBoolean("tamna_tema", false);
        vezanje.switchTamnaTema.setChecked(tamnaTema);
        vezanje.switchObavijesti.setChecked(postavke.getBoolean("obavijesti", true));
    }
}