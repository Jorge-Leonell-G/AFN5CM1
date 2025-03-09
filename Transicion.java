/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoafn;

/**
 *
 * @author leone
 */
public class Transicion {
    public char simbolo; //s1
    public char simboloSup; //s2
    public Estado destino; //e2
    
     //Metodo constructor por defecto
    public Transicion() {
        this.destino = null;
    }
    
    //Metodo constructor con simbolo y estado destino
    public Transicion(char simbolo, Estado edoDestino) {
        this.simbolo = simbolo;
        this.destino = edoDestino;
    }
    
     //Metodo constructor con símbolos inferior, superior y estado destino
    public Transicion(char inf, char sup, Estado edoDestino) {
        this.simbolo = inf;
        this.simboloSup = sup;
        this.destino = edoDestino;
    }
    /*
    Transicion(char c, Estado estadoInicial) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    */
}
