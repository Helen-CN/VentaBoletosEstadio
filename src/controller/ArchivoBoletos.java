package controller;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import model.Boleto;

public class ArchivoBoletos {

    public static void guardar(HashMap<String, LinkedList<Boleto>> boletosPorCategoria) {
        try {
            for (String categoria : boletosPorCategoria.keySet()) {
                PrintWriter writer = new PrintWriter("vendidos_" + categoria + ".txt");
                for (Boleto boleto : boletosPorCategoria.get(categoria)) {
                    if (boleto != null && boleto.isVendido()) {  // Solo guarda boletos vendidos
                        writer.println(String.join(",",
                            boleto.getId(),
                            boleto.getCategoria(),
                            String.valueOf(boleto.getPrecio()),
                            boleto.getAsiento(),
                            "true" // Siempre vendido
                        ));
                    }
                }
                writer.close();
            }
        } catch (Exception e) {
            System.err.println("Error al guardar boletos: " + e.getMessage());
        }
    }

    public static void cargar(HashMap<String, LinkedList<Boleto>> boletosPorCategoria) {
        try {
            for (String categoria : boletosPorCategoria.keySet()) {
                File file = new File("vendidos_" + categoria + ".txt");
                if (!file.exists() || file.length() == 0) continue;  // Saltar si no existe o está vacío

                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    String linea = scanner.nextLine().trim();
                    if (linea.isEmpty()) continue;  // Ignorar líneas vacías

                    String[] datos = linea.split(",");
                    if (datos.length != 5) {  // Validar estructura
                        System.err.println("Línea ignorada (formato incorrecto): " + linea);
                        continue;
                    }

                    try {
                        Boleto boleto = new Boleto(
                            datos[0], // ID
                            datos[1], // Categoría
                            Double.parseDouble(datos[2]), // Precio
                            datos[3]  // Asiento
                        );
                        boleto.setVendido(true); // Todos los cargados están vendidos
                        boletosPorCategoria.get(categoria).add(boleto);
                    } catch (Exception e) {
                        System.err.println("Error al procesar boleto: " + linea);
                        e.printStackTrace();
                    }
                }
                scanner.close();
            }
        } catch (Exception e) {
            System.err.println("Error al cargar boletos: " + e.getMessage());
        }
    }
}