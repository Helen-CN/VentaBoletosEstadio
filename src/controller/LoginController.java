package controller;

import animatefx.animation.BounceInDown;
import animatefx.animation.Shake;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.GestorUsuarios;
import model.Usuario;
import view.MainView;

public class LoginController {

    private MainView mainApp;
    private GestorUsuarios gestor;

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;
    @FXML private VBox loginPane; // Importante para animaciones
    @FXML private Hyperlink forgotPasswordLink;
    @FXML private Button registerButton;

    public void setMainApp(MainView mainApp) {
        this.mainApp = mainApp;
    }

    public void setGestorUsuarios(GestorUsuarios gestor) {
        this.gestor = gestor;
    }

    @FXML
    private void initialize() {
        // Animación de entrada al abrir la ventana
        new BounceInDown(loginPane).play();

        // Acciones de los botones/enlaces adicionales
        forgotPasswordLink.setOnAction(event -> handleForgotPassword());
        registerButton.setOnAction(event -> handleRegister());
    }

    @FXML
    private void handleLogin() {
        String correo = usernameField.getText();
        String contrasena = passwordField.getText();

        if (gestor.autenticarUsuario(correo, contrasena)) {
            String nombre = gestor.obtenerNombre(correo);
            Usuario usuario = new Usuario(nombre, correo);
            usuario.cargarHistorialDesdeArchivo(); // si deseas cargar el historial aquí

            messageLabel.setStyle("-fx-text-fill: #2ecc71;"); // Verde éxito
            messageLabel.setText("¡Bienvenido, " + nombre + "!");

            mainApp.mostrarMenuPrincipal(usuario); // pasa al menú principal
        } else {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Credenciales incorrectas.");
            new Shake(loginPane).play(); // ¡Animación de error!
        }
    }

    private void handleForgotPassword() {
        // Aquí puedes abrir una ventana nueva o mostrar un popup
        System.out.println("Olvidaste tu contraseña clickeado...");
        messageLabel.setStyle("-fx-text-fill: #f39c12;");
        messageLabel.setText("Recuperación de contraseña no disponible aún.");
    }
    @FXML
    private void handleRegister() {
        // Aquí abrirías el formulario de registro
        System.out.println("Botón de registrarse clickeado...");
        messageLabel.setStyle("-fx-text-fill: #3498db;");
        messageLabel.setText("Función de registro en construcción.");
    }
}
