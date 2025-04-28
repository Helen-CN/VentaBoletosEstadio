package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase que gestiona el registro, autenticación y carga de usuarios.
 * Utiliza un archivo de texto para persistir los datos.
 */
public class GestorUsuarios {

    private static final String ARCHIVO_USUARIOS = "usuarios.txt";
    private Map<String, String> usuarios = new HashMap<>();

    /**
     * Constructor que inicializa el gestor cargando usuarios existentes.
     */
    public GestorUsuarios() {
        cargarUsuarios();
    }

    /**
     * Carga los usuarios existentes desde el archivo de almacenamiento.
     * Si el archivo no existe, se creará al registrar un nuevo usuario.
     */
    private void cargarUsuarios() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_USUARIOS))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 3) {
                    String correo = partes[1];
                    String contrasena = partes[2];
                    usuarios.put(correo, contrasena);
                }
            }
        } catch (IOException e) {
            System.out.println("📂 No se encontró archivo de usuarios, se creará automáticamente al registrar uno nuevo.");
        }
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param nombre Nombre del usuario.
     * @param correo Correo electrónico del usuario.
     * @param contrasena Contraseña del usuario.
     * @return true si el registro fue exitoso, false si el correo ya estaba registrado o hubo error.
     */
    public boolean registrarUsuario(String nombre, String correo, String contrasena) {
        if (usuarios.containsKey(correo)) {
            return false; // Usuario ya existente
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO_USUARIOS, true))) {
            writer.println(nombre + "," + correo + "," + contrasena);
            usuarios.put(correo, contrasena);
            return true;
        } catch (IOException e) {
            System.out.println("❌ Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene el nombre del usuario dado su correo electrónico.
     *
     * @param correo Correo electrónico del usuario.
     * @return Nombre del usuario o "Desconocido" si no se encuentra.
     */
    public String obtenerNombre(String correo) {
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_USUARIOS))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 3 && partes[1].equals(correo)) {
                    return partes[0]; // Retorna el nombre encontrado
                }
            }
        } catch (IOException e) {
            System.out.println("❌ Error al leer el archivo de usuarios: " + e.getMessage());
        }
        return "Desconocido"; // Valor por defecto si no se encuentra
    }

    /**
     * Verifica si las credenciales proporcionadas son correctas.
     *
     * @param correo Correo electrónico ingresado.
     * @param contrasena Contraseña ingresada.
     * @return true si las credenciales coinciden; false en caso contrario.
     */
    public boolean autenticarUsuario(String correo, String contrasena) {
        return usuarios.containsKey(correo) && usuarios.get(correo).equals(contrasena);
    }
}
