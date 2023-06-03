package modell;

import javafx.beans.property.ReadOnlyObjectProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;

/**
 * A sakkprogram lelke, ez tárol a játékkal kapcsolatos minden információt.
 */
public class SakktablaModell 
{
    private static final Logger logger = LogManager.getLogger(SakktablaModell.class);
    
    /**
     * Egy enum annak a tárolására, hogy ki van soron a játékban.
     */
    public enum SoronKovetkezoJatekos
    {
        /**
         * Világos játékos
         */
        VILAGOS,
        /**
         * Sötét játékos
         */
        SOTET;
    }
    
    private ReadOnlyObjectWrapper<SoronKovetkezoJatekos>  soronKovetkezoJatekos =
        new ReadOnlyObjectWrapper<>(SoronKovetkezoJatekos.VILAGOS);
    
    private ReadOnlyObjectWrapper<MezoBirtokos>  winner =
        new ReadOnlyObjectWrapper<>(MezoBirtokos.VILAGOS);
    
    private int megtettLepesekSzama = 0;
    
    /**
     * A tábla mérete, final típusú.
     */
    public static final int TABLA_MERET = 8;
    
    private ReadOnlyObjectWrapper<MezoBirtokos>[][] tabla = new ReadOnlyObjectWrapper[TABLA_MERET][TABLA_MERET];
    private MezoBirtokos[][] kezdoTabla = new MezoBirtokos[TABLA_MERET][TABLA_MERET];
    
    /**
     * A modell konstruktora, ez felel az alapbeállítások elvégzéséért, és a tábla szerkezeti modelljének felállításáért.
     */
    public SakktablaModell() 
    {
        inicializal(); // Konstruktort nem lehet hívni, de függvényt tud hívni a reset().
    }
    
    private void inicializal()
    {
        logger.info("A tábla modelljének inicializálása...");
        setDefaultValues();
        for (var i = 0; i < TABLA_MERET; i++) 
        {
            for (var j = 0; j < TABLA_MERET; j++) 
            {
                tabla[i][j] = new ReadOnlyObjectWrapper<MezoBirtokos>
                (
                    switch (i) 
                    {
                        case 1 -> MezoBirtokos.SOTET;
                        case TABLA_MERET - 2 -> MezoBirtokos.VILAGOS;
                        default -> MezoBirtokos.URES;
                        // A tömb fentről lefele számoz, de a sakkban fordítva van.
                    }
                );
                kezdoTabla[i][j] = tabla[i][j].get();
            }
        }
        logger.info("A tábla modellje inicializálódott!");
        logger.info("A tábla mezői a modellben:\n" + toString());
        logger.info("Világos következik.");
    }
    
    /**
     * Visszaállítja a tábla modelljét a kezdeti állapotba, a wrapereket nem törli, csak újra beállítja, így a kapcsolat a GUI-val nem szakad meg.
     */
    public void reset()
    {
        logger.info("A tábla modelljének visszaállítása kezdőállapotba...");
        setDefaultValues();
        for (var i = 0; i < TABLA_MERET; i++) 
        {
            for (var j = 0; j < TABLA_MERET; j++) 
            {
                setMezo(new Pozicio(i,j), kezdoTabla[i][j]);
            }
        }
        logger.info("A tábla modellje visszaállt kezdőállapotba!");
        logger.info("A tábla mezői a modellben:\n" + toString());
        logger.info("Világos következik.");
    }
    
    private void setDefaultValues()
    {
        megtettLepesekSzama = 0;
        soronKovetkezoJatekos.set(SoronKovetkezoJatekos.VILAGOS);
    }
    
    /**
     * Egy csak olvasható nézet visszaadását szolgálja.
     * Ideális megfigyelés alapú grafikus alkalmazásokhoz.
     * 
     * @param i tábla i-edik sora
     * @param j tábla j-edik oszlopa
     * @return egy csak olvasható nézetet
     */
    public ReadOnlyObjectProperty<MezoBirtokos> MezoProperty(int i, int j) 
    {
        return tabla[i][j].getReadOnlyProperty();
    }
    
    /**
     * A tábla adott mezőjén lévő figurát/tulajdonost adja vissza.
     * @param p egy Pozicio-példány
     * @return az adott pozícióban lévő mező értéke (enum)
     */
    public MezoBirtokos getMezo(Pozicio p) 
    {
        return tabla[p.sor()][p.oszlop()].get();
    }
    
    /**
     * Visszaadja, hogy ki következik lépni a játékban.
     * @return a játékban aktuálisan lépni tudó játékos (enum)
     */
    public SoronKovetkezoJatekos getSoronKovetkezoJatekos()
    {
        return soronKovetkezoJatekos.get();
    }
    
