package proyectoafn;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Graph {
    
    public static String graficarAFN(AFN afn, String nombreAFN, String directorioSalida) throws IOException {
        if (directorioSalida == null || directorioSalida.isEmpty()) {
            directorioSalida = "Grafos";
        }

        File directory = new File(directorioSalida);
        if (!directory.exists()) {
            directory.mkdir();
        }

        //String nombreSanitizado = sanitizarNombreArchivo(nombreAFN);
        String rutaDot = directorioSalida + "/" + nombreAFN + ".dot";
        String rutaImagen = directorioSalida + "/" + nombreAFN + ".png";

        StringBuilder dot = new StringBuilder();
        dot.append("digraph AFN {\n");
        dot.append("    rankdir=LR;\n");
        dot.append("    label=\"\\n\\n\\nAFN: ").append(nombreAFN).append("\";\n");
        dot.append("    labelloc=\"b\";\n");
        dot.append("    fontsize=20;\n");
        dot.append("    node [shape = point]; start;\n");
        dot.append("    start -> ").append(afn.estadoInicial.idEdo).append(" [label=\"inicio\"];\n");

        Set<Estado> estados = afn.estadosAFN;

        for (Estado estado : estados) {
            if (estado.estadoAceptacion) {
                dot.append("    ").append(estado.idEdo).append(" [shape=doublecircle];\n");
            } else {
                dot.append("    ").append(estado.idEdo).append(" [shape=circle];\n");
            }
        }

        for (Estado estado : estados) {
            for (Transicion transicion : estado.transiciones) {
                String simbolo = transicion.simboloSup == 'ε' ? "ε" : String.valueOf(transicion.simboloSup);
                dot.append("    ").append(estado.idEdo)
                   .append(" -> ")
                   .append(transicion.destino.idEdo)
                   .append(" [label=\"").append(simbolo).append("\"];\n");
            }
        }

        dot.append("}\n");

        // Escribir archivo .dot
        FileWriter writer = new FileWriter(rutaDot);
        writer.write(dot.toString());
        writer.close();

        // Ejecutar Graphviz para generar la imagen
        ProcessBuilder pb = new ProcessBuilder("dot", "-Tpng", rutaDot, "-o", rutaImagen);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "/" + rutaImagen.replace("\\", "/");
    }

    /*
    private static String sanitizarNombreArchivo(String nombre) {
        return nombre.replaceAll("[^a-zA-Z0-9_-]", "_");
    }
    */
}
