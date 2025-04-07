package proyectoafn;

import java.util.*;

public class AFN {
    public int idAFN;
    Estado edoInicial;
    HashSet<Estado> edosAceptacion = new HashSet<>();
    HashSet<Character> alfabeto = new HashSet<>();
    //public Estado estadoFinal;
    public char simbolo;
    HashSet<Estado> edosAFN = new HashSet<>();
    boolean AFNUnionLex;
    public static HashSet<AFN> ConjuntoAFNs = new HashSet<>();
    
    //getters y setters
    public int getidAFN(){
        return idAFN;
    }
    
    public Estado getEdoInicial(){
        return edoInicial;
    }
    
    public void setEdoInicial(Estado edoInicial){
        this.edoInicial = edoInicial;
    }
    
    public HashSet<Estado> getEdosAceptacion(){
        return edosAceptacion;
    }
    
    public void setEdosAcept(Estado edo){
        this.edosAceptacion.add(edo);
    }
    
    public void removerEdosAceptacion(Estado edo){
        this.edosAceptacion.remove(edo);
    }
    
    public HashSet<Character> getAlfabeto(){
        return alfabeto;
    }
    
    public void setEdoAFN(Estado edo){
        this.edosAFN.add(edo);
    }

    public void removeEdoAFN(Estado edo){
        this.edosAFN.remove(edo);
    }

    public void setSimbolos(char simb){
        this.alfabeto.add(simb);
    }
    
    public HashSet<Estado> getEstados() {
        return edosAFN;
    }

    public char getSimbolo() {
        return simbolo;
    }
    
    public HashSet<AFN> getConjuntoAFNs() {
        return ConjuntoAFNs;
    }

    //Metodo constructor de inicializacion
    public AFN() {
        this.idAFN = 0;
        //this.edoInicial = new Estado();
        this.edoInicial = null;
        this.edosAceptacion = new HashSet<>();
        this.alfabeto = new HashSet<>();
        this.edosAFN = new HashSet<>();
        AFNUnionLex = false;
    }
    
    //Metodos constrcutores adicionales
    /*
    public AFN(Estado edoIni){
        this.edoInicial = edoIni;
        this.edosAFN.add(edoIni);
    }
    
    public AFN(Estado edoIni, Estado edoAcept){
        this.edoInicial = edoIni;
        this.edosAFN.add(edoIni);
        this.edosAFN.add(edoAcept);
        this.edosAceptacion.add(edoAcept);
    }

    private void agregarTransicionConOrigen(Estado origen, char simbolo, char simboloSup, Estado destino) {
        origen.agregarTransicion(new Transicion(origen, simbolo, simboloSup, destino));
    }
    */
    
    //----- OPERACIONES DEL AFN -----
    //crear AFN basico con el ingreso de un solo simbolo
    public AFN crearAFNBasico(char s){
        Estado e1 = new Estado();
        Estado e2 = new Estado();
        Transicion t = new Transicion(s,e2);
        
        //AFN f = new AFN();
        
        edoInicial = e1;
        alfabeto.add(s);
        edosAFN.add(e1);
        edosAFN.add(e2);
        e2.edoAceptacion = true;
        edosAceptacion.add(e2);
        AFNUnionLex = false;
        
        return this;
    }

    public AFN crearAFNBasico(char s1, char s2) {
        //AFN f = new AFN();
        Estado e1 = new Estado();
        Estado e2 = new Estado();
        e1.transiciones.add(new Transicion(s1,s2,e2));
        e2.edoAceptacion = true;
        edoInicial = e1;
        
        for(char c = s1; c <= s2; c++){
            alfabeto.add(c);
        }

        edosAceptacion.add(e2);
        //f.idAFN = id;
        edosAFN.add(e1);
        edosAFN.add(e2);
        AFNUnionLex = false;

        return this;
    }
    
