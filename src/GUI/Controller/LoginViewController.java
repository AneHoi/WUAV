package GUI.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

public class LoginViewController implements Initializable {

    @FXML private Button btnLogin;
    @FXML private PasswordField pswPassword;
    @FXML private TextField txtUsername;
    @FXML
    private ImageView imgWUAVLogo;

    private DropShadow shadow = new DropShadow(0, 4, 4, Color.color(0, 0, 0, 0.25));
    private String logo = "data/Images/logoWhite.png";
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SetImg();
        pswPassword.setEffect(shadow);
        txtUsername.setEffect(shadow);
        btnLogin.setEffect(shadow);
        imgWUAVLogo.setEffect(shadow);
    }

    private void SetImg() {
        imgWUAVLogo.setImage(loadImages(logo));
    }

    private Image loadImages(String url) {
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

    public void login(ActionEvent event) {
        //TODO Make an accout that can enter

    }
}
