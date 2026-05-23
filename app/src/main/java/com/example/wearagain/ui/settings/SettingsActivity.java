package com.example.wearagain.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

        vezanje.btnSacuvaj.setOnClickListener(v -> sacuvajPostavke());
    }

    private void ucitajPostavke() {
        vezanje.switchObavijesti.setChecked(
                postavke.getBoolean("obavijesti", true));
        vezanje.switchTamnaTema.setChecked(
                postavke.getBoolean("tamna_tema", true));
    }

    private void sacuvajPostavke() {
        SharedPreferences.Editor urednik = postavke.edit();
        urednik.putBoolean("obavijesti", vezanje.switchObavijesti.isChecked());
        urednik.putBoolean("tamna_tema", vezanje.switchTamnaTema.isChecked());
        urednik.apply();

        Toast.makeText(this, "Postavke sačuvane", Toast.LENGTH_SHORT).show();
    }
}
