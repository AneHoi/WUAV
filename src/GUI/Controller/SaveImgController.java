package GUI.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class SaveImgController implements Initializable {


    @FXML
    private ImageView imgView;
    private Image image;

    public void setImgView(Image img){
        this.image = img;
    }

    public void handleSave(ActionEvent event) {
        //TODO saveImg to database
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imgView.setImage(image);
    }
}
