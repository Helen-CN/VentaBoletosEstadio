package controller;

import model.Boleto;
import java.util.LinkedList;

/**
 * Clase utilitaria para mostrar reportes de boletos vendidos por categoría.
 */
public class ReporteBoletos {

    /**
     * Muestra en consola los boletos vendidos de una categoría específica.
     *
     * @param categoria Nombre de la categoría (VIP, Preferencial, General).
     * @param vendidos  Lista de boletos vendidos en esa categoría.
     */
    public static void mostrarVendidos(String categoria, LinkedList<Boleto> vendidos) {
        System.out.println("\n===============================");
        System.out.println("📄 Reporte de Boletos Vendidos - Categoría: " + categoria);
        System.out.println("===============================");

        if (vendidos.isEmpty()) {
            System.out.println("⚠️ No hay boletos vendidos en esta categoría.");
        } else {
            for (Boleto boleto : vendidos) {
                System.out.println(boleto);
            }
        }
        System.out.println("===============================\n");
    }
}
