package controller;

import animatefx.animation.*;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import model.GestorUsuarios;
import view.MainView;

public class RegisterController {

    private MainView mainApp;
    private GestorUsuarios gestor;

    @FXML private VBox registerPane;
    @FXML private ImageView logo;
    @FXML private Label titleLabel;
    @FXML private TextField nombreField;
    @FXML private TextField correoField;
    @FXML private PasswordField contrasenaField;
    @FXML private Label mensajeLabel;
    @FXML private ProgressIndicator progressIndicator;
    @FXML private Button crearCuentaButton;
    @FXML private Button volverLoginButton;

    public void setMainApp(MainView mainApp) {
        this.mainApp = mainApp;
    }

    public void setGestorUsuarios(GestorUsuarios gestor) {
        this.gestor = gestor;
    }

    @FXML
    private void initialize() {
        new ZoomIn(registerPane).setSpeed(1.5).play();

        // Configurar Tooltips en los campos
        nombreField.setTooltip(new Tooltip("Ingresa tu nombre completo"));
        correoField.setTooltip(new Tooltip("Ingresa un correo válido (ejemplo@email.com)"));
        contrasenaField.setTooltip(new Tooltip("Crea una contraseña segura"));
    }

    @FXML
    private void handleRegistrar() {
        String nombre = nombreField.getText().trim();
        String correo = correoField.getText().trim();
        String contrasena = contrasenaField.getText().trim();

        if (validarCampos(nombre, correo, contrasena)) {
            progressIndicator.setVisible(true);
            new Flash(progressIndicator).play();

            PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
            pause.setOnFinished(event -> {
                boolean registrado = gestor.registrarUsuario(nombre, correo, contrasena);
                mostrarResultado(registrado);
            });
            pause.play();
        }
    }

    private boolean validarCampos(String nombre, String correo, String contrasena) {
        if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            animarError("Todos los campos son obligatorios");
            return false;
        }
        return true;
    }

    private void mostrarResultado(boolean registrado) {
        progressIndicator.setVisible(false);

        if (registrado) {
            mostrarMensajeExito("¡Registro exitoso!");

            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(e -> mainApp.mostrarLogin());
            pause.play();
        } else {
            animarError("Correo ya registrado");
        }
    }

    private void animarError(String mensaje) {
        mensajeLabel.setText(mensaje);
        mensajeLabel.setStyle("-fx-text-fill: #e74c3c;");
        new Shake(registerPane).play();
    }

    private void mostrarMensajeExito(String mensaje) {
        mensajeLabel.setText(mensaje);
        mensajeLabel.setStyle("-fx-text-fill: #2ecc71;");
        new BounceIn(mensajeLabel).play();
    }

    @FXML
    private void handleVolver() {
        try {
            ZoomOut zoomOut = new ZoomOut(registerPane);
            zoomOut.setSpeed(1.5);
            zoomOut.setOnFinished(e -> Platform.runLater(() -> {
                if (mainApp != null) {
                    mainApp.mostrarLogin();
                } else {
                    System.err.println("Error: mainApp es null");
                }
            }));
            zoomOut.play();
        } catch (Exception ex) {
            System.err.println("Error en handleVolver: " + ex.getMessage());
            ex.printStackTrace();
            if (mainApp != null) {
                mainApp.mostrarLogin();
            }
        }
    }
}
