package view;

import controller.LoginController;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import model.GestorUsuarios;
import java.io.IOException;

public class LoginView {
    private final MainView mainView;
    private final GestorUsuarios gestor;

    public LoginView(MainView mainView, GestorUsuarios gestor) {
        this.mainView = mainView;
        this.gestor = gestor;
    }

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