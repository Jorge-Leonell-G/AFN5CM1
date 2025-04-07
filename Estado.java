package proyectoafn;

import java.util.HashSet;

public class Estado {

    public int idEdo;
    private HashSet<Estado> edos = new HashSet<Estado>(); 
    HashSet<Transicion> transiciones = new HashSet<>();
    boolean edoAceptacion;
    int token;
    private static int contadorIdEdo = 0;
    
    //getters y setters
    public int getIdEdo(){
        return this.idEdo;
    }
    
    public void setIdEdo(int idEstado){
        this.idEdo = idEstado;
    }
    
    public HashSet<Estado> getEdos(){
        return this.edos;
    }

    public void setEdos(HashSet<Estado> Edos){
        this.edos = Edos;
    }
    
    public HashSet<Transicion> getTransiciones(){
        return this.transiciones;
    }

    public void setTransiciones(Transicion t){
        this.transiciones.add(t);
    }

    public boolean getEdoAceptacion(){
        return this.edoAceptacion;
    }

    public void setEdoAceptacion(boolean EdoAcept){
        this.edoAceptacion = EdoAcept;
    }

    public int getToken(){
        return this.token;
    }

    public void setToken(int Token){
        this.token = Token;
    }
    
    //Constructores
    
    //Constructor base
    public Estado() {
        this.idEdo = contadorIdEdo++;
        this.edos = new HashSet<>();
        this.transiciones.clear();
        this.edoAceptacion = false;
        this.token = -1;
    }
    
    //Constructores adicionales
    public Estado(boolean edoAcept, int Token){
        this.idEdo = contadorIdEdo++;
        this.edoAceptacion = edoAcept;
        this.token = Token;
    }
    
    public Estado(Transicion t, boolean EdoAcept, int Token){
        this.idEdo = contadorIdEdo++;
        this.edoAceptacion = EdoAcept;
        this.token = Token;
        this.transiciones.add(t);
    }

    public Estado(boolean EdoAcept, int Token, HashSet<Estado> Edos){
        this.idEdo = contadorIdEdo++;
        this.edoAceptacion = EdoAcept;
        this.token = Token;
        this.edos = Edos;
    }
    
    
    /*
    public Estado(int idEdo) {
        this.idEdo = idEdo;
        this.token = -1;
        this.edoAceptacion = false;
    }

    public Estado(int idEdo, int token) {
        this.idEdo = idEdo;
        this.token = token;
        this.edoAceptacion = false;
    }
    
    public void agregarTransicion(Transicion t) {
        transiciones.add(t);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Estado)) return false;
        Estado otro = (Estado) obj;
        return this.idEdo.equals(otro.idEdo);
    }

    @Override
    public int hashCode() {
        return idEdo.hashCode();
    }
    */
}
