package GUI.Controller;

import BE.Section;
import BE.Technician;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.text.html.ImageView;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

public class AddSectionView implements Initializable {
    public javafx.scene.image.ImageView imgViewLogo;
    public Button btnSubmitSection;
    public Button btnAddImage;
    public Button btnAddSketch;
    public TextField txtSectionTitle, txtSketchPath, txtSketchComment, txtImagePath, txtImageComment, txtDescription;

    private byte[] data;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void handleAddSketch(ActionEvent actionEvent) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Image");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
            Stage stage = (Stage) btnAddSketch.getScene().getWindow();
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null && selectedFile.getName().endsWith(".png") || selectedFile != null && selectedFile.getName().endsWith(".jpg")
                    || selectedFile != null && selectedFile.getName().endsWith(".gif")) {
                Image image = new Image(selectedFile.toURI().toString());
                txtSketchPath.setText(image.getUrl());

            }
            try {
                data = Files.readAllBytes(selectedFile.getAbsoluteFile().toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

    }catch (Exception ex){
        ex.printStackTrace();}
    }

    public void handleAddImage(ActionEvent actionEvent) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Image");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
            Stage stage = (Stage) btnAddImage.getScene().getWindow();
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null && selectedFile.getName().endsWith(".png") || selectedFile != null && selectedFile.getName().endsWith(".jpg")
                    || selectedFile != null && selectedFile.getName().endsWith(".gif")) {
                Image image = new Image(selectedFile.toURI().toString());
                txtImagePath.setText(image.getUrl());

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void handleSubmit(ActionEvent actionEvent) {

    }
}
