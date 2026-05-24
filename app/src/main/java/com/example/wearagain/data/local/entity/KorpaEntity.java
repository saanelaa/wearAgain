package com.example.wearagain.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "korpa")
public class KorpaEntity {

    @PrimaryKey
    @NonNull
    private String proizvodId;
    private String naziv;
    private double cijena;
    private String velicina;
    private String slikaUrl;
    private String korisnikId;

    public KorpaEntity() {}

    @NonNull
    public String getProizvodId() { return proizvodId; }
    public void setProizvodId(@NonNull String proizvodId) { this.proizvodId = proizvodId; }

    public String getNaziv() { return naziv; }
    public void setNaziv(String naziv) { this.naziv = naziv; }

    public double getCijena() { return cijena; }
    public void setCijena(double cijena) { this.cijena = cijena; }

    public String getVelicina() { return velicina; }
    public void setVelicina(String velicina) { this.velicina = velicina; }

    public String getSlikaUrl() { return slikaUrl; }
    public void setSlikaUrl(String slikaUrl) { this.slikaUrl = slikaUrl; }

    public String getKorisnikId() { return korisnikId; }
    public void setKorisnikId(String korisnikId) { this.korisnikId = korisnikId; }
}
