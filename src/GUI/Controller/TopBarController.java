package GUI.Controller;

import BE.User;
import GUI.Controller.Util.ControllerAssistant;
import GUI.Controller.Util.Util;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class TopBarController implements Initializable {

    public Rectangle rectangle;
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

    private void handleLogOut() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Are you sure you wish to log out?", ButtonType.YES,ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            LoginController loginController2 = new LoginController();
            Stage thisWindow = (Stage) lblLogOut.getScene().getWindow();
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setController(loginController2);
            loader.setLocation(getClass().getResource("/GUI/View/LoginView.fxml"));
            util.openNewWindow(stage, loader, "Could not open new Login Window");
            thisWindow.close();


        }
    }

    private void loadUserInfo() {
        imgLogo.setImage(util.loadImages(logo));
        rectangle.setArcWidth(30.0);   // Corner radius
        rectangle.setArcHeight(30.0);
        ImagePattern pattern = new ImagePattern( //TODO change to users profile picture
                new Image("file:data/Images/ProfilePicture.png", 100, 150, true, true) // Resizing
        );
        rectangle.setFill(pattern);
        lblFirstName.setText("Michael"); //TODO Change to users real name
        lblLastName.setText("Tonnesen"); //TODO Chnage to users real last name
        lblLogOut.setText("Log Out");
    }
}