    public AFN unirAFNs(AFN f2) {
        //AFN f = new AFN();
        Estado e1 = new Estado();
        Estado e2 = new Estado();
        //char epsilon = SimbolosEspeciales.EPSILON;
        e1.transiciones.add(new Transicion(SimbolosEspeciales.EPSILON, this.edoInicial));
        e1.transiciones.add(new Transicion(SimbolosEspeciales.EPSILON, f2.edoInicial));

        /*
        agregarTransicionConOrigen(e1, epsilon, epsilon, this.estadoInicial);
        agregarTransicionConOrigen(e1, epsilon, epsilon, f2.estadoInicial);
        */
        
        for (Estado e : this.edosAceptacion) {
            e.transiciones.add(new Transicion(SimbolosEspeciales.EPSILON, e2));
            e.edoAceptacion = false;
        }

        for (Estado e : f2.edosAceptacion) {
            e.transiciones.add(new Transicion(SimbolosEspeciales.EPSILON, e2));
            e.edoAceptacion = false;
        }
        
        this.edosAceptacion.clear();
        f2.edosAceptacion.clear();
        //conjunto de estados
        this.edoInicial = e1;
        //this.edosAFN.addAll(this.edosAFN);
        this.edosAFN.addAll(f2.edosAFN);
        //estados "individuales"
        this.edosAFN.add(e1);
        this.edosAFN.add(e2);
        e2.edoAceptacion = true;
        this.edosAceptacion.add(e2);
        //this.alfabeto.addAll(this.alfabeto);
        this.alfabeto.addAll(f2.alfabeto);

        return this;
    }

    public AFN concatenarAFNs(AFN f2) {
        //El for anidado representa que a cada transicion saliente del edoInicial de f2
        //se le agregan a los estados de aceptacion de this
        for (Transicion t : f2.edoInicial.transiciones) {
            for (Estado e : edosAceptacion) {
                //agregarTransicionConOrigen(e, t.getSimbolo(), t.getSimboloSup(), t.getDestino());
                e.transiciones.add(t);
                e.edoAceptacion = false; //Los edos de acept de this dejan de serlo
            }
        }

        //Se procede a eliminar el estado inicial de f2, de la coleccion de edos
        f2.edosAFN.remove(f2.edoInicial);
        ConjuntoAFNs.remove(f2);
        f2.limpiarAFN();
        //Se actualiza la info del nuevo automata resultado de la concatenacion
        this.edosAceptacion = f2.edosAceptacion;
        this.edosAFN.addAll(f2.edosAFN);
        this.alfabeto.addAll(f2.alfabeto);
        
        return this;
    }

    public AFN opcional() {
       //Se crea un nuevo estado inicial y de aceptacion
        Estado nuevoInicial = new Estado();
        Estado nuevoFinal = new Estado();
        
        nuevoInicial.transiciones.add(new Transicion(SimbolosEspeciales.EPSILON, edoInicial));
        nuevoInicial.transiciones.add(new Transicion(SimbolosEspeciales.EPSILON, nuevoFinal));
        for (Estado e : edosAceptacion) {
            e.transiciones.add(new Transicion(SimbolosEspeciales.EPSILON, nuevoFinal));
            e.edoAceptacion = false;
        }

        edoInicial = nuevoInicial;
        nuevoFinal.edoAceptacion = true;
        edosAceptacion.clear();
        edosAceptacion.add(nuevoFinal);
        edosAFN.add(nuevoInicial);
        edosAFN.add(nuevoFinal);

        return this;
    }

    public AFN cerraduraKleene() {
        //Se crea de igual forma un nuevo edo inicial y un nuevo edo de aceptacion
        Estado nuevoInicial = new Estado();
        Estado nuevoFinal = new Estado();
        
        nuevoInicial.transiciones.add(new Transicion(SimbolosEspeciales.EPSILON, edoInicial));
        nuevoInicial.transiciones.add(new Transicion(SimbolosEspeciales.EPSILON, nuevoFinal));
        for (Estado e : edosAceptacion) {
            e.transiciones.add(new Transicion(SimbolosEspeciales.EPSILON, nuevoFinal));
            e.transiciones.add(new Transicion(SimbolosEspeciales.EPSILON, edoInicial));
            e.edoAceptacion = false;
        }
        
        //actualizamos el automata obtenido
        edoInicial = nuevoInicial;
        nuevoFinal.edoAceptacion = true;
        edosAceptacion.clear();
        edosAceptacion.add(nuevoFinal);
        edosAFN.add(nuevoInicial);
        edosAFN.add(nuevoFinal);

        return this;
    }

    public AFN cerraduraPositiva() {
        //Se crea un nuevo estado inicial asi como un nuevo estado de aceptacion
        Estado nuevoInicial = new Estado();
        Estado nuevoFinal = new Estado();
        //char epsilon = SimbolosEspeciales.EPSILON;
        nuevoInicial.transiciones.add(new Transicion(SimbolosEspeciales.EPSILON, edoInicial));

        for (Estado e : edosAceptacion) {
            e.transiciones.add(new Transicion(SimbolosEspeciales.EPSILON, nuevoFinal));
            e.transiciones.add(new Transicion(SimbolosEspeciales.EPSILON, edoInicial));
            e.edoAceptacion = false;
        }

        edoInicial = nuevoInicial;
        nuevoFinal.edoAceptacion = true;
        edosAceptacion.clear();
        edosAceptacion.add(nuevoFinal);
        edosAFN.add(nuevoInicial);
        edosAFN.add(nuevoFinal);

        return this;
    }
    
