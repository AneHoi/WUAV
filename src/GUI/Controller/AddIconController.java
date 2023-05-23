package GUI.Controller;

import BE.TextsAndImagesOnReport;
import GUI.Model.Model;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddIconController implements Initializable {
    @FXML
    private Button btnSave, btnChooseImage;
    @FXML
    private ImageView imgView;
    @FXML
    private VBox vBox;
    @FXML
    private Label lblImage, lblTitle;
    @FXML
    private TextField txtAddComment;

    private Image image;
    private byte[] dataImage;

    private TextsAndImagesOnReport textOrImage;
    private Model model;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imgView.setFitWidth(200);
        model = Model.getInstance();
        btnSave.setDisable(true);

    }
    public void handleChooseImage() {
        vBox.getChildren().remove(imgView);
        vBox.getChildren().remove(lblImage);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.setInitialDirectory(new File("..\\WUAV\\data\\Images\\DrawingProgramTest"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        Stage stage = (Stage) btnChooseImage.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null && selectedFile.getName().endsWith(".png") || selectedFile != null && selectedFile.getName().endsWith(".jpg") || selectedFile != null && selectedFile.getName().endsWith(".gif")) {
            image = new Image(selectedFile.toURI().toString());
            imgView.setImage(image);
            vBox.getChildren().add(0, imgView);
            vBox.getChildren().add(1, lblImage);
            lblImage.setText(image.getUrl());
            btnChooseImage.setText("Change Image");
            btnSave.setDisable(false);
        }
        try {
            if (selectedFile != null) {
                dataImage = Files.readAllBytes(selectedFile.getAbsoluteFile().toPath());
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load image", ButtonType.OK);
            alert.showAndWait();
        }
    }
    public void handleSave() {
        try {
            model.addDrwaingIcon(dataImage, txtAddComment.getText());
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not save icon in the Database", ButtonType.CANCEL);
            alert.showAndWait();
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Image added successfully", ButtonType.OK);
        alert.showAndWait();
        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }

}
