package GUI.Controller;

import GUI.Model.Model;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class IndexController implements Initializable {
    @FXML
    private BorderPane borderIndex;
    private ControllerAssistant controllerAssistant;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controllerAssistant = ControllerAssistant.getInstance();
        controllerAssistant.setBorderPane(borderIndex);
        try {
            Model model = new Model();
            controllerAssistant.loadCenter("CaseHomePageView.fxml");
            controllerAssistant.loadLeft("BurgerBarView.fxml");
            controllerAssistant.loadTop("TopBarView.fxml");
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load the application: \n" + e, ButtonType.OK);
            alert.showAndWait();
        }


    }
}
