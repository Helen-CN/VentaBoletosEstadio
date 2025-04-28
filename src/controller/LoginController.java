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

/**
 * Controlador para la ventana de inicio de sesión.
 * Gestiona el login, navegación al registro y animaciones de la interfaz.
 */
public class LoginController {

    private MainView mainApp;
    private GestorUsuarios gestor;

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;
    @FXML private VBox loginPane;
    @FXML private Hyperlink forgotPasswordLink;
    @FXML private Button registerButton;

    /**
     * Establece la referencia al MainView principal.
     *
     * @param mainApp Objeto principal de la aplicación.
     */
    public void setMainApp(MainView mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Establece el gestor de usuarios.
     *
     * @param gestor Gestor de autenticación y manejo de usuarios.
     */
    public void setGestorUsuarios(GestorUsuarios gestor) {
        this.gestor = gestor;
    }

    /**
     * Inicializa los componentes gráficos y animaciones al cargar la vista.
     */
    @FXML
    private void initialize() {
        new BounceInDown(loginPane).play();
        new FadeIn(loginPane).setDelay(javafx.util.Duration.seconds(0.3)).play();

        forgotPasswordLink.setOnAction(event -> handleForgotPassword());
        registerButton.setOnAction(event -> handleRegister());
    }

    /**
     * Maneja la acción de iniciar sesión.
     * Valida credenciales e inicia sesión o muestra error.
     */
    @FXML
    private void handleLogin() {
        String correo = usernameField.getText();
        String contrasena = passwordField.getText();

        if (gestor.autenticarUsuario(correo, contrasena)) {
            String nombre = gestor.obtenerNombre(correo);
            Usuario usuario = new Usuario(nombre, correo);
            usuario.cargarHistorialDesdeArchivo();

            mostrarMensajeExito("¡Bienvenido, " + nombre + "!");
            mainApp.mostrarMenuPrincipal(usuario);
        } else {
            mostrarMensajeError("Credenciales incorrectas.");
            new Shake(loginPane).play(); // Animación de vibración por error
        }
    }

    /**
     * Maneja la acción al presionar "¿Olvidaste tu contraseña?".
     */
    private void handleForgotPassword() {
        System.out.println("Olvidaste tu contraseña clickeado...");
        mostrarMensajeAdvertencia("Recuperación de contraseña no disponible aún.");
    }

    /**
     * Maneja la acción de cambiar a la ventana de registro.
     */
    @FXML
    private void handleRegister() {
        FadeOut fadeOut = new FadeOut(loginPane);
        fadeOut.setSpeed(1.5);
        fadeOut.setOnFinished(e -> mainApp.mostrarRegistro());
        fadeOut.play();
    }

    // Métodos de ayuda para mostrar mensajes
    private void mostrarMensajeExito(String mensaje) {
        messageLabel.setStyle("-fx-text-fill: #2ecc71;");
        messageLabel.setText(mensaje);
    }

    private void mostrarMensajeError(String mensaje) {
        messageLabel.setStyle("-fx-text-fill: red;");
        messageLabel.setText(mensaje);
    }

    private void mostrarMensajeAdvertencia(String mensaje) {
        messageLabel.setStyle("-fx-text-fill: #f39c12;");
        messageLabel.setText(mensaje);
    }
}
