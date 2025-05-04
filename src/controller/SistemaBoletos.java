package controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;
import model.Boleto;
import model.Usuario;

/**
 * Clase que gestiona la venta de boletos para un estadio.
 * Organiza la información mediante mapas, listas enlazadas y matrices.
 */
public class SistemaBoletos {

    // Constantes para las categorías
    private static final String VIP = "VIP";
    private static final String PREFERENCIAL = "Preferencial";
    private static final String GENERAL = "General";

    private final HashMap<String, LinkedList<Boleto>> boletosPorCategoria = new HashMap<>();
    private final HashMap<String, boolean[][]> matrizAsientos = new HashMap<>();
    private final HashMap<String, Double> precios = new HashMap<>();

    /**
     * Constructor que inicializa precios, asientos y listas de boletos.
     */
    public SistemaBoletos() {
        inicializarPrecios();
        inicializarAsientos();
        inicializarListasBoletos();
    }

    private void inicializarPrecios() {
        precios.put(VIP, 150.0);
        precios.put(PREFERENCIAL, 100.0);
        precios.put(GENERAL, 50.0);
    }

    private void inicializarAsientos() {
        matrizAsientos.put(VIP, new boolean[3][5]);           // 15 asientos
        matrizAsientos.put(PREFERENCIAL, new boolean[4][6]);  // 24 asientos
        matrizAsientos.put(GENERAL, new boolean[5][8]);       // 40 asientos
    }

    private void inicializarListasBoletos() {
        boletosPorCategoria.put(VIP, new LinkedList<>());
        boletosPorCategoria.put(PREFERENCIAL, new LinkedList<>());
        boletosPorCategoria.put(GENERAL, new LinkedList<>());
    }

    /**
     * Vende un boleto en una categoría y asiento específico.
     *
     * @param categoria Categoría (VIP, Preferencial, General)
     * @param fila      Fila del asiento (inicia en 0)
     * @param columna   Columna del asiento (inicia en 0)
     * @param usuario   Usuario que compra
     * @throws IllegalArgumentException si la categoría o asiento son inválidos
     */
    public void venderBoleto(String categoria, int fila, int columna, Usuario usuario) {
        if (!matrizAsientos.containsKey(categoria)) {
            throw new IllegalArgumentException("Categoría inválida: " + categoria);
        }

        boolean[][] matriz = matrizAsientos.get(categoria);

        if (fila < 0 || fila >= matriz.length || columna < 0 || columna >= matriz[0].length) {
            throw new IllegalArgumentException("Asiento fuera de rango: Fila " + fila + ", Columna " + columna);
        }

        if (matriz[fila][columna]) {
            throw new IllegalArgumentException("El asiento F" + (fila + 1) + "C" + (columna + 1) + " ya está ocupado.");
        }

        matriz[fila][columna] = true;

        String asiento = "F" + (fila + 1) + "C" + (columna + 1);
        String id = UUID.randomUUID().toString();
        double precio = precios.getOrDefault(categoria, 0.0);

        Boleto nuevo = new Boleto(id, categoria, precio, asiento);
        nuevo.setVendido(true);

        boletosPorCategoria.get(categoria).add(nuevo);
        usuario.agregarCompra(nuevo);
    }

    /**
     * Guarda el estado actual de los boletos vendidos en archivo.
     * También imprime un resumen en consola.
     */
    public void guardarEstado() {
        ArchivoBoletos.guardar(boletosPorCategoria);

        for (Map.Entry<String, LinkedList<Boleto>> entry : boletosPorCategoria.entrySet()) {
            ReporteBoletos.mostrarVendidos(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Carga los boletos desde archivo y reconstruye las matrices de ocupación.
     */
    public void cargarEstado() {
        ArchivoBoletos.cargar(boletosPorCategoria);

        for (String categoria : boletosPorCategoria.keySet()) {
            boolean[][] matriz = matrizAsientos.get(categoria);
            if (matriz == null) continue;

            for (Boleto b : boletosPorCategoria.get(categoria)) {
                if (b.isVendido()) {
                    String asiento = b.getAsiento();  // Ej: F2C4
                    try {
                        int fila = Integer.parseInt(asiento.substring(1, asiento.indexOf("C"))) - 1;
                        int columna = Integer.parseInt(asiento.substring(asiento.indexOf("C") + 1)) - 1;
                        matriz[fila][columna] = true;
                    } catch (Exception e) {
                        System.err.println("Error al reconstruir asiento: " + asiento);
                    }
                }
            }
        }
    }

    // ================== Getters ====================

    public boolean[][] getMatriz(String categoria) {
        return matrizAsientos.get(categoria);
    }

    public HashMap<String, LinkedList<Boleto>> getBoletosPorCategoria() {
        return boletosPorCategoria;
    }

    public HashMap<String, boolean[][]> getMatrizAsientos() {
        return matrizAsientos;
    }
}
