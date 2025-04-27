package controller;

import animatefx.animation.BounceInDown;
import animatefx.animation.FadeIn;
import animatefx.animation.FadeOut;
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
    @FXML private VBox loginPane;
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
        // Animaciones al abrir
        new BounceInDown(loginPane).play();
        new FadeIn(loginPane).setDelay(javafx.util.Duration.seconds(0.3)).play();

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
            usuario.cargarHistorialDesdeArchivo();

            messageLabel.setStyle("-fx-text-fill: #2ecc71;");
            messageLabel.setText("¡Bienvenido, " + nombre + "!");
            mainApp.mostrarMenuPrincipal(usuario);

        } else {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Credenciales incorrectas.");
            new Shake(loginPane).play(); // Vibración de error
        }
    }

    private void handleForgotPassword() {
        System.out.println("Olvidaste tu contraseña clickeado...");
        messageLabel.setStyle("-fx-text-fill: #f39c12;");
        messageLabel.setText("Recuperación de contraseña no disponible aún.");
    }

    @FXML
    private void handleRegister() {
        // Animación de salida
        FadeOut fadeOut = new FadeOut(loginPane);
        fadeOut.setSpeed(1.5);
        fadeOut.setOnFinished(e -> {
            mainApp.mostrarRegistro();
        });
        fadeOut.play();
    }
}
