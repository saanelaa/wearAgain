package com.example.wearagain.model;

public class Proizvod {

    private String id;
    private String naziv;
    private String opis;
    private double cijena;
    private String velicina;
    private String kategorija;
    private String slikaUrl;
    private String korisnikId;

    public Proizvod() {}

    public Proizvod(String id, String naziv, String opis, double cijena,
                    String velicina, String kategorija, String slikaUrl, String korisnikId) {
        this.id = id;
        this.naziv = naziv;
        this.opis = opis;
        this.cijena = cijena;
        this.velicina = velicina;
        this.kategorija = kategorija;
        this.slikaUrl = slikaUrl;
        this.korisnikId = korisnikId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNaziv() { return naziv; }
    public void setNaziv(String naziv) { this.naziv = naziv; }

    public String getOpis() { return opis; }
    public void setOpis(String opis) { this.opis = opis; }

    public double getCijena() { return cijena; }
    public void setCijena(double cijena) { this.cijena = cijena; }

    public String getVelicina() { return velicina; }
    public void setVelicina(String velicina) { this.velicina = velicina; }

    public String getKategorija() { return kategorija; }
    public void setKategorija(String kategorija) { this.kategorija = kategorija; }

    public String getSlikaUrl() { return slikaUrl; }
    public void setSlikaUrl(String slikaUrl) { this.slikaUrl = slikaUrl; }

    public String getKorisnikId() { return korisnikId; }
    public void setKorisnikId(String korisnikId) { this.korisnikId = korisnikId; }
}
