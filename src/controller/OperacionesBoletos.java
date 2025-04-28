package controller;

import model.Boleto;
import model.Usuario;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Clase utilitaria para operaciones especiales sobre boletos,
 * como deshacer la última compra realizada por un usuario.
 */
public class OperacionesBoletos {

    /**
     * Deshace la última compra realizada por un usuario.
     * Libera el asiento correspondiente y elimina el boleto de los registros de venta.
     *
     * @param usuario             Usuario que solicita deshacer la compra.
     * @param matrizAsientos      Mapa de matrices de asientos por categoría.
     * @param boletosPorCategoria Mapa de boletos vendidos organizados por categoría.
     */
    public static void deshacerUltimaCompra(Usuario usuario, 
                                            HashMap<String, boolean[][]> matrizAsientos, 
                                            HashMap<String, LinkedList<Boleto>> boletosPorCategoria) {

        if (usuario.getHistorialCompras().isEmpty()) {
            System.out.println("⚠️ No tienes compras para deshacer.");
            return;
        }

        Boleto ultimo = usuario.deshacerCompra(); // Quitar del stack de historial
        String categoria = ultimo.getCategoria();
        String asiento = ultimo.getAsiento(); // Formato ejemplo: F2C4

        // Extraer fila y columna
        int fila = Integer.parseInt(asiento.substring(1, asiento.indexOf("C"))) - 1;
        int columna = Integer.parseInt(asiento.substring(asiento.indexOf("C") + 1)) - 1;

        // Validar existencia de la matriz
        boolean[][] matriz = matrizAsientos.get(categoria);
        if (matriz != null) {
            matriz[fila][columna] = false; // Liberar asiento
        }

        // Validar existencia de lista de boletos vendidos
        LinkedList<Boleto> boletosCategoria = boletosPorCategoria.get(categoria);
        if (boletosCategoria != null) {
            boletosCategoria.removeIf(b -> b.getId().equals(ultimo.getId()));
        }

        System.out.println("✅ Se ha deshecho la última compra. Asiento liberado: " + asiento);
    }
}
