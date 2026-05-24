package com.example.wearagain.ui.product;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wearagain.databinding.ActivityUredjivanjeObjaveBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UredjivanjeObjaveActivity extends AppCompatActivity {

    private ActivityUredjivanjeObjaveBinding vezanje;
    private DatabaseReference bazaPodataka;
    public static final String KLJUC_PROIZVOD_ID = "proizvod_id";
    private String proizvodId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vezanje = ActivityUredjivanjeObjaveBinding.inflate(getLayoutInflater());
        setContentView(vezanje.getRoot());

        bazaPodataka = FirebaseDatabase.getInstance("https://wearagain-4f746-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("proizvodi");

        proizvodId = getIntent().getStringExtra(KLJUC_PROIZVOD_ID);

        postaviSpinnere();

        if (proizvodId != null) {
            ucitajProizvod();
        }

        vezanje.btnSacuvaj.setOnClickListener(v -> sacuvajPromjene());
        vezanje.btnObrisi.setOnClickListener(v -> obrisiObjavu());
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

    private void ucitajProizvod() {
        bazaPodataka.child(proizvodId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        vezanje.etNaziv.setText(snapshot.child("naziv").getValue(String.class));
                        vezanje.etOpis.setText(snapshot.child("opis").getValue(String.class));
                        vezanje.etGrad.setText(snapshot.child("grad").getValue(String.class));
                        vezanje.etDrzava.setText(snapshot.child("drzava").getValue(String.class));

                        Double cijena = snapshot.child("cijena").getValue(Double.class);
                        if (cijena != null) {
                            vezanje.etCijena.setText(String.valueOf(cijena));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });
    }

    private void sacuvajPromjene() {
        String naziv = vezanje.etNaziv.getText().toString().trim();
        String opis = vezanje.etOpis.getText().toString().trim();
        String cijenaStr = vezanje.etCijena.getText().toString().trim();
        String grad = vezanje.etGrad.getText().toString().trim();
        String drzava = vezanje.etDrzava.getText().toString().trim();
        String kategorija = vezanje.spinnerKategorija.getSelectedItem().toString();
        String velicina = vezanje.spinnerVelicina.getSelectedItem().toString();

        if (naziv.isEmpty() || opis.isEmpty() || cijenaStr.isEmpty()) {
            Toast.makeText(this, "Popunite sva polja", Toast.LENGTH_SHORT).show();
            return;
        }

        bazaPodataka.child(proizvodId).child("naziv").setValue(naziv);
        bazaPodataka.child(proizvodId).child("opis").setValue(opis);
        bazaPodataka.child(proizvodId).child("cijena").setValue(Double.parseDouble(cijenaStr));
        bazaPodataka.child(proizvodId).child("grad").setValue(grad);
        bazaPodataka.child(proizvodId).child("drzava").setValue(drzava);
        bazaPodataka.child(proizvodId).child("kategorija").setValue(kategorija);
        bazaPodataka.child(proizvodId).child("velicina").setValue(velicina)
                .addOnSuccessListener(a ->
                        Toast.makeText(this, "Objava uspješno ažurirana!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Greška: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void obrisiObjavu() {
        bazaPodataka.child(proizvodId).removeValue()
                .addOnSuccessListener(a -> {
                    Toast.makeText(this, "Objava obrisana!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Greška: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}