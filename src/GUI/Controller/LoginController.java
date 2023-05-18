package GUI.Controller;

import GUI.Controller.Util.Util;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private Button btnLogin;
    @FXML private PasswordField pswPassword;
    @FXML private TextField txtUsername;
    @FXML
    private ImageView imgWUAVLogo;
    private String logo = "data/Images/logoWhite.png";
    private Util util = new Util();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SetImg();
        util.addShadow(pswPassword, txtUsername, btnLogin, imgWUAVLogo);
    }


    private void SetImg() {
        imgWUAVLogo.setImage(util.loadImages(logo));
    }

    public void login(ActionEvent event) {
        //TODO Make an accout that can enter

    }
}
