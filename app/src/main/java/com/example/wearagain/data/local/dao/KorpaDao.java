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

    @Query("SELECT * FROM korpa WHERE korisnikId = :korisnikId")
    LiveData<List<KorpaEntity>> dohvatiKorpu(String korisnikId);

    @Query("SELECT COUNT(*) FROM korpa WHERE korisnikId = :korisnikId")
    LiveData<Integer> brojStavki(String korisnikId);

    @Delete
    void ukloniIzKorpe(KorpaEntity stavka);

    @Query("DELETE FROM korpa WHERE korisnikId = :korisnikId")
    void isprazniKorpu(String korisnikId);
}
