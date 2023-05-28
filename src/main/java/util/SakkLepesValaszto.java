package util;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import modell.*;

public class SakkLepesValaszto 
{
    public enum LepesFazis 
    {
        FIGURA_VALASZTAS,
        MEZO_VALASZTAS,
        LEPESRE_KESZ
    }
    
    private SakktablaModell modell;
    private ReadOnlyObjectWrapper<LepesFazis> lepesFazis = new ReadOnlyObjectWrapper<>(LepesFazis.FIGURA_VALASZTAS);
    private boolean invalidSelection = false;
    private Pozicio from;
    private Pozicio to;

    public SakkLepesValaszto(SakktablaModell modell) 
    {
        this.modell = modell;
    }
    
    public LepesFazis getLepesFazis()
    {
        return lepesFazis.get();
    }
    
    public ReadOnlyObjectProperty<LepesFazis> lepesFazisProperty() 
    {
        return lepesFazis.getReadOnlyProperty();
    }
    
    public boolean isReadyToMove() 
    {
        return lepesFazis.get() == LepesFazis.LEPESRE_KESZ;
    }
    
    public void kivalaszt(Pozicio pozicio) 
    {
        switch (lepesFazis.get()) 
        {
            case FIGURA_VALASZTAS -> figuraPozicioValasztas(pozicio);
            case MEZO_VALASZTAS -> mezoPozicioValasztas(pozicio);
            case LEPESRE_KESZ -> throw new IllegalStateException();
        }
    }
    
    private void figuraPozicioValasztas(Pozicio pozicio) 
    {
        if (!modell.isEmpty(pozicio)) 
        {
            from = pozicio;
            lepesFazis.set(LepesFazis.MEZO_VALASZTAS);
            invalidSelection = false;
        } else 
        {
            invalidSelection = true;
        }
    }
    
    private void mezoPozicioValasztas(Pozicio pozicio)
    {
        if (modell.canMove(from, pozicio)) 
        {
            to = pozicio;
            lepesFazis.set(LepesFazis.LEPESRE_KESZ);
            invalidSelection = false;
        } else 
        {
            invalidSelection = true;
        }
    }
    
    public Pozicio getFrom() 
    {
        if (lepesFazis.get() == LepesFazis.FIGURA_VALASZTAS) 
        {
            throw new IllegalStateException();
        }
        return from;
    }
    
    public Pozicio getTo() 
    {
        if (lepesFazis.get() != LepesFazis.LEPESRE_KESZ) 
        {
            throw new IllegalStateException();
        }
        return to;
    }

    public boolean isInvalidSelection() 
    {
        return invalidSelection;
    }
    
    public void lepj() 
    {
        if (lepesFazis.get() != LepesFazis.LEPESRE_KESZ) 
        {
            throw new IllegalStateException();
        }
        modell.move(from, to);
        reset();
    }

    public void reset() 
    {
        from = null;
        to = null;
        lepesFazis.set(LepesFazis.FIGURA_VALASZTAS);
        invalidSelection = false;
    }
}
