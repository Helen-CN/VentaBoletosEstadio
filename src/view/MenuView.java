package view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import controller.SistemaBoletos;
import model.Usuario;
import model.Boleto;

/**
 * Vista principal del menú para usuarios logueados.
 * Permite ver historial de compras, comprar boletos y cerrar sesión.
 */
public class MenuView {

    private final MainView mainView;
    private final SistemaBoletos sistema;
    private final Usuario usuario;

    /**
     * Constructor de la vista de menú.
     *
     * @param mainView Ventana principal de la aplicación.
     * @param sistema  Sistema de gestión de boletos.
     * @param usuario  Usuario actualmente logueado.
     */
    public MenuView(MainView mainView, SistemaBoletos sistema, Usuario usuario) {
        this.mainView = mainView;
        this.sistema = sistema;
        this.usuario = usuario;
    }

    /**
     * Crea la escena del menú principal.
     *
     * @return Objeto Scene para el menú de usuario.
     */
    public Scene crearScene() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));

        Label bienvenida = new Label("👋 Hola, " + usuario.getNombre());
        bienvenida.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button btnHistorial = new Button("📜 Ver historial de compras");
        btnHistorial.setPrefWidth(250);
        btnHistorial.setOnAction(e -> mostrarHistorial());

        Button btnComprar = new Button("🎟 Comprar boletos");
        btnComprar.setPrefWidth(250);
        btnComprar.setOnAction(e -> mainView.mostrarCompraBoletos(usuario));

        Button btnSalir = new Button("🔒 Cerrar sesión");
        btnSalir.setPrefWidth(250);
        btnSalir.setOnAction(e -> mainView.mostrarLogin());

        layout.getChildren().addAll(bienvenida, btnHistorial, btnComprar, btnSalir);

        return new Scene(layout, 400, 300);
    }

    /**
     * Muestra un historial de las compras realizadas por el usuario.
     */
    private void mostrarHistorial() {
        StringBuilder sb = new StringBuilder();
        for (Boleto b : usuario.getHistorialCompras()) {
            sb.append(b).append("\n");
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("📜 Historial de Compras");
        alert.setHeaderText("Tus boletos comprados:");
        alert.setContentText(sb.isEmpty() ? "⚠️ No has comprado boletos todavía." : sb.toString());
        alert.showAndWait();
    }
}
