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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ReportHomePageView implements Initializable {
    public Button btnAddSection;
    @FXML
    private ImageView imgBack, imgForward;
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
    private ControllerAssistant controllerAssistant;
    private String back = "data/Images/Backward.png";
    private String forward = "data/Images/Forward.png";


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controllerAssistant = ControllerAssistant.getInstance();
        model = Model.getInstance();
        currentReport = model.getCurrentReport();
        imgBack.setImage(loadImages(back));
        imgBack.setOnMouseClicked(event -> goBack());
        imgForward.setImage(loadImages(forward));
        imgForward.setDisable(true);
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
        vboxSectionAdding.getChildren().clear();
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
            sketch.setImage(s.getSketch());
            sketch.setFitHeight(80);
            sketch.setFitWidth(80);
            Label sketchComment = new Label(s.getSketchComment());
            ImageView image = new ImageView();
            image.setImage(s.getImage());
            image.setFitHeight(80);
            image.setFitWidth(80);
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
            Button btnDeleteSection = new Button("Delete Section");
            Button btnEditSection = new Button("Edit Section");
            HBox btnHBox = new HBox(btnEditSection, btnDeleteSection);
            btnEditSection.getStyleClass().add("AddAndEditSectionButtons");
            btnEditSection.setUnderline(true);
            btnEditSection.setPrefHeight(30);
            btnEditSection.setPrefWidth(150);
            btnEditSection.setOnMouseClicked(event -> handleEditSection(s));
            btnDeleteSection.getStyleClass().add("AddAndEditSectionButtons");
            btnDeleteSection.setUnderline(true);
            btnDeleteSection.setPrefHeight(30);
            btnDeleteSection.setPrefWidth(150);
            btnDeleteSection.setOnMouseClicked(event -> handleDeleteEvent(s, btnHBox, bp));
            btnHBox.setSpacing(20);
            vboxSectionAdding.getChildren().add(0, btnHBox);
            vboxSectionAdding.getChildren().add(0, bp);

        }
        vboxSectionAdding.getChildren().add(btnAddSection);
    }

    private void handleDeleteEvent(Section s, Node btnHBox, BorderPane bp) {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to delete this section?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            try {
                model.deleteSection(s.getSectionID());
            } catch (SQLException e) {
                e.printStackTrace();
                Alert exceptionAlert = new Alert(Alert.AlertType.ERROR, "Could not delete Section" + e, ButtonType.CANCEL);
                exceptionAlert.showAndWait();
            }
        } else {
            // Do nothing
        }
        updateSectionInfo();
    }

    private void handleEditSection(Section s) {
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
        updateSectionInfo();
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

    private void goBack() {
        try {
            controllerAssistant.loadCenter("CaseHomePageView.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not go back", ButtonType.OK);
            alert.showAndWait();
        }
    }


}
