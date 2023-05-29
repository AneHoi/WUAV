package GUI.Controller;

import GUI.Controller.Util.ControllerAssistant;
import GUI.Controller.Util.Util;
import GUI.Model.Model;
import javafx.application.Platform;
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
import java.util.Objects;
import java.util.ResourceBundle;

public class IndexController implements Initializable {

    @FXML
    private BorderPane borderIndex;
    private ControllerAssistant controllerAssistant;
    private Util util;
    private Model model;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        util = new Util();
        controllerAssistant = ControllerAssistant.getInstance();

        loadLogIn();


    }

    private void loadLogIn() {
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/GUI/View/LoginView.fxml"));
        Parent r1;
        try {
            r1 = (Parent) loginLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        LoginController loginController = loginLoader.getController();
        Stage loginStage = new Stage();
        loginStage.setTitle("Log In");
        loginStage.setScene(new Scene(r1));
        loginStage.setOnCloseRequest(event -> System.exit(0));
        loginStage.showAndWait();


        if (loginController != null) {
            if (loginController.isLoginIsSuccessful()) {
                try {
                    controllerAssistant.loadCenter("UserHomePageView.fxml");
                    controllerAssistant.loadLeft("BurgerBarView.fxml");
                    controllerAssistant.loadTop("TopBarView.fxml");
                    loginStage.close();
                    System.out.println(controllerAssistant.getLoggedInUser().getPassword());
                } catch (
                        IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load the application: \n" + e, ButtonType.OK);
                    alert.showAndWait();
                } catch (
                        Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Could not get password", ButtonType.CLOSE);
                    alert.showAndWait();
                }
            } else {
                try {
                    if (controllerAssistant.getLoggedInUser().getPassword() != null && !Objects.equals(controllerAssistant.getLoggedInUser().getPassword(), "")) {
                        if (controllerAssistant.getLoggedInUser().getPassword() == model.checkPassword(controllerAssistant.getLoggedInUser().getUserName(), controllerAssistant.getLoggedInUser().getPassword())) {
                            Stage stage = new Stage();
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation((getClass().getResource("/GUI/View/ChangePasswordView.fxml")));
                            stage.setTitle("WUAV");
                            util.openNewWindow(stage, loader, "Could not open program");
                            stage.show();
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}

