package GUI.Controller;

import BE.User;
import GUI.Controller.Util.Util;
import GUI.Model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class ChangePasswordController implements Initializable {
    public ImageView imgWUAVLogo;
    public PasswordField pswOldPassword;
    public PasswordField pswNewPassword;
    public PasswordField pswNewPasswordCheck;
    public Button btnChangePassword;
    private Util util;
    private Model model;
    private User user;

        @Override
    public void initialize(URL location, ResourceBundle resources) {
        setImg();
            util.addShadow(pswOldPassword, pswNewPassword, pswNewPasswordCheck, btnChangePassword, imgWUAVLogo);
    }
    private void setImg() {
        String logo = "data/Images/logoWhite.png";
        imgWUAVLogo.setImage(util.loadImages(logo));
    }
    public void handleChangePassword(ActionEvent actionEvent) throws Exception {
        if(pswOldPassword.getText() == "WUAV1234") {
            if (pswNewPassword == pswNewPasswordCheck) {
                if (pswNewPassword.getText().length() >= 8) {
                    model.setPassword(user.getUserName(), pswNewPassword.getText());
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Password must be 8 or more characters", ButtonType.CLOSE);
                }
            }
        }
    }


}
