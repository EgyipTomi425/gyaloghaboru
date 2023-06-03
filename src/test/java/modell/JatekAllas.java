/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modell;

import com.sun.source.tree.AssertTree;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import java.io.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 * @author kecyke
 */
public class JatekAllas 
{
    @Test
    public void testToString() 
    {
        SakktablaModell sakktablaModell = new SakktablaModell();
        String expected = 
                "URES URES URES URES URES URES URES URES\n" +
                "SOTET SOTET SOTET SOTET SOTET SOTET SOTET SOTET\n" +
                "URES URES URES URES URES URES URES URES\n" +
                "URES URES URES URES URES URES URES URES\n" +
                "URES URES URES URES URES URES URES URES\n" +
                "URES URES URES URES URES URES URES URES\n" +
                "VILAGOS VILAGOS VILAGOS VILAGOS VILAGOS VILAGOS VILAGOS VILAGOS\n" +
                "URES URES URES URES URES URES URES URES";
        assertEquals(expected, sakktablaModell.toString());
    }
    
    @Test
    public void ures()
    {
        SakktablaModell sakktablaModell = new SakktablaModell();
        assertTrue(sakktablaModell.isEmpty(new Pozicio(3, 5)));
    }
    
    @Test
    public void jatekosok_helyes_lepesi_sorrendje()
    {
        SakktablaModell sakktablaModell = new SakktablaModell();
        
        assertTrue(sakktablaModell.getSoronKovetkezoJatekos().name().equals("VILAGOS"));
        sakktablaModell.move(new Pozicio(6, 3), new Pozicio(5, 3));
        assertTrue(sakktablaModell.getSoronKovetkezoJatekos().name().equals("SOTET"));
        sakktablaModell.move(new Pozicio(1, 3), new Pozicio(2, 3));
        assertTrue(sakktablaModell.getSoronKovetkezoJatekos().name().equals("VILAGOS"));
        sakktablaModell.move(new Pozicio(1, 0), new Pozicio(3, 0));
        assertTrue(sakktablaModell.getSoronKovetkezoJatekos().name().equals("SOTET"));
    }
    
    @Test
    public void vilagos_move()
    {
        SakktablaModell sakktablaModell = new SakktablaModell();
        sakktablaModell.move(new Pozicio(6, 3), new Pozicio(5, 3));
        assertTrue(sakktablaModell.getMezo(new Pozicio(5, 3)).name().equals("VILAGOS"));
        assertTrue(sakktablaModell.getMezo(new Pozicio(6, 3)).name().equals("URES")); // Mert elléptünk
    }
    
    @Test
    public void sotet_move()
    {
        SakktablaModell sakktablaModell = new SakktablaModell();
        sakktablaModell.move(new Pozicio(6, 3), new Pozicio(5, 3)); // Világosnak is lépnie kell először
        sakktablaModell.move(new Pozicio(1, 3), new Pozicio(2, 3));     
        assertTrue(sakktablaModell.getMezo(new Pozicio(2, 3)).name().equals("SOTET"));
        assertTrue(sakktablaModell.getMezo(new Pozicio(1, 3)).name().equals("URES")); // Mert elléptünk
    }
    
    @Test 
    public void vilagos_lepes()
    {
        SakktablaModell sakktablaModell = new SakktablaModell();
        boolean kapott_ertek = sakktablaModell.canMove(new Pozicio(6, 3), new Pozicio(5, 3));
        assertTrue(kapott_ertek);
    }
    
    @Test 
    public void sotet_lepes()
    {
        SakktablaModell sakktablaModell = new SakktablaModell();
        sakktablaModell.move(new Pozicio(6, 3), new Pozicio(5, 3));
        boolean kapott_ertek = sakktablaModell.canMove(new Pozicio(1, 3), new Pozicio(2, 3));
        assertTrue(kapott_ertek);
    }
    
    @Test
    public void vilagos_utesLepes_jobbra()
    {
        SakktablaModell sakktablaModell = new SakktablaModell();
        sakktablaModell.move(new Pozicio(6, 6), new Pozicio(5, 6)); // Világosnak kell lépnie először
        sakktablaModell.move(new Pozicio(1, 3), new Pozicio(5, 3));
        boolean kapott_ertek = sakktablaModell.canMove(new Pozicio(6, 2), new Pozicio(5, 3));
        assertTrue(kapott_ertek);
    }
    
