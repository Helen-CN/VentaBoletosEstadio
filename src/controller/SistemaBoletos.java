package controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;
import model.Boleto;
import model.Usuario;

/**
 * Clase que gestiona la venta de boletos para un estadio.
 * Utiliza estructuras de datos como listas enlazadas, matrices y mapas para organizar la información.
 */
public class SistemaBoletos {

    private HashMap<String, LinkedList<Boleto>> boletosPorCategoria = new HashMap<>();
    private HashMap<String, boolean[][]> matrizAsientos = new HashMap<>();
    private HashMap<String, Double> precios = new HashMap<>();

    /**
     * Constructor que inicializa las categorías de boletos, precios y matrices de asientos.
     */
    public SistemaBoletos() {
        inicializarPrecios();
        inicializarAsientos();
        inicializarListasBoletos();
    }

    private void inicializarPrecios() {
        precios.put("VIP", 150.0);
        precios.put("Preferencial", 100.0);
        precios.put("General", 50.0);
    }

    private void inicializarAsientos() {
        matrizAsientos.put("VIP", new boolean[3][5]);
        matrizAsientos.put("Preferencial", new boolean[4][6]);
        matrizAsientos.put("General", new boolean[5][8]);
    }

    private void inicializarListasBoletos() {
        boletosPorCategoria.put("VIP", new LinkedList<>());
        boletosPorCategoria.put("Preferencial", new LinkedList<>());
        boletosPorCategoria.put("General", new LinkedList<>());
    }

    /**
     * Vende un boleto en una posición específica de la categoría indicada.
     *
     * @param categoria Categoría del boleto.
     * @param fila Fila del asiento.
     * @param columna Columna del asiento.
     * @param usuario Usuario que realiza la compra.
     * @throws IllegalArgumentException si el asiento no está disponible o fuera de rango.
     */
    public void venderBoleto(String categoria, int fila, int columna, Usuario usuario) {
        boolean[][] matriz = matrizAsientos.get(categoria);

        if (matriz == null) {
            throw new IllegalArgumentException("Categoría inválida.");
        }
        if (fila < 0 || fila >= matriz.length || columna < 0 || columna >= matriz[0].length) {
            throw new IllegalArgumentException("Asiento fuera de rango.");
        }
        if (matriz[fila][columna]) {
            throw new IllegalArgumentException("El asiento ya está ocupado.");
        }

        matriz[fila][columna] = true;

        String asiento = "F" + (fila + 1) + "C" + (columna + 1);
        String id = UUID.randomUUID().toString();
        double precio = precios.get(categoria);

        Boleto nuevo = new Boleto(id, categoria, precio, asiento);
        nuevo.setVendido(true);

        boletosPorCategoria.get(categoria).add(nuevo);
        usuario.agregarCompra(nuevo);
    }

    /**
     * Guarda el estado actual de los boletos vendidos.
     */
    public void guardarEstado() {
        ArchivoBoletos.guardar(this.boletosPorCategoria);

        // Mostrar un resumen en consola (opcional)
        for (String categoria : boletosPorCategoria.keySet()) {
            ReporteBoletos.mostrarVendidos(categoria, boletosPorCategoria.get(categoria));
        }
    }

    /**
     * Carga el estado de los boletos vendidos desde archivo.
     * También reconstruye la matriz de asientos ocupados.
     */
    public void cargarEstado() {
        ArchivoBoletos.cargar(this.boletosPorCategoria);

        for (String categoria : boletosPorCategoria.keySet()) {
            for (Boleto b : boletosPorCategoria.get(categoria)) {
                if (b.isVendido()) {
                    String asiento = b.getAsiento();
                    int fila = Integer.parseInt(asiento.substring(1, asiento.indexOf("C"))) - 1;
                    int columna = Integer.parseInt(asiento.substring(asiento.indexOf("C") + 1)) - 1;
                    matrizAsientos.get(categoria)[fila][columna] = true;
                }
            }
        }
    }

    /**
     * Devuelve la matriz de asientos de una categoría específica.
     *
     * @param categoria Categoría solicitada.
     * @return Matriz de asientos ocupados/disponibles.
     */
    public boolean[][] getMatriz(String categoria) {
        return matrizAsientos.get(categoria);
    }

    /**
     * Devuelve el mapa de boletos organizados por categoría.
     *
     * @return Mapa de boletos.
     */
    public HashMap<String, LinkedList<Boleto>> getBoletosPorCategoria() {
        return boletosPorCategoria;
    }

    /**
     * Devuelve el mapa de matrices de asientos.
     *
     * @return Mapa de matrices.
     */
    public HashMap<String, boolean[][]> getMatrizAsientos() {
        return matrizAsientos;
    }
}
