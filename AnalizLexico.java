/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoafn;

import java.util.Stack;

/**
 *
 * @author leone
 */
public class AnalizLexico {
    int token, edoActual, edoTransicion;
    String cadenaSigma;
    public String lexema;
    boolean pasoPorEdoAcept;
    int iniLexema, finLexema, indiceCaracterActual;
    char caracterActual;
    public Stack<Integer> Pila;
    public String yytext;
    AFD automataAFD;
    
    //metodo constructor por defecto
    public AnalizLexico(){
        cadenaSigma = "";
        pasoPorEdoAcept = false;
        iniLexema = finLexema - 1;
        indiceCaracterActual = -1;
        token = -1;
        Pila.clear();
        automataAFD = null;
    }
    
    //constructor con un inicio establecido y un final fuera de indice
    public AnalizLexico(String sigma, String fileAFD){
        this.automataAFD = new AFD();
        this.cadenaSigma = sigma;
        this.pasoPorEdoAcept = false;
        this.iniLexema = 0;
        this.finLexema = -1;
        this.indiceCaracterActual = 0;
        this.token = -1;
        this.Pila.clear();
        this.automataAFD.LeerAFD(fileAFD);
    }
    
    // Copia del estado actual del analizador lexico
    public AnalizLexico getEdoAnalizLexico() {
        AnalizLexico EdoActualAnaliz = new AnalizLexico();
        EdoActualAnaliz.caracterActual = this.caracterActual;
        EdoActualAnaliz.edoActual = this.edoActual;
        EdoActualAnaliz.edoTransicion = this.edoTransicion;
        EdoActualAnaliz.finLexema = this.finLexema;
        EdoActualAnaliz.indiceCaracterActual = this.indiceCaracterActual;
        EdoActualAnaliz.iniLexema = this.iniLexema;
        EdoActualAnaliz.yytext = this.yytext;
        EdoActualAnaliz.pasoPorEdoAcept = this.pasoPorEdoAcept;
        EdoActualAnaliz.token = this.token;
        // Realizar una copia profunda de la pila
        EdoActualAnaliz.Pila = (Stack<Integer>) this.Pila.clone();
        return EdoActualAnaliz;
    }
    
    // Restaurar un estado previamente guardado mediante una bandera
    public boolean setEdoAnalizLexico(AnalizLexico e) {
        this.caracterActual = e.caracterActual;
        this.edoActual = e.edoActual;
        this.edoTransicion = e.edoTransicion;
        this.finLexema = e.finLexema;
        this.indiceCaracterActual = e.indiceCaracterActual;
        this.iniLexema = e.iniLexema;
        this.yytext = e.yytext;
        this.pasoPorEdoAcept = e.pasoPorEdoAcept;
        this.token = e.token;
        this.Pila = (Stack<Integer>) e.Pila.clone();
        return true;
    }
    
    // Método para reiniciar el analizador léxico con una nueva cadena de entrada
    public void SetSigma(String newSigma) {
        cadenaSigma = newSigma;
        pasoPorEdoAcept = false;
        iniLexema = 0;
        finLexema = -1;
        indiceCaracterActual = 0;
        token = -1;
        Pila.clear();
    }
    
    // Método principal de análisis léxico
    public int yylex() {
        while (true) {
            // Guardar el índice actual en la pila para permitir retroceso
            Pila.push(indiceCaracterActual);

            if (indiceCaracterActual >= cadenaSigma.length()) {
                yytext = "";
                return SimbolosEspeciales.FIN;
            }

            iniLexema = indiceCaracterActual;
            edoActual = 0;
            pasoPorEdoAcept = false;
            finLexema = -1;
            token = -1;

            while (indiceCaracterActual < cadenaSigma.length()) {
                caracterActual = cadenaSigma.charAt(indiceCaracterActual);
                
                // Acceder a la tabla de transición del AFD
                edoTransicion = automataAFD.tablaAFD[edoActual][(int) caracterActual];
                
                if (edoTransicion != -1) {
                    // Verificar si el estado es de aceptación
                    if (automataAFD.tablaAFD[edoTransicion][256] != -1) {
                        pasoPorEdoAcept = true;
                        finLexema = indiceCaracterActual;
                        token = automataAFD.tablaAFD[edoTransicion][256];
                    }
                    indiceCaracterActual++;
                    edoActual = edoTransicion;
                    continue;
                }
                break;
            }
 
            // Manejo de error: si no pasó por un estado de aceptación
            if (!pasoPorEdoAcept) {
                indiceCaracterActual = iniLexema + 1;
                yytext = cadenaSigma.substring(iniLexema, iniLexema + 1);
                token = SimbolosEspeciales.ERROR;
                return token;
            }

            // Extraer lexema y ajustar el índice
            yytext = cadenaSigma.substring(iniLexema, finLexema + 1);
            indiceCaracterActual = finLexema + 1;

            // Verificar si se omite el token, en cuyo caso se continúa
            if (token == SimbolosEspeciales.OMITIR) {
                continue;
            } else {
                return token;
            }
        }
    }

    // Método para retroceder al estado anterior
    public boolean UndoToken() {
        if (Pila.isEmpty()) {
            return false;
        }
        indiceCaracterActual = Pila.pop();
        return true;
    }
    
    public String yytext() {
        return this.yytext;
    }
    
    // Método para imprimir todos los yytext y tokens uno a uno
    public void ImprimirTokens() {
        // Reiniciar el analizador léxico antes de comenzar
        SetSigma(this.cadenaSigma);

        int tokenActual;
        
        // Iterar mientras no se alcance el fin de la cadena
        while (true) {
            tokenActual = yylex();
            if (tokenActual == SimbolosEspeciales.FIN) {
                break; // Detener si se encuentra el token de fin
            }

            // Imprimir el token y el lexema asociado
            System.out.println("Token: " + tokenActual + ", yytext: " + yytext());

            // Verificar si hay un error
            if (tokenActual == SimbolosEspeciales.ERROR) {
                System.out.println("Error léxico en: " + yytext());
                break; // Detener si se encuentra un error
            }
        }
    }
    
    /*
    public AnalizLexico(String sigma, String fileAFD){
        
    }
*/
}