    @Test
    public void vilagos_utesLepes_balra()
    {
        SakktablaModell sakktablaModell = new SakktablaModell();
        sakktablaModell.move(new Pozicio(6, 6), new Pozicio(5, 6)); // Világosnak kell lépnie először
        sakktablaModell.move(new Pozicio(1, 3), new Pozicio(5, 3));
        boolean kapott_ertek = sakktablaModell.canMove(new Pozicio(6, 4), new Pozicio(5, 3));
        assertTrue(kapott_ertek);
    }
    
    @Test
    public void sotet_utesLepes_jobbra()
    {
        SakktablaModell sakktablaModell = new SakktablaModell();
        sakktablaModell.move(new Pozicio(6, 6), new Pozicio(5, 6)); // Világosnak kell lépnie először
        sakktablaModell.move(new Pozicio(1, 3), new Pozicio(5, 3));
        boolean kapott_ertek = sakktablaModell.canMove(new Pozicio(6, 2), new Pozicio(5, 3));
        assertTrue(kapott_ertek);
    }
    
    @Test
    public void sotet_utesLepes_balra()
    {
        SakktablaModell sakktablaModell = new SakktablaModell();
        sakktablaModell.move(new Pozicio(6, 6), new Pozicio(2, 6));
        boolean kapott_ertek = sakktablaModell.canMove(new Pozicio(1, 5), new Pozicio(2, 6));
        assertTrue(kapott_ertek);
    }
    
    @Test
    public void vilagos_duplaLepes_szabalyos()
    {
        SakktablaModell sakktablaModell = new SakktablaModell();
        boolean kapott_ertek = sakktablaModell.canMove(new Pozicio(6, 3), new Pozicio(5, 3));
        assertTrue(kapott_ertek);
    }
    
    @Test
    public void vilagos_nem_tud_lepni_ha_allnak_elotte()
    {
        SakktablaModell sakktablaModell = new SakktablaModell();
        sakktablaModell.move(new Pozicio(6, 6), new Pozicio(5, 6)); // Világosnak kell lépnie először
        sakktablaModell.move(new Pozicio(1, 3), new Pozicio(5, 3));
        boolean kapott_ertek = sakktablaModell.canMove(new Pozicio(6, 3), new Pozicio(5, 3));
        assertFalse(kapott_ertek);
    }
    
    @Test
    public void vilagos_szabalytalan_duplaLepes_nem_kezdo_helyrol()
    {
        SakktablaModell sakktablaModell = new SakktablaModell();
        sakktablaModell.move(new Pozicio(6, 6), new Pozicio(5, 6));
        sakktablaModell.move(new Pozicio(1, 3), new Pozicio(2, 3));
        boolean kapott_ertek = sakktablaModell.canMove(new Pozicio(5, 6), new Pozicio(3, 6));
        assertFalse(kapott_ertek);
    }
    
    @Test
    public void sotet_duplaLepes_szabalyos()
    {
        SakktablaModell sakktablaModell = new SakktablaModell();
        sakktablaModell.move(new Pozicio(6, 6), new Pozicio(5, 6));
        boolean kapott_ertek = sakktablaModell.canMove(new Pozicio(1, 3), new Pozicio(3, 3));
        assertTrue(kapott_ertek);
    }
    
    @Test
    public void sotet_nem_tud_lepni_ha_allnak_elotte()
    {
        SakktablaModell sakktablaModell = new SakktablaModell();
        sakktablaModell.move(new Pozicio(6, 6), new Pozicio(1, 6));
        boolean kapott_ertek = sakktablaModell.canMove(new Pozicio(1, 6), new Pozicio(3, 6));
        assertFalse(kapott_ertek);
    }
    
    @Test
    public void sotet_szabalytalan_duplaLepes_nem_kezdo_helyrol()
    {
        SakktablaModell sakktablaModell = new SakktablaModell();
        sakktablaModell.move(new Pozicio(6, 6), new Pozicio(5, 6));
        sakktablaModell.move(new Pozicio(1, 3), new Pozicio(2, 3));
        boolean kapott_ertek = sakktablaModell.canMove(new Pozicio(2, 3), new Pozicio(5, 3));
        assertFalse(kapott_ertek);
    }
    
    @Test
    public void testIsGameOver()
    {
        SakktablaModell sakktablaModell = new SakktablaModell();
        sakktablaModell.move(new Pozicio(6, 0), new Pozicio(0, 6));
        assertTrue(sakktablaModell.isGameOver());
    }
}
