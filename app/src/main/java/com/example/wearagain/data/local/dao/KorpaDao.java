package com.example.wearagain.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.wearagain.data.local.entity.KorpaEntity;

import java.util.List;

@Dao
public interface KorpaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void dodajUKorpu(KorpaEntity stavka);

    @Query("SELECT * FROM korpa")
    LiveData<List<KorpaEntity>> dohvatiKorpu();

    @Query("SELECT COUNT(*) FROM korpa")
    LiveData<Integer> brojStavki();

    @Delete
    void ukloniIzKorpe(KorpaEntity stavka);

    @Query("DELETE FROM korpa")
    void isprazniKorpu();
}
