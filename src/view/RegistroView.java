package view;

import controller.RegisterController;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import model.GestorUsuarios;
import java.io.IOException;

/**
 * Vista de registro de usuario.
 * <p>
 * Esta clase gestiona la creación de la vista de registro, donde el usuario puede registrarse en el sistema.
 * Utiliza el controlador RegisterController para manejar la lógica de registro y el GestorUsuarios para interactuar con los datos de usuario.
 * </p>
 */
public class RegistroView {
    private final MainView mainView;
    private final GestorUsuarios gestor;

    /**
     * Constructor de la clase RegistroView.
     * 
     * @param mainView La vista principal de la aplicación.
     * @param gestor El gestor de usuarios para interactuar con los datos de registro.
     */
    public RegistroView(MainView mainView, GestorUsuarios gestor) {
        this.mainView = mainView;
        this.gestor = gestor;
    }

    /**
     * Crea la escena de registro y carga el archivo FXML correspondiente.
     * Se establece el controlador y la hoja de estilos para la vista.
     * 
     * @return La escena configurada para el registro de usuario.
     * @throws IOException Si hay un error al cargar el archivo FXML o la hoja de estilos.
     */
    public Scene crearScene() throws IOException {
        // Cargar el archivo FXML correspondiente a la vista de registro
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Registro.fxml"));
        Parent root = loader.load();

        // Obtener el controlador de la vista y configurarlo con la vista principal y el gestor de usuarios
        RegisterController controller = loader.getController();
        controller.setMainApp(mainView);
        controller.setGestorUsuarios(gestor);

        // Crear la escena con el contenido cargado desde FXML
        Scene scene = new Scene(root);

        // Añadir la hoja de estilos CSS para mejorar la apariencia visual
        scene.getStylesheets().add(getClass().getResource("/css/registro.css").toExternalForm());

        return scene;
    }
}
