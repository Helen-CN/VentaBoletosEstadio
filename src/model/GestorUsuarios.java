/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author helen
 */
public class GestorUsuarios 
{
    private static final String ARCHIVO_USUARIOS = "usuarios.txt";
    private Map<String, String> usuarios = new HashMap<>();

    public GestorUsuarios() 
    {
        cargarUsuarios();
    }

    private void cargarUsuarios() 
    {
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_USUARIOS))) 
        {
            String linea;
            while ((linea = reader.readLine()) != null) 
            {
                String[] partes = linea.split(",");
                if (partes.length == 3) 
                {
                    String correo = partes[1];
                    String contrasena = partes[2];
                    usuarios.put(correo, contrasena);
                }
            }
        } catch (IOException e) 
        {
            System.out.println("📂 No se encontró archivo de usuarios, se creará al registrar.");
        }
    }

    public boolean registrarUsuario(String nombre, String correo, String contrasena) 
    {
        if (usuarios.containsKey(correo)) return false; // ya existe

        try (PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO_USUARIOS, true))) 
        {
            writer.println(nombre + "," + correo + "," + contrasena);
            usuarios.put(correo, contrasena);
            return true;
        } catch (IOException e) 
        {
            System.out.println("❌ Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }
    
    public String obtenerNombre(String correo) {
    try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_USUARIOS))) {
        String linea;
        while ((linea = reader.readLine()) != null) {
            String[] partes = linea.split(",");
            if (partes.length == 3 && partes[1].equals(correo)) {
                return partes[0]; // Nombre del usuario
            }
        }
    } catch (IOException e) {
        System.out.println("Error al leer el archivo: " + e.getMessage());
    }
    return "Desconocido"; // Valor por defecto
    }

    public boolean autenticarUsuario(String correo, String contrasena) 
    {
        return usuarios.containsKey(correo) && usuarios.get(correo).equals(contrasena);
    }
}
