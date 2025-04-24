package view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.GestorUsuarios;
import model.Usuario;

public class LoginView {
    private final MainView mainView;
    private final GestorUsuarios gestor;

    public LoginView(MainView mainView, GestorUsuarios gestor) {
        this.mainView = mainView;
        this.gestor = gestor;
    }

    public Scene crearScene() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        TextField txtCorreo = new TextField();
        txtCorreo.setPromptText("Correo");

        PasswordField txtContrasena = new PasswordField();
        txtContrasena.setPromptText("Contraseña");

        Label lblMensaje = new Label();

        Button btnLogin = new Button("Iniciar Sesión");
        btnLogin.setOnAction(e -> {
            String correo = txtCorreo.getText().trim();
            String contrasena = txtContrasena.getText().trim();

            if (gestor.autenticarUsuario(correo, contrasena)) {
                Usuario usuario = new Usuario("Desconocido", correo);
                usuario.cargarHistorialDesdeArchivo();
                mainView.mostrarMenuPrincipal(usuario);
            } else {
                lblMensaje.setText("❌ Correo o contraseña incorrectos.");
            }
        });

        Button btnIrARegistro = new Button("Registrarse");
        btnIrARegistro.setOnAction(e -> mainView.mostrarRegistro());

        layout.getChildren().addAll(
            new Label("🎟️ Sistema de Boletos - Login"),
            txtCorreo, txtContrasena, btnLogin, btnIrARegistro, lblMensaje
        );

        return new Scene(layout, 400, 300);
    }
}