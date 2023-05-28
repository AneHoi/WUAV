package GUI.Controller;

import GUI.Controller.Util.ControllerAssistant;
import GUI.Controller.Util.Util;
import GUI.Model.Model;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class IndexController implements Initializable {
    @FXML
    private BorderPane borderIndex;
    private ControllerAssistant controllerAssistant;
    private Util util;
    private Model model;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controllerAssistant = ControllerAssistant.getInstance();
        controllerAssistant.setBorderPane(borderIndex);
        try {
            controllerAssistant.loadCenter("UserHomePageView.fxml");
            controllerAssistant.loadLeft("BurgerBarView.fxml");
            controllerAssistant.loadTop("TopBarView.fxml");
            System.out.println(controllerAssistant.getLoggedInUser().getPassword());
            if(controllerAssistant.getLoggedInUser().getPassword() == model.checkPassword(controllerAssistant.getLoggedInUser().getUserName(), controllerAssistant.getLoggedInUser().getPassword())){
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation((getClass().getResource("/GUI/View/ChangePasswordView.fxml")));
                stage.setTitle("WUAV");
                util.openNewWindow(stage,loader,"Could not open program");
                stage.show();
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load the application: \n" + e, ButtonType.OK);
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not get password", ButtonType.CLOSE);
            alert.showAndWait();
        }


    }
}
