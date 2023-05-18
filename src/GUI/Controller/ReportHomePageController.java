package GUI.Controller;

import BE.*;
import BLL.util.PDFGenerator;
import GUI.Model.Model;
import com.itextpdf.kernel.colors.Lab;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

public class ReportHomePageController implements Initializable {

    @FXML
    private Button btnAddSketch, btnAddImage, btnAddTextField, btnAddLoginDetails, btnSubmitReportForReview;
    @FXML
    private ImageView imgBack, imgForward;
    @FXML
    private Label lblReportStatus, lblCustomerName, lblReportName, lblCustomerAddress, lblCustomerEmail, lblCustomerTelephone, lblCaseName, lblCaseID, lblCaseCreated, lblCaseTechnicians, lblCaseContactPerson, lblReportDescription;
    @FXML
    private VBox vboxSectionAdding, vboxAddingLoginDetails;
    private Customer currentCustomer;
    private Case currentCase;
    private Report currentReport;
    private int nextPosition;
    private Model model;
    private ControllerAssistant controllerAssistant;
    private String back = "data/Images/Backward.png";
    private String forward = "data/Images/Forward.png";
    private int numberOfLoginDetails;
    private DropShadow shadow = new DropShadow(0, 4, 4, Color.color(0, 0, 0, 0.25));


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controllerAssistant = ControllerAssistant.getInstance();
        model = Model.getInstance();
        nextPosition = 0;
        currentReport = model.getCurrentReport();
        lblReportStatus.setText(currentReport.getIsActive());
        imgBack.setImage(loadImages(back));
        imgBack.setOnMouseClicked(event -> goBack());
        imgForward.setImage(loadImages(forward));
        imgForward.setDisable(true);
        currentCase = model.getCurrentCase();
        currentCustomer = model.getCurrentCustomer();
        updateReport();
    }

    private void updateReport() {
        updateReportInfo();
        updateImagesTextsAndSketches();
        updateLoginDetails();
        checkForReportStatus();
    }

    private void updateLoginDetails() {
        vboxAddingLoginDetails.getChildren().clear();
        List<LoginDetails> loginDetails = new ArrayList<>();
        try {
            loginDetails = model.getLoginDetails(currentReport.getReportID());
            numberOfLoginDetails = loginDetails.size();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not get login details from database", ButtonType.CANCEL);
            alert.showAndWait();
        }
        for (LoginDetails ld : loginDetails) {
            if (ld.isNoLoginDetails()) {
                BorderPane bp = new BorderPane();
                bp.setCenter(new Label("No login details for this report"));
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
                btnEdit.setOnAction(event -> editLoginDetails(ld));
                btnEdit.getStyleClass().add("orangeButtons");
                btnDelete.setGraphic(imgViewDelete);
                btnDelete.setOnAction(event -> deleteLoginDetails(ld));
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
                vboxAddingLoginDetails.getChildren().add(bp);
            } else {
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
                btnEdit.setOnAction(event -> editLoginDetails(ld));
                btnEdit.getStyleClass().add("orangeButtons");
                btnDelete.setGraphic(imgViewDelete);
                btnDelete.setOnAction(event -> deleteLoginDetails(ld));
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
                Label component = new Label("Component:");
                Label componentName = new Label(ld.getComponent());
                HBox hbUserPass = new HBox();
                VBox names = new VBox();
                VBox data = new VBox();
                Label username = new Label("Username: ");
                Label password = new Label("Password: ");
                Label usernameData = new Label(ld.getUsername());
                Label passwordData = new Label(ld.getPassword());
                Label additionalInfo = new Label("Additional Info: ");
                Label additionalInfoData = new Label(ld.getAdditionalInfo());
                names.getChildren().addAll(username, password);
                data.getChildren().addAll(usernameData, passwordData);
                hbUserPass.getChildren().addAll(names, data);
                hbUserPass.setAlignment(Pos.CENTER);
                vbCenter.getChildren().addAll(component, componentName, hbUserPass, additionalInfo, additionalInfoData);
                vbCenter.setAlignment(Pos.CENTER);
                bp.setCenter(vbCenter);
                VBox vbBottom = new VBox();
                Label lblCreatedBy = new Label("Added by: " + ld.getAddedBy().getFullName());
                Label lblCreatedON = new Label("Added on: " + ld.getCreatedDate() + " - " + ld.getCreatedTime());
                vbBottom.getChildren().addAll(lblCreatedBy, lblCreatedON);
                vbBottom.setAlignment(Pos.BOTTOM_RIGHT);
                bp.setBottom(vbBottom);
                vboxAddingLoginDetails.getChildren().add(bp);
            }
        }
    }

    private void deleteLoginDetails(LoginDetails ld) {
        Alert areYouSureAlert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to delete these login details?", ButtonType.YES, ButtonType.NO);
        areYouSureAlert.showAndWait();
        if (areYouSureAlert.getResult() == ButtonType.YES) {
            try {
                model.deleteLoginDetails(ld.getLoginDetailsID());
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Could not delete login details in database", ButtonType.CANCEL);
                alert.showAndWait();
            }
        }
        updateReport();
    }

    private void editLoginDetails(LoginDetails ld) {
        AddLoginDetailsController addLoginDetailsController = new AddLoginDetailsController();
        addLoginDetailsController.setCurrentReport(currentReport);
        addLoginDetailsController.setCurrentLoginDetails(ld);
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/GUI/View/AddLoginDetailsView.fxml"));
        loader.setController(addLoginDetailsController);
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open Add Login Details Window", ButtonType.CANCEL);
            alert.showAndWait();
        }
        updateReport();
    }


    private void checkForReportStatus() {
        disableEditing();
        if (lblReportStatus.getText().equals("Open")
                && ((currentReport.getAssignedTechnician().equals(controllerAssistant.getLoggedInUser().getFullName())
                || controllerAssistant.getLoggedInUser().getUserType() == 1)
                || controllerAssistant.getLoggedInUser().getUserType() == 2)) {
            enableEditing();
        } else if (lblReportStatus.getText().equals("Submitted For Review")
                && (controllerAssistant.getLoggedInUser().getUserType() == 1)
                || controllerAssistant.getLoggedInUser().getUserType() == 2) {
            enableEditing();
            btnSubmitReportForReview.setText("Close Report");
            btnSubmitReportForReview.setDisable(false);
            addShadow(btnSubmitReportForReview);
        } else if (lblReportStatus.getText().equals("Closed")) {
            btnSubmitReportForReview.setText("Generate PDF");
            btnSubmitReportForReview.setDisable(false);
            addShadow(btnSubmitReportForReview);
        }
    }

    private void disableEditing() {
        btnAddImage.setDisable(true);
        btnAddTextField.setDisable(true);
        btnAddSketch.setDisable(true);
        btnSubmitReportForReview.setDisable(true);
        vboxSectionAdding.setDisable(true);
        removeShadow(btnAddImage, btnAddSketch, btnAddTextField, btnAddLoginDetails, btnSubmitReportForReview);
    }


    private void enableEditing() {
        btnAddImage.setDisable(false);
        btnAddTextField.setDisable(false);
        btnAddSketch.setDisable(false);
        btnSubmitReportForReview.setDisable(false);
        vboxSectionAdding.setDisable(false);
        addShadow(btnAddSketch, btnAddTextField, btnAddLoginDetails, btnAddImage);
    }

    public List<TextsAndImagesOnReport> textsAndImagesOnReportList() {
        List<TextsAndImagesOnReport> reportData = null;
        try {
            reportData = new ArrayList<>();
            reportData = model.getImagesAndTextsForReport(currentReport.getReportID());
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not get images and text", ButtonType.CANCEL);
            alert.showAndWait();
        }
        return reportData;
    }

    private void updateImagesTextsAndSketches() {
        vboxSectionAdding.getChildren().clear();
        nextPosition = 0;
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
        VBox vbLeft = new VBox();
        VBox vbRight = new VBox();
        Button btnUp = new Button();
        Button btnDown = new Button();
        Button btnEdit = new Button();
        Button btnDelete = new Button();
        ImageView imgViewEdit = new ImageView();
        ImageView imgViewDelete = new ImageView();
        ImageView imgUp = new ImageView();
        ImageView imgDown = new ImageView();
        imgUp.setImage(loadImages("data/Images/Up Arrow.png"));
        imgDown.setImage(loadImages("data/Images/Down Arrow.png"));
        imgUp.setFitWidth(40);
        imgUp.setFitHeight(40);
        imgDown.setFitWidth(40);
        imgDown.setFitHeight(40);
        btnUp.setGraphic(imgUp);
        btnDown.setGraphic(imgDown);
        btnUp.getStyleClass().add("orangeButtons");
        btnDown.getStyleClass().add("orangeButtons");
        btnUp.setOnAction(event -> moveUp(textOrImage));
        btnDown.setOnAction(event -> moveDown(textOrImage));
        vbLeft.getChildren().addAll(btnUp, btnDown);
        vbLeft.setSpacing(10);
        vbLeft.setPadding(new Insets(10));
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
        addShadow(btnDelete, btnEdit, btnUp, btnDown);
        vbRight.getChildren().addAll(btnEdit, btnDelete);
        vbRight.setSpacing(10);
        vbRight.setPadding(new Insets(10));
        bp.setRight(vbRight);
        bp.setLeft(vbLeft);
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
        VBox vbLeft = new VBox();
        VBox vbRight = new VBox();
        Button btnUp = new Button();
        Button btnDown = new Button();
        Button btnEdit = new Button();
        Button btnDelete = new Button();
        ImageView imgUp = new ImageView();
        ImageView imgDown = new ImageView();
        ImageView imgViewEdit = new ImageView();
        ImageView imgViewDelete = new ImageView();
        imgUp.setImage(loadImages("data/Images/Up Arrow.png"));
        imgDown.setImage(loadImages("data/Images/Down Arrow.png"));
        imgUp.setFitWidth(40);
        imgUp.setFitHeight(40);
        imgDown.setFitWidth(40);
        imgDown.setFitHeight(40);
        btnUp.setGraphic(imgUp);
        btnDown.setGraphic(imgDown);
        btnUp.getStyleClass().add("orangeButtons");
        btnDown.getStyleClass().add("orangeButtons");
        btnUp.setOnAction(event -> moveUp(textOrImage));
        btnDown.setOnAction(event -> moveDown(textOrImage));
        vbLeft.getChildren().addAll(btnUp, btnDown);
        vbLeft.setSpacing(10);
        vbLeft.setPadding(new Insets(10));
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
        addShadow(btnDelete, btnEdit, btnUp, btnDown);
        vbRight.getChildren().addAll(btnEdit, btnDelete);
        vbRight.setSpacing(10);
        vbRight.setPadding(new Insets(10));
        bp.setLeft(vbLeft);
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

    private void moveUp(TextsAndImagesOnReport textOrImage) {
        int textOrImageID = textOrImage.getTextOrImageID();
        int positionOnReport = textOrImage.getPositionOnReport();
        try {
            model.moveItemUp(textOrImageID, positionOnReport);
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not move item up", ButtonType.OK);
            alert.showAndWait();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
        updateReport();
    }


    private void moveDown(TextsAndImagesOnReport textOrImage) {
        int positionOnReport = textOrImage.getPositionOnReport();
        int textOrImageID = textOrImage.getTextOrImageID();
        try {
            model.moveItemDown(textOrImageID, positionOnReport);
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not move item down", ButtonType.OK);
            alert.showAndWait();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
        updateReport();
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
        updateReport();
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
        updateReport();
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
        updateReport();
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
        updateReport();
    }

    public void handleSubmitReport(ActionEvent event) throws FileNotFoundException {
        if (btnSubmitReportForReview.getText().equals("Submit Report")) {
            submitForReview();
        } else if (btnSubmitReportForReview.getText().equals("Close Report")) {
            closeReport();
        } else if (btnSubmitReportForReview.getText().equals("Generate PDF")) {
            generatePDF(getPath());
            //TODO open pdf
        }
    }

    private void generatePDF(String path) throws FileNotFoundException {
        PDFGenerator pdfGenerator = new PDFGenerator();

        pdfGenerator.generateReport(currentReport, currentCase, currentCustomer, textsAndImagesOnReportList(), path);
        File file = new File(path +"\\"+ currentReport.getReportName()+".pdf");
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open pdf");
        }

    }
    private String getPath(){
        String path = "";
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select a folder");
        directoryChooser.getInitialDirectory();
        if(btnSubmitReportForReview.getText().equals("Generate PDF")){
            Stage stage = (Stage) btnSubmitReportForReview.getScene().getWindow();
            File selectedFile = directoryChooser.showDialog(stage);
            if(selectedFile != null){
                path = selectedFile.getPath();
            }
        }
        System.out.println(path);
        return path;
    }


    private void closeReport() {
        Alert areYouSureAlert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to submit your report for review?", ButtonType.YES, ButtonType.NO);
        areYouSureAlert.showAndWait();
        if (areYouSureAlert.getResult() == ButtonType.YES) {
            try {
                model.closeReport(currentReport.getReportID());
                model.closeCase(currentCase);
                disableEditing();
                btnSubmitReportForReview.setText("Generate PDF");
                Alert success = new Alert(Alert.AlertType.INFORMATION, "Report is now closed", ButtonType.OK);
                success.showAndWait();
                currentReport.setIsActive("Closed");
                lblReportStatus.setText(currentReport.getIsActive());
                checkForReportStatus();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Could not submit report for review");
                alert.showAndWait();
            }
        }
    }

    private void submitForReview() {
        if (numberOfLoginDetails < 1) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "This report has no login details added, you need to specify login details before it can be submitted for review.\nIf login details are not needed for this report, please check the box 'No login details for this report' in the next window.", ButtonType.OK);
            alert.showAndWait();
            handleAddLoginDetails(new ActionEvent());
            return;
        }
        Alert areYouSureAlert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to submit your report for review?", ButtonType.YES, ButtonType.NO);
        areYouSureAlert.showAndWait();
        if (areYouSureAlert.getResult() == ButtonType.YES) {
            try {
                model.submitReportForReview(currentReport.getReportID());
                disableEditing();
                Alert success = new Alert(Alert.AlertType.INFORMATION, "Report submitted successfully", ButtonType.OK);
                success.showAndWait();
                currentReport.setIsActive("Submitted For Review");
                lblReportStatus.setText(currentReport.getIsActive());
                checkForReportStatus();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Could not submit report for review");
                alert.showAndWait();
            }
        }
    }

    public void handleAddLoginDetails(ActionEvent actionEvent) {
        AddLoginDetailsController addLoginDetailsController = new AddLoginDetailsController();
        addLoginDetailsController.setCurrentReport(currentReport);
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/GUI/View/AddLoginDetailsView.fxml"));
        loader.setController(addLoginDetailsController);
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open Add Login Details Window", ButtonType.CANCEL);
            alert.showAndWait();
        }
        updateReport();
    }
}
