package view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import controller.SistemaBoletos;
import model.Usuario;

public class CompraView {
    private final MainView mainView;
    private final SistemaBoletos sistema;
    private final Usuario usuario;

    public CompraView(MainView mainView, SistemaBoletos sistema, Usuario usuario) {
        this.mainView = mainView;
        this.sistema = sistema;
        this.usuario = usuario;
    }

    public Scene crearScene() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        ComboBox<String> comboCategoria = new ComboBox<>();
        comboCategoria.getItems().addAll("VIP", "Preferencial", "General");
        comboCategoria.setPromptText("Selecciona una categoría");

        GridPane gridAsientos = new GridPane();
        gridAsientos.setHgap(5);
        gridAsientos.setVgap(5);
        gridAsientos.setPadding(new Insets(10));

        comboCategoria.setOnAction(e -> {
            String categoria = comboCategoria.getValue();
            if (categoria != null) {
                actualizarAsientos(gridAsientos, categoria);
            }
        });

        Button btnVolver = new Button("Volver");
        btnVolver.setOnAction(e -> mainView.mostrarMenuPrincipal(usuario));

        layout.getChildren().addAll(
            new Label("Compra de Boletos"),
            comboCategoria, gridAsientos, btnVolver
        );

        return new Scene(layout, 600, 600);
    }

    private void actualizarAsientos(GridPane grid, String categoria) {
        grid.getChildren().clear();
        boolean[][] matriz = sistema.getMatriz(categoria);

        for (int fila = 0; fila < matriz.length; fila++) {
            for (int col = 0; col < matriz[fila].length; col++) {
                Button btn = new Button("F" + (fila + 1) + "C" + (col + 1));
                btn.setPrefSize(70, 40);

                if (matriz[fila][col]) {
                    btn.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                    btn.setDisable(true);
                } else {
                    btn.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                    int f = fila;
                    int c = col;
                    btn.setOnAction(e -> {
                        sistema.venderBoleto(categoria, f, c, usuario);
                        actualizarAsientos(grid, categoria);
                    });
                }
                grid.add(btn, col, fila);
            }
        }
    }
}