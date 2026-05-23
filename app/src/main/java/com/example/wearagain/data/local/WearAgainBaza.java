package com.example.wearagain.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.wearagain.data.local.dao.ProizvodDao;
import com.example.wearagain.data.local.entity.ProizvodEntity;

@Database(entities = {ProizvodEntity.class}, version = 1, exportSchema = false)
public abstract class WearAgainBaza extends RoomDatabase {

    private static WearAgainBaza instanca;

    public abstract ProizvodDao proizvodDao();

    public static synchronized WearAgainBaza dohvatiInstancu(Context context) {
        if (instanca == null) {
            instanca = Room.databaseBuilder(
                    context.getApplicationContext(),
                    WearAgainBaza.class,
                    "wearagain_baza"
            ).fallbackToDestructiveMigration().build();
        }
        return instanca;
    }
}
