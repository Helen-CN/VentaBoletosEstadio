package view;

import controller.CompraController;
import controller.ForgotPasswordController;
import controller.LoginController;
import controller.SistemaBoletos;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import model.GestorUsuarios;
import model.Usuario;

/**
 * Clase principal que inicia la aplicación de venta de boletos.
 */
public class MainView extends Application {

    private Stage primaryStage;
    private SistemaBoletos sistema;
    private GestorUsuarios gestorUsuarios;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.sistema = new SistemaBoletos();
        this.gestorUsuarios = new GestorUsuarios();

        sistema.cargarEstado(); // Cargar boletos existentes
        configurarStage();
        mostrarLogin();
    }

    @Override
    public void stop() {
        if (sistema != null) {
            sistema.guardarEstado(); // Guardar estado de los boletos
        }
    }

    /**
     * Configura propiedades iniciales de la ventana principal.
     */
    private void configurarStage() {
        primaryStage.setTitle("Sistema de Venta de Boletos");
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(300);
    }

    /**
     * Cambia la escena actual mostrando el nodo raíz proporcionado.
     */
    private void cambiarEscena(Parent root) {
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Muestra la ventana de inicio de sesión.
     */
    public void mostrarLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Parent root = loader.load();

            LoginController controller = loader.getController();
            controller.setMainApp(this);
            controller.setGestorUsuarios(gestorUsuarios);

            cambiarEscena(root);
        } catch (IOException e) {
            mostrarError("Error al cargar la ventana de Login.", e);
        }
    }

    /**
     * Muestra la ventana de registro.
     */
    public void mostrarRegistro() {
        try {
            RegistroView registroView = new RegistroView(this, gestorUsuarios);
            Scene escenaRegistro = registroView.crearScene();

            if (escenaRegistro != null) {
                primaryStage.setScene(escenaRegistro);
            } else {
                throw new RuntimeException("No se pudo crear la escena de registro");
            }
        } catch (Exception e) {
            mostrarError("Error al mostrar la ventana de Registro.", e);
            mostrarLogin();
        }
    }

    /**
     * Muestra el menú principal tras iniciar sesión.
     */
    public void mostrarMenuPrincipal(Usuario usuario) {
        MenuView menuView = new MenuView(this, sistema, usuario);
        primaryStage.setScene(menuView.crearScene());
    }

    /**
     * Muestra la ventana de compra de boletos.
     */
    public void mostrarCompraBoletos(Usuario usuario) {
        try {
            if (usuario == null || usuario.getCorreo() == null) {
                System.out.println("Error: usuario invalido al intentar mostrar Compra");
                mostrarError("Usuario no valido.", new Exception("Usuario nulo o sin correo"));
                mostrarLogin();
                return;
            }
            System.out.println("Mostrando Compra para usuario: " + usuario.getCorreo());
            usuario.cargarHistorialDesdeArchivo(); // Cargar historial al mostrar la ventana
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Compra.fxml"));
            Parent root = loader.load();

            CompraController controller = loader.getController();
            controller.inicializar(this, sistema, usuario);

            cambiarEscena(root);
        } catch (IOException e) {
            mostrarError("Error al cargar la ventana de Compra.", e);
        }
    }

    /**
     * Muestra la ventana de recuperación de contraseña.
     */
    public void mostrarRecuperarContrasena() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/forgot_password.fxml"));
            Parent root = loader.load();

            ForgotPasswordController controller = loader.getController();
            controller.setMainApp(this);
            controller.setGestorUsuarios(gestorUsuarios);

            cambiarEscena(root);
        } catch (IOException e) {
            mostrarError("Error al cargar recuperacion de contrasena.", e);
        }
    }

    /**
     * Muestra una alerta en caso de error.
     */
    private void mostrarError(String mensaje, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(mensaje);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
        e.printStackTrace();
    }

    public static void main(String[] args) {
        launch(args);
    }
}