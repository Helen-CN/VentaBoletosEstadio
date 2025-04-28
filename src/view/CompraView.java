package view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import controller.SistemaBoletos;
import model.Usuario;

/**
 * Vista para la compra de boletos.
 * Permite al usuario seleccionar la categoría y asiento disponible.
 */
public class CompraView {

    private final MainView mainView;
    private final SistemaBoletos sistema;
    private final Usuario usuario;

    /**
     * Constructor de la vista de compra.
     *
     * @param mainView Ventana principal de la aplicación.
     * @param sistema  Sistema de gestión de boletos.
     * @param usuario  Usuario que realiza la compra.
     */
    public CompraView(MainView mainView, SistemaBoletos sistema, Usuario usuario) {
        this.mainView = mainView;
        this.sistema = sistema;
        this.usuario = usuario;
    }

    /**
     * Crea la escena completa para la compra de boletos.
     *
     * @return Objeto Scene listo para mostrarse.
     */
    public Scene crearScene() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));

        Label titulo = new Label("🎟️ Compra de Boletos");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

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

        Button btnVolver = new Button("🔙 Volver");
        btnVolver.setPrefWidth(100);
        btnVolver.setOnAction(e -> mainView.mostrarMenuPrincipal(usuario));

        layout.getChildren().addAll(titulo, comboCategoria, gridAsientos, btnVolver);

        return new Scene(layout, 600, 600);
    }

    /**
     * Actualiza el grid de asientos para una categoría específica.
     *
     * @param grid      GridPane donde se dibujarán los asientos.
     * @param categoria Categoría seleccionada (VIP, Preferencial, General).
     */
    private void actualizarAsientos(GridPane grid, String categoria) {
        grid.getChildren().clear();
        boolean[][] matriz = sistema.getMatriz(categoria);

        for (int fila = 0; fila < matriz.length; fila++) {
            for (int col = 0; col < matriz[fila].length; col++) {
                Button btnAsiento = new Button("F" + (fila + 1) + "C" + (col + 1));
                btnAsiento.setPrefSize(70, 40);

                if (matriz[fila][col]) {
                    configurarBotonOcupado(btnAsiento);
                } else {
                    configurarBotonDisponible(btnAsiento, categoria, fila, col, grid);
                }

                grid.add(btnAsiento, col, fila);
            }
        }
    }

    /**
     * Configura el botón de asiento como ocupado.
     *
     * @param btnAsiento Botón que representa un asiento.
     */
    private void configurarBotonOcupado(Button btnAsiento) {
        btnAsiento.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        btnAsiento.setDisable(true);
    }

    /**
     * Configura el botón de asiento como disponible y su acción de compra.
     *
     * @param btnAsiento Botón que representa un asiento.
     * @param categoria  Categoría a la que pertenece el asiento.
     * @param fila       Fila del asiento.
     * @param col        Columna del asiento.
     * @param grid       GridPane donde actualizar los asientos después de comprar.
     */
    private void configurarBotonDisponible(Button btnAsiento, String categoria, int fila, int col, GridPane grid) {
        btnAsiento.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        btnAsiento.setOnAction(e -> {
            sistema.venderBoleto(categoria, fila, col, usuario);
            actualizarAsientos(grid, categoria); // Refrescar visualmente la matriz
        });
    }
}
