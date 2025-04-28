package view;

import controller.LoginController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import model.GestorUsuarios;
import java.io.IOException;

/**
 * Vista que gestiona la creación de la escena de inicio de sesión (Login).
 */
public class LoginView {

    private final MainView mainView;
    private final GestorUsuarios gestor;

    /**
     * Constructor de la vista de login.
     *
     * @param mainView Ventana principal de la aplicación.
     * @param gestor   Gestor de usuarios para autenticación.
     */
    public LoginView(MainView mainView, GestorUsuarios gestor) {
        this.mainView = mainView;
        this.gestor = gestor;
    }

    /**
     * Crea y configura la escena de inicio de sesión.
     *
     * @return Objeto Scene listo para mostrarse.
     * @throws IOException Si ocurre un error al cargar el archivo FXML.
     */
    public Scene crearScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
        Parent root = loader.load();

        LoginController controller = loader.getController();
        controller.setMainApp(mainView);
        controller.setGestorUsuarios(gestor);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/login.css").toExternalForm());

        return scene;
    }
}
