package GUI.Controller;

import GUI.Controller.Util.ControllerAssistant;
import GUI.Controller.Util.Util;
import GUI.Model.Model;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    private Util util = new Util();
    private Model model = new Model();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controllerAssistant = ControllerAssistant.getInstance();
        controllerAssistant.setBorderPane(borderIndex);
        try {
            controllerAssistant.loadCenter("UserHomePageView.fxml");
            controllerAssistant.loadLeft("BurgerBarView.fxml");
            controllerAssistant.loadTop("TopBarView.fxml");
            if(model.checkPassword(controllerAssistant.getLoggedInUser().getPassword(), "WUAV1234")){
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/View/ChangePasswordView.fxml"));
                    Parent root = loader.load();
                    ChangePasswordController changePasswordController = loader.getController();
                    changePasswordController.setUser(controllerAssistant.getLoggedInUser());
                    Stage stage = new Stage();
                    stage.setTitle("Change password");
                    stage.setScene(new Scene(root));
                    stage.showAndWait();
            }
            else{

            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load the application: \n" + e, ButtonType.OK);
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open change password page", ButtonType.CLOSE);
            alert.showAndWait();
        }


    }
}
