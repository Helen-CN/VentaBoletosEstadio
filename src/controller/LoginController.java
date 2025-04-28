package controller;

import animatefx.animation.BounceInDown;
import animatefx.animation.FadeIn;
import animatefx.animation.FadeOut;
import animatefx.animation.Shake;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
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
    @FXML private Button loginButton;
    @FXML private StackPane loaderPane;
    @FXML private ProgressIndicator loadingSpinner;
    


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
        new FadeIn(loginPane).setDelay(Duration.seconds(0.3)).play();

        forgotPasswordLink.setOnAction(event -> handleForgotPassword());
        registerButton.setOnAction(event -> handleRegister());

        // Hacer login al presionar ENTER
        usernameField.setOnAction(e -> handleLogin());
        passwordField.setOnAction(e -> handleLogin());
        loginButton.setOnAction(e -> handleLogin());
    }

    /**
     * Maneja la acción de iniciar sesión.
     * Valida credenciales e inicia sesión o muestra error.
     */
    @FXML
    private void handleLogin() {
        clearFieldStyles();

        String correo = usernameField.getText();
        String contrasena = passwordField.getText();

        if (correo.isEmpty() || contrasena.isEmpty()) {
            if (correo.isEmpty()) usernameField.setStyle("-fx-border-color: red;");
            if (contrasena.isEmpty()) passwordField.setStyle("-fx-border-color: red;");
            mostrarMensajeError("Por favor llena todos los campos.");
            new Shake(loginPane).play();
            return;
        }

        // Mostrar loader con fade in
        loaderPane.setVisible(true);
        new FadeIn(loaderPane).setSpeed(1.5).play();

        // Simular proceso de carga
        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
        pause.setOnFinished(e -> {
            // Ocultar loader con fade out
            FadeOut fadeOut = new FadeOut(loaderPane);
            fadeOut.setSpeed(1.5);
            fadeOut.setOnFinished(event -> loaderPane.setVisible(false));
            fadeOut.play();

            // Después de la carga, verificar login
            if (gestor.autenticarUsuario(correo, contrasena)) {
                String nombre = gestor.obtenerNombre(correo);
                Usuario usuario = new Usuario(nombre, correo);
                usuario.cargarHistorialDesdeArchivo();

                mostrarMensajeExito("¡Bienvenido, " + nombre + "!");
                mainApp.mostrarMenuPrincipal(usuario);
            } else {
                mostrarMensajeError("Credenciales incorrectas.");
                new Shake(loginPane).play();
            }
        });
        pause.play();
    }



    /**
     * Maneja la acción al presionar "¿Olvidaste tu contraseña?".
     */
    private void handleForgotPassword() {
    mainApp.mostrarRecuperarContrasena();
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
        ocultarMensajeAutomatico();
    }

    private void mostrarMensajeError(String mensaje) {
        messageLabel.setStyle("-fx-text-fill: #e74c3c;");
        messageLabel.setText(mensaje);
        ocultarMensajeAutomatico();
    }

    private void mostrarMensajeAdvertencia(String mensaje) {
        messageLabel.setStyle("-fx-text-fill: #f39c12;");
        messageLabel.setText(mensaje);
        ocultarMensajeAutomatico();
    }

    private void ocultarMensajeAutomatico() {
        PauseTransition pause = new PauseTransition(Duration.seconds(4));
        pause.setOnFinished(e -> messageLabel.setText(""));
        pause.play();
    }

    private void clearFieldStyles() {
        usernameField.setStyle(null);
        passwordField.setStyle(null);
    }
}
