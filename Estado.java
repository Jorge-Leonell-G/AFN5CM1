package proyectoafn;

import java.util.HashSet;
import java.util.Set;

public class Estado {

    // Transiciones asociadas a este estado
    public Set<Transicion> transiciones = new HashSet<>();
    public boolean estadoAceptacion;
    public String idEdo;
    public int token;

    // Constructor que permite establecer un identificador para el estado
    public Estado(String idEdo) {
        this.idEdo = idEdo;
        this.token = -1;  // Inicialmente sin token
        this.estadoAceptacion = false; // Por defecto, no es estado de aceptación
    }

    // Constructor que también asigna un token
    public Estado(String idEdo, int token) {
        this.idEdo = idEdo;
        this.token = token;
        this.estadoAceptacion = false; // Por defecto, no es estado de aceptación
    }

    // Constructor por defecto
    public Estado() {
        this.idEdo = "Desconocido";  // o un valor por defecto
        this.token = -1;
        this.estadoAceptacion = false;
    }

    // Método para agregar una transición a este estado
    public void agregarTransicion(Transicion t) {
        transiciones.add(t);
    }

    // Método para verificar si el estado es un estado de aceptación
    public boolean esEstadoAceptacion() {
        return this.estadoAceptacion;
    }

    public void setEstadoAceptacion(boolean estadoAceptacion) {
        this.estadoAceptacion = estadoAceptacion;
    }

    public String getIdEdo() {
        return this.idEdo;
    }

    // Métodos getter y setter para el token
    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    // Obtener las transiciones del estado
    public Set<Transicion> getTransiciones() {
        return transiciones;
    }
}
