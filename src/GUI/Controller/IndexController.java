package GUI.Controller;

import GUI.Model.Model;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class IndexController implements Initializable {
    @FXML
    private BorderPane borderIndex;
    private ControllerAssistant controllerAssistant;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        openLogin();
        controllerAssistant = ControllerAssistant.getInstance();
        controllerAssistant.setBorderPane(borderIndex);
        try {
            controllerAssistant.loadCenter("UserHomePageView.fxml");
            controllerAssistant.loadLeft("BurgerBarView.fxml");
            controllerAssistant.loadTop("TopBarView.fxml");
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load the application: \n" + e, ButtonType.OK);
            alert.showAndWait();
        }


    }

    private void openLogin() {
        LoginController loginController = new LoginController();
        loginController.setTxtUsername("Michael Tonnesen");
        loginController.setPswPassword("password");
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setController(loginController);
        loader.setLocation(getClass().getResource("/GUI/View/LoginView.fxml"));
        stage.setTitle("Login");
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open Login page", ButtonType.CANCEL);
            alert.showAndWait();
        }

    }
}