    public HashSet<Estado> cerraduraEpsilon(Estado e){
        HashSet<Estado> R = new HashSet<Estado>(); //Conjunto de estados
        Stack<Estado> S = new Stack<>(); //Pila de estados
        Estado aux, edo;
        R.clear();
        S.clear();
        
        S.push(e); //Estado e para calcular la cerradura
        while (S.size() != 0){ //Mientras la pila no esta vacia
            aux = S.pop(); //Sacamos el elemento de la pila
            R.add(aux); //Y lo agregamos al conjunto resultado
            for (Transicion t : aux.transiciones){ //Evaluamos si dicha transicion hay transiciones con epsilon
                if((edo = t.getEdoTransicion(SimbolosEspeciales.EPSILON)) != null)
                    if(!R.contains(edo)) //El elemento no se encuentra en el conjunto resultado 
                        S.push(edo); //Se agrega para su posterior analisis hasta que la pila este vacia
            }
        }
        return R; //Conjunto resultado
    }
    
    public HashSet<Estado> cerraduraEpsilon(HashSet<Estado> conjEdos){
        HashSet<Estado> R = new HashSet<Estado>();
        Stack<Estado> S = new Stack<Estado>();
        Estado aux, edo;
        R.clear();
        S.clear();
        for (Estado e : conjEdos) //iteracion sobre los estados en el conjunto de estados
            S.push(e); //Agregamos el estado a la pila
        while (S.size() != 0){
            aux = S.pop();
            R.add(aux);
            for (Transicion t : aux.transiciones)
                if((edo = t.getEdoTransicion(SimbolosEspeciales.EPSILON)) != null)
                    if (!R.contains(edo))
                        S.push(edo);
        }
        return R;
    }
    
    public HashSet<Estado> Mover(Estado edo, char simb){
        HashSet<Estado> C = new HashSet<Estado>();
        Estado aux;
        C.clear();
        
        //Realizamos cada transicion dentro del estado edo
        for(Transicion t : edo.transiciones){
            aux = t.getEdoTransicion(simb); //Verificamos si hay transicion con el simbolo
            if(aux != null)
                C.add(aux); //Si hubo una transicion con simb y agregamos aux al conjunto
        }
        return C; //conjunto de estados
    }
    
    public HashSet<Estado> Mover(HashSet<Estado> edos, char simb){
        HashSet<Estado> C = new HashSet<Estado>();
        Estado aux;
        C.clear();
        
        //Realizamos cada transicion barriendo cada estado dentro del conjunto, dentro del estado edo
        for(Estado edo : edos)
            for(Transicion t : edo.transiciones){
                aux = t.getEdoTransicion(simb); //Verificamos si hay transicion con el simbolo
                if(aux != null)
                    C.add(aux); //Si hubo una transicion con simb y agregamos aux al conjunto
            }
        return C; //conjunto de estados
    }
    
    public HashSet<Estado> IrA(HashSet<Estado> edos, char simb){
        HashSet<Estado> C = new HashSet<Estado>();
        C.clear();
        C = cerraduraEpsilon(Mover(edos, simb));
        return C;
    }
    
    public void unionEspecialAFNs(AFN f, int Token){
        Estado e;
        if(!this.AFNUnionLex){
            this.edosAFN.clear();
            this.alfabeto.clear();
            e = new Estado();
            e.transiciones.add(new Transicion(SimbolosEspeciales.EPSILON, f.edoInicial));
            this.edoInicial = e;
            this.edosAFN.add(e);
            this.AFNUnionLex = true;
        }
        else
            this.edoInicial.transiciones.add(new Transicion(SimbolosEspeciales.EPSILON, f.edoInicial));
        for(Estado edoAceptacion : f.edosAceptacion)
            edoAceptacion.token = Token;
        this.edosAceptacion.addAll(f.edosAceptacion);
        this.edosAFN.addAll(f.edosAFN);
        this.alfabeto.addAll(f.alfabeto);
    }
    
