package util;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import modell.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * GUI felépítését segíti meg, hogy a figurák kijelölését jól kezeljük.
 */
public class SakkLepesValaszto 
{
    /**
     * Az aktuális lépési fázist tároló enum.
     */
    public enum LepesFazis 
    {
        /**
         * A figura kiválasztásának fázisa.
         */
        FIGURA_VALASZTAS,
        /**
         * A mező kiválasztásának fázisa.
         */
        MEZO_VALASZTAS,
        /**
         * Lépésre kézség státusza, általában hibákat előzünk meg vele.
         */
        LEPESRE_KESZ
    }
    
    private static final Logger logger = LogManager.getLogger(SakkLepesValaszto.class);
    
    private SakktablaModell modell;
    private ReadOnlyObjectWrapper<LepesFazis> lepesFazis = new ReadOnlyObjectWrapper<>(LepesFazis.FIGURA_VALASZTAS);
    private boolean invalidSelection = false;
    private Pozicio from;
    private Pozicio to;

    /**
     * A konstruktor, ami a játékmodellt várja bemenetül.
     * @param modell egy sakktáblamodell
     */
    public SakkLepesValaszto(SakktablaModell modell) 
    {
        this.modell = modell;
    }
    
    /**
     * Az aktuális lépésfázist adja vissza.
     * @return aktuális lépésfázis (enum)
     */
    public LepesFazis getLepesFazis()
    {
        return lepesFazis.get();
    }
    
    /**
     * Egy csak olvasható megfigyelhető nézetet ad vissza a lépés fázisáról.
     * @return ReadOnlyObjectProperty<LepesFazis>
     */
    public ReadOnlyObjectProperty<LepesFazis> lepesFazisProperty() 
    {
        return lepesFazis.getReadOnlyProperty();
    }
    
    /**
     * Megállapítja, hogy lépésre készek vagyunk-e.
     * @return lépésre készség igazságértéke
     */
    public boolean isReadyToMove() 
    {
        return lepesFazis.get() == LepesFazis.LEPESRE_KESZ;
    }
    
    /**
     * Kiválasztja a figurát adott pozícóban (vagy mezőt).
     * @param pozicio Pozicio-példány
     */
    public void kivalaszt(Pozicio pozicio) 
    {
        switch (lepesFazis.get()) 
        {
            case FIGURA_VALASZTAS -> figuraPozicioValasztas(pozicio);
            case MEZO_VALASZTAS -> mezoPozicioValasztas(pozicio);
            case LEPESRE_KESZ -> 
            {
                logger.error("A lépésfázis-kezelésben hiba lépett fel!");
                throw new IllegalStateException();
            }
        }
    }
    
    private void figuraPozicioValasztas(Pozicio pozicio) 
    {
        if (!modell.isEmpty(pozicio) &&
            modell.getSoronKovetkezoJatekos().name().equals(modell.getMezo(pozicio).name())) 
        {
            from = pozicio;
            lepesFazis.set(LepesFazis.MEZO_VALASZTAS);
            invalidSelection = false;
            logger.info("A(z) {} mezőn lévő figura kiválasztva.",pozicio);
        } else 
        {
            invalidSelection = true;
            logger.info("A(z) {} mezőn lévő figura nem választható ki.",pozicio);
        }
    }
    
    private void mezoPozicioValasztas(Pozicio pozicio)
    {
        if (modell.canMove(from, pozicio)) 
        {
            to = pozicio;
            lepesFazis.set(LepesFazis.LEPESRE_KESZ);
            invalidSelection = false;
            logger.info("A(z) {} mező kiválasztva célmezőnek.",pozicio);
        } else 
        {
            invalidSelection = true;
            logger.info("A(z) {} mező nem választható ki célmezőnek.",pozicio);
            reset();
        }
    }
    
    /**
     * Ellenőrzést végez
     * @return (boolean)
     */
    public Pozicio getFrom() 
    {
        if (lepesFazis.get() == LepesFazis.FIGURA_VALASZTAS) 
        {
            logger.error("A lépésfázis-kezelésben hiba lépett fel!");
            throw new IllegalStateException();
        }
        return from;
    }
    
    /**
     * Ellenőrzést végez
     * @return boolean
     */
    public Pozicio getTo() 
    {
        if (lepesFazis.get() != LepesFazis.LEPESRE_KESZ) 
        {
            logger.error("A lépésfázis-kezelésben hiba lépett fel!");
            throw new IllegalStateException();
        }
        return to;
    }

    /**
     * Visszaadja, hogy a kiválasztásunk helyes-e.
     * @return kiválasztás helyessége (boolean)
     */
    public boolean isInvalidSelection() 
    {
        return invalidSelection;
    }
    
    /**
     * A lépés megvalósítása
     */
    public void lepj() 
    {
        if (lepesFazis.get() != LepesFazis.LEPESRE_KESZ) 
        {
            logger.error("A lépésfázis-kezelésben hiba lépett fel!");
            throw new IllegalStateException();
        }
        modell.move(from, to);
        reset();
    }

    /**
     * Minden lépésfázist nulláz.
     */
    public void reset() 
    {
        from = null;
        to = null;
        lepesFazis.set(LepesFazis.FIGURA_VALASZTAS);
        invalidSelection = false;
        logger.info("A mező kiválasztása nullázva lett.");
    }
}
