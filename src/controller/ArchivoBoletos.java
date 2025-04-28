package controller;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import model.Boleto;

/**
 * Clase utilitaria para guardar y cargar los boletos vendidos en archivos de texto.
 */
public class ArchivoBoletos {

    /**
     * Guarda los boletos vendidos en archivos separados por categoría.
     *
     * @param boletosPorCategoria Mapa que contiene la lista de boletos por categoría.
     */
    public static void guardar(HashMap<String, LinkedList<Boleto>> boletosPorCategoria) {
        try {
            for (String categoria : boletosPorCategoria.keySet()) {
                try (PrintWriter writer = new PrintWriter("vendidos_" + categoria + ".txt")) {
                    for (Boleto boleto : boletosPorCategoria.get(categoria)) {
                        if (boleto != null && boleto.isVendido()) { // Solo guardar boletos vendidos
                            writer.println(String.join(",",
                                boleto.getId(),
                                boleto.getCategoria(),
                                String.valueOf(boleto.getPrecio()),
                                boleto.getAsiento(),
                                "true"
                            ));
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error al guardar boletos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Carga los boletos vendidos desde archivos separados por categoría.
     *
     * @param boletosPorCategoria Mapa que contendrá los boletos cargados por categoría.
     */
    public static void cargar(HashMap<String, LinkedList<Boleto>> boletosPorCategoria) {
        try {
            for (String categoria : boletosPorCategoria.keySet()) {
                File file = new File("vendidos_" + categoria + ".txt");
                if (!file.exists() || file.length() == 0) {
                    continue; // Saltar si no existe o está vacío
                }

                try (Scanner scanner = new Scanner(file)) {
                    while (scanner.hasNextLine()) {
                        String linea = scanner.nextLine().trim();
                        if (linea.isEmpty()) {
                            continue; // Ignorar líneas vacías
                        }

                        String[] datos = linea.split(",");
                        if (datos.length != 5) {
                            System.err.println("⚠️ Línea ignorada por formato incorrecto: " + linea);
                            continue;
                        }

                        try {
                            Boleto boleto = new Boleto(
                                datos[0], // ID
                                datos[1], // Categoría
                                Double.parseDouble(datos[2]), // Precio
                                datos[3]  // Asiento
                            );
                            boleto.setVendido(true); // Todos los cargados se consideran vendidos
                            boletosPorCategoria.get(categoria).add(boleto);
                        } catch (Exception ex) {
                            System.err.println("❌ Error al procesar boleto: " + linea);
                            ex.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error general al cargar boletos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
