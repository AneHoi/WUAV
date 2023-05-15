package GUI.Controller;

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
    private String username;
    private String password;
    @FXML
    private ImageView imgWUAVLogo;

    private DropShadow shadow = new DropShadow(0, 4, 4, Color.color(0, 0, 0, 0.25));
    private String logo = "data/Images/logoWhite.png";
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtUsername.setText(username);
        pswPassword.setText(password);
        SetImg();
        setEffect(pswPassword, txtUsername, btnLogin, imgWUAVLogo);
    }

    private void setEffect(Node... node) {
        for (Node nodes: node) {
            nodes.setEffect(shadow);
        }
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
    public void setTxtUsername(String username){
        this.username = username;
    }
    public void setPswPassword(String password){
        this.password = password;
    }

    public void login(ActionEvent event) {
        //TODO Make an accout that can enter

        //This closes the window
        ((Node)(event.getSource())).getScene().getWindow().hide();

    }

}
