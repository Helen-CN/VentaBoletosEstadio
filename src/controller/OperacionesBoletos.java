package controller;

import model.Boleto;
import model.Usuario;

import java.util.HashMap;

public class OperacionesBoletos {

    public static void deshacerUltimaCompra(Usuario usuario, HashMap<String, boolean[][]> matrizAsientos, HashMap<String, java.util.LinkedList<Boleto>> boletosPorCategoria) {
        if (usuario.getHistorialCompras().isEmpty()) {
            System.out.println("No tienes compras para deshacer.");
            return;
        }

        Boleto ultimo = usuario.getHistorialCompras().pop(); // quitar del stack
        String categoria = ultimo.getCategoria();
        String asiento = ultimo.getAsiento(); // ejemplo: F2C4

        // Extraer fila y columna
        int fila = Integer.parseInt(asiento.substring(1, asiento.indexOf("C"))) - 1;
        int columna = Integer.parseInt(asiento.substring(asiento.indexOf("C") + 1)) - 1;

        // Liberar asiento
        boolean[][] matriz = matrizAsientos.get(categoria);
        matriz[fila][columna] = false;

        // Quitar de lista de boletos vendidos
        boletosPorCategoria.get(categoria).removeIf(b -> b.getId().equals(ultimo.getId()));

        System.out.println("Se ha deshecho la última compra. Asiento liberado: " + asiento);
    }
}
