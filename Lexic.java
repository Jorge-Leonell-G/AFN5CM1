/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoafn;

import java.util.Stack;

public class Lexic {
    public int token, EdoActual, EdoTransicion;
    public String sigma;
    public String yytext;
    public boolean PasoPorEdoAcept;
    public int IniLexema, FinLexema, IndiceCaracterActual;
    public char CaracterActual;
    public Stack<Integer> Pila;
    public AFD AutomataFD;

    // Constructor sin parámetros
    public Lexic () {
        this.sigma = "";
        this.PasoPorEdoAcept = false;
        this.IniLexema = -1;
        this.FinLexema = -1;
        this.IndiceCaracterActual = -1;
        this.token = -1;
        this.Pila = new Stack<>();
        this.AutomataFD = null;
    }

    // Constructor con cadena de entrada y archivo AFD
    public Lexic(String Csigma, String archivo) {
        this.AutomataFD = new AFD();
        this.sigma = Csigma;
        this.PasoPorEdoAcept = false;
        this.IniLexema = 0;
        this.FinLexema = -1;
        this.IndiceCaracterActual = 0;
        this.token = -1;
        this.Pila = new Stack<>();
        AutomataFD.LeerAFD(archivo);
    }

    // Método para clonar el estado actual del analizador léxico
    public Lexic GetEdoAnalizLexico() {
        Lexic EdoActualAnaliz = new Lexic();
        EdoActualAnaliz.CaracterActual = this.CaracterActual;
        EdoActualAnaliz.EdoActual = this.EdoActual;
        EdoActualAnaliz.EdoTransicion = this.EdoTransicion;
        EdoActualAnaliz.FinLexema = this.FinLexema;
        EdoActualAnaliz.IndiceCaracterActual = this.IndiceCaracterActual;
        EdoActualAnaliz.IniLexema = this.IniLexema;
        EdoActualAnaliz.yytext = this.yytext;
        EdoActualAnaliz.PasoPorEdoAcept = this.PasoPorEdoAcept;
        EdoActualAnaliz.token = this.token;
        // Realizar una copia profunda de la pila
        EdoActualAnaliz.Pila = (Stack<Integer>) this.Pila.clone();
        return EdoActualAnaliz;
    }

    // Método para restaurar un estado previamente guardado
    public boolean SetEdoAnalizLexico(Lexic e) {
        this.CaracterActual = e.CaracterActual;
        this.EdoActual = e.EdoActual;
        this.EdoTransicion = e.EdoTransicion;
        this.FinLexema = e.FinLexema;
        this.IndiceCaracterActual = e.IndiceCaracterActual;
        this.IniLexema = e.IniLexema;
        this.yytext = e.yytext;
        this.PasoPorEdoAcept = e.PasoPorEdoAcept;
        this.token = e.token;
        this.Pila = (Stack<Integer>) e.Pila.clone();
        return true;
    }

    // Método para reiniciar el analizador léxico con una nueva cadena de entrada
    public void SetSigma(String ssigma) {
        sigma = ssigma;
        PasoPorEdoAcept = false;
        IniLexema = 0;
        FinLexema = -1;
        IndiceCaracterActual = 0;
        token = -1;
        Pila.clear();
    }

    // Método principal de análisis léxico
    public int yylex() {
        while (true) {
            // Guardar el índice actual en la pila para permitir retroceso
            Pila.push(IndiceCaracterActual);

            if (IndiceCaracterActual >= sigma.length()) {
                yytext = "";
                return SimbolosEspeciales.FIN;
            }

            IniLexema = IndiceCaracterActual;
            EdoActual = 0;
            PasoPorEdoAcept = false;
            FinLexema = -1;
            token = -1;

            while (IndiceCaracterActual < sigma.length()) {
                CaracterActual = sigma.charAt(IndiceCaracterActual);
                
                // Acceder a la tabla de transición del AFD
                EdoTransicion = AutomataFD.tablaAFD[EdoActual][(int) CaracterActual];
                
                if (EdoTransicion != -1) {
                    // Verificar si el estado es de aceptación
                    if (AutomataFD.tablaAFD[EdoTransicion][256] != -1) {
                        PasoPorEdoAcept = true;
                        FinLexema = IndiceCaracterActual;
                        token = AutomataFD.tablaAFD[EdoTransicion][256];
                    }
                    IndiceCaracterActual++;
                    EdoActual = EdoTransicion;
                    continue;
                }
                break;
            }
 
            // Manejo de error: si no pasó por un estado de aceptación
            if (!PasoPorEdoAcept) {
                IndiceCaracterActual = IniLexema + 1;
                yytext = sigma.substring(IniLexema, IniLexema + 1);
                token = SimbolosEspeciales.ERROR;
                return token;
            }

            // Extraer lexema y ajustar el índice
            yytext = sigma.substring(IniLexema, FinLexema + 1);
            IndiceCaracterActual = FinLexema + 1;

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
        IndiceCaracterActual = Pila.pop();
        return true;
    }
    
    public String yytext() {
        return this.yytext;
    }
    
    // Método para imprimir todos los yytext y tokens uno a uno
    public void ImprimirTokens() {
        // Reiniciar el analizador léxico antes de comenzar
        SetSigma(this.sigma);

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
}
