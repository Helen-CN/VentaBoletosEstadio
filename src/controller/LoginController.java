package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.GestorUsuarios;
import model.Usuario;
import view.MainView;

public class LoginController {

    private MainView mainApp;
    private GestorUsuarios gestor;

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    public void setMainApp(MainView mainApp) {
        this.mainApp = mainApp;
    }

    public void setGestorUsuarios(GestorUsuarios gestor) {
        this.gestor = gestor;
    }

    @FXML
    private void handleLogin() {
        String correo = usernameField.getText();
        String contrasena = passwordField.getText();

        if (gestor.autenticarUsuario(correo, contrasena)) {
            String nombre = gestor.obtenerNombre(correo);
            Usuario usuario = new Usuario(nombre, correo);
            usuario.cargarHistorialDesdeArchivo(); // si deseas cargar el historial aquí

            messageLabel.setText("¡Bienvenido, " + nombre + "!");
            mainApp.mostrarMenuPrincipal(usuario); // pasa al menú principal
        } else {
            messageLabel.setText("Credenciales incorrectas.");
        }
    }
}
