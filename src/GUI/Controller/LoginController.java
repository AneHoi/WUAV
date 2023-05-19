package GUI.Controller;

import GUI.Controller.Util.Util;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private Button btnLogin;
    @FXML private PasswordField pswPassword;
    @FXML private TextField txtUsername;
    @FXML
    private ImageView imgWUAVLogo;
    private final Util util = new Util();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SetImg();
        util.addShadow(pswPassword, txtUsername, btnLogin, imgWUAVLogo);
    }


    private void SetImg() {
        String logo = "data/Images/logoWhite.png";
        imgWUAVLogo.setImage(util.loadImages(logo));
    }

    public void login() {
        //TODO Make an account that can enter

    }
}
