package GUI.Controller;

import BE.User;
import GUI.Controller.Util.ControllerAssistant;
import GUI.Controller.Util.Util;
import GUI.Model.Model;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {


    private boolean loginIsSuccessful = false;
    @FXML
    private Button btnLogin;
    @FXML
    private PasswordField pswPassword;
    @FXML
    private TextField txtUsername;
    @FXML
    private ImageView imgWUAVLogo;
    private final Util util = new Util();
    private ControllerAssistant controllerAssistant;
    private User user;
    private Model model;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        controllerAssistant = ControllerAssistant.getInstance();
        SetImg();
        util.addShadow(pswPassword, txtUsername, btnLogin, imgWUAVLogo);
    }

    /**
     * Sets the image of the WUAV logo.
     * It loads the image file from the specified path and sets it to the ImageView.
     */
    private void SetImg() {
        String logo = "data/Images/logoWhite.png";
        imgWUAVLogo.setImage(util.loadImages(logo));
    }
    /**
     * Displays an information alert with the specified message.
     * The alert is styled using the "Main.css" stylesheet.
     */
    private void displayAlert(String alert) {
        Alert alertMessage = new Alert(Alert.AlertType.INFORMATION);
        alertMessage.setTitle("Alert");
        DialogPane dialogPane = alertMessage.getDialogPane();
        dialogPane.getStylesheets().add("/GUI/View/css/Main.css");
        dialogPane.getStyleClass().add("dialog");
        alertMessage.setHeaderText(alert);
        alertMessage.showAndWait();

    }

    /**
     * Checks if the password is valid, must contain 8 characters and not contain any special characters.
     */
    public boolean validPassword(String password) {
        String specialChars = "!,.:;<>\\/()#%=+?'*";
        if (password.length() >= 8) {
            for (int i = 0; i < password.length() - 1; i++) {
                for (int j = 0; j < specialChars.length() - 1; j++) {
                    if (password.charAt(i) == specialChars.charAt(j)) {
                        return false;
                    }
                }
            }
        } else if (password.length() < 8) {
            return false;
        }

        return true;
    }

    /**
     * Performs the login operation.
     * Validates the username and password entered by the user,
     * checks if the password is valid, and retrieves the user from the model.
     * If the login is successful, sets the logged-in user in the assistant controller.
     * If the user's password needs to be changed, opens the change password dialog.
     * Closes the login window after a successful login.
     */
    public void login() {
        if (txtUsername.getText().isEmpty() || pswPassword.getText().isEmpty()) {
            displayAlert("Missing username or password");
            return;
        }
        if (!validPassword(pswPassword.getText())) {
            displayAlert("Password contains illegal characters");
            return;
        }
        try {
            user = model.checkLogIn(txtUsername.getText().trim(), pswPassword.getText().trim());

            if (user == null) {
                displayAlert("Invalid username or password");
                return;
            }
            controllerAssistant.setLoggedInUser(user);

            if (model.checkPassword(controllerAssistant.getLoggedInUser().getPassword(), "WUAV1234")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/View/ChangePasswordView.fxml"));
                Parent root = loader.load();
                ChangePasswordController changePasswordController = loader.getController();
                changePasswordController.setUser(controllerAssistant.getLoggedInUser());
                Stage stage1 = new Stage();
                stage1.setTitle("Change password");
                stage1.setScene(new Scene(root));
                stage1.initModality(Modality.APPLICATION_MODAL);
                stage1.showAndWait();
            }
            loginIsSuccessful = true;
            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
            displayAlert("Failed to log in");
        }

    }

    /**
     * Getter for loginIsSuccessful.
     */
    public boolean isLoginIsSuccessful() {
        return loginIsSuccessful;
    }


}
