package sakkApplication;

import lombok.Getter;
import lombok.ToString;

/**
 * Feldolgozás után egyes játékosról statisztikai információkat tartalmazó osztály.
 */
@Getter
@ToString
public class JatekosStatisztika 
{
    private final String nev;
    private final String gyozelmiArany;
    private final double lejatszottPartik;
    private final String atlagosNyertesLepesSzam;

    /**
     * Konstruktor, ami információkat kér be a játékosról.
     * @param nev a játékos neve
     * @param gyozelmiArany a játékos győzelmi aránya (0-1 közötti double)
     * @param lejatszottPartik a lejátszott meccsek száma
     * @param atlagosNyertesLepesSzam  győzelem esetén a győzelemre vivő átlagos lépések száma
     */
    public JatekosStatisztika(String nev, String gyozelmiArany, double lejatszottPartik, String atlagosNyertesLepesSzam) 
    {
        this.nev = nev;
        this.gyozelmiArany = gyozelmiArany;
        this.lejatszottPartik = lejatszottPartik;
        this.atlagosNyertesLepesSzam = atlagosNyertesLepesSzam;
    }
}