    /**
     * Visszaadja, hogy ki nyert.
     * @return visszaadja a győztest
     */
    public SoronKovetkezoJatekos getWinner()
    {
        return soronKovetkezoJatekos.get();
    }
    
    /**
     * Visszaadja a megtett lépések számát oldalanként.
     * @return a megtett lépések számá (játékosonként)
     */
    public int getMegtettLepesekSzama()
    {
        return megtettLepesekSzama;
    }
    
    private void setMezo(Pozicio p, MezoBirtokos mezo) 
    {
        tabla[p.sor()][p.oszlop()].set(mezo);
    }
    
    private void changeSoronKovetkezoJatekos()
    {
        if (getSoronKovetkezoJatekos().equals(SoronKovetkezoJatekos.VILAGOS)) 
        {
            soronKovetkezoJatekos.set(SoronKovetkezoJatekos.SOTET);
            logger.info("Sötét következik.");
        } else 
        {
            soronKovetkezoJatekos.set(SoronKovetkezoJatekos.VILAGOS);
            logger.info("Világos következik.");
        }
    }
    
    private void setWinner()
    {
        if (getSoronKovetkezoJatekos().name().equals("VLAGOS"))
        {
            winner.set(MezoBirtokos.SOTET);
        } else
        {
            winner.set(MezoBirtokos.VILAGOS);
        }
    }
    
    private void novelMegtettLepesekSzama()
    {
        if (getSoronKovetkezoJatekos().name().equals("VILAGOS"))
        {
            megtettLepesekSzama++;
        }
    }
    
    private void gameOver()
    {
        logger.info("Játék vége!");
        setWinner();
    }
    
    /**
     * Segítségével mozgathatjuk a figurákat a modellben.
     * A canMove()-al végezhető ellenőrzés, hogy a lépés szabályos-e.
     * @param from a kiinduló-Pozicio
     * @param to  a cél-Pozicio
     */
    public void move(Pozicio from, Pozicio to) 
    {
        setMezo(to, getMezo(from));
        setMezo(from, MezoBirtokos.URES);
        novelMegtettLepesekSzama();
        logger.info("A(z) {} mezőről a(z) {} mezőre lépés sikeres volt.",to,from);
        if(gyalogAlapsoron())
        {
            gameOver();
        } else
        {
            changeSoronKovetkezoJatekos();
        }
    }
    
    /**
     * Megállapítja, hogy a lépés szabályos-e.
     * @param from a kiinduló-Pozicio
     * @param to  a cél-Pozicio
     * @return az adott lépés szabályos-e (logikai)
     */
    public boolean canMove(Pozicio from, Pozicio to) 
    {
        return !isEmpty(from) &&
               isGoodPlayerMove(from) &&
               isOnBoard(from) &&
               isOnBoard(to) && 
               isPawnMove(from, to) &&
               !isGameOver();
    }
    
    /**
     * Visszaadja, hogy az adott mező üres-e a tábla modelljében.
     * @param p Pozicio-példány
     * @return adott pozícióban lévő mező üres-e (logikai)
     */
    public boolean isEmpty(Pozicio p) 
    {
        return getMezo(p) == MezoBirtokos.URES;
    }
    
    /**
     * Az adot pozíció alapján megállapítja, hogy megfelelő játékos lép-e, hogy ne léphessen kétszer.
     * @param p Pozicio-példány
     * @return jó játékos lép-e (logikai)
     */
    public boolean isGoodPlayerMove(Pozicio p)
    {
        return (getSoronKovetkezoJatekos().name().equals(getMezo(p).name()));
    }
    
    /**
     * Megállapítja, hogy tényleg a tábla részére mutat a Pozicio-példányunk.
     * @param p Pozicio-példány
     * @return táblán van-e az adott pozíció (logikai)
     */
    public static boolean isOnBoard(Pozicio p) 
    {
        return 0 <= p.sor() && p.sor() < TABLA_MERET && 0 <= p.oszlop() && p.oszlop() < TABLA_MERET;
    }
    
    /**
     * Visszaadja, hogy adott figura és szín tényleg tudja-e azt a lépést.
     * @param from kiinduló-Pozicio-példány
     * @param to cél-Pozicio-példány
     * @return a lépés szabályos-e (logikai)
     */
    public boolean isPawnMove(Pozicio from, Pozicio to) 
    {
        MezoBirtokos tulajdonos = getMezo(from);
        if(isDuplaLepes(from, to)) return true;
        if(tulajdonos == MezoBirtokos.VILAGOS)
        {
            return isPawnMoveVilagos(from, to);
        } else if (tulajdonos == MezoBirtokos.SOTET)
        {
            return isPawnMoveSotet(from,to); 
        }
        return false;
    }
    
