package GUI.Controller.Util;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Util {
    private final DropShadow shadow = new DropShadow(0, 4, 4, Color.color(0, 0, 0, 0.25));
    private ControllerAssistant controllerAssistant = ControllerAssistant.getInstance();

    public Image loadImages(String url) {
        Image image = null;
        try {
            InputStream img = new FileInputStream(url);
            image = new Image(img);
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load an image, following error occurred:\n" + e, ButtonType.CANCEL);
            alert.showAndWait();
        }
        return image;
    }

    public void addShadow(Node... node) {
        for (Node nodes : node) {
            nodes.setEffect(shadow);
        }
    }

    public void removeShadow(Node... node) {
        for (Node nodes : node) {
            nodes.setEffect(null);
        }
    }
    public ObservableList<String> checkLoggedInUser(ObservableList<String> userTypes) {
        String admin = "Admin";
        String projectManager = "ProjectManager";
        String technician = "Technician";
        String salesRepresentative = "SalesRepresentative";
        int userType = controllerAssistant.getLoggedInUser().getUserType();
        userTypes.clear();
        if (userType == 1) {
            userTypes.addAll(admin, projectManager, technician, salesRepresentative);
        } else if (userType == 2) {
            userTypes.addAll(technician);
        }
        return userTypes;
    }
    public void openNewWindow(String title, String fxmlPath, Class controller, String errorMessage){
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setController(controller);
        loader.setLocation(getClass().getResource(fxmlPath));
        stage.setTitle(title);
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, errorMessage, ButtonType.CANCEL);
            alert.showAndWait();
        }
    }
}
