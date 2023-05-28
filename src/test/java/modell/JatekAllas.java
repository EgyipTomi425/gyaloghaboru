/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modell;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import java.io.*;

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
                "VILAGOS VILAGOS VILAGOS VILAGOS VILAGOS VILAGOS VILAGOS VILAGOS\n" +
                "URES URES URES URES URES URES URES URES\n" +
                "URES URES URES URES URES URES URES URES\n" +
                "URES URES URES URES URES URES URES URES\n" +
                "URES URES URES URES URES URES URES URES\n" +
                "SOTET SOTET SOTET SOTET SOTET SOTET SOTET SOTET\n" +
                "URES URES URES URES URES URES URES URES";
        assertEquals(expected, sakktablaModell.toString());
    }
}