    public AFN UnirAFNAnaliLex(Map<AFN, Integer> afnsTokens) {
        AFN unido = new AFN();

        Estado e = new Estado();
        unido.edoInicial = e;
        unido.edosAFN.add(e);

        // Itera sobre el mapa de AFN y tokens seleccionados
        for (Map.Entry<AFN, Integer> entry : afnsTokens.entrySet()) {
            AFN afn = entry.getKey();
            int token = entry.getValue();
            unido.idAFN = afn.idAFN;

            // Agrega la transición epsilon desde el estado inicial del AFN unido al estado inicial del AFN actual
            unido.edoInicial.transiciones.add(new Transicion(SimbolosEspeciales.EPSILON, afn.edoInicial));

            // Asigna el token a cada estado de aceptación del AFN actual
            for (Estado EdoAcep : afn.edosAceptacion) {
                EdoAcep.token = token;
            }

            // Agrega los estados y el alfabeto del AFN actual al AFN unido
            unido.edosAceptacion.addAll(afn.edosAceptacion);
            unido.edosAFN.addAll(afn.edosAFN);
            unido.alfabeto.addAll(afn.alfabeto);

            // Limpia el AFN actual después de agregarlo al AFN unido
            afn.edosAFN.clear();
            afn.alfabeto.clear();
            afn.edosAceptacion.clear();
            afn.edoInicial = null;
            ConjuntoAFNs.remove(afn);
        }

        // Agrega el AFN unido al conjunto de AFNs
        ConjuntoAFNs.add(unido);
        int i = 0;

        // Imprimir estados y sus tokens
        for (Estado estado : unido.edosAFN) {
            System.out.println(i + " .Estado ID: " + estado.idEdo + ", Token: " + estado.token);

            // Imprimir transiciones de cada estado
            for (Transicion transicion : estado.transiciones) {
                System.out.println("   Transición: " + transicion.simboloSup + " -> Estado ID: " + transicion.edo.idEdo);
            }
            i++;
        }

        return unido;
    }
    
    private int indiceCaracter(char[] arregloAlfabeto, char c){
        for(int i = 0; i < arregloAlfabeto.length; i++)
            if(arregloAlfabeto[i]==c)
                return i;
        return -1;
    }
    
    //creacion de la tabla para posterior analisis lexico
     public AFD ConvAFNaAFD() {
        int j = 0;
        Set<Character> ArrAlfabeto = new HashSet<>(this.alfabeto); // Clonar el alfabeto del AFN para usar en el AFD
        boolean existe;

        Set<Si> EdosAFD = new HashSet<>();
        Queue<Si> EdosSinAnalizar = new LinkedList<>();

        // Crear el primer estado del AFD a partir de la cerradura-ε del estado inicial del AFN
        Si Ij = new Si();
        Ij.ConjI = cerraduraEpsilon(this.edoInicial);
        Ij.j = j;
        EdosAFD.add(Ij);
        EdosSinAnalizar.add(Ij);
        j++;

        System.out.println("Estado inicial del AFD creado: " + Ij.ConjI);

        // Mientras haya estados sin analizar
        while (!EdosSinAnalizar.isEmpty()) {
            Ij = EdosSinAnalizar.poll();
            System.out.println("Analizando estado: " + Ij.j + " con conjunto: " + Ij.ConjI);

            // Para cada símbolo en el alfabeto
            for (char c : ArrAlfabeto) {
                Si Ik = new Si(); // Crear un nuevo objeto Ik en cada iteración
                Ik.ConjI = IrA((HashSet<Estado>) Ij.ConjI, c); // IrA: calcula el conjunto destino al aplicar el símbolo c

                if (Ik.ConjI.isEmpty()) {
                    System.out.println("Conjunto destino vacío para símbolo '" + c + "', continuando...");
                    continue; // Si el conjunto destino está vacío, continuamos
                }
                existe = false;

                // Verificar si el conjunto destino ya existe en EdosAFD
                for (Si estadoExistente : EdosAFD) {
                    if (estadoExistente.ConjI.equals(Ik.ConjI)) {
                        existe = true;
                        Ij.TransicionesAFD[(int) c] = estadoExistente.j; // Asignar transición al estado existente
                        System.out.println("Transición a estado existente: " + estadoExistente.j + " para símbolo '" + c + "'");
                        break;
                    }
                }

                if (!existe) {
                    Ik.j = j;
                    Ij.TransicionesAFD[(int) c] = Ik.j; // Asignar nueva transición
                    EdosAFD.add(Ik);
                    EdosSinAnalizar.add(Ik);
                    System.out.println("Nueva transición añadida: Estado " + Ik.j + " para símbolo '" + c + "'");
                    j++;
                }
            }
        }
        // Configuración de la tabla AFD
        AFD afd = new AFD(j);
        afd.numEstados = j;
        afd.alfabeto = this.alfabeto;

        // Asignación de los estados de aceptación y transición en la tabla
        for (Si estadoAFD : EdosAFD) {
            int estadoIndice = estadoAFD.j;
            afd.tablaAFD[estadoIndice] = estadoAFD.TransicionesAFD;

            for (Estado acept : this.edosAceptacion) {
                if (estadoAFD.ConjI.contains(acept)) {
                    afd.tablaAFD[estadoIndice][256] = acept.token; // Marca el estado de aceptación
                    System.out.println("Estado de aceptación encontrado: " + estadoIndice + " para token: " + acept.token);
                }
            }
        }

        return afd;
    }
    
