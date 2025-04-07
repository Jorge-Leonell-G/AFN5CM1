/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoafn;

import static proyectoafn.SimbolosEspeciales.EPSILON;

/**
 *
 * @author leone
 */
public class Operaciones {
    
    //crear AFN basico con el ingreso de un solo simbolo
    public AFN crearAFNBasico(char s){
        Estado e1 = new Estado();
        Estado e2 = new Estado();
        Transicion t = new Transicion(s,e2);
        
        AFN f = new AFN();
        
        f.edoInicial = e1;
        f.alfabeto.add(s);
        f.edosAFN.add(e1);
        f.edosAFN.add(e2);
        e2.edoAceptacion = true;
        f.edosAceptacion.add(e2);
        
        return f;
    }

    public AFN crearAFNBasico(char s1, char s2) {
        AFN f = new AFN();
        Estado e1 = new Estado();
        Estado e2 = new Estado();
        e1.transiciones.add(new Transicion(s1,s2,e2));
        e2.edoAceptacion = true;
        f.edoInicial = e1;
        
        for(char c = s1; c <= s2; c++){
            f.alfabeto.add(c);
        }

        f.edosAceptacion.add(e2);
        //f.idAFN = id;
        f.edosAFN.add(e1);
        f.edosAFN.add(e2);
        //AFNUnionLex = false;

        return f;
    }


    public AFN unirAFNs(AFN f1, AFN f2) {
        AFN f = new AFN();
        Estado e1 = new Estado();
        Estado e2 = new Estado();
        //char epsilon = SimbolosEspeciales.EPSILON;
        e1.transiciones.add(new Transicion(SimbolosEspeciales.EPSILON, f1.edoInicial));
        e1.transiciones.add(new Transicion(SimbolosEspeciales.EPSILON, f2.edoInicial));

        /*
        agregarTransicionConOrigen(e1, epsilon, epsilon, this.estadoInicial);
        agregarTransicionConOrigen(e1, epsilon, epsilon, f2.estadoInicial);
        */
        
        for (Estado e : f1.edosAceptacion) {
            e.transiciones.add(new Transicion(SimbolosEspeciales.EPSILON, e2));
            e.edoAceptacion = false;
        }

        for (Estado e : f2.edosAceptacion) {
            e.transiciones.add(new Transicion(SimbolosEspeciales.EPSILON, e2));
            e.edoAceptacion = false;
        }
        
        f.edosAceptacion.clear();
        f.edoInicial = e1;
        f.edosAFN.addAll(f1.edosAFN);
        f.edosAFN.addAll(f2.edosAFN);
        f.edosAFN.add(e1);
        f.edosAFN.add(e2);
        e2.edoAceptacion = true;
        f.edosAceptacion.add(e2);
        f.alfabeto.addAll(f1.alfabeto);
        f.alfabeto.addAll(f2.alfabeto);

        return f;
    }

    public AFN concatenarAFNs(AFN f1, AFN f2) {
        for (Transicion t : f2.edoInicial.transiciones) {
            for (Estado e : f1.edosAceptacion) {
                agregarTransicionConOrigen(e, t.getSimbolo(), t.getSimboloSup(), t.getDestino());
                e.estadoAceptacion = false;
            }
        }

        this.estadosAFN.addAll(f2.estadosAFN);
        this.estadosAceptacion.clear();
        this.estadosAceptacion.addAll(f2.estadosAceptacion);
        this.estadosAFN.remove(f2.estadoInicial);
        this.alfabeto.addAll(f2.alfabeto);

        ConjuntoAFNs.remove(f2);
        f2.limpiarAFN();

        return this;
    }

    public AFN opcional() {
        AFN f = new AFN();
        Estado e1 = new Estado();
        Estado e2 = new Estado();
        char epsilon = SimbolosEspeciales.EPSILON;

        agregarTransicionConOrigen(e1, epsilon, epsilon, this.estadoInicial);
        agregarTransicionConOrigen(e1, epsilon, epsilon, e2);

        for (Estado e : this.estadosAceptacion) {
            agregarTransicionConOrigen(e, epsilon, epsilon, e2);
            e.estadoAceptacion = false;
        }

        e2.estadoAceptacion = true;
        f.estadoInicial = e1;
        f.estadosAFN.addAll(this.estadosAFN);
        f.estadosAFN.add(e1);
        f.estadosAFN.add(e2);
        f.estadosAceptacion.add(e2);
        f.alfabeto.addAll(this.alfabeto);

        return f;
    }

    public Operaciones cerraduraKleene() {
        this.cerraduraPositiva();
        char epsilon = SimbolosEspeciales.EPSILON;

        for (Estado e : this.estadosAceptacion) {
            agregarTransicionConOrigen(this.estadoInicial, epsilon, epsilon, e);
        }

        return this;
    }

    public Operaciones cerraduraPositiva() {
        AFN f = new AFN();
        Estado nuevoInicial = new Estado();
        Estado nuevoFinal = new Estado();
        char epsilon = SimbolosEspeciales.EPSILON;

        agregarTransicionConOrigen(nuevoInicial, epsilon, epsilon, this.estadoInicial);

        for (Estado e : this.estadosAceptacion) {
            agregarTransicionConOrigen(e, epsilon, epsilon, this.estadoInicial);
            agregarTransicionConOrigen(e, epsilon, epsilon, nuevoFinal);
            e.estadoAceptacion = false;
        }

        nuevoFinal.estadoAceptacion = true;
        f.estadoInicial = nuevoInicial;
        f.estadosAceptacion.add(nuevoFinal);
        f.estadosAFN.addAll(this.estadosAFN);
        f.estadosAFN.add(nuevoInicial);
        f.estadosAFN.add(nuevoFinal);
        f.alfabeto.addAll(this.alfabeto);

        return f;
    }
    
}
