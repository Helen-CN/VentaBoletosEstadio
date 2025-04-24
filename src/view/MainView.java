package view;

import controller.SistemaBoletos;
import javafx.application.Application;
import javafx.stage.Stage;
import model.GestorUsuarios;
import model.Usuario;

public class MainView extends Application {

    private Stage primaryStage;
    private SistemaBoletos sistema;
    private GestorUsuarios gestor;

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
            sistema.guardarEstado(); // Usa tu ArchivoBoletos existente
        }
        // El guardado de usuarios sigue igual
    }

    private void configurarStage() {
        primaryStage.setTitle("Sistema de Venta de Boletos");
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(300);
    }

    public void mostrarLogin() {
        primaryStage.setScene(new LoginView(this, gestor).crearScene());
        primaryStage.show();
    }

    public void mostrarRegistro() {
        primaryStage.setScene(new RegistroView(this, gestor).crearScene());
    }

    public void mostrarMenuPrincipal(Usuario usuario) {
        primaryStage.setScene(new MenuView(this, sistema, usuario).crearScene());
    }

    public void mostrarCompraBoletos(Usuario usuario) {
        primaryStage.setScene(new CompraView(this, sistema, usuario).crearScene());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
