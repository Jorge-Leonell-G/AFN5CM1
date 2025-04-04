/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoafn;

import java.util.Stack;

public class AFNDR {
    Lexic lex;
    int id = 0;
    Stack<AFN> pila = new Stack<>();

    public AFNDR(String afd, String sigma, int id) {
        lex = new Lexic(sigma, afd);
        this.id = id;
    }

    public AFN creado() {
        if (!pila.isEmpty()) {
            return pila.pop(); // Devuelve el AFN final construido
        }
        return null;
    }

    public boolean E() {
        if (T()) {
            return Ep();
        }
        return false;
    }

    private boolean Ep() {
        int token = lex.yylex();

        if (token == 10) { // OR
            if (T()) {
                AFN f2 = pila.pop();
                AFN f1 = pila.pop();
                f1.unirAFNs(f2); // Combina los dos AFN
                pila.push(f1); // Regresa el resultado a la pila
                return Ep();
            }
            return false;
        } else {
            lex.UndoToken();
            return true;
        }
    }

    private boolean T() {
        if (C()) {
            return Tp();
        }
        return false;
    }

    private boolean Tp() {
        int token = lex.yylex();

        // Concatenación implícita
        if (token == 110 || token == 60 || token == 80) { // SIMB, (, [
            lex.UndoToken();
            token = 20; // Actúa como concatenación
        }

        if (token == 20) { // AND o concatenación implícita
            if (C()) {
                AFN f2 = pila.pop();
                AFN f1 = pila.pop();
                f1.Concatenar(f2); // Concatenar los dos AFN
                pila.push(f1); // Regresa el resultado a la pila
                return Tp();
            }
            return false;
        } else {
            lex.UndoToken();
            return true;
        }
    }

    private boolean C() {
        if (F()) {
            return Cp();
        }
        return false;
    }

    private boolean Cp() {
        int token = lex.yylex();

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
            lex.UndoToken();
            return true;
        }
    }

    private boolean F() {
        int token = lex.yylex();

        switch (token) {
            case 60: // (
                if (E()) {
                    token = lex.yylex();
                    return token == 70; // )
                }
                return false;

            case 80: // [
                char s1, s2;
                token = lex.yylex();
                if (token == 110) { // SIMB inicio
                    s1 = lex.yytext().charAt(0);
                    token = lex.yylex();
                    if (token == 100) { // ,
                        token = lex.yylex();
                        if (token == 110) { // SIMB fin
                            s2 = lex.yytext().charAt(0);
                            token = lex.yylex();
                            if (token == 90) { // ]
                                AFN f = new AFN();
                                f = f.crearAFNBasico(s1, s2, id++);
                                pila.push(f); // Agrega el AFN básico a la pila
                                return true;
                            }
                        }
                    }
                }
                return false;

            case 110: // SIMB
                char simb = lex.yytext().charAt(0);
                AFN f = new AFN();
                f = f.crearAFNBasico(simb, simb, id++);
                pila.push(f); // Agrega el AFN básico a la pila
                return true;

            case 120: // \
                token = lex.yylex();
                if (token >= 10 && token <= 90) { // Operadores escapados
                    char escape = lex.yytext().charAt(0);
                    AFN escapedAFN = new AFN();
                    escapedAFN = escapedAFN.crearAFNBasico(escape, escape, id++);
                    pila.push(escapedAFN);
                    return true;
                }
                return false;

            default:
                return false;
        }
    }
}
