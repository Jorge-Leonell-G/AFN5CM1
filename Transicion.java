package proyectoafn;

public class Transicion {
    public char simboloInf; // símbolo inicial
    public char simboloSup; // símbolo final (para rangos)
    //private Estado origen;
    public Estado edo;
    private int posicionEdo;
    
    public char getSimbInf(){
        return this.simboloInf;
    }
    
     public void setSimbInf(char SimbInf){
        this.simboloInf = SimbInf;
    }

    public char getSimbSup(){
        return this.simboloSup;
    }

    public void setSimbSup(char SimbSup){
        this.simboloSup = SimbSup;
    }

    public Estado getEdo(){
        return this.edo;
    }

    public void setEdo(Estado Edo){
        this.edo = Edo;
    }

    public int getPosicionEdo(){
        return this.posicionEdo;
    }

    // Constructores
    public Transicion() {
        this.edo = null;
    }
    
    // Transición simple sin origen
    public Transicion(char simbolo, Estado e) {
        this.simboloInf = simbolo;
        this.simboloSup = simbolo; //Se asume que simboloSup = simbolo
        this.edo = e;
    }

    // Transición con rango de símbolos sin origen
    public Transicion(char simboloInicio, char simboloFin, Estado e) {
        this.simboloInf = simboloInicio;
        this.simboloSup = simboloFin;
        this.edo = e;
    }

    // Transición con origen, rango de símbolos y destino
    public Transicion(Estado edoOrigen, char inf, char sup, Estado e) {
        //this.origen = edoOrigen;
        this.simboloInf = inf;
        this.simboloSup = sup;
        this.edo = e;
    }
    
    //Metodos para inicializar los elementos de la instancia Transicion
    public void setTransicion(char s1, char s2, Estado e){
        simboloInf = s1;
        simboloSup = s2;
        edo = e;
    }
    
    public void setTransicion(char s1, Estado e){
        simboloInf = s1;
        simboloSup = s1;
        edo = e;
    }
    
    //metodo para indicar si bajo el caracter simbolo se tiene una transicion a un estado
    public Estado getEdoTransicion(char simbolo){
        if (simboloInf <= simbolo && simbolo <= simboloSup){
            return edo;
        }
        return null;
    }

    /*
    @Override
    public String toString() {
        String origenStr = (origen != null) ? origen.getIdEdo() : "?";
        String destinoStr = (destino != null) ? destino.getIdEdo() : "?";
        return origenStr + " -" + simbolo + "-> " + destinoStr;
    }
    */
}
