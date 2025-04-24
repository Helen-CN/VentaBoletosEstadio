package controller;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.UUID;
import model.Boleto;
import model.Usuario;

public class SistemaBoletos 
{
    private HashMap<String, LinkedList<Boleto>> boletosPorCategoria = new HashMap<>();
    private HashMap<String, boolean[][]> matrizAsientos = new HashMap<>();
    private HashMap<String, Double> precios = new HashMap<>();



    public  SistemaBoletos() 
    {
        // Precios por categoría
        precios.put("VIP", 150.0);
        precios.put("Preferencial", 100.0);
        precios.put("General", 50.0);

        // Inicializar matrices de asientos por categoría
        matrizAsientos.put("VIP", new boolean[3][5]);           // 3 filas, 5 columnas
        matrizAsientos.put("Preferencial", new boolean[4][6]);  // 4x6
        matrizAsientos.put("General", new boolean[5][8]);       // 5x8

        // Inicializar listas de boletos vacías
        boletosPorCategoria.put("VIP", new LinkedList<>());
        boletosPorCategoria.put("Preferencial", new LinkedList<>());
        boletosPorCategoria.put("General", new LinkedList<>());
    }

    // Vender boleto en una posición específica
    public void venderBoleto(String categoria, int fila, int columna, Usuario usuario) 
    {
        boolean[][] matriz = matrizAsientos.get(categoria);

        if (fila < 0 || fila >= matriz.length || columna < 0 || columna >= matriz[0].length)
        {
            System.out.println("Asiento fuera de rango.");
            return;
        }

        if (matriz[fila][columna]) 
        {
            System.out.println("Asiento ya está ocupado.");
            return;
        }

        matriz[fila][columna] = true;

        String asiento = "F" + (fila + 1) + "C" + (columna + 1);
        String id = UUID.randomUUID().toString();
        double precio = precios.get(categoria);

        Boleto nuevo = new Boleto(id, categoria, precio, asiento);
        nuevo.setVendido(true);

        boletosPorCategoria.get(categoria).add(nuevo);
        usuario.agregarCompra(nuevo);

        System.out.println("¡Compra exitosa! Asiento: " + asiento + ", Precio: $" + precio);
    }

    public void guardarEstado() {
        ArchivoBoletos.guardar(this.boletosPorCategoria);

        // Opcional: Generar reporte al guardar
        for (String categoria : boletosPorCategoria.keySet()) {
            ReporteBoletos.mostrarVendidos(categoria, boletosPorCategoria.get(categoria));
        }
     }
    
     public void cargarEstado() {
        ArchivoBoletos.cargar(this.boletosPorCategoria);
        
        // Reconstruye matriz de asientos ocupados
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
    public boolean[][] getMatriz(String categoria) 
    {
        return matrizAsientos.get(categoria);
    }
    
    public HashMap<String, LinkedList<Boleto>> getBoletosPorCategoria() 
    {
    return boletosPorCategoria;
    }
    
    public HashMap<String, boolean[][]> getMatrizAsientos() 
    {
        return matrizAsientos;
    }   


}
