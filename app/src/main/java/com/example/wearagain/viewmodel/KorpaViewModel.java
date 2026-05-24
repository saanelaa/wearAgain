package com.example.wearagain.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.wearagain.data.local.WearAgainBaza;
import com.example.wearagain.data.local.dao.KorpaDao;
import com.example.wearagain.data.local.entity.KorpaEntity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KorpaViewModel extends AndroidViewModel {

    private KorpaDao korpaDao;
    private ExecutorService executor;
    private String korisnikId;

    public KorpaViewModel(@NonNull Application aplikacija) {
        super(aplikacija);
        korpaDao = WearAgainBaza.dohvatiInstancu(aplikacija).korpaDao();
        executor = Executors.newSingleThreadExecutor();
        korisnikId = FirebaseAuth.getInstance().getCurrentUser() != null ?
                FirebaseAuth.getInstance().getCurrentUser().getUid() : "";
    }

    public LiveData<List<KorpaEntity>> dohvatiKorpu() {
        return korpaDao.dohvatiKorpu(korisnikId);
    }

    public void dodajUKorpu(KorpaEntity stavka) {
        stavka.setKorisnikId(korisnikId);
        executor.execute(() -> korpaDao.dodajUKorpu(stavka));
    }

    public void ukloniIzKorpe(KorpaEntity stavka) {
        executor.execute(() -> korpaDao.ukloniIzKorpe(stavka));
    }

    public void isprazniKorpu() {
        executor.execute(() -> korpaDao.isprazniKorpu(korisnikId));
    }
}