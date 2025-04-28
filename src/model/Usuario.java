package model;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Stack;

/**
 * Clase que representa un usuario del sistema de venta de boletos.
 * Cada usuario mantiene un historial de compras en una pila.
 */
public class Usuario {

    private String nombre;
    private String correo;
    private Stack<Boleto> historialCompras;

    /**
     * Constructor para crear un nuevo usuario.
     *
     * @param nombre Nombre del usuario.
     * @param correo Correo electrónico del usuario.
     */
    public Usuario(String nombre, String correo) {
        this.nombre = nombre;
        this.correo = correo;
        this.historialCompras = new Stack<>();
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    /**
     * Agrega un boleto al historial de compras del usuario.
     *
     * @param boleto Boleto comprado.
     */
    public void agregarCompra(Boleto boleto) {
        historialCompras.push(boleto);
    }

    /**
     * Deshace la última compra realizada, eliminándola del historial.
     *
     * @return El boleto eliminado o null si no hay compras.
     */
    public Boleto deshacerCompra() {
        if (!historialCompras.isEmpty()) {
            return historialCompras.pop();
        }
        return null;
    }

    public Stack<Boleto> getHistorialCompras() {
        return historialCompras;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "Nombre='" + nombre + '\'' +
                ", Correo='" + correo + '\'' +
                ", Compras Realizadas=" + historialCompras.size() +
                '}';
    }

    /**
     * Guarda el historial de compras del usuario en un archivo de texto.
     */
    public void guardarHistorialEnArchivo() {
        try {
            new File("data_usuarios").mkdirs(); // Asegura que el directorio exista

            String ruta = "data_usuarios/historial_" + correo.replace("@", "_") + ".txt";

            try (PrintWriter writer = new PrintWriter(ruta)) {
                for (Boleto b : historialCompras) {
                    if (b != null) {
                        writer.println(
                                b.getId() + "," +
                                b.getCategoria() + "," +
                                b.getPrecio() + "," +
                                b.getAsiento()
                        );
                    }
                }
            }
            System.out.println("✅ Historial de " + correo + " guardado (" + historialCompras.size() + " boletos)");
        } catch (Exception e) {
            System.err.println("❌ Error grave al guardar historial:");
            e.printStackTrace();
        }
    }

    /**
     * Carga el historial de compras del usuario desde un archivo de texto.
     */
    public void cargarHistorialDesdeArchivo() {
        try {
            String ruta = "data_usuarios/historial_" + correo.replace("@", "_") + ".txt";
            File file = new File(ruta);

            if (!file.exists()) {
                System.out.println("⚠️ No existe historial para " + correo);
                return;
            }

            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String linea = scanner.nextLine().trim();
                    if (!linea.isEmpty()) {
                        String[] datos = linea.split(",");
                        if (datos.length == 4) {
                            Boleto b = new Boleto(
                                    datos[0], datos[1],
                                    Double.parseDouble(datos[2]),
                                    datos[3]
                            );
                            b.setVendido(true);
                            historialCompras.push(b);
                        }
                    }
                }
            }
            System.out.println("📥 Historial cargado (" + historialCompras.size() + " boletos)");
        } catch (Exception e) {
            System.err.println("❌ Error grave al cargar historial:");
            e.printStackTrace();
        }
    }
}
