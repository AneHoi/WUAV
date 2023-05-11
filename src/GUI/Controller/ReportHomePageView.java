package GUI.Controller;

import BE.*;
import GUI.Model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
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

    @FXML
    private Button btnAddSketch, btnAddImage, btnAddTextField;
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
    private int nextPosition;
    private Model model;
    private ControllerAssistant controllerAssistant;
    private String back = "data/Images/Backward.png";
    private String forward = "data/Images/Forward.png";


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controllerAssistant = ControllerAssistant.getInstance();
        model = Model.getInstance();
        nextPosition = 0;
        currentReport = model.getCurrentReport();
        imgBack.setImage(loadImages(back));
        imgBack.setOnMouseClicked(event -> goBack());
        imgForward.setImage(loadImages(forward));
        imgForward.setDisable(true);
        currentCase = model.getCurrentCase();
        currentCustomer = model.getCurrentCustomer();
        currentAddendum = model.getCurrentAddendum();
        updateReportInfo();
        updateImagesTextsAndSketches();
    }

    private void updateImagesTextsAndSketches() {
        vboxSectionAdding.getChildren().removeAll();
        int currentReportID = currentReport.getReportID();
        List<TextOnReport> allTextFields = new ArrayList<>();
        List<ImageOnReport> allImagesOnReport = new ArrayList<>();
        try {
            allImagesOnReport = model.getAllImagesForReport(currentReportID);
            allTextFields = model.getAllTextFieldsForReport(currentReportID);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not get images and text fields", ButtonType.CANCEL);
            alert.showAndWait();
        }
        for (TextOnReport text : allTextFields) {
            BorderPane bp = new BorderPane();
            bp.setPrefWidth(800);
            bp.setStyle("-fx-border-width: 3");
            bp.setStyle("-fx-border-color: BLACK");
            Label lblText = new Label(text.getText());
            bp.setCenter(lblText);
            VBox vbBottom = new VBox();
            Label lblCreatedBy = new Label("Added by: " + text.getAddedByTech().getFullName());
            Label lblCreatedON = new Label("Added on: "+ text.getCreatedDate() + " - " + text.getCreatedTime());
            vbBottom.getChildren().addAll(lblCreatedBy,lblCreatedON);
            vbBottom.setAlignment(Pos.BOTTOM_RIGHT);
            bp.setBottom(vbBottom);
            vboxSectionAdding.getChildren().add(text.getPositionOnReport(),bp);
            nextPosition++;
        }
        for (ImageOnReport image : allImagesOnReport) {
            BorderPane bp = new BorderPane();
            bp.setPrefWidth(1050);
            bp.setStyle("-fx-border-width: 3");
            bp.setStyle("-fx-border-color: BLACK");
            VBox vbCenter = new VBox();
            ImageView imageView = new ImageView();
            Label lblText = new Label(image.getImageComment());
            vbCenter.getChildren().addAll(imageView,lblText);
            vbCenter.setAlignment(Pos.CENTER);
            bp.setCenter(vbCenter);
            VBox vbBottom = new VBox();
            Label lblCreatedBy = new Label("Added by: " + image.getAddedByTech().getFullName());
            Label lblCreatedON = new Label("Added on: "+ image.getCreatedDate() + " - " + image.getCreatedTime());
            vbBottom.getChildren().addAll(lblCreatedBy,lblCreatedON);
            vbBottom.setAlignment(Pos.BOTTOM_RIGHT);
            bp.setBottom(vbBottom);
            vboxSectionAdding.getChildren().add(image.getPositionOnReport(),bp);
            nextPosition++;
        }


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


    public void handleAddTextField(ActionEvent actionEvent) {
        AddTextFieldView addTextFieldView = new AddTextFieldView();
        addTextFieldView.setCurrentReport(currentReport);
        addTextFieldView.setNextAvailablePosition(nextPosition);
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/GUI/View/AddTextFieldView.fxml"));
        loader.setController(addTextFieldView);
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open Add Text Window", ButtonType.CANCEL);
            alert.showAndWait();
        }
        updateImagesTextsAndSketches();
    }

    public void handleAddImage(ActionEvent actionEvent) {
        AddImageView addImageView = new AddImageView();
        addImageView.setCurrentReport(currentReport);
        addImageView.setNextAvailablePosition(nextPosition);
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/GUI/View/AddImageView.fxml"));
        loader.setController(addImageView);
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open Add Image Window", ButtonType.CANCEL);
            alert.showAndWait();
        }
        updateImagesTextsAndSketches();
    }

    public void handleAddSketch(ActionEvent actionEvent) {
    }
}
