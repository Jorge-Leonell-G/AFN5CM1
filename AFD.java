package proyectoafn;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class AFD {
    public Set<Character> alfabeto;
    public int numEstados;
    public int[][] tablaAFD;
    public int estadoInicial = 0;
    public Set<Integer> estadosFinales = new HashSet<>();

    public AFD() {
        this.alfabeto = new HashSet<>();
        this.numEstados = 0;
        this.tablaAFD = new int[0][257];
    }

    public AFD(int numeroDeEstados) {
        this.alfabeto = new HashSet<>();
        this.numEstados = numeroDeEstados;
        this.tablaAFD = new int[numEstados][257];
        inicializarTabla();
    }

    public AFD(int numeroDeEstados, Set<Character> alfabeto) {
        this.alfabeto = new HashSet<>(alfabeto);
        this.numEstados = numeroDeEstados;
        this.tablaAFD = new int[numEstados][257];
        inicializarTabla();
    }

    private void inicializarTabla() {
        for (int i = 0; i < numEstados; i++) {
            for (int j = 0; j < 257; j++) {
                tablaAFD[i][j] = -1;
            }
        }
    }

    public void setAlfabeto(Set<Character> nuevoAlfabeto) {
        this.alfabeto = nuevoAlfabeto;
    }

    public void agregarEstadoFinal(int estado) {
        if (estado >= 0 && estado < numEstados) {
            estadosFinales.add(estado);
        }
    }

    public void agregarTransicion(int estadoOrigen, char simbolo, int estadoDestino) {
        if (!alfabeto.contains(simbolo)) {
            System.out.println("Símbolo no está en el alfabeto.");
            return;
        }

        if (estadoOrigen >= 0 && estadoOrigen < numEstados &&
            estadoDestino >= 0 && estadoDestino < numEstados) {
            tablaAFD[estadoOrigen][(int) simbolo] = estadoDestino;
        } else {
            System.out.println("Estados fuera de rango.");
        }
    }

    public boolean validarCadena(String cadena) {
        int estadoActual = estadoInicial;

        for (int i = 0; i < cadena.length(); i++) {
            char simbolo = cadena.charAt(i);

            if (!alfabeto.contains(simbolo)) {
                System.out.println("Símbolo '" + simbolo + "' no pertenece al alfabeto.");
                return false;
            }

            int siguienteEstado = tablaAFD[estadoActual][(int) simbolo];

            if (siguienteEstado == -1) {
                return false;
            }

            estadoActual = siguienteEstado;
        }

        return estadosFinales.contains(estadoActual);
    }

    public void GuardarAFD(String NombArchivo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(NombArchivo))) {
            writer.write(numEstados + "\n");

            // Guardar alfabeto como una línea de caracteres separados por coma
            StringBuilder sbAlfabeto = new StringBuilder();
            for (char c : alfabeto) {
                sbAlfabeto.append(c).append(",");
            }
            if (sbAlfabeto.length() > 0) {
                sbAlfabeto.setLength(sbAlfabeto.length() - 1); // quitar última coma
            }
            writer.write(sbAlfabeto.toString() + "\n");

            // Guardar estados finales
            StringBuilder sbFinales = new StringBuilder();
            for (int e : estadosFinales) {
                sbFinales.append(e).append(",");
            }
            if (sbFinales.length() > 0) {
                sbFinales.setLength(sbFinales.length() - 1);
            }
            writer.write(sbFinales.toString() + "\n");

            // Guardar tabla de transiciones
            for (int i = 0; i < numEstados; i++) {
                for (int j = 0; j < 257; j++) {
                    writer.write(String.valueOf(tablaAFD[i][j]));
                    if (j != 256) {
                        writer.write(";");
                    }
                }
                writer.write("\n");
            }

        } catch (IOException e) {
            System.err.println("No se pudo abrir el archivo: " + e.getMessage());
        }
    }

    public void LeerAFD(String NombArchivo) {
        try (BufferedReader reader = new BufferedReader(new FileReader(NombArchivo))) {
            // Leer número de estados
            this.numEstados = Integer.parseInt(reader.readLine());

            // Leer alfabeto
            this.alfabeto = new HashSet<>();
            String lineaAlfabeto = reader.readLine();
            if (lineaAlfabeto != null && !lineaAlfabeto.isEmpty()) {
                for (String s : lineaAlfabeto.split(",")) {
                    if (!s.isEmpty()) alfabeto.add(s.charAt(0));
                }
            }

            // Leer estados finales
            this.estadosFinales = new HashSet<>();
            String lineaFinales = reader.readLine();
            if (lineaFinales != null && !lineaFinales.isEmpty()) {
                for (String s : lineaFinales.split(",")) {
                    if (!s.isEmpty()) estadosFinales.add(Integer.parseInt(s));
                }
            }

            // Leer tabla de transiciones
            this.tablaAFD = new int[numEstados][257];
            for (int i = 0; i < numEstados; i++) {
                String renglon = reader.readLine();
                if (renglon != null) {
                    String[] valoresRenglon = renglon.split(";");
                    for (int k = 0; k < 257 && k < valoresRenglon.length; k++) {
                        tablaAFD[i][k] = Integer.parseInt(valoresRenglon[k]);
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("No se puede abrir el archivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error al convertir cadena a número: " + e.getMessage());
        }
    }
}
