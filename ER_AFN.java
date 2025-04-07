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
public class ER_AFN {
    String regExp;
    public AFN result;
    public AnalizLexico L;
    int id = 0;
    Stack<AFN> pila = new Stack<>();
    
    public ER_AFN (String sigma, String AFD, int id){
        regExp = sigma;
        L = new AnalizLexico(regExp, AFD); //Pasamos la regexp y el archivo
        this.id = id;
    }
    
    public AFN creado(){
        if(pila.size() != 0){
            return pila.pop();
        }
        return null;
    }
    
    //Inicio de los metodos para el descenso recursivo y el analisis lexico
    public boolean E() {
        if (T()) {
            return Ep();
        }
        return false;
    }
    
    private boolean T() {
        if (C()) {
            return Tp();
        }
        return false;
    }
    
    private boolean Ep() {
        int token = L.yylex();
        if (token == 10) { // OR
            if (T()) {
                AFN f2 = pila.pop();
                AFN f1 = pila.pop();
                f1.unirAFNs(f2); // Combina los dos AFN con Thompson
                pila.push(f1); // Regresa el resultado a la pila
                return Ep();
            }
            return false;
        } else {
            L.UndoToken();
            return true;
        }
    }

    private boolean C() {
        if (F()) {
            return Cp();
        }
        return false;
    }

    private boolean Tp() {
        int token = L.yylex();
        // Concatenación implícita
        if (token == 110 || token == 60 || token == 80) { // SIMB, (, [
            L.UndoToken();
            token = 20; // Actúa como concatenación
        }
        if (token == 20) { // AND o concatenación implícita
            if (C()) {
                AFN f2 = pila.pop();
                AFN f1 = pila.pop();
                f1.concatenarAFNs(f2); // Concatenar los dos AFN
                pila.push(f1); // Regresa el resultado a la pila
                return Tp();
            }
            return false;
        } else {
            L.UndoToken();
            return true;
        }
    }
    

    private boolean F() {
         int token = L.yylex();
        switch (token) {
            case 60: // (
                if (E()) {
                    token = L.yylex();
                    return token == 70; // )
                }
                return false;

            case 80: // [
                char st1, st2;
                token = L.yylex();
                if (token == 110) { // SIMB inicio
                    st1 = L.yytext().charAt(0);
                    token = L.yylex();
                    if (token == 100) { // ,
                        token = L.yylex();
                        if (token == 110) { // SIMB fin
                            st2 = L.yytext().charAt(0);
                            token = L.yylex();
                            if (token == 90) { // ]
                                AFN f = new AFN();
                                f = f.crearAFNBasico(st1, st2, id++);
                                pila.push(f); // Agrega el AFN básico a la pila
                                return true;
                            }
                        }
                    }
                }
                return false;

            case 110: // SIMB
                char simbolo = L.yytext().charAt(0);
                AFN f = new AFN();
                f = f.crearAFNBasico(simbolo, simbolo, id++);
                pila.push(f); // Agrega el AFN básico a la pila
                return true;

            case 120: // \
                token = L.yylex();
                if (token >= 10 && token <= 90) { // Operadores escapados
                    char escapado = L.yytext().charAt(0);
                    AFN escapedAFN = new AFN();
                    escapedAFN = escapedAFN.crearAFNBasico(escapado, escapado, id++);
                    pila.push(escapedAFN);
                    return true;
                }
                return false;

            default:
                return false;
        }
    }

    private boolean Cp() {
        int token = L.yylex();
        if (token == 30) { // +
            AFN f = pila.pop();
            f.cerraduraPositiva();
            pila.push(f);
            return Cp();
        } else if (token == 40) { // *
            AFN f = pila.pop();
            f.cerraduraKleene();
            pila.push(f);
            return Cp();
        } else if (token == 50) { // ?
            AFN f = pila.pop();
            f.opcional();
            pila.push(f);
            return Cp();
        } else {
            L.UndoToken();
            return true;
        }
    }
     
}
