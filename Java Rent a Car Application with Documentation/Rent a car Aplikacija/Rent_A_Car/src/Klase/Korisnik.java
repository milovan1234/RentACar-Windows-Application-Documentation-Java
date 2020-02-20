package Klase;

import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class Korisnik {

    protected static Scanner ulaz = new Scanner(System.in);
    protected String ime;
    protected String prezime;
    protected String korisnickoIme;
    protected String lozinka;
    protected String datRodjenja;
    protected String tip;
    protected static String ulogovanKorisnik;
    protected static ArrayList<Korisnik> korisnici = new ArrayList<Korisnik>();

    public static void setKorisnici(ArrayList<Korisnik> korisnici) {
        Korisnik.korisnici = korisnici;
    }

    public static ArrayList<Korisnik> getKorisnici() {
        return korisnici;
    }

    public static void prikaziKorisnike() {
        System.out.println("Prikaz korsisnika: ");
        for (int i = 0; i < korisnici.size(); i++) {
            System.out.println(korisnici.get(i));
        }
    }
    public static void prikaziKupce() {
        System.out.println("Prikaz svih kupaca: ");
        for (int i = 0; i < korisnici.size(); i++) {
            if(korisnici.get(i) instanceof Kupac)
                System.out.println(korisnici.get(i));
        }
    }

    public String getIme() {
        return ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public String getLozinka() {
        return lozinka;
    }

    public String getDatRodjenja() {
        return datRodjenja;
    }

    public String getTip() {
        return tip;
    }

    public Korisnik() {
    }

    public Korisnik(String ime, String prezime, String korisnickoIme, String lozinka, String datRodjenja, String tip) {
        this.ime = ime;
        this.prezime = prezime;
        this.korisnickoIme = korisnickoIme;
        this.lozinka = lozinka;
        this.datRodjenja = datRodjenja;
        this.tip = tip;
    }

    @Override
    public String toString() {
        return korisnickoIme + " " + lozinka + " " + ime + " " + prezime + " " + datRodjenja + " " + tip;
    }

}
