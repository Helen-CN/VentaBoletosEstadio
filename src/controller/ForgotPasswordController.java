package controller;

import animatefx.animation.BounceInDown;
import animatefx.animation.FadeOut;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.animation.PauseTransition;
import model.GestorUsuarios;
import view.MainView;

public class ForgotPasswordController {

    private MainView mainApp;
    private GestorUsuarios gestor;

    @FXML private VBox forgotPasswordPane;
    @FXML private TextField emailField;
    @FXML private Button sendButton;
    @FXML private Button backToLoginButton;
    @FXML private Label messageLabel;

    public void setMainApp(MainView mainApp) {
        this.mainApp = mainApp;
    }

    public void setGestorUsuarios(GestorUsuarios gestor) {
        this.gestor = gestor;
    }

    @FXML
    private void initialize() {
        new BounceInDown(forgotPasswordPane).play();

        sendButton.setOnAction(e -> handleSend());
        backToLoginButton.setOnAction(e -> handleBack());
    }

    private void handleSend() {
        String correo = emailField.getText();

        if (correo.isEmpty()) {
            mostrarMensajeError("Por favor ingresa tu correo.");
            return;
        }

        if (!correo.contains("@") || !correo.contains(".")) {
            mostrarMensajeError("Correo inválido.");
            return;
        }

        mostrarMensajeExito("¡Instrucciones enviadas al correo!");

        PauseTransition pause = new PauseTransition(Duration.seconds(2.5));
        pause.setOnFinished(e -> handleBack());
        pause.play();
    }

    private void handleBack() {
        FadeOut fadeOut = new FadeOut(forgotPasswordPane);
        fadeOut.setSpeed(1.5);
        fadeOut.setOnFinished(e -> mainApp.mostrarLogin());
        fadeOut.play();
    }

    private void mostrarMensajeError(String mensaje) {
        messageLabel.setStyle("-fx-text-fill: #e74c3c;");
        messageLabel.setText(mensaje);
    }

    private void mostrarMensajeExito(String mensaje) {
        messageLabel.setStyle("-fx-text-fill: #2ecc71;");
        messageLabel.setText(mensaje);
    }
}
