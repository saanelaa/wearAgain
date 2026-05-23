package com.example.wearagain.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.wearagain.data.local.WearAgainBaza;
import com.example.wearagain.data.local.dao.ProizvodDao;
import com.example.wearagain.data.local.entity.ProizvodEntity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProizvodRepository {

    private ProizvodDao proizvodDao;
    private ExecutorService executor;

    public ProizvodRepository(Application aplikacija) {
        WearAgainBaza baza = WearAgainBaza.dohvatiInstancu(aplikacija);
        proizvodDao = baza.proizvodDao();
        executor = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<ProizvodEntity>> dohvatiSveProizvode() {
        sinkronizujSaFirebase();
        return proizvodDao.dohvatiSve();
    }

    public LiveData<List<ProizvodEntity>> pretraziProizvode(String upit) {
        return proizvodDao.pretrazi(upit);
    }

    private void sinkronizujSaFirebase() {
        FirebaseDatabase.getInstance("https://wearagain-4f746-default-rtdb.europe-west1.firebasedatabase.app/").getReference("proizvodi")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        List<ProizvodEntity> proizvodi = new ArrayList<>();
                        for (DataSnapshot dijete : snapshot.getChildren()) {
                            ProizvodEntity p = dijete.getValue(ProizvodEntity.class);
                            if (p != null) {
                                p.setId(dijete.getKey());
                                proizvodi.add(p);
                            }
                        }
                        executor.execute(() -> {
                            proizvodDao.obrisiSve();
                            proizvodDao.umetniSve(proizvodi);
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });
    }
}
