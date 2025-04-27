package view;

import controller.RegisterController;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import model.GestorUsuarios;
import java.io.IOException;

public class RegistroView {
    private final MainView mainView;
    private final GestorUsuarios gestor;

    public RegistroView(MainView mainView, GestorUsuarios gestor) {
        this.mainView = mainView;
        this.gestor = gestor;
    }

    public Scene crearScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Registro.fxml"));
        Parent root = loader.load();
        
        RegisterController controller = loader.getController();
        controller.setMainApp(mainView);
        controller.setGestorUsuarios(gestor);
        
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/registro.css").toExternalForm());
        return scene;
    }
}