/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package modell;

public record Pozicio(int sor, int oszlop) 
{
    @Override
    public String toString() 
    {
        return String.format("(%d,%d)", sor, oszlop);
    }
}