    /*
    public AFD convAFNaAFD(){
        int cardAlfabeto, numEdosAFD; //cardinalidad del alfabeto y numero de estados del AFD
        int i, j, r;
        char[] arrAlfabeto;
        ConjIj Ij, Ik; //Ij es el conjunto de estados e Ik es el conjunto auxiliar
        boolean existe;
        
        HashSet<Estado> conjAux = new HashSet<Estado>();
        HashSet<ConjIj> edosAFD = new HashSet<ConjIj>();
        Queue<ConjIj> edosSinAnalizar = new LinkedList<>();
        
        edosAFD.clear();
        edosSinAnalizar.clear();
        
        cardAlfabeto = alfabeto.size();
        arrAlfabeto = new char[cardAlfabeto];
        i = 0;
        for(char c : alfabeto)
            arrAlfabeto[i++] = c;
        j = 0; //contador para los estados del AFD
        Ij = new ConjIj(cardAlfabeto);
        Ij.ConjI = cerraduraEpsilon(this.edoInicial);
        Ij.j = j;
        edosAFD.add(Ij);
        edosSinAnalizar.offer(Ij);
        j++;
        while(edosSinAnalizar.size() != 0){
            Ij = edosSinAnalizar.poll();
            //Se calcula el IrA del Ij con cada simbolo del alfbeto
            for(char c : arrAlfabeto){
                Ik = new ConjIj(cardAlfabeto);
                Ik.ConjI = IrA(Ij.ConjI, c);
                if(Ik.ConjI.size() == 0) //No hubo transiciones
                    continue;
                //Revisemos si el conjunto de estados ya existe
                existe = false; //activacion de bandera
                for(ConjIj I : edosAFD){
                    if(I.ConjI.equals(Ik.ConjI)){
                        existe = true;
                        r = indiceCaracter(arrAlfabeto, c);
                        Ij.TransicionesAFD[r] = I.j;
                        break;
                    }
                }
                if(!existe){ //Si el conjunto Ik no existia, sera un nuevo estado a agregar
                    Ik.j = j; //Colocamos el indice correspondiente al nuevo estado
                    r = indiceCaracter(arrAlfabeto,c);
                    Ij.TransicionesAFD[r] = Ik.j;
                    edosAFD.add(Ik);
                    edosSinAnalizar.offer(Ik);
                    j++;
                }
            }
        }
        
        //Ajuste de la tabla del AFD
        AFD afd = new AFD(j);
        afd.numEstados = j;
         afd.alfabeto = this.alfabeto;

        // Asignación de los estados de aceptación y transición en la tabla
        for (s estadoAFD : edosAFD) {
            int estadoIndice = estadoAFD.j;
            afd.tablaAFD[estadoIndice] = estadoAFD.TransicionesAFD;

            for (Estado acept : this.ed) {
                if (estadoAFD.ConjI.contains(acept)) {
                    afd.tablaAFD[estadoIndice][256] = acept.token; // Marca el estado de aceptación
                    System.out.println("Estado de aceptación encontrado: " + estadoIndice + " para token: " + acept.token);
                }
            }
        }
        //numEdosAFD = j;
        return afd;
    }
    */
    
    private void limpiarAFN() {
        this.edosAFN.clear();
        this.alfabeto.clear();
        this.edosAceptacion.clear();
        this.edoInicial = null;
        ConjuntoAFNs.remove(this);
    }

    /*
    public List<Transicion> getTransiciones() {
        List<Transicion> transiciones = new ArrayList<>();
        for (Estado estado : edosAFN) {
            transiciones.addAll(estado.getTransiciones());
        }
        return transiciones;
    }
    */
    
}
