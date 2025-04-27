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

public class RegisterController {
    private MainView mainApp;
    private GestorUsuarios gestor;

    @FXML private VBox registerPane;
    @FXML private TextField nombreField;
    @FXML private TextField correoField;
    @FXML private PasswordField contrasenaField;
    @FXML private Label mensajeLabel;
    @FXML private ProgressIndicator progressIndicator;

    public void setMainApp(MainView mainApp) {
        this.mainApp = mainApp;
    }

    public void setGestorUsuarios(GestorUsuarios gestor) {
        this.gestor = gestor;
    }

    @FXML
    private void initialize() {
        new ZoomIn(registerPane).setSpeed(1.5).play();
    }

    @FXML
    private void handleRegistrar() {
        String nombre = nombreField.getText().trim();
        String correo = correoField.getText().trim();
        String contrasena = contrasenaField.getText().trim();

        if (validarCampos(nombre, correo, contrasena)) {
            progressIndicator.setVisible(true);
            new Flash(progressIndicator).play();

            // Simulamos operación asíncrona
            PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
            pause.setOnFinished(e -> {
                boolean registrado = gestor.registrarUsuario(nombre, correo, contrasena);
                mostrarResultado(registrado);
            });
            pause.play();
        }
    }

    private boolean validarCampos(String nombre, String correo, String contrasena) {
        if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            animarError("⚠️ Todos los campos son obligatorios");
            return false;
        }
        return true;
    }

    private void mostrarResultado(boolean registrado) {
        progressIndicator.setVisible(false);
        
        if (registrado) {
            mensajeLabel.setText("✅ ¡Registro exitoso!");
            mensajeLabel.setStyle("-fx-text-fill: #2ecc71;");
            new BounceIn(mensajeLabel).play();
            
            // Volver al login después de 2 segundos
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(e -> mainApp.mostrarLogin());
            pause.play();
        } else {
            animarError("❌ Correo ya registrado");
        }
    }

    private void animarError(String mensaje) {
        mensajeLabel.setText(mensaje);
        mensajeLabel.setStyle("-fx-text-fill: #e74c3c;");
        new Shake(registerPane).play();
    }

    @FXML
    private void handleVolver() {
        try {
            // 1. Verifica que los elementos esenciales existan
            if (registerPane == null) {
                throw new IllegalStateException("El panel de registro no fue inicializado");
            }

            // 2. Crea y configura la animación
            ZoomOut zoomOut = new ZoomOut(registerPane);
            zoomOut.setSpeed(1.5);

            // 3. Maneja el fin de la animación
            zoomOut.setOnFinished(e -> {
                if (mainApp != null) {
                    Platform.runLater(() -> mainApp.mostrarLogin());
                } else {
                    System.err.println("Error: mainApp es null");
                }
            });

            // 4. Ejecuta la animación
            zoomOut.play();

        } catch (Exception e) {
            System.err.println("Error en handleVolver: " + e.getMessage());
            e.printStackTrace();

            // Fallback: Volver sin animación si hay error
            if (mainApp != null) {
                mainApp.mostrarLogin();
            }
        }
    }
}
