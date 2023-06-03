package sakkApplication;

import lombok.Getter;

/**
 * Egy játékról tartalmaz eredményeket.
 */
@Getter
public class GameResult
{
    private String vilagos; 
    private String sotet; 
    private int lepesek; 
    private String gyoztes;
    
    /**
     * Konstruktor, aminek a játék eredményeit kell megadni.
     * @param vilagos játékos neve, aki világossal játszott
     * @param sotet játékos neve, aki sötéttel játszott
     * @param lepesek a megtett lépések száma
     * @param gyoztes a nyertes szín
     */
    public GameResult(String vilagos, String sotet, int lepesek, String gyoztes)
    {
        this.vilagos = vilagos;
        this.sotet = sotet;
        this.lepesek = lepesek;
        this.gyoztes = gyoztes;
    }
    
    /**
     * A szín helyett a győztes nevét visszaadő függvény.
     * @return szín helyet a győztes nevét visszaadó String
     */
    public String getGyoztesNev()
    {
        if (getGyoztes().equals("VILAGOS"))
        {
            return vilagos;
        } else
        {
            return sotet;
        }
    }
}
