package view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.GestorUsuarios;

public class RegistroView {
    private final MainView mainView;
    private final GestorUsuarios gestor;

    public RegistroView(MainView mainView, GestorUsuarios gestor) {
        this.mainView = mainView;
        this.gestor = gestor;
    }

    public Scene crearScene() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre");

        TextField txtCorreo = new TextField();
        txtCorreo.setPromptText("Correo");

        PasswordField txtContrasena = new PasswordField();
        txtContrasena.setPromptText("Contraseña");

        Label lblMensaje = new Label();

        Button btnRegistrar = new Button("Registrar");
        btnRegistrar.setOnAction(e -> {
            String nombre = txtNombre.getText().trim();
            String correo = txtCorreo.getText().trim();
            String contrasena = txtContrasena.getText().trim();

            if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
                lblMensaje.setText("⚠️ Todos los campos son obligatorios.");
            } else {
                boolean registrado = gestor.registrarUsuario(nombre, correo, contrasena);
                lblMensaje.setText(registrado ? 
                    "✅ ¡Registro exitoso! Ahora puedes iniciar sesión." : 
                    "❌ Este correo ya está registrado.");
            }
        });

        Button btnVolver = new Button("Volver al Login");
        btnVolver.setOnAction(e -> mainView.mostrarLogin());

        layout.getChildren().addAll(
            new Label("📝 Registro de Usuario"),
            txtNombre, txtCorreo, txtContrasena, btnRegistrar, btnVolver, lblMensaje
        );

        return new Scene(layout, 400, 300);
    }
}