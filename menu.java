package proyectoafn;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;  // Import especifico para evitar conflictos de ambiguedad entre java.util y java.awt
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class menu {
    public static void main(String[] args) {
        JFrame frame = new JFrame("AFD y Operaciones de AFN");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(550, 600);
        frame.setLayout(new FlowLayout());

        // Entrada de cadena
        JLabel inputLabel = new JLabel("Ingrese una cadena:");
        JTextField inputField = new JTextField(15);
        JButton validateButton = new JButton("Validar AFD");

        // Botones de operaciones de AFN
        JButton createAFNButton = new JButton("Crear AFN básico");
        JButton unionButton = new JButton("Unir 2 AFN");
        JButton concatButton = new JButton("Concatenar 2 AFN");
        JButton kleeneButton = new JButton("Cerradura *");
        JButton positiveClosureButton = new JButton("Cerradura +");
        JButton optionalButton = new JButton("Operación Opcional");
        JButton viewAFNsButton = new JButton("Ver lista de AFNs");

        // Área de resultado
        JTextArea resultArea = new JTextArea(10, 40);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        // Añadir componentes al frame
        frame.add(inputLabel);
        frame.add(inputField);
        frame.add(validateButton);

        frame.add(createAFNButton);
        frame.add(unionButton);
        frame.add(concatButton);
        frame.add(kleeneButton);
        frame.add(positiveClosureButton);
        frame.add(optionalButton);
        frame.add(viewAFNsButton);
        frame.add(scrollPane);

        // AFD vacío
        AFD afd1 = new AFD();
        final Set<Character> currentAlphabet = new HashSet<>();

        // Ruta para guardar los archivos
        String directoryPath = "AFNbasicos";
        String graphPath = "Grafos";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdir(); // Crear carpeta si no existe
        }
        
        // AFNs para operaciones
        AFN afn1 = new AFN();
        AFN afn2 = new AFN();
        
        // Expresión regular para validar el símbolo ingresado (permitir solo caracteres alfabéticos y evitar símbolos especiales)
        Pattern symbolPattern = Pattern.compile("^[a-zA-Z]$");

        // Acción: Crear AFN básico con entrada de símbolo
        createAFNButton.addActionListener(e -> {
            JTextField symbolInput = new JTextField(1);
            Object[] message = {
                "Ingrese un símbolo para el AFN básico:", symbolInput
            };

            int option = JOptionPane.showConfirmDialog(frame, message, "Crear AFN básico", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                String inputText = symbolInput.getText().trim();
                if (inputText.length() != 1) {
                    JOptionPane.showMessageDialog(frame, "Debe ingresar un solo símbolo.");
                    return;
                }
                char simbolo = inputText.charAt(0);
                // Validar que el símbolo ingresado es alfabético (sin caracteres especiales)
                Matcher matcher = symbolPattern.matcher(String.valueOf(simbolo));
                if (!matcher.matches()) {
                    JOptionPane.showMessageDialog(frame, "El símbolo ingresado es inválido. Solo se permiten letras.");
                    return;
                }
                int id = 0; // ID único para cada AFN creado
                //afn1 = new AFN(); // Reiniciar instancia si es necesario
                afn1.crearAFNBasico(simbolo);

                // Datos adicionales
                String estadoInicial = "q0";
                String estadoFinal = "qF"; // Estado final arbitrario
                List<Transicion> transiciones = new ArrayList<>();
                transiciones.add(new Transicion('a', 'b', new Estado()));  // Ejemplo de transición
                transiciones.add(new Transicion('b', 'c', new Estado())); // Otra transición

                Set<Character> alfabeto = new HashSet<>(Arrays.asList('a', 'b')); // Ejemplo de alfabeto
                String fechaCreacion = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                // Crear archivo con los datos completos
                File afnFile = new File(directoryPath + "/" + simbolo + ".txt");
                if (afnFile.exists()) {
                    JOptionPane.showMessageDialog(frame, "El símbolo '" + simbolo + "' ya ha sido utilizado para un AFN básico.");
                    return;  // Evitar la creación del nuevo AFN
                }
                
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(afnFile))) {
                    writer.write("Simbolo: " + simbolo + "\n");
                    writer.write("Estado inicial: " + estadoInicial + "\n");
                    writer.write("Estado final: " + estadoFinal + "\n");

                    // Escribir las transiciones
                    writer.write("Transiciones:\n");
                    for (Transicion t : transiciones) {
                        writer.write("  " + t.simboloInf + " -> " + t.edo.getIdEdo() + "\n");
                    }

                    writer.write("Alfabeto: " + alfabeto.toString() + "\n");
                    writer.write("Fecha de creación: " + fechaCreacion + "\n");
                    writer.flush();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Error al guardar el archivo: " + ex.getMessage());
                }
                
                // Graficar el AFN y generar la imagen (en formato PNG)
                try {
                    String rutaImagen = Graph.graficarAFN(afn1, String.valueOf(simbolo), graphPath);
                    resultArea.setText("AFN básico creado con símbolo '" + simbolo + "' y guardado en " + afnFile.getAbsolutePath() + "\nImagen generada en: " + graphPath);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Error al generar la imagen del AFN: " + ex.getMessage());
                }

                resultArea.setText("AFN básico creado con símbolo '" + simbolo + "' y guardado en " + afnFile.getAbsolutePath());
            }
        });

        // Acción: Ver lista de AFNs creados (muestra los archivos de la carpeta)
        viewAFNsButton.addActionListener(e -> {
            File[] files = directory.listFiles((dir, name) -> name.endsWith(".txt"));
            if (files == null || files.length == 0) {
                JOptionPane.showMessageDialog(frame, "No se han creado AFNs básicos aún.");
            } else {
                StringBuilder afnList = new StringBuilder("Lista de AFNs creados:\n");
                for (File file : files) {
                    afnList.append("- ").append(file.getName()).append("\n");
                }
                JOptionPane.showMessageDialog(frame, afnList.toString());
            }
        });

        // Acción: validar cadena
        validateButton.addActionListener(e -> {
            String input = inputField.getText();
            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Por favor ingrese una cadena para validar.");
                return;
            }
            for (char c : input.toCharArray()) {
                if (!currentAlphabet.contains(c)) {
                    resultArea.setText("La cadena contiene símbolos fuera del alfabeto.");
                    return;
                }
            }
            boolean isValid = afd1.validarCadena(input);
            resultArea.setText("La cadena es " + (isValid ? "aceptada" : "rechazada"));
        });

        // Acción: Unión de AFNs
        unionButton.addActionListener(e -> {
            afn1.unirAFNs(afn2);
            resultArea.setText("AFNs unidos.");
        });

        // Acción: Concatenación
        concatButton.addActionListener(e -> {
            afn1.concatenarAFNs(afn2);
            resultArea.setText("AFNs concatenados.");
        });

        // Acción: Cerradura de Kleene
        kleeneButton.addActionListener(e -> {
            afn1.cerraduraKleene();
            resultArea.setText("Cerradura de Kleene aplicada.");
        });

        // Acción: Cerradura positiva
        positiveClosureButton.addActionListener(e -> {
            afn1.cerraduraPositiva();
            resultArea.setText("Cerradura positiva aplicada.");
        });

        // Acción: Operación opcional
        optionalButton.addActionListener(e -> {
            afn1.opcional();
            resultArea.setText("Operación opcional aplicada.");
        });

        frame.setVisible(true);
    }
}
