/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoafn;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author leone
 */
class Si {
    public int j;
    public Set<Estado> ConjI;
    public int[] TransicionesAFD;

    public Si() {
        this.j = -1;
        this.ConjI = new HashSet<>();
        this.TransicionesAFD = new int[257];

        for (int x = 0; x < 257; x++) {
            TransicionesAFD[x] = -1;
        }
    }
}
