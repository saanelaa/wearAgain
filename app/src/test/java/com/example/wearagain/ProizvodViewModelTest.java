package com.example.wearagain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.example.wearagain.data.local.entity.ProizvodEntity;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ProizvodViewModelTest {

    private ProizvodEntity proizvod;

    @Before
    public void postavi() {
        proizvod = new ProizvodEntity(
                "1",
                "Zimska jakna",
                "Topla zimska jakna",
                45.0,
                "M",
                "Jakne",
                "",
                "korisnik1"
        );
    }

    @Test
    public void provjeriNazivProizvoda() {
        assertEquals("Zimska jakna", proizvod.getNaziv());
    }

    @Test
    public void provjeriCijenuProizvoda() {
        assertEquals(45.0, proizvod.getCijena(), 0.0);
    }

    @Test
    public void provjeriVelicinu() {
        assertEquals("M", proizvod.getVelicina());
    }

    @Test
    public void provjeriKategoriju() {
        assertEquals("Jakne", proizvod.getKategorija());
    }

    @Test
    public void provjeriId() {
        assertNotNull(proizvod.getId());
        assertEquals("1", proizvod.getId());
    }

    @Test
    public void provjeriKorisnikId() {
        assertEquals("korisnik1", proizvod.getKorisnikId());
    }

    @Test
    public void promjeniNazivIProivjeri() {
        proizvod.setNaziv("Ljetna haljina");
        assertEquals("Ljetna haljina", proizvod.getNaziv());
    }

    @Test
    public void promjeniCijenuIProivjeri() {
        proizvod.setCijena(25.0);
        assertEquals(25.0, proizvod.getCijena(), 0.0);
    }

    @Test
    public void provjeriListuProizvoda() {
        List<ProizvodEntity> lista = Arrays.asList(
                proizvod,
                new ProizvodEntity("2", "Traperice", "Plave traperice", 30.0, "L", "Pantalone", "", "korisnik2")
        );
        assertEquals(2, lista.size());
        assertEquals("Traperice", lista.get(1).getNaziv());
    }

    @Test
    public void provjeriOpisProizvoda() {
        assertNotNull(proizvod.getOpis());
        assertFalse(proizvod.getOpis().isEmpty());
    }
}
