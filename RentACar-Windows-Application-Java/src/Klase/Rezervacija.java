/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Klase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bokkacheck
 */
public class Rezervacija {

    private int idAuta;
    private String korisnickoIme;
    private Date pocetak;
    private Date kraj;
    private double cena;
    private static ArrayList<Rezervacija> rezervacije = new ArrayList<Rezervacija>();

    public static void setRezervacije(ArrayList<Rezervacija> rezervacije) {
        Rezervacija.rezervacije = rezervacije;
    }

    public static ArrayList<Rezervacija> getRezervacije() {
        return rezervacije;
    }

    public Rezervacija(int idAuta, String korisnickoIme, String pocetak, String kraj, double cena) {
        this.idAuta = idAuta;
        this.korisnickoIme = korisnickoIme;
        try {
            this.pocetak = new SimpleDateFormat("dd.MM.yyyy").parse(pocetak);
            this.kraj = new SimpleDateFormat("dd.MM.yyyy").parse(kraj);
        } catch (ParseException ex) {
            System.out.println("Greske! Neispravan format datuma.");
        }
        this.cena = cena;
    }

    public int getIdAuta() {
        return idAuta;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public Date getPocetak() {
        return pocetak;
    }

    public Date getKraj() {
        return kraj;
    }

    public double getCena() {
        return cena;
    }

    @Override
    public String toString() {
        String auto = "";
        for (int i = 0; i < Automobil.getAutomobili().size(); i++) {
            if (Automobil.getAutomobili().get(i).getIdAuta() == this.idAuta) {
                auto += Automobil.getAutomobili().get(i).getMarka() + " " + Automobil.getAutomobili().get(i).getModel();
            }
            }
            return auto +  " " + new SimpleDateFormat("dd.MM.yyyy").format(pocetak) + " - " + new SimpleDateFormat("dd.MM.yyyy").format(kraj) + " cena:" + cena;
        }

    

    public static ArrayList<Rezervacija> rezervacijeZaAuto(int idAuta) {
        ArrayList<Rezervacija> oRezeravacije = new ArrayList<Rezervacija>();
        for (int i = 0; i < rezervacije.size(); i++) {
            if (rezervacije.get(i).idAuta == idAuta) {
                oRezeravacije.add(rezervacije.get(i));
            }
        }
        return oRezeravacije;
    }

    public static void sortirajRezervacije() {
        for (int i = 0; i < rezervacije.size() - 1; i++) {
            for (int j = i + 1; j < rezervacije.size(); j++) {
                if (rezervacije.get(i).idAuta > rezervacije.get(j).idAuta || (rezervacije.get(i).idAuta == rezervacije.get(j).idAuta && rezervacije.get(i).pocetak.after(rezervacije.get(j).pocetak))) {
                    Rezervacija pom = rezervacije.get(i);
                    rezervacije.set(i, rezervacije.get(j));
                    rezervacije.set(j, pom);
                }
            }
        }
    }

    public static void rezervisiAuto() {
        Scanner ulaz = new Scanner(System.in);
        ArrayList<Ponuda> iPonude = Ponuda.getiPonude();
        if (iPonude.size() != 0) {
            while (true) {
                System.out.println("Unesite datum pocetka rezervacije (primer: " + new SimpleDateFormat("dd.MM.yyyy").format(new Date()) + ") ili < da se vratite nazad");
                String datumOd = ulaz.nextLine();
                if (datumOd.equals("<")) {
                    return;
                }
                System.out.println("Unesite datum kraj rezervacije (primer: " + new SimpleDateFormat("dd.MM.yyyy").format(new Date()) + ") ili < da se vratite nazad");
                String datumDo = ulaz.nextLine();
                if (datumDo.equals("<")) {
                    return;
                }
                if (ValidacijaPodataka.validacijaRezervisanje(datumOd, datumDo)) {
                    Date pocetak = new Date();
                    Date kraj = new Date();
                    try {
                        pocetak = new SimpleDateFormat("dd.MM.yyyy").parse(datumOd);
                        kraj = new SimpleDateFormat("dd.MM.yyyy").parse(datumDo);
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                    double ukupnaCena = 0.0;
                    for (int i = 0; i < iPonude.size(); i++) {
                        ukupnaCena = 0.0;
                        if (!pocetak.before(iPonude.get(i).getDatumOd()) && !pocetak.after(iPonude.get(i).getDatumDo())) {
                            int brojac = 0;
                            for (int j = i; j < iPonude.size(); j++) {
                                if (iPonude.get(i).getIdAuta() == iPonude.get(j).getIdAuta()) {
                                    if (i == j || brojDana(iPonude.get(j).getDatumOd(), iPonude.get(i + brojac++).getDatumDo()) == 1) {
                                        ukupnaCena = ukupnaCena + iPonude.get(j).getCenaDan() * ((brojDana(iPonude.get(j).getDatumDo(), iPonude.get(j).getDatumOd()) + 1));
                                        if (!iPonude.get(j).getDatumDo().before(kraj)) {
                                            ukupnaCena = ukupnaCena - iPonude.get(i).getCenaDan() * (brojDana(pocetak, iPonude.get(i).getDatumOd()));
                                            ukupnaCena = ukupnaCena - iPonude.get(j).getCenaDan() * (brojDana(iPonude.get(j).getDatumDo(), kraj));
                                            ubaciRezervaciju(iPonude.get(i).getIdAuta(), pocetak, kraj, datumOd, datumDo, ukupnaCena);
                                            return;
                                        }
                                    } else {
                                        i = j;
                                        break;
                                    }
                                } else {
                                    i = j;
                                    break;
                                }
                            }
                        }
                    }
                    System.out.println("Nije moguce rezrevisati automobil u trazenom terminu.");
                }
            }
        }
    }

    public static int brojDana(Date prvi, Date drugi) {
        return (int) ((prvi.getTime() - drugi.getTime()) / 86400000);
    }

    private static void ubaciRezervaciju(int idbrAuta, Date pocetak, Date kraj, String datumOd, String datumDo, double ukupnaCena) {
        boolean nadjen = false;
        for (int k = 0; k < rezervacije.size(); k++) {
            if (rezervacije.get(k).idAuta == idbrAuta && pocetak.before(rezervacije.get(k).pocetak) && !nadjen) {
                nadjen = true;
                rezervacije.add(rezervacije.get(rezervacije.size() - 1));
                for (int m = rezervacije.size() - 2; m >= k; m--) {
                    System.out.println("1");
                    rezervacije.set(m + 1, rezervacije.get(m));
                }
                System.out.println("2");
                rezervacije.set(k, new Rezervacija(idbrAuta, Korisnik.ulogovanKorisnik, datumOd, datumDo, ukupnaCena));
            }
        }
        if (!nadjen) {
            rezervacije.add(new Rezervacija(idbrAuta, Korisnik.ulogovanKorisnik, datumOd, datumDo, ukupnaCena));
        }
        System.out.println("Rezervacija uspesno dodata.");
        System.out.println("Ukupna cena rezervacije je " + ukupnaCena + "din.");
    }
}
