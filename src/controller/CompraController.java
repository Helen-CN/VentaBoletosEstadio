package controller;

import animatefx.animation.FadeIn;
import animatefx.animation.ZoomIn;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import model.Usuario;
import view.MainView;

import java.util.ArrayList;
import java.util.List;

public class CompraController {

    @FXML
    private BorderPane root;
    @FXML
    private ComboBox<String> comboCategoria;
    @FXML
    private ImageView mapaEstadio;
    @FXML
    private ImageView ImagenCancha;
    @FXML
    private GridPane gridAsientos;
    @FXML
    private Label mensajeLabel, zonaLabel, asientoLabel, precioLabel,zonaTotal;
    @FXML
    private Button btnCargarZona, btnConfirmar, btnVolver, btnPreferente, btnVIPzona1, btnVIPzona2, btnGeneral;
    @FXML
    private ImageView cargarZonaIcon, mapaIcon, zonaIcon, asientoIcon, precioIcon, confirmarIcon, volverIcon;

    private MainView mainView;
    private SistemaBoletos sistema;
    private Usuario usuario;
    private String zonaSeleccionada;
    private int filaSeleccionada = -1, colSeleccionada = -1;
    
    private final List<String> historialAsientos = new ArrayList<>();
    private final List<String> historialPrecios = new ArrayList<>();
    
    private static final String[] ZONAS = {"VIP", "Preferencial", "General"};
    private static final double[] PRECIOS = {150.0, 100.0, 50.0};
    private double totalPrecio = 0.0;
    
    public void inicializar(MainView mainView, SistemaBoletos sistema, Usuario usuario) {
        this.mainView = mainView;
        this.sistema = sistema;
        this.usuario = usuario;

        try {
            mapaEstadio.setImage(new Image(getClass().getResourceAsStream("/images/stadium_bg.png")));
        } catch (Exception e) {
            System.err.println("Error al cargar imagen del estadio: " + e.getMessage());
        }

        try {
            ImagenCancha.setImage(new Image(getClass().getResourceAsStream("/images/Cancha.png")));
            cargarZonaIcon.setImage(new Image(getClass().getResourceAsStream("/images/map_icon.png")));
            mapaIcon.setImage(new Image(getClass().getResourceAsStream("/images/stadium_icon.png")));
            zonaIcon.setImage(new Image(getClass().getResourceAsStream("/images/location_icon.png")));
            asientoIcon.setImage(new Image(getClass().getResourceAsStream("/images/seat_icon.png")));
            precioIcon.setImage(new Image(getClass().getResourceAsStream("/images/money_icon.png")));
            confirmarIcon.setImage(new Image(getClass().getResourceAsStream("/images/check_icon.png")));
            volverIcon.setImage(new Image(getClass().getResourceAsStream("/images/arrow_back.png")));
        } catch (Exception e) {
            System.err.println("Error al cargar iconos: " + e.getMessage());
        }

        btnPreferente.setOnAction(e -> {
            cargarZona("Preferencial");
            comboCategoria.setValue("Preferencial");
            mostrarMensaje("Zona " + "Preferencial" + " cargada");
        });
        btnVIPzona1.setOnAction(e -> {
            cargarZona("VIP");
            comboCategoria.setValue("VIP");
            mostrarMensaje("Zona " + "VIP" + " cargada");
        });
        btnVIPzona2.setOnAction(e -> {
            cargarZona("VIP");
            comboCategoria.setValue("VIP");
            mostrarMensaje("Zona " + "VIP" + " cargada");
        });
        btnGeneral.setOnAction(e -> {
            cargarZona("General");
            comboCategoria.setValue("General");
            mostrarMensaje("Zona " + "General" + " cargada");
        });

        comboCategoria.getItems().addAll(ZONAS);
        comboCategoria.setOnAction(e -> btnCargarZona.setDisable(comboCategoria.getValue() == null));

        btnCargarZona.setOnAction(e -> cargarZona(comboCategoria.getValue()));
        btnConfirmar.setOnAction(e -> confirmarCompra());
        btnVolver.setOnAction(e -> mainView.mostrarMenuPrincipal(usuario));

        if (usuario.getHistorialCompras() == null) {
            usuario.setHistorialCompras(new java.util.Stack<>());
        }

        new FadeIn(root).play();
    }

