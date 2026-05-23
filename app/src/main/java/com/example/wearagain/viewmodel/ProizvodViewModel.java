package com.example.wearagain.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.wearagain.data.local.entity.ProizvodEntity;
import com.example.wearagain.data.repository.ProizvodRepository;

import java.util.List;

public class ProizvodViewModel extends AndroidViewModel {

    private ProizvodRepository repozitorij;
    private LiveData<List<ProizvodEntity>> listaProizvoda;

    public ProizvodViewModel(@NonNull Application aplikacija) {
        super(aplikacija);
        repozitorij = new ProizvodRepository(aplikacija);
        listaProizvoda = repozitorij.dohvatiSveProizvode();
    }

    public LiveData<List<ProizvodEntity>> dohvatiProizvode() {
        return listaProizvoda;
    }

    public LiveData<List<ProizvodEntity>> pretraziProizvode(String upit) {
        return repozitorij.pretraziProizvode(upit);
    }
}
