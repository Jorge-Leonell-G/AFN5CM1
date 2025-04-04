/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoafn;
import java.util.HashSet;
import java.util.Set;
import proyectoafn.Transicion;
/**
 *
 * @author leone
 */
class Estado {
    
    Set<Transicion> transiciones = new HashSet<>();
    boolean estadoAceptacion;
    //nuevos campos añadidos para el descenso recursivo (id y token)
    String IdEdo;
    String token;
    
    void agregarTransicion(Transicion t) {
        transiciones.add(t);
    }

    void setEstadoAceptacion(boolean b) {
        this.estadoAceptacion = estadoAceptacion;
    }
    /*
    class Transiciones {

        public Transiciones() {
        }
    }
    */
}
