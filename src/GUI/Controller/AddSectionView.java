package GUI.Controller;

import BE.Addendum;
import BE.Report;
import BE.Section;
import BE.Technician;
import GUI.Model.Model;
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
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class AddSectionView implements Initializable {
    public javafx.scene.image.ImageView imgViewLogo;
    public Button btnSubmitSection;
    public Button btnAddImage;
    public Button btnAddSketch;
    public TextField txtSectionTitle, txtSketchPath, txtSketchComment, txtImagePath, txtImageComment, txtDescription;

    private byte[] dataSketch;
    private byte[] dataImage;
    private Image imageSketch;
    private Image imageImage;
    Section section;
    Technician technician;
    Report report;
    Addendum addendum;
    Model model = new Model();
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
                imageSketch = new Image(selectedFile.toURI().toString());
                txtSketchPath.setText(imageSketch.getUrl());

            }
            try {
                dataSketch = Files.readAllBytes(selectedFile.getAbsoluteFile().toPath());
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
                imageImage = new Image(selectedFile.toURI().toString());
                txtImagePath.setText(imageImage.getUrl());

            }
            try {
                dataImage = Files.readAllBytes(selectedFile.getAbsoluteFile().toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void handleSubmit(ActionEvent actionEvent) throws Exception {
        Section section = new Section(this.section.getSectionID(), txtSectionTitle.getText(), imageSketch, txtSketchComment.getText(),imageImage, txtImageComment.getText(), txtDescription.getText(), this.technician.getFullName(), this.report.getReportID(), this.addendum.getReportID(), LocalDate.now(), LocalTime.now());
        model.createSection(section);
    }
}