    private void cargarZona(String categoria) {
        if (categoria == null) {
            return;
        }

        zonaSeleccionada = categoria;
        gridAsientos.getChildren().clear();
        boolean[][] matriz = sistema.getMatriz(categoria);
        if (matriz == null) {
            mostrarMensaje("Error: Categoría inválida");
            return;
        }
        resetSeleccion();

        int filas = matriz.length;
        int cols = matriz[0].length;
        int centerCol = cols / 2;

        for (int fila = 0; fila < filas; fila++) {
            for (int col = 0; col < cols; col++) {
                ImageView asiento = new ImageView();
                asiento.setFitWidth(40);
                asiento.setFitHeight(40);

                int offset = (int) (Math.sin(Math.PI * fila / (filas - 1)) * (centerCol - Math.abs(col - centerCol)));
                int adjustedCol = col + offset;

                try {
                    if (matriz[fila][col]) {
                        asiento.setImage(new Image(getClass().getResourceAsStream("/images/seat_taken.png")));
                    } else {
                        asiento.setImage(new Image(getClass().getResourceAsStream("/images/seat_available.png")));
                        int f = fila, c = col;
                        asiento.setOnMouseClicked(e -> seleccionarAsiento(f, c));
                    }
                } catch (Exception e) {
                    System.err.println("Error al cargar imagen para asiento: " + e.getMessage());
                    asiento.setImage(null);
                }

                GridPane.setRowIndex(asiento, fila);
                GridPane.setColumnIndex(asiento, adjustedCol);
                gridAsientos.getChildren().add(asiento);
            }
        }

        mostrarMensaje("Zona " + categoria + " cargada");
        new ZoomIn(gridAsientos).play();
    }

    private void seleccionarAsiento(int fila, int col) {
        filaSeleccionada = fila;
        colSeleccionada = col;

        // Guardar información sin ID
        String asientoInfo = "Fila " + (fila + 1) + ", Columna " + (col + 1);
        int zonaIndex = java.util.Arrays.asList(ZONAS).indexOf(zonaSeleccionada);
        double precio = PRECIOS[zonaIndex];
        String precioInfo = "$" + precio;

    // Sumar al total
        totalPrecio += precio;

        historialAsientos.add(asientoInfo);
        historialPrecios.add(precioInfo);

        // Actualizar visualmente el asiento seleccionado
        gridAsientos.getChildren().forEach(node -> {
            if (node instanceof ImageView) {
                node.setOpacity(1.0); // quitar selección visual anterior
            }
        });

        for (javafx.scene.Node node : gridAsientos.getChildren()) {
            if (node instanceof ImageView imageView) {
                Integer row = GridPane.getRowIndex(node);
                Integer column = GridPane.getColumnIndex(node);
                int offset = (int) (Math.sin(Math.PI * fila / (sistema.getMatriz(zonaSeleccionada).length - 1))
                        * (sistema.getMatriz(zonaSeleccionada)[0].length / 2 - Math.abs(col - sistema.getMatriz(zonaSeleccionada)[0].length / 2)));
                int adjustedCol = col + offset;

                if (row != null && column != null && row == fila && column == adjustedCol) {
                    imageView.setImage(new Image(getClass().getResourceAsStream("/images/seat_selected.png")));
                    break;
                }
            }
        }

        // Mostrar zona
        zonaLabel.setText("Zona: " + zonaSeleccionada);

        // Mostrar historial acumulado
        asientoLabel.setText("Asientos:\n" + String.join("\n", historialAsientos));
        precioLabel.setText("Precios:\n" + String.join("\n", historialPrecios));
        zonaTotal.setText("Total: $" + String.format("%.2f", totalPrecio));
        btnConfirmar.setDisable(false);
    }

    private void confirmarCompra() {
        if (zonaSeleccionada != null && filaSeleccionada != -1 && colSeleccionada != -1) {
            try {
                sistema.venderBoleto(zonaSeleccionada, filaSeleccionada, colSeleccionada, usuario);
                usuario.guardarHistorialEnArchivo();
                mostrarMensaje("Boleto en " + zonaSeleccionada + " comprado con éxito");
                cargarZona(zonaSeleccionada);
                resetSeleccion();
            } catch (IllegalArgumentException e) {
                mostrarMensaje("Error: " + e.getMessage());
            }
        }
    }

    private void resetSeleccion() {
        filaSeleccionada = -1;
        colSeleccionada = -1;
        zonaLabel.setText("Zona: - ");
        asientoLabel.setText("Asiento: - ");
        precioLabel.setText("Precio: - ");
        btnConfirmar.setDisable(true);
    }

    private void mostrarMensaje(String texto) {
        mensajeLabel.setText(texto);
        mensajeLabel.setVisible(true);
        new ZoomIn(mensajeLabel).play();
    }
}
