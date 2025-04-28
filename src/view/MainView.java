package view;

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
    private GestorUsuarios gestor;
    private GestorUsuarios gestorUsuarios; // atributo global


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.sistema = new SistemaBoletos();
        this.gestor = new GestorUsuarios();

        sistema.cargarEstado(); // Carga los boletos existentes
        configurarStage();
        mostrarLogin();
    }

    @Override
    public void stop() {
        if (sistema != null) {
            sistema.guardarEstado(); // Guarda el estado de los boletos al cerrar
        }
    }

    /**
     * Configura las propiedades iniciales del Stage principal.
     */
    private void configurarStage() {
        primaryStage.setTitle("Sistema de Venta de Boletos");
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(300);
    }

    /**
     * Cambia la escena actual mostrando el nodo raíz proporcionado.
     * @param root Nodo raíz a mostrar en la ventana principal.
     */
    private void cambiarEscena(Parent root) {
        primaryStage.setScene(new Scene(root));
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
            controller.setGestorUsuarios(gestor);

            cambiarEscena(root);
        } catch (IOException e) {
            mostrarError("Error al cargar la ventana de Login.", e);
        }
    }

    /**
     * Muestra la ventana de registro de nuevos usuarios.
     */
    public void mostrarRegistro() {
        try {
            RegistroView registroView = new RegistroView(this, gestor);
            Scene escenaRegistro = registroView.crearScene();

            if (escenaRegistro != null) {
                primaryStage.setScene(escenaRegistro);
            } else {
                throw new RuntimeException("No se pudo crear la escena de registro");
            }
        } catch (Exception e) {
            mostrarError("Error al mostrar la ventana de Registro.", e);
            mostrarLogin(); // Regresar a login si falla
        }
    }

    /**
     * Muestra el menú principal tras iniciar sesión.
     * @param usuario Usuario autenticado.
     */
    public void mostrarMenuPrincipal(Usuario usuario) {
        primaryStage.setScene(new MenuView(this, sistema, usuario).crearScene());
    }

    /**
     * Muestra la ventana de compra de boletos.
     * @param usuario Usuario autenticado.
     */
    public void mostrarCompraBoletos(Usuario usuario) {
        primaryStage.setScene(new CompraView(this, sistema, usuario).crearScene());
    }

    /**
     * Muestra una alerta de error en caso de excepción.
     * @param mensaje Mensaje principal del error.
     * @param e Excepción capturada.
     */
    private void mostrarError(String mensaje, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(mensaje);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
        e.printStackTrace();
    }
    
    public void mostrarRecuperarContrasena() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/forgot_password.fxml"));
            Parent root = loader.load();

            ForgotPasswordController controller = loader.getController();
            controller.setMainApp(this);
            controller.setGestorUsuarios(gestorUsuarios); // si necesitas el gestor

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        launch(args);
    }
}
