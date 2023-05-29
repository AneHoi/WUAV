package GUI.Controller.Util;

import BE.Case;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Util {
    private final DropShadow shadow = new DropShadow(0, 4, 4, Color.color(0, 0, 0, 0.25));
    private ControllerAssistant controllerAssistant = ControllerAssistant.getInstance();

    /**
     * Utility method to load a URL to an image
     *
     * @param url
     * @return
     */
    public Image loadImages(String url) {
        Image image = null;
        try {
            InputStream img = new FileInputStream(url);
            image = new Image(img);
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load an image, following error occurred:\n" + e, ButtonType.CANCEL);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add("/GUI/View/css/Main.css");
            dialogPane.getStyleClass().add("dialog");
            alert.showAndWait();
        }
        return image;
    }

    /**
     * Utility method to add shadow to buttons, textfields etc.
     *
     * @param node
     */
    public void addShadow(Node... node) {
        for (Node nodes : node) {
            nodes.setEffect(shadow);
        }
    }

    /**
     * Utility method to remove the shadows from buttons, textfields etc.
     *
     * @param node
     */
    public void removeShadow(Node... node) {
        for (Node nodes : node) {
            nodes.setEffect(null);
        }
    }

    /**
     * Utility tool to make sure the different roles have access to creating new users but only the ones below their own role.
     *
     * @param userTypes
     * @return
     */
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

    /**
     * Utility tool to open a new window as a popup.
     * @param stage
     * @param loader
     * @param errorText
     */

    public void openNewWindow(Stage stage, FXMLLoader loader, String errorText) {
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, errorText, ButtonType.CANCEL);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add("/GUI/View/css/Main.css");
            dialogPane.getStyleClass().add("dialog");
            alert.showAndWait();
        }
    }

    /**
     * Checks if the case is older than 4 years.
     * @param selectedCase is the case to be checked
     * @return boolean
     */
    public boolean tooOld(Case selectedCase) {
        LocalDateTime dateToday = LocalDate.now().atStartOfDay();
        if (selectedCase.getDateClosed() != null) {
            LocalDateTime dateClosed = selectedCase.getDateClosed().atStartOfDay();
            long daysBetween = Duration.between(dateClosed, dateToday).toDays();
            if (daysBetween > selectedCase.getDaysToKeep()) {
                return true;
            }
        }
        return false;
    }
}
