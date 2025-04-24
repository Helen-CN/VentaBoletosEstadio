package model;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Stack;

public class Usuario {

    private String nombre;
    private String correo;
    private Stack<Boleto> historialCompras;

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

    public void agregarCompra(Boleto boleto) {
        historialCompras.push(boleto);
    }

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
        return "Usuario{"
                + "Nombre='" + nombre + '\''
                + ", Correo='" + correo + '\''
                + ", Compras Realizadas=" + historialCompras.size()
                + '}';
    }

    public void guardarHistorialEnArchivo() {
        try {
            // Crear directorio si no existe
            new File("data_usuarios").mkdirs();

            // Ruta exacta del archivo
            String ruta = "data_usuarios/historial_" + correo.replace("@", "_") + ".txt";
            System.out.println("Guardando en: " + new File(ruta).getAbsolutePath());

            try (PrintWriter writer = new PrintWriter(ruta)) {
                for (Boleto b : historialCompras) {
                    if (b != null) {
                        writer.println(
                                b.getId() + ","
                                + b.getCategoria() + ","
                                + b.getPrecio() + ","
                                + b.getAsiento()
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

    public void cargarHistorialDesdeArchivo() {
        try {
            String ruta = "data_usuarios/historial_" + correo.replace("@", "_") + ".txt";
            File file = new File(ruta);

            if (!file.exists()) {
                System.out.println("⚠️ No existe historial para " + correo);
                return;
            }

            System.out.println("Cargando historial desde: " + file.getAbsolutePath());

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
