package proyectoafn;

import java.io.*;
import java.util.Set;

public class Graph {

    public static String graficarAFN(AFN afn, String nombreAFN, String directorioSalida) throws IOException {
    if (directorioSalida == null || directorioSalida.isEmpty()) {
        directorioSalida = "Grafos";
    }

    File carpeta = new File(directorioSalida);
    if (!carpeta.exists()) {
        carpeta.mkdirs();
    }

    String pathDot = directorioSalida + "/" + nombreAFN + ".dot";
    String pathImagen = directorioSalida + "/" + nombreAFN + ".png";

    StringBuilder dot = new StringBuilder();
    dot.append("digraph AFN {\n");
    dot.append("    rankdir=LR;\n");
    dot.append("    label=\"\\n\\n\\nAFN: ").append(nombreAFN).append("\";\n");
    dot.append("    labelloc=b;\n");
    dot.append("    fontsize=20;\n");
    dot.append("    node [shape=circle]; start;\n");
    dot.append("    start -> ").append(afn.getEdoInicial().getIdEdo()).append(" [label=\"inicio\"];\n");

    // Estados
    for (Estado estado : afn.getEstados()) {
        String shape = estado.edoAceptacion ? "doublecircle" : "circle";
        dot.append("    ").append(estado.getIdEdo()).append(" [shape=").append(shape).append("];\n");
    }

    // Transiciones
    /*
    for (Transicion t : afn.getTransiciones()) {
        String simbolo = t.simboloSup == 'ε' ? "ε" : String.valueOf(t.simboloSup);
        dot.append("    ").append(t.origen.getIdEdo())
           .append(" -> ").append(t.destino.getIdEdo())
           .append(" [label=\"").append(simbolo).append("\"];\n");
    }
    */
    
    // Transiciones desde cada estado
    for (Estado estado : afn.getEstados()) {
        for (Transicion t : estado.getTransiciones()) {
            if (t.getEdo() == null) continue;

            int origen = estado.getIdEdo();
            int destino = t.getEdo().getIdEdo();

            String label;
            if (t.getSimbInf() == SimbolosEspeciales.EPSILON) {
                label = "ε";
            } else if (t.getSimbInf() == t.getSimbSup()) {
                label = String.valueOf(t.getSimbInf());
            } else {
                label = t.getSimbInf() + "-" + t.getSimbSup();
            }

            dot.append("    ").append(origen)
           .append(" -> ").append(destino)
           .append(" [label=\"").append(label).append("\"];\n");
        }
}

    dot.append("}\n");

    FileWriter writer = new FileWriter(pathDot);
    writer.write(dot.toString());
    writer.close();

    ProcessBuilder pb = new ProcessBuilder("dot", "-Tpng", pathDot, "-o", pathImagen);
    Process process = pb.start();

    try {
        process.waitFor();
    } catch (InterruptedException e) {
        e.printStackTrace();
    }

    return "/" + pathImagen.replace("\\", "/");
}

}
