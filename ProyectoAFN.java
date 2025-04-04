/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package proyectoafn;
import java.util.HashSet;
import java.util.Set;


/**
 *
 * @author leone
 */
public class ProyectoAFN {

    public static void main(String[] args) {
        
        /*
        Operacion opcional ?
        */
        
        AFN f = new AFN();
        
        // Definir estados
        Estado inicial = new Estado();
        Estado aceptacion = new Estado();
        aceptacion.setEstadoAceptacion(true);

        // Configurar transiciones del AFN original
        inicial.agregarTransicion(new Transicion('a', aceptacion));

        // Agregar estados al AFN
        f.estadoInicial = inicial;
        f.estados.add(inicial);
        f.estados.add(aceptacion);
        f.estadosAceptacion.add(aceptacion);
        f.alfabeto.add('a');

        // Aplicar la operación opcional()
        AFN afnOpcional = f.opcional();

        // Verificar que el nuevo AFN tiene la estructura esperada
        System.out.println("Estado inicial: " + afnOpcional.estadoInicial);
        System.out.println("Estados de aceptación:");
        for (Estado e : afnOpcional.estadosAceptacion) {
            System.out.println(e);
        }    
    }
    
}
