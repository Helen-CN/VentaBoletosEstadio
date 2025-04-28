package controller;

import animatefx.animation.*;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import model.GestorUsuarios;
import view.MainView;

/**
 * Controlador para la ventana de registro de nuevos usuarios.
 * Gestiona la validación, registro de usuarios y animaciones.
 */
public class RegisterController {

    private MainView mainApp;
    private GestorUsuarios gestor;

    @FXML private VBox registerPane;
    @FXML private TextField nombreField;
    @FXML private TextField correoField;
    @FXML private PasswordField contrasenaField;
    @FXML private Label mensajeLabel;
    @FXML private ProgressIndicator progressIndicator;

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
     * @param gestor Gestor de registro y autenticación de usuarios.
     */
    public void setGestorUsuarios(GestorUsuarios gestor) {
        this.gestor = gestor;
    }

    /**
     * Inicializa los componentes gráficos y animaciones al cargar la vista.
     */
    @FXML
    private void initialize() {
        new ZoomIn(registerPane).setSpeed(1.5).play();
    }

    /**
     * Maneja el proceso de registro de un nuevo usuario.
     * Valida campos y simula el registro de manera asíncrona.
     */
    @FXML
    private void handleRegistrar() {
        String nombre = nombreField.getText().trim();
        String correo = correoField.getText().trim();
        String contrasena = contrasenaField.getText().trim();

        if (validarCampos(nombre, correo, contrasena)) {
            progressIndicator.setVisible(true);
            new Flash(progressIndicator).play();

            PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
            pause.setOnFinished(e -> {
                boolean registrado = gestor.registrarUsuario(nombre, correo, contrasena);
                mostrarResultado(registrado);
            });
            pause.play();
        }
    }

    /**
     * Valida que todos los campos requeridos estén completos.
     *
     * @param nombre Nombre ingresado.
     * @param correo Correo ingresado.
     * @param contrasena Contraseña ingresada.
     * @return true si todos los campos están correctos; false en caso contrario.
     */
    private boolean validarCampos(String nombre, String correo, String contrasena) {
        if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            animarError("⚠️ Todos los campos son obligatorios");
            return false;
        }
        return true;
    }

    /**
     * Muestra el resultado del intento de registro.
     *
     * @param registrado true si el registro fue exitoso; false si falló.
     */
    private void mostrarResultado(boolean registrado) {
        progressIndicator.setVisible(false);

        if (registrado) {
            mostrarMensajeExito("✅ ¡Registro exitoso!");

            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(e -> mainApp.mostrarLogin());
            pause.play();
        } else {
            animarError("❌ Correo ya registrado");
        }
    }

    /**
     * Muestra un mensaje de error animado.
     *
     * @param mensaje Texto del error.
     */
    private void animarError(String mensaje) {
        mensajeLabel.setText(mensaje);
        mensajeLabel.setStyle("-fx-text-fill: #e74c3c;");
        new Shake(registerPane).play();
    }

    /**
     * Muestra un mensaje de éxito animado.
     *
     * @param mensaje Texto de éxito.
     */
    private void mostrarMensajeExito(String mensaje) {
        mensajeLabel.setText(mensaje);
        mensajeLabel.setStyle("-fx-text-fill: #2ecc71;");
        new BounceIn(mensajeLabel).play();
    }

    /**
     * Maneja la acción de volver a la pantalla de login.
     */
    @FXML
    private void handleVolver() {
        try {
            if (registerPane == null) {
                throw new IllegalStateException("El panel de registro no fue inicializado");
            }

            ZoomOut zoomOut = new ZoomOut(registerPane);
            zoomOut.setSpeed(1.5);
            zoomOut.setOnFinished(e -> {
                if (mainApp != null) {
                    Platform.runLater(() -> mainApp.mostrarLogin());
                } else {
                    System.err.println("Error: mainApp es null");
                }
            });
            zoomOut.play();

        } catch (Exception e) {
            System.err.println("Error en handleVolver: " + e.getMessage());
            e.printStackTrace();

            if (mainApp != null) {
                mainApp.mostrarLogin();
            }
        }
    }
}
