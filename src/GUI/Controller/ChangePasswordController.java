package GUI.Controller;

import BE.User;
import GUI.Controller.Util.Util;
import GUI.Model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class ChangePasswordController implements Initializable {
    @FXML
    private ImageView imgWUAVLogo;
    @FXML
    private PasswordField pswOldPassword, pswNewPassword, pswNewPasswordCheck;
    @FXML
    private Button btnChangePassword;
    private Util util = new Util();
    private Model model;
    private User user;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        setImg();
        util.addShadow(pswOldPassword, pswNewPassword, pswNewPasswordCheck, btnChangePassword, imgWUAVLogo);
    }

    private void setImg() {
        String logo = "data/Images/logoWhite.png";
        imgWUAVLogo.setImage(util.loadImages(logo));
    }

    public void handleChangePassword(ActionEvent actionEvent) throws Exception {
        if (model.checkPassword(user.getPassword(), pswOldPassword.getText())) {
            if (pswNewPassword.getText().equals(pswNewPasswordCheck.getText())) {
                if (pswNewPassword.getText().length() >= 8) {
                    model.setPassword(user.getUserName(), pswNewPassword.getText());
                    ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Password must be 8 or more characters", ButtonType.CLOSE);
                }
            }
        }
    }

    public void setUser(User user) {
        this.user = user;
    }

}
