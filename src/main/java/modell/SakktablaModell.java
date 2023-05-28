package modell;

import javafx.beans.property.ReadOnlyObjectProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;

public class SakktablaModell 
{
    private static final Logger logger = LogManager.getLogger(SakktablaModell.class);
    
    public static final int TABLA_MERET = 8;
    
    private ReadOnlyObjectWrapper<MezoBirtokos>[][] board = new ReadOnlyObjectWrapper[TABLA_MERET][TABLA_MERET];

    public SakktablaModell() 
    {
        for (var i = 0; i < TABLA_MERET; i++) 
        {
            for (var j = 0; j < TABLA_MERET; j++) 
            {
                board[i][j] = new ReadOnlyObjectWrapper<MezoBirtokos>
                (
                    switch (i) 
                    {
                        case 1 -> MezoBirtokos.VILAGOS;
                        case TABLA_MERET - 2 -> MezoBirtokos.SOTET;
                        default -> MezoBirtokos.URES;
                    }
                );
            }
        }
        logger.info("A tábla modellje inicializálódott!");
        logger.info("A tábla mezői a modellben:\n" + toString());
    }
    
    public ReadOnlyObjectProperty<MezoBirtokos> getMezoProperty(int i, int j) 
    {
        return board[i][j].getReadOnlyProperty();
    }
    
    public MezoBirtokos getMezo(Pozicio p) 
    {
        return board[p.sor()][p.oszlop()].get();
    }
    
    private void setMezo(Pozicio p, MezoBirtokos mezo) 
    {
        board[p.sor()][p.oszlop()].set(mezo);
    }
    
    public void move(Pozicio from, Pozicio to) 
    {
        setMezo(to, getMezo(from));
        setMezo(from, MezoBirtokos.URES);
    }
    
    public boolean canMove(Pozicio from, Pozicio to) 
    {
        return isOnBoard(from) && isOnBoard(to) && !isEmpty(from) && isPawnMove(from, to);
    }
    
    public boolean isEmpty(Pozicio p) 
    {
        return getMezo(p) == MezoBirtokos.URES;
    }
    
    public static boolean isOnBoard(Pozicio p) 
    {
        return 0 <= p.sor() && p.sor() < TABLA_MERET && 0 <= p.oszlop() && p.oszlop() < TABLA_MERET;
    }
    
    public boolean isPawnMove(Pozicio from, Pozicio to) 
    {
        MezoBirtokos tulajdonos = getMezo(from);
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
        int dx = from.sor() - to.sor();
        int dy = Math.abs(from.oszlop() - to.oszlop());
        if (isEmpty(to))
        {
            return (!((~dx == 0) ^ true) && dy == 0);
            // 50 milliós kérdés; mi a dx értéke, ha a kifejezés igaz?
        } else if(!isEmpty(to) && getMezo(to) != MezoBirtokos.VILAGOS)
        {
            return (!((~dx == 0) ^ true) && dy == 1);
        }
        return false;
    }
    
    private boolean isPawnMoveSotet(Pozicio from, Pozicio to)
    {
        int dx = from.sor() - to.sor();
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
    
    @Override
    public String toString() 
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < TABLA_MERET; i++) 
        {
            for (int j = 0; j < TABLA_MERET; j++) 
            {
                sb.append(board[i][j].get().toString()).append(' ');
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append('\n');
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
