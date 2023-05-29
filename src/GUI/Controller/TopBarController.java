package GUI.Controller;

import BE.User;
import GUI.Controller.Util.ControllerAssistant;
import GUI.Controller.Util.Util;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class TopBarController implements Initializable {

    @FXML
    private Rectangle rectangle;
    @FXML
    private HBox hboxTop;
    @FXML
    private ImageView imgProfilePicture, imgLogo;
    @FXML
    private Label lblFirstName, lblLastName, lblLogOut;


    private String logo = "data/Images/WUAV Logo.png";
    private String profilePicture = "data/Images/ProfilePicture.png";

    private User loggedInUser;

    private ControllerAssistant controllerAssistant;
    private Util util = new Util();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblLogOut.setOnMouseClicked(event -> handleLogOut());
        controllerAssistant = ControllerAssistant.getInstance();
        loggedInUser = controllerAssistant.getLoggedInUser();
        loadUserInfo();
        lblLogOut.getStyleClass().add("burgerFont");

    }

    /**
     * Opens alert box to check if you wish to log out, if yes is pressed closes window and reopens login window
     */
    private void handleLogOut() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Are you sure you wish to log out?", ButtonType.YES, ButtonType.NO);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add("/GUI/View/css/Main.css");
        dialogPane.getStyleClass().add("dialog");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            Stage thisWindow = (Stage) lblLogOut.getScene().getWindow();
            controllerAssistant.setLoggedInUser(null);
            thisWindow.close();
            restartApplication();
        }
    }

    private void restartApplication() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/view/Index.fxml"));
            Parent root = loader.load();
            Stage primaryStage = new Stage();
            primaryStage.setTitle("WUAV");
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(true);
            primaryStage.setOnCloseRequest(event -> System.exit(0));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the logged-in user, set the image, firstname and lastname. And sets the text on the logout button.
     */
    private void loadUserInfo() {
        User user = controllerAssistant.getLoggedInUser();
        imgLogo.setImage(util.loadImages(logo));
        rectangle.setArcWidth(30.0);   // Corner radius
        rectangle.setArcHeight(30.0);

        Image profilePicture;
        if (user.getProfilePicture() != null) {
            profilePicture = user.getProfilePicture();
        } else {
            profilePicture = new Image("file:data/Images/Wuav_skabelon-mand.png", 100, 150, true, true);
        }
        rectangle.setFill(new ImagePattern(profilePicture));

        if(user.getFullName().contains(" ")) {
            String firstName = user.getFullName().substring(0, user.getFullName().indexOf(" "));
            String lastName = user.getFullName().substring(user.getFullName().indexOf(" "));
            lblFirstName.setText(firstName);
            lblLastName.setText(lastName);
        } else {
            lblFirstName.setText(user.getFullName());
            lblLastName.setVisible(false);
        }
        lblLogOut.setText("Log Out");
    }

}
