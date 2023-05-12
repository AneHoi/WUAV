package GUI.Controller;

import BE.Report;
import GUI.Model.Model;
import javafx.event.ActionEvent;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class AddImageController implements Initializable {
    @FXML
    private Button btnSave, btnChooseImage;
    @FXML
    private TextField txtAddComment;
    private byte[] dataImage;
    @FXML
    private VBox vBox;
    private Label lblImage;
    private ImageView imgView;
    private Image image;
    private Report currentReport;
    private ControllerAssistant controllerAssistant;
    private Model model;
    private int nextPosition;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        controllerAssistant = ControllerAssistant.getInstance();
        lblImage = new Label();
        imgView = new ImageView();
        imgView.setFitWidth(200);
        imgView.setFitHeight(200);
        btnSave.setDisable(true);
    }

    public void setCurrentReport(Report currentReport) {
        this.currentReport = currentReport;
    }


    public void handleChooseImage(ActionEvent actionEvent) {
        vBox.getChildren().remove(imgView);
        vBox.getChildren().remove(lblImage);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        Stage stage = (Stage) btnChooseImage.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null && selectedFile.getName().endsWith(".png") || selectedFile != null && selectedFile.getName().endsWith(".jpg") || selectedFile != null && selectedFile.getName().endsWith(".gif")) {
            image = new Image(selectedFile.toURI().toString());
            imgView.setImage(image);
            vBox.getChildren().add(1, imgView);
            vBox.getChildren().add(2, lblImage);
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

    public void handleSave(ActionEvent actionEvent) {
        int position = nextPosition;
        int reportID = currentReport.getReportID();
        String comment = txtAddComment.getText();
        int userID = controllerAssistant.getLoggedInUser().getUserID();
        LocalDate createdDate = LocalDate.now();
        LocalTime createdTime = LocalTime.now();

        try {
            model.SaveImageToReport(position, reportID, dataImage, comment, userID, createdDate, createdTime);
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not save image in the Database", ButtonType.CANCEL);
            alert.showAndWait();
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Image added successfully", ButtonType.OK);
        alert.showAndWait();
        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }

    public void setNextAvailablePosition(int nextPosition) {
        this.nextPosition = nextPosition;
    }
}
