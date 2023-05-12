package GUI.Controller;

import BE.*;
import GUI.Model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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

public class ReportHomePageController implements Initializable {

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
    private int nextPosition;
    private Model model;
    private ControllerAssistant controllerAssistant;
    private String back = "data/Images/Backward.png";
    private String forward = "data/Images/Forward.png";

    private DropShadow shadow = new DropShadow(0, 4, 4, Color.color(0, 0, 0, 0.25));


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
        updateReportInfo();
        updateImagesTextsAndSketches();
        addShadow(btnAddImage, btnAddSketch, btnAddTextField);
    }

    private void updateImagesTextsAndSketches() {
        vboxSectionAdding.getChildren().clear();
        int currentReportID = currentReport.getReportID();
        List<TextsAndImagesOnReport> textsAndImagesOnReports = new ArrayList<>();
        try {
            textsAndImagesOnReports = model.getImagesAndTextsForReport(currentReportID);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not get images and text fields", ButtonType.CANCEL);
            alert.showAndWait();
        }
        for (TextsAndImagesOnReport textOrImage : textsAndImagesOnReports) {
            if (textOrImage.getTextOrImage().equals("Text")) {
                setUpTextField(textOrImage);
            } else {
                setUpImageField(textOrImage);
            }
            nextPosition++;
        }
    }

    private void setUpImageField(TextsAndImagesOnReport textOrImage) {
        try {
            textOrImage.setImageWithBytes(textOrImage.getImageData());
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Image data not found", ButtonType.CANCEL);
            alert.showAndWait();
        }
        BorderPane bp = new BorderPane();
        VBox vbRight = new VBox();
        Button btnEdit = new Button();
        Button btnDelete = new Button();
        ImageView imgViewEdit = new ImageView();
        ImageView imgViewDelete = new ImageView();
        imgViewEdit.setImage(loadImages("data/Images/Edit.png"));
        imgViewDelete.setImage(loadImages("data/Images/Trash Can.png"));
        imgViewDelete.setFitHeight(40);
        imgViewDelete.setFitWidth(40);
        imgViewEdit.setFitWidth(40);
        imgViewEdit.setFitHeight(40);
        btnEdit.setGraphic(imgViewEdit);
        btnEdit.setOnAction(event -> editImage(textOrImage));
        btnEdit.getStyleClass().add("orangeButtons");
        btnDelete.setGraphic(imgViewDelete);
        btnDelete.setOnAction(event -> deletePartOfReport(textOrImage));
        btnDelete.getStyleClass().add("orangeButtons");
        btnEdit.setText(null);
        btnDelete.setText(null);
        addShadow(btnDelete, btnEdit);
        vbRight.getChildren().addAll(btnEdit, btnDelete);
        vbRight.setSpacing(10);
        vbRight.setPadding(new Insets(10));
        bp.setRight(vbRight);
        bp.setPrefWidth(700);
        bp.setStyle("-fx-border-width: 3");
        bp.setStyle("-fx-border-color: BLACK");
        VBox vbCenter = new VBox();
        ImageView imageView = new ImageView();
        imageView.setImage(textOrImage.getImage());
        Label lblText = new Label(textOrImage.getImageComment());
        vbCenter.getChildren().addAll(imageView, lblText);
        vbCenter.setAlignment(Pos.CENTER);
        bp.setCenter(vbCenter);
        VBox vbBottom = new VBox();
        Label lblCreatedBy = new Label("Added by: " + textOrImage.getAddedByTech().getFullName());
        Label lblCreatedON = new Label("Added on: " + textOrImage.getCreatedDate() + " - " + textOrImage.getCreatedTime());
        vbBottom.getChildren().addAll(lblCreatedBy, lblCreatedON);
        vbBottom.setAlignment(Pos.BOTTOM_RIGHT);
        bp.setBottom(vbBottom);
        vboxSectionAdding.getChildren().add(bp);
    }


    private void setUpTextField(TextsAndImagesOnReport textOrImage) {
        BorderPane bp = new BorderPane();
        VBox vbRight = new VBox();
        Button btnEdit = new Button();
        Button btnDelete = new Button();
        ImageView imgViewEdit = new ImageView();
        ImageView imgViewDelete = new ImageView();
        imgViewEdit.setImage(loadImages("data/Images/Edit.png"));
        imgViewDelete.setImage(loadImages("data/Images/Trash Can.png"));
        imgViewDelete.setFitHeight(40);
        imgViewDelete.setFitWidth(40);
        imgViewEdit.setFitWidth(40);
        imgViewEdit.setFitHeight(40);
        btnEdit.setGraphic(imgViewEdit);
        btnEdit.setOnAction(event -> editText(textOrImage));
        btnEdit.getStyleClass().add("orangeButtons");
        btnDelete.setGraphic(imgViewDelete);
        btnDelete.setOnAction(event -> deletePartOfReport(textOrImage));
        btnDelete.getStyleClass().add("orangeButtons");
        btnEdit.setText(null);
        btnDelete.setText(null);
        addShadow(btnDelete, btnEdit);
        vbRight.getChildren().addAll(btnEdit, btnDelete);
        vbRight.setSpacing(10);
        vbRight.setPadding(new Insets(10));
        bp.setRight(vbRight);
        bp.setPrefWidth(700);
        bp.setStyle("-fx-border-width: 3");
        bp.setStyle("-fx-border-color: BLACK");
        Label lblText = new Label(textOrImage.getText());
        bp.setCenter(lblText);
        VBox vbBottom = new VBox();
        Label lblCreatedBy = new Label("Added by: " + textOrImage.getAddedByTech().getFullName());
        Label lblCreatedON = new Label("Added on: " + textOrImage.getCreatedDate() + " - " + textOrImage.getCreatedTime());
        vbBottom.getChildren().addAll(lblCreatedBy, lblCreatedON);
        vbBottom.setAlignment(Pos.BOTTOM_RIGHT);
        bp.setBottom(vbBottom);
        vboxSectionAdding.getChildren().add(bp);
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
        AddTextFieldController addTextFieldController = new AddTextFieldController();
        addTextFieldController.setCurrentReport(currentReport);
        addTextFieldController.setNextAvailablePosition(nextPosition);
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/GUI/View/AddTextFieldView.fxml"));
        loader.setController(addTextFieldController);
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
        AddImageController addImageController = new AddImageController();
        addImageController.setCurrentReport(currentReport);
        addImageController.setNextAvailablePosition(nextPosition);
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/GUI/View/AddImageView.fxml"));
        loader.setController(addImageController);
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

    private void addShadow(Node... node) {
        for (Node nodes : node) {
            nodes.setEffect(shadow);
        }
    }

    private void removeShadow(Node... node) {
        for (Node nodes : node) {
            nodes.setEffect(null);
        }
    }

    private void deletePartOfReport(TextsAndImagesOnReport textOrImage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this part of the report?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            try {
                model.deletePartOfReport(textOrImage);
                updateImagesTextsAndSketches();
            } catch (SQLException e) {
                e.printStackTrace();
                Alert alert1 = new Alert(Alert.AlertType.ERROR, "Could not delete part in database", ButtonType.CANCEL);
                alert1.showAndWait();
            }
        }
    }

    private void editImage(TextsAndImagesOnReport textOrImage) {
        AddImageController addImageController = new AddImageController();
        addImageController.setCurrentReport(currentReport);
        addImageController.setNextAvailablePosition(nextPosition);
        addImageController.setCurrentImage(textOrImage);
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/GUI/View/AddImageView.fxml"));
        loader.setController(addImageController);
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

    private void editText(TextsAndImagesOnReport textOrImage) {
        AddTextFieldController addTextFieldController = new AddTextFieldController();
        addTextFieldController.setCurrentReport(currentReport);
        addTextFieldController.setNextAvailablePosition(nextPosition);
        addTextFieldController.setCurrentText(textOrImage);
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/GUI/View/AddTextFieldView.fxml"));
        loader.setController(addTextFieldController);
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
}
