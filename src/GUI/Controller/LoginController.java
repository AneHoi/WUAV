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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {


    public boolean loginIsSuccessful = false;
    @FXML private Button btnLogin;
    @FXML private PasswordField pswPassword;
    @FXML private TextField txtUsername;
    @FXML
    private ImageView imgWUAVLogo;
    private final Util util = new Util();
    private ControllerAssistant controllerAssistant = new ControllerAssistant();
    private User user;
    private Model model = new Model();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SetImg();
        util.addShadow(pswPassword, txtUsername, btnLogin, imgWUAVLogo);
    }


    private void SetImg() {
        String logo = "data/Images/logoWhite.png";
        imgWUAVLogo.setImage(util.loadImages(logo));
    }
    private void displayAlert(String alert){
        Alert alertMessage = new Alert(Alert.AlertType.INFORMATION);
        alertMessage.setTitle("Alert");
        alertMessage.setHeaderText(alert);
        alertMessage.showAndWait();
    }
    public boolean validPassword(String password){
        String specialChars = "!,.:;<>\\/()#%=+?'*";
        if (password.length() >= 8){
            for (int i = 0; i < password.length() - 1; i++) {
                for (int j = 0; j < specialChars.length() - 1; j++){
                    if(password.charAt(i) == specialChars.charAt(j)){
                        return false;
                    }
                }
            }
        }else if (password.length() < 8){
            return false;
        }

        return true;
    }

    public void login() {
        if (txtUsername.getText().isEmpty() || pswPassword.getText().isEmpty()){
            displayAlert("Missing username or password");
            return;
        }
        if(!validPassword(pswPassword.getText())){
            displayAlert("Password contains illegal characters");
            return;
        }
        try {
            user = model.checkLogIn(txtUsername.getText().trim(),pswPassword.getText().trim());

            if(user == null){
                displayAlert("Invalid username or password");
                return;
            }
            loginIsSuccessful = true;
            controllerAssistant.setLoggedInUser(user);


            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.close();

            if(model.checkPassword(controllerAssistant.getLoggedInUser().getPassword(), "WUAV1234")){
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
        } catch (Exception e) {
            e.printStackTrace();
            displayAlert("Failed to log in");
        }

    }

    public boolean isLoginIsSuccessful() {
        return loginIsSuccessful;
    }


}