    private boolean isPawnMoveVilagos(Pozicio from, Pozicio to)
    {
        int dx = to.sor() - from.sor();
        int dy = Math.abs(from.oszlop() - to.oszlop());
        if (isEmpty(to))
        {
            return (!((~dx == 0) ^ true) && dy == 0);
            // 50 milliós kérdés; mi a dx értéke, ha a kifejezés igaz? (Ez egy poén akar lenni.)
        } else if(!isEmpty(to) && getMezo(to) != MezoBirtokos.VILAGOS)
        {
            return (!((~dx == 0) ^ true) && dy == 1);
        }
        return false;
    }
    
    private boolean isPawnMoveSotet(Pozicio from, Pozicio to)
    {
        int dx = to.sor() - from.sor();
        int dy = Math.abs(from.oszlop() - to.oszlop());
        if (isEmpty(to))
        {
            return (dx == 1 && dy == 0);
        } else if(!isEmpty(to) && getMezo(to) != MezoBirtokos.SOTET)
        {
            return (dx == 1 && dy == 1);
        }
        return false;
    }
    
    private boolean isDuplaLepes(Pozicio from, Pozicio to)
    {
        int dx = to.sor() - from.sor();
        int dy = Math.abs(from.oszlop() - to.oszlop());
        
        if (Math.abs(dx) == 2 && dy == 0 && (from.sor() == TABLA_MERET - 2 || from.sor() == 1))
        {
            if(getMezo(from).name().equals("VILAGOS") && isEmpty(new Pozicio(to.getSor()+1,to.getOszlop())))
            {
                return true;
            }
            else if(getMezo(from).name().equals("SOTET") && isEmpty(new Pozicio(to.getSor()-1,to.getOszlop())))
            {
                return true;
            }
        } else
        {
            return false;
        }
        return false;
    }
    
    /**
     * Megállapítja, hogy vége van-e a játéknak.
     * @return játék végét megállapító logikai kifejezés
     */
    public boolean isGameOver()
    {                           
        return gyalogAlapsoron() || (!vanMegMindketFigura());
    }
    
    /**
     * Megállapítja, hogy bejutott-e egy gyalog az ellenfél alapsorára.
     * @return van-e gyalog az alapsoron (logikai)
     */
    public boolean gyalogAlapsoron()
    {
        for (int j = 0; j < TABLA_MERET; j++) 
        {
            if (!getMezo(new Pozicio(0, j)).equals(MezoBirtokos.URES)) 
            {
                logger.info("{} bejutott az ellenfél alapsorára!",getMezo(new Pozicio(0,j)).name());
                return true;
            }
        }

        for (int j = 0; j < TABLA_MERET; j++) 
        {
            if (!getMezo(new Pozicio(TABLA_MERET - 1, j)).equals(MezoBirtokos.URES)) 
            {
                logger.info("{} bejutott az ellenfél alapsorára!",getMezo(new Pozicio(TABLA_MERET - 1,j)).name());
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Visszaadja, hogy mindkét játékosnak valóban vannak még figurái.
     * @return van-e még mindkét játékosnak figurája (logikai)
     */
    public boolean vanMegMindketFigura() // A tábla elég kicsi ahhoz, hogy még hatékonyan keressünk 1-1 figurát,
    {                                    // így nem kell a figurákat se számon tartani, ha viszont kell, akkor célszerű lecserélni  
        boolean vanVilagos = false;
        boolean vanSotet = false;

        for (int i = 0; i < TABLA_MERET; i++) 
        {
            for (int j = 0; j < TABLA_MERET; j++) 
            {
                if (getMezo(new Pozicio(i, j)).equals(MezoBirtokos.VILAGOS)) 
                {
                    vanVilagos = true;
                }
                else if (getMezo(new Pozicio(i, j)).equals(MezoBirtokos.SOTET)) 
                {
                    vanSotet = true;
                }
                if (vanVilagos && vanSotet) 
                {
                    break;
                }
            }
            if (vanVilagos && vanSotet) 
            {
                break;
            }
        }

        if (!vanVilagos) 
        {
            logger.info("Nincs több világos figura a táblán!");
            return false;
        }
        else if(!vanSotet)
        {
            logger.info("Nincs több sötét figura a táblán!");
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() 
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < TABLA_MERET; i++) 
        {
            for (int j = 0; j < TABLA_MERET; j++) 
            {
                sb.append(tabla[i][j].get().toString()).append(' ');
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append('\n');
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
