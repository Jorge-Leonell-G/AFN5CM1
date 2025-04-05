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
public class AFN {
    //variables globales
    //Recordamos que un AFN está formado por las siguientes variables
   public Estado estadoInicial; //Estado inicial S que pertenece al conjunto E
   public Set<Estado> estadosAFN = new HashSet<>(); //Conjunto finito no vacío de estados
   public Set<Character> alfabeto = new HashSet<>(); //Alfabeto sigma
   public Set<Estado> estadosAceptacion = new HashSet<>(); //Conjunto de estados de aceptacion F
   
   //Las transiciones se implementan posteriormente invocando a la clase Transicion mediante el objeto t
   
   //Ademas de lo anterior, tenemos los siguientes datos miembro de la clase AFN
   public int idAFN;
   public  static final Set<AFN> ConjuntoAFNs = new HashSet<>(); //repositorio (conjunto) de AFN's
   
   // Metodo constructor
    public AFN() {
        this.idAFN = 0;
        this.estadoInicial = new Estado();
        this.estadosAFN = new HashSet<>();
        this.estadosAceptacion = new HashSet<>();
        this.alfabeto = new HashSet<>();
    }
    
     public Set<AFN> getConjuntoAFNs() {
        return ConjuntoAFNs;
    }

     //1. Crear AFN basico (con y sin rango como parametros del metodo)
     public AFN crearAFNBasico(char s1, char s2, int id){
         AFN f = new AFN();
         Estado e1 = new Estado();
         Estado e2 = new Estado();
         
         //Primera invocacion de la clase Transicion
         e1.transiciones.add(new Transicion(s1, s2, e2));
         e2.estadoAceptacion = true;
         f.estadoInicial = e1;
         
         //bucle forEach para rellenar el alfabeto con los caracteres presentes en el AFN
         for(char c = s1; c <= s2; c++){
             f.alfabeto.add(c);
         }
         f.estadosAceptacion.add(e2);
         f.idAFN = id;
         f.estadosAFN.add(e1);
         f.estadosAFN.add(e2);
         
         return f;    
         
     }
     
     //2. Unir 2 AFN
     public AFN unirAFNs(AFN f2){
         AFN f = new AFN();
         Estado e1 = new Estado();
         Estado e2 = new Estado();
         
         char epsilon = SimbolosEspeciales.EPSILON;
         
         e1.transiciones.add(new Transicion(epsilon, this.estadoInicial));
         e1.transiciones.add(new Transicion(epsilon, f2.estadoInicial));
         
         //bucle "forEach" para las transiciones epsilon hacia el nuevo estado de aceptacion
         for (Estado e : this.estadosAceptacion){
             e.transiciones.add(new Transicion(epsilon, e2));
             e.estadoAceptacion = false;
         }
         
         for (Estado e : f2.estadosAceptacion){
             e.transiciones.add(new Transicion(epsilon, e2));
             e.estadoAceptacion = false;
         }
         
         f.estadoInicial = e1;
         
         f.estadosAFN.addAll(this.estadosAFN);
         f.estadosAFN.addAll(f2.estadosAFN);
         f.estadosAFN.add(e1);
         f.estadosAFN.add(e2);
         e2.estadoAceptacion = true;
         
         f.estadosAceptacion.add(e2);
         f.alfabeto.addAll(this.alfabeto);
         f.alfabeto.addAll(f2.alfabeto);
         
         return f;
     }
     
