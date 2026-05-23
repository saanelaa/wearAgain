package com.example.wearagain.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.wearagain.data.local.entity.ProizvodEntity;

import java.util.List;

@Dao
public interface ProizvodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void umetniProizvod(ProizvodEntity proizvod);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void umetniSve(List<ProizvodEntity> proizvodi);

    @Query("SELECT * FROM proizvodi")
    LiveData<List<ProizvodEntity>> dohvatiSve();

    @Query("SELECT * FROM proizvodi WHERE naziv LIKE '%' || :upit || '%'")
    LiveData<List<ProizvodEntity>> pretrazi(String upit);

    @Query("DELETE FROM proizvodi")
    void obrisiSve();
}
