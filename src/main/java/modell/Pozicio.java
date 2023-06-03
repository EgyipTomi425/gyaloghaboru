/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package modell;

/**
 * Egy Pozizicio-példányt tároló record.
 */
public record Pozicio(int sor, int oszlop) 
{
    @Override
    public String toString() 
    {
        return String.format("(%d,%d)", sor, oszlop);
    }
    
    /**
     * Visszaadja a Pozicio-példány adott sorát.
     * @return a Pozicio sora
     */
    public int getSor()
    {
        return sor;
    }
    
    /**
     * Visszaadja a Pozicio-példány adott oszlopát.
     * @return a Pozicio oszlopa
     */
    public int getOszlop()
    {
        return oszlop;
    }
}