    public AFN concatenarAFNs (AFN f2) {
       
        // Agregar transiciones desde los estados de aceptación del AFN actual hacia los estados de f2.EdoInicial
        for (Transicion t : f2.estadoInicial.transiciones) {
            for (Estado e : this.estadosAceptacion) {
                e.transiciones.add(t);
                e.estadoAceptacion = false;
            }
        }
        
        this.estadosAFN.addAll(f2.estadosAFN);
        this.estadosAceptacion.clear(); //eliminamos los estados de aceptacion hasta el momento
        this.estadosAceptacion.addAll(f2.estadosAceptacion);
        this.estadosAFN.remove(f2.estadoInicial);
        this.alfabeto.addAll(f2.alfabeto);

        for (char c : this.alfabeto) {
            System.out.println(c);
        }

        System.out.println("Concatenado.");
        

        ConjuntoAFNs.remove(f2);
        f2.limpiarAFN();

        int i = 0;

        System.out.println("Edo inicial: " + this.estadoInicial.idEdo + ", Token: " + this.estadoInicial.token);
        System.out.println("Edos de aceptación:");
        
        for (Estado estado : this.estadosAceptacion) {            
            System.out.println(i + " .Estado ID: " + estado.idEdo + ", Token: " + estado.token);

            // Imprimir transiciones de cada estado
            for (Transicion transicion : estado.transiciones) {
                System.out.println("   Transición: " + transicion.simboloSup + " -> Estado ID: " + transicion.destino.idEdo);
            }
            i++;
        }
        i=0;
        System.out.println("Demás edos:");
        // Imprimir estados y sus tokens después de la unión
        for (Estado estado : this.estadosAFN) {
            System.out.println(i + " .Estado ID: " + estado.idEdo + ", Token: " + estado.token);

            // Imprimir transiciones de cada estado
            for (Transicion transicion : estado.transiciones) {
                System.out.println("   Transición: " + transicion.simboloSup + " -> Estado ID: " + transicion.destino.idEdo);
            }
            i++;
        }

    // Imprimir alfabeto
    System.out.println("Alfabeto:");
    for (char c : this.alfabeto) {
        System.out.println("  " + c);
    }
        
        return this;
    }
   
    public AFN opcional() {
        AFN f = new AFN();
        Estado e1 = new Estado();
        Estado e2 = new Estado();
        
        e1.agregarTransicion(new Transicion('ε', this.estadoInicial));
        e1.agregarTransicion(new Transicion('ε', e2));
        
        for (Estado e : this.estadosAceptacion) {
            e.agregarTransicion(new Transicion('ε', e2));
            e.setEstadoAceptacion(false);
        }
        
        e2.setEstadoAceptacion(true);
        f.estadoInicial = e1;
        f.alfabeto.addAll(this.alfabeto);
        f.estadosAFN.addAll(this.estadosAFN);
        f.estadosAFN.add(e1);
        f.estadosAFN.add(e2);
        f.estadosAceptacion.add(e2);
        
        return f;
    }

     public AFN cerraduraKleene() {
        this.cerraduraPositiva();
        char Epsilon = SimbolosEspeciales.EPSILON;

        for (Estado e : this.estadosAceptacion) {
            this.estadoInicial.transiciones.add(new Transicion(Epsilon, e));
        }

        System.out.println("Cerradura *");

        return this;
    }

    public AFN cerraduraPositiva() {
        AFN f = new AFN();

        Estado nuevoInicial = new Estado();
        Estado nuevoFinal = new Estado();

        char epsilon = SimbolosEspeciales.EPSILON;

        // Transición de nuevo inicial al estado inicial del AFN actual
        nuevoInicial.transiciones.add(new Transicion(epsilon, this.estadoInicial));

        // Transición de los estados de aceptación al estado inicial (ciclo)
        for (Estado e : this.estadosAceptacion) {
            e.transiciones.add(new Transicion(epsilon, this.estadoInicial));
            e.transiciones.add(new Transicion(epsilon, nuevoFinal));
            e.estadoAceptacion = false;
        }

        nuevoFinal.estadoAceptacion = true;

        f.estadoInicial = nuevoInicial;
        f.estadosAceptacion.add(nuevoFinal);

        // Copiamos todos los estados del AFN original
        f.estadosAFN.addAll(this.estadosAFN);

        // Añadimos los nuevos estados
        f.estadosAFN.add(nuevoInicial);
        f.estadosAFN.add(nuevoFinal);

        // Copiamos el alfabeto
        f.alfabeto.addAll(this.alfabeto);

        System.out.println("Cerradura positiva aplicada.");

        return f;
    }
    
    private void limpiarAFN() {
        this.estadosAFN.clear();
        this.alfabeto.clear();
        this.estadosAceptacion.clear();
        this.estadoInicial = null;
        ConjuntoAFNs.remove(this);
    }
     
}
