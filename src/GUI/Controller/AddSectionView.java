package GUI.Controller;

import BE.Addendum;
import BE.Report;
import BE.Section;
import BE.Technician;
import GUI.Model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.text.html.ImageView;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.SQLException;
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
    private Section currentSection;
    private ControllerAssistant controllerAssistant;
    private Report report;
    private Addendum addendum;
    Model model = new Model();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controllerAssistant = ControllerAssistant.getInstance();
        if (currentSection != null) {
            btnSubmitSection.setText("Update Section");
            txtDescription.setText(currentSection.getDescription());
            txtSectionTitle.setText(currentSection.getSectionTitle());
            txtImageComment.setText(currentSection.getImageComment());
            txtSketchComment.setText(currentSection.getSketchComment());
        }
    }

    public void handleAddSketch(ActionEvent actionEvent) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Image");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
            Stage stage = (Stage) btnAddSketch.getScene().getWindow();
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null && selectedFile.getName().endsWith(".png") || selectedFile != null && selectedFile.getName().endsWith(".jpg") || selectedFile != null && selectedFile.getName().endsWith(".gif")) {
                imageSketch = new Image(selectedFile.toURI().toString());
                txtSketchPath.setText(imageSketch.getUrl());

            }
            try {
                dataSketch = Files.readAllBytes(selectedFile.getAbsoluteFile().toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleAddImage(ActionEvent actionEvent) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Image");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
            Stage stage = (Stage) btnAddImage.getScene().getWindow();
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null && selectedFile.getName().endsWith(".png") || selectedFile != null && selectedFile.getName().endsWith(".jpg") || selectedFile != null && selectedFile.getName().endsWith(".gif")) {
                imageImage = new Image(selectedFile.toURI().toString());
                txtImagePath.setText(imageImage.getUrl());

            }
            try {
                dataImage = Files.readAllBytes(selectedFile.getAbsoluteFile().toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleSubmit(ActionEvent actionEvent) {
        if (btnSubmitSection.getText().equals("Update Section")) {
            currentSection.setDescription(txtDescription.getText());
            currentSection.setSectionTitle(txtSectionTitle.getText());
            currentSection.setSketchComment(txtSketchComment.getText());
            currentSection.setImageComment(txtImageComment.getText());
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Section updated successfully", ButtonType.OK);
            alert.showAndWait();
            Stage stage = (Stage) btnSubmitSection.getScene().getWindow();
            stage.close();
            try {
                model.updateCurrentSection(currentSection);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {
            int reportOrAddendumID = 0;
            Section section = null;
            if (report != null) {
                reportOrAddendumID = report.getReportID();
                section = new Section(txtSectionTitle.getText(), txtSketchComment.getText(), txtImageComment.getText(), txtDescription.getText(), controllerAssistant.getLoggedInUser().getFullName(), reportOrAddendumID, LocalDate.now(), LocalTime.now());
                try {
                    model.createSectionForReport(section);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Created section successfully", ButtonType.OK);
                    alert.showAndWait();
                    Stage stage = (Stage) btnSubmitSection.getScene().getWindow();
                    stage.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Could not create Section for Report", ButtonType.CANCEL);
                    alert.showAndWait();
                }
            } else {
                reportOrAddendumID = addendum.getReportID();
                section = new Section(txtSectionTitle.getText(), txtSketchComment.getText(), txtImageComment.getText(), txtDescription.getText(), controllerAssistant.getLoggedInUser().getFullName(), reportOrAddendumID, LocalDate.now(), LocalTime.now());
                try {
                    model.createSectionForAddendum(section);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Created section successfully", ButtonType.OK);
                    alert.showAndWait();
                    Stage stage = (Stage) btnSubmitSection.getScene().getWindow();
                    stage.close();
                } catch (SQLException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Could not create Section for Addendum", ButtonType.CANCEL);
                    alert.showAndWait();
                    e.printStackTrace();
                }
            }

        }
    }

    public void setCurrentSection(Section s) {
        this.currentSection = s;
    }


    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public Addendum getAddendum() {
        return addendum;
    }

    public void setAddendum(Addendum addendum) {
        this.addendum = addendum;
    }
}
