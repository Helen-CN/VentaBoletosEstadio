package view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import controller.SistemaBoletos;
import model.Usuario;
import model.Boleto;

public class MenuView {
    private final MainView mainView;
    private final SistemaBoletos sistema;
    private final Usuario usuario;

    public MenuView(MainView mainView, SistemaBoletos sistema, Usuario usuario) {
        this.mainView = mainView;
        this.sistema = sistema;
        this.usuario = usuario;
    }

    public Scene crearScene() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Button btnHistorial = new Button("📜 Ver historial");
        btnHistorial.setOnAction(e -> mostrarHistorial());

        Button btnComprar = new Button("🎟 Comprar boleto");
        btnComprar.setOnAction(e -> mainView.mostrarCompraBoletos(usuario));

        Button btnSalir = new Button("Cerrar sesión");
        btnSalir.setOnAction(e -> mainView.mostrarLogin());

        layout.getChildren().addAll(
            new Label("Hola, " + usuario.getCorreo()),
            btnHistorial, btnComprar, btnSalir
        );

        return new Scene(layout, 400, 250);
    }

    private void mostrarHistorial() {
        StringBuilder sb = new StringBuilder();
        for (Boleto b : usuario.getHistorialCompras()) {
            sb.append(b).append("\n");
        }
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Historial de Compras");
        alert.setHeaderText("Tus boletos:");
        alert.setContentText(sb.toString());
        alert.showAndWait();
    }
}