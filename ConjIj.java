/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoafn;

import java.util.HashSet;

/**
 *
 * @author leone
 */
public class ConjIj {
    public int j;
    public HashSet<Estado> ConjI;
    public int[] TransicionesAFD;
    
    //metodo constructor
    public ConjIj(int CardAlf){
        j = -1;
        ConjI = new HashSet<Estado>();
        ConjI.clear();
        TransicionesAFD = new int[CardAlf+1];
        for(int k = 0; k <= CardAlf; k++)
            TransicionesAFD[k] = -1;
    }
}
