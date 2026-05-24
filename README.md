# WearAgain 

Android aplikacija za preprodaju odjeće koja omogućava korisnicima da pregledavaju, kupuju i prodaju odjevne predmete i dodatke. Inspirisana platformama poput Vinted-a.

## Setup projekta

1. Otvori projekt u Android Studiju
2. Dodaj `google-services.json` u `/app` folder (preuzmi sa Firebase konzole)
3. U Firebase konzoli uključi Authentication (Email/Password) i kreiraj Realtime Database u test modu
4. Pokreni emulator ili povezi fizički uređaj
5. Klikni **Run**


## Funkcionalnosti

**Splash Screen** — animirani uvodni ekran sa logo-om, fade-in i scale animacijom

**Registracija i prijava** — kreiranje naloga sa imenom, usernameom, emailom i lozinkom; prijava putem Firebase Authentication

**Home ekran** — grid prikaz oglasa sa slikom, nazivom, cijenom i veličinom; pretraga po nazivu i kategoriji u realnom vremenu

**Detalji oglasa** — slika, naziv, cijena, kategorija, veličina, lokacija, opis i username prodavača

**Dodavanje objave** — odabir slike iz galerije, upload na ImgBB, unos svih detalja artikla

**Uređivanje i brisanje** — korisnik može mijenjati ili obrisati vlastite oglase

**Korpa** — dodavanje artikala, prikaz ukupne cijene i potvrda kupovine

**Profil** — pregled i uređivanje imena i usernamea, promjena lozinke, odjava

**Postavke** — uključivanje obavijesti i tamne teme

## Tehnologije

- Java
- MVVM arhitektura (Model-View-ViewModel)
- Room (lokalna baza podataka)
- Firebase Authentication + Realtime Database
- Retrofit + OkHttp (ImgBB API za upload slika)
- Glide (učitavanje slika)
- ViewBinding

Aplikacija je razvijena u edukativne svrhe i demonstrira principi razvoja Android aplikacija uključujući MVVM arhitekturu, rad sa Firebase servisima i lokalnom Room bazom.
