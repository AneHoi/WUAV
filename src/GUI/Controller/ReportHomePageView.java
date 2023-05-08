package GUI.Controller;

import BE.*;
import GUI.Model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ReportHomePageView implements Initializable {
    public Button btnAddSection;
    @FXML
    private Label lblCustomerName, lblReportName, lblCustomerAddress, lblCustomerEmail, lblCustomerTelephone, lblCaseName, lblCaseID, lblCaseCreated, lblCaseTechnicians, lblCaseContactPerson, lblReportDescription;
    @FXML
    private VBox vboxSectionAdding;
    private Customer currentCustomer;
    private Case currentCase;
    private Report currentReport;
    private Addendum currentAddendum;
    private Section currentSection;
    private Model model;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        currentReport = model.getCurrentReport();
        currentCase = model.getCurrentCase();
        currentCustomer = model.getCurrentCustomer();
        currentAddendum = model.getCurrentAddendum();
        updateReportInfo();
        updateSectionInfo();
    }


    private void updateReportInfo() {
        lblCustomerName.setText(currentCustomer.getCustomerName());
        lblReportName.setText(currentReport.getReportName());
        lblCustomerAddress.setText(currentCustomer.getAddress());
        lblCustomerEmail.setText(currentCustomer.getEmail());
        lblCustomerTelephone.setText(currentCustomer.getPhoneNumber());
        lblCaseName.setText(currentCase.getCaseName());
        lblCaseID.setText(String.valueOf(currentCase.getCaseID()));
        lblCaseCreated.setText(String.valueOf(currentCase.getCreatedDate()));
        lblCaseTechnicians.setText(currentCase.getAssignedTechnician());
        lblCaseContactPerson.setText(currentCase.getContactPerson());
        lblReportDescription.setText(currentReport.getReportDescription());

    }

    public void handleAddSection(ActionEvent actionEvent) {
        AddSectionView sectionView = new AddSectionView();
        sectionView.setReport(currentReport);
        sectionView.setAddendum(currentAddendum);
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setController(sectionView);
        loader.setLocation(getClass().getResource("/GUI/View/AddSectionView.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open Add Section Window", ButtonType.CANCEL);
            alert.showAndWait();
        }
        updateSectionInfo();
    }

    private void updateSectionInfo() {
        List<Section> allSections = new ArrayList<>();
        try {
            allSections = model.getAllSections(currentReport.getReportID());
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not get Sections from database", ButtonType.CANCEL);
            alert.showAndWait();
        }
        for (Section s : allSections) {
            BorderPane bp = new BorderPane();
            VBox vb = new VBox();
            Label title = new Label(s.getSectionTitle());
            ImageView sketch = new ImageView();
            Label sketchComment = new Label(s.getSketchComment());
            ImageView image = new ImageView();
            Label imageComment = new Label(s.getImageComment());
            Label description = new Label(s.getDescription());
            VBox vboxRight = new VBox();
            Label createdBy = new Label("Created by: " + s.getMadeByTechnician());
            Label createdTime = new Label("Created time: " + s.getTime() + " - " + s.getCreatedDate());
            vboxRight.getChildren().addAll(createdBy, createdTime);
            vb.getChildren().addAll(title, sketch, sketchComment, image, imageComment, description);
            bp.setCenter(vb);
            bp.setRight(vboxRight);
            bp.setPrefWidth(1030);
            bp.setStyle("-fx-border-color: BLACK");
            title.getStyleClass().add("sectionTitle");
            if (s.getSketch() != null) {
                sketch.setImage(s.getSketch());
            }
            if (s.getImage() != null) {
                image.setImage(s.getImage());
            }
            sketchComment.getStyleClass().add("sectionComments");
            imageComment.getStyleClass().add("sectionComments");
            vboxRight.setAlignment(Pos.BOTTOM_RIGHT);
            vboxRight.setPrefWidth(Region.USE_COMPUTED_SIZE);
            vboxRight.setPrefHeight(Region.USE_COMPUTED_SIZE);

            Button btnEditSection = new Button("Edit Section");
            btnEditSection.getStyleClass().add("AddAndEditSectionButtons");
            btnEditSection.setUnderline(true);
            btnEditSection.setPrefHeight(50);
            btnEditSection.setPrefWidth(220);
            btnEditSection.setOnMouseClicked(event -> handleEditSection(s, btnEditSection,bp));
            vboxSectionAdding.getChildren().add(0, btnEditSection);
            vboxSectionAdding.getChildren().add(0, bp);
        }
    }

    private void handleEditSection(Section s, Node btn, Node section) {
        AddSectionView sectionView = new AddSectionView();
        sectionView.setCurrentSection(s);
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setController(sectionView);
        loader.setLocation(getClass().getResource("/GUI/View/AddSectionView.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open Add Section Window", ButtonType.CANCEL);
            alert.showAndWait();
        }
        vboxSectionAdding.getChildren().removeAll(btn,section);
        updateSectionInfo();
    }
}
