package GUI.Controller;

import BE.*;
import BLL.util.PDFGenerator;
import GUI.Controller.Util.ControllerAssistant;
import GUI.Controller.Util.Util;
import GUI.Model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ReportHomePageController implements Initializable {

    @FXML
    private Button btnAddSketch, btnAddImage, btnAddTextField, btnAddLoginDetails, btnSubmitReportForReview;
    @FXML
    private ImageView imgBack;
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
    private Util util = new Util();
    private DropShadow shadow = new DropShadow(0, 4, 4, Color.color(0, 0, 0, 0.25));


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controllerAssistant = ControllerAssistant.getInstance();
        model = Model.getInstance();
        nextPosition = 0;
        currentReport = model.getCurrentReport();
        lblReportStatus.setText(currentReport.getIsActive());
        imgBack.setImage(util.loadImages(back));
        imgBack.setOnMouseClicked(event -> goBack());
        currentCase = model.getCurrentCase();
        currentCustomer = model.getCurrentCustomer();
        updateReport();
    }

    /**
     * Calls updateReportInfo, updateImagesAndSketches, updateLoginDetails and checkForReportStatus methods.
     */
    private void updateReport() {
        updateReportInfo();
        updateImagesTextsAndSketches();
        updateLoginDetails();
        checkForReportStatus();
    }

    /**
     * Creates list of login details, gets login details from current report, inserts borderpane with no login details message or all login details into a borderpane
     */
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
                vBoxGraphics(ld, bp);
                vboxAddingLoginDetails.getChildren().add(bp);
            } else {
                BorderPane bp = new BorderPane();
                vBoxGraphics(ld, bp);
                VBox vbCenter = new VBox();
                vbCenter.setStyle("-fx-border-width: 3");
                vbCenter.setStyle("-fx-border-color: BLACK");
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

    /**
     * Sets up vboxes with images, text or login detail and adds edit and delete buttons
     */
    private void vBoxGraphics(LoginDetails ld, BorderPane bp) {
        VBox vbRight = new VBox();
        Button btnEdit = new Button();
        Button btnDelete = new Button();
        ImageView imgViewEdit = new ImageView();
        ImageView imgViewDelete = new ImageView();
        heightsAndWidthImageView(btnEdit, imgViewEdit, imgViewDelete);
        btnEdit.setOnAction(event -> editLoginDetails(ld));
        btnEdit.getStyleClass().add("orangeButtons");
        btnDelete.setGraphic(imgViewDelete);
        btnDelete.setOnAction(event -> deleteLoginDetails(ld));
        btnDelete.getStyleClass().add("orangeButtons");
        btnEdit.setText(null);
        btnDelete.setText(null);
        util.addShadow(btnDelete, btnEdit);
        vbRight.getChildren().addAll(btnEdit, btnDelete);
        vbRight.setSpacing(10);
        vbRight.setPadding(new Insets(10));
        bp.setRight(vbRight);
        bp.setPrefWidth(700);
    }

    /**
     * sets height and width for images in imageview
     */
    private void heightsAndWidthImageView(Button btnEdit, ImageView imgViewEdit, ImageView imgViewDelete) {
        imgViewEdit.setImage(util.loadImages("data/Images/Edit.png"));
        imgViewDelete.setImage(util.loadImages("data/Images/Trash Can.png"));
        imgViewDelete.setFitHeight(20);
        imgViewDelete.setFitWidth(20);
        imgViewEdit.setFitWidth(20);
        imgViewEdit.setFitHeight(20);
        btnEdit.setGraphic(imgViewEdit);
    }

    /**
     * Open alert to check if login details should be deleted, if yes calls deleteLoginDetails method from model. Calls update report method.
     */
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

    /**
     * Sets addLoginDetailsController with current report as the report, sets current login details as the login details ld.
     * Calls method openLogInDetailsView with this controller as the controller.
     */
    private void editLoginDetails(LoginDetails ld) {
        AddLoginDetailsController addLoginDetailsController = new AddLoginDetailsController();
        addLoginDetailsController.setCurrentReport(currentReport);
        addLoginDetailsController.setCurrentLoginDetails(ld);
        openLogInDetailsView(addLoginDetailsController);
    }

    /**
     * Opens the AddLoginDetailsView, sets controller and calls method updateReport.
     */
    private void openLogInDetailsView(AddLoginDetailsController addLoginDetailsController) {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/GUI/View/AddLoginDetailsView.fxml"));
        loader.setController(addLoginDetailsController);
        util.openNewWindow(stage, loader, "Could not open Add Login Details Window");
        updateReport();
    }

    /**
     * Checks report status.
     *  It disables editing by default and enables editing and updates button properties depending on the report status and the user's type.
     */
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
            util.addShadow(btnSubmitReportForReview);
        } else if (lblReportStatus.getText().equals("Closed")) {
            btnSubmitReportForReview.setText("Generate PDF");
            btnSubmitReportForReview.setDisable(false);
            util.addShadow(btnSubmitReportForReview);
        }
    }

    /**
     * Disables editing by disabling different UI components. Remove shadows from UI components.
     */
    private void disableEditing() {
        btnAddImage.setDisable(true);
        btnAddTextField.setDisable(true);
        btnAddSketch.setDisable(true);
        btnSubmitReportForReview.setDisable(true);
        vboxSectionAdding.setDisable(true);
        util.removeShadow(btnAddImage, btnAddSketch, btnAddTextField, btnAddLoginDetails, btnSubmitReportForReview);
    }

    /**
     * Enables editing by enabling different UI components and adding shadows to the UI components.
     */
    private void enableEditing() {
        btnAddImage.setDisable(false);
        btnAddTextField.setDisable(false);
        btnAddSketch.setDisable(false);
        btnSubmitReportForReview.setDisable(false);
        vboxSectionAdding.setDisable(false);
        util.addShadow(btnAddSketch, btnAddTextField, btnAddLoginDetails, btnAddImage);
    }

    /**
     * Creates list of textsAndImagesOnReports, and inserts data from getImagesAndTextsForReport method in model class from current report id. Returns list.
     */
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

    /**
     * Creates list of LoginDetails, inserts data from getLoginDetails method in model class from current report id. Returns list.
     */
    private List<LoginDetails> loginDetailsList(){
        List<LoginDetails> loginDetails = null;
        try{
            loginDetails = new ArrayList<>();
            loginDetails = model.getLoginDetails(currentReport.getReportID());
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not get login details", ButtonType.CANCEL);
            alert.showAndWait();
        }
        return loginDetails;
    }

    /**
     * This method updates the images, texts, and sketches displayed in the section adding VBox.
     * It clears the existing elements in the VBox, gets the texts and images for the current report from the model,
     * and sets up the corresponding UI elements for each text or image.
     */
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

    /**
     * Sets up the image field UI element for the given TextsAndImagesOnReport object.
     * It gets the image data, creates and configures the necessary UI components, and adds them to the section adding VBox.
     */
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
        setGraphicsForBorderPane(textOrImage, vbLeft, btnUp, btnDown, btnEdit, btnDelete, imgViewEdit, imgViewDelete, imgUp, imgDown);

        btnEdit.setOnAction(event -> editImage(textOrImage));
        stylingButtonAndVbox(textOrImage, vbRight, btnUp, btnDown, btnEdit, btnDelete, imgViewDelete);
        bp.setRight(vbRight);
        bp.setLeft(vbLeft);
        bp.setPrefWidth(700);
        VBox vbCenter = new VBox();
        vbCenter.setStyle("-fx-border-width: 3");
        vbCenter.setStyle("-fx-border-color: BLACK");
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

    /**
     * Sets the graphics for the buttons and VBox on the left side of the BorderPane.
     * It loads the images for the up and down arrows, configures the image views and buttons, and adds them to the specified VBox.
     */
    private void setGraphicsForBorderPane(TextsAndImagesOnReport textOrImage, VBox vbLeft, Button btnUp, Button btnDown, Button btnEdit, Button btnDelete, ImageView imgViewEdit, ImageView imgViewDelete, ImageView imgUp, ImageView imgDown) {
        imgUp.setImage(util.loadImages("data/Images/Up Arrow.png"));
        imgDown.setImage(util.loadImages("data/Images/Down Arrow.png"));
        imgUp.setFitWidth(20);
        imgUp.setFitHeight(20);
        imgDown.setFitWidth(20);
        imgDown.setFitHeight(20);
        btnUp.setGraphic(imgUp);
        btnDown.setGraphic(imgDown);
        btnUp.setOnAction(event -> moveUp(textOrImage));
        btnDown.setOnAction(event -> moveDown(textOrImage));
        vbLeft.getChildren().addAll(btnUp, btnDown);
        vbLeft.setSpacing(10);
        vbLeft.setPadding(new Insets(10));
        heightsAndWidthImageView(btnEdit, imgViewEdit, imgViewDelete);
    }

    /**
     * Applies styling to the buttons and VBox on the right side of the BorderPane.
     * It sets the style class for the edit and delete buttons, configures the delete button's graphic,
     * sets the actions for the delete button, removes the text from the edit and delete buttons,
     * adds shadows to the buttons, and adds the buttons to the specified VBox.
     */
    private void stylingButtonAndVbox(TextsAndImagesOnReport textOrImage, VBox vbRight, Button btnUp, Button btnDown, Button btnEdit, Button btnDelete, ImageView imgViewDelete) {
        btnEdit.getStyleClass().add("orangeButtons");
        btnDelete.setGraphic(imgViewDelete);
        btnDelete.setOnAction(event -> deletePartOfReport(textOrImage));
        btnDelete.getStyleClass().add("orangeButtons");
        btnEdit.setText(null);
        btnDelete.setText(null);
        util.addShadow(btnDelete, btnEdit, btnUp, btnDown);
        vbRight.getChildren().addAll(btnEdit, btnDelete);
        vbRight.setSpacing(10);
        vbRight.setPadding(new Insets(10));
    }

    /**
     * Sets up a text field in the report.
     * Creates a BorderPane to hold the text field and related components,
     * configures the buttons and image views on the left side of the BorderPane,
     * sets the action for the edit button, applies styling to the buttons and VBox on the right side of the BorderPane,
     * configures the text field and labels, and adds the components to the BorderPane and VBox.
     */
    private void setUpTextField(TextsAndImagesOnReport textOrImage) {
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
        setGraphicsForBorderPane(textOrImage, vbLeft, btnUp, btnDown, btnEdit, btnDelete, imgViewEdit, imgViewDelete, imgUp, imgDown);
        btnEdit.setOnAction(event -> editText(textOrImage));
        stylingButtonAndVbox(textOrImage, vbRight, btnUp, btnDown, btnEdit, btnDelete, imgViewDelete);
        bp.setLeft(vbLeft);
        bp.setRight(vbRight);
        bp.setPrefWidth(700);
        Label lblText = new Label(textOrImage.getText());
        BorderPane bpCenter = new BorderPane();
        bpCenter.setCenter(lblText);
        bpCenter.setStyle("-fx-border-width: 3");
        bpCenter.setStyle("-fx-border-color: BLACK");
        VBox vbBottom = new VBox();
        Label lblCreatedBy = new Label("Added by: " + textOrImage.getAddedByTech().getFullName());
        Label lblCreatedON = new Label("Added on: " + textOrImage.getCreatedDate() + " - " + textOrImage.getCreatedTime());
        vbBottom.getChildren().addAll(lblCreatedBy, lblCreatedON);
        vbBottom.setAlignment(Pos.BOTTOM_RIGHT);
        bpCenter.setBottom(vbBottom);
        bp.setCenter(bpCenter);
        vboxSectionAdding.getChildren().add(bp);
    }

    /**
     * Moves a text or image item up in the report.
     * It retrieves the ID and position of the text or image item,
     * calls the model to move the item up in the database,
     * handles any SQL exceptions or illegal state exceptions that may occur,
     * updates the report after the item has been moved.
     */
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

    /**
     * Moves a text or image item down in the report.
     * It retrieves the ID and position of the text or image item,
     * calls the model to move the item down in the database,
     * handles any SQL exceptions or illegal state exceptions that may occur,
     * updates the report after the item has been moved.
     */
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

    /**
     * Updates the information displayed in the report.
     * Sets the appropriate labels with the current customer, report, and case information.
     */
    private void updateReportInfo() {
        lblCustomerName.setText(currentCustomer.getCustomerName());
        lblReportName.setText(" "+currentReport.getReportName());
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

    /**
     * Navigates back to the Case Home Page view.
     * Calls the controllerAssistant to load the Case Home Page view,
     */
    private void goBack() {
        try {
            controllerAssistant.loadCenter("CaseHomePageView.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not go back", ButtonType.OK);
            alert.showAndWait();
        }
    }

    /**
     * Handles the action event for adding a text field to the report.
     * It creates an instance of AddTextFieldController, sets the necessary data (current report and next available position),
     * opens a new window for adding the text field using the controller and FXMLLoader,
     * and updates the report after the window is closed.
     */
    public void handleAddTextField(ActionEvent actionEvent) {
        AddTextFieldController addTextFieldController = new AddTextFieldController();
        addTextFieldController.setCurrentReport(currentReport);
        addTextFieldController.setNextAvailablePosition(nextPosition);
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/GUI/View/AddTextFieldView.fxml"));
        loader.setController(addTextFieldController);
        util.openNewWindow(stage, loader, "Could not open Add Text Window");
        updateReport();
    }

    /**
     * Handles the action event for adding an image to the report.
     * It creates an instance of AddImageController, sets the necessary data (current report and next available position),
     * opens the add image window using the openAddImage method,
     * and updates the report after the window is closed.
     */
    public void handleAddImage(ActionEvent actionEvent) {
        AddImageController addImageController = new AddImageController();
        addImageController.setCurrentReport(currentReport);
        addImageController.setNextAvailablePosition(nextPosition);
        openAddImage(addImageController);
        updateReport();
    }

    /**
     * Opens the Add Image window by creating a new stage, loading the AddImageView.fxml file,
     * setting the AddImageController as the controller, and calling the openNewWindow method of the util object.
     * It takes an instance of AddImageController as a parameter to pass it to the controller of the Add Image window.
     */
    private void openAddImage(AddImageController addImageController) {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/GUI/View/AddImageView.fxml"));
        loader.setController(addImageController);
        util.openNewWindow(stage, loader, "Could not open Add Image Window");
    }

    /**
     * Handles the event when the "Add Sketch" button is clicked. It opens the Draw Sketch window
     * by loading the DrawSketchView.fxml file, setting up the DrawSketchController, creating a new stage, and
     * displaying the window. After the window is closed, it updates the report.
     */
    public void handleAddSketch(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/GUI/View/DrawSketchView.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            DrawSketchController drawSketchController = fxmlLoader.getController();
            drawSketchController.setNextPosition(nextPosition);
            drawSketchController.setCurrentReport(currentReport);
            Stage stage = new Stage();
            stage.setTitle("Draw your sketch here");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open the Drawing window", ButtonType.CANCEL);
            alert.showAndWait();
        }
        updateReport();
    }

    /**
     * Deletes a part of the report (text or image) based on the provided TextsAndImagesOnReport object.
     * It displays a confirmation alert box to ensure the user wants to deletion this. If the yes,
     * the part is deleted from the database using the model's deletePartOfReport method, and the report is updated by
     * calling the updateImagesTextsAndSketches method.
     */
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

    /**
     * Allows the user to edit an image in the report. It opens the Add Image window with the provided
     * TextsAndImagesOnReport object preloaded for editing. The current report, next available position, and the
     * current image are set in the AddImageController. After editing the image, the report's images, texts, and sketches
     * are updated by calling the updateImagesTextsAndSketches method. Calls the updateReport method.
     */
    private void editImage(TextsAndImagesOnReport textOrImage) {
        AddImageController addImageController = new AddImageController();
        addImageController.setCurrentReport(currentReport);
        addImageController.setNextAvailablePosition(nextPosition);
        addImageController.setCurrentImage(textOrImage);
        openAddImage(addImageController);
        updateImagesTextsAndSketches();
        updateReport();
    }

    /**
     * Allows the user to edit a text field in the report. It opens the Add Text Field window with the provided
     * TextsAndImagesOnReport object preloaded for editing. The current report, next available position, and the current
     * text field are set in the AddTextFieldController. After editing the text field, the report's images, texts, and
     * sketches are updated by calling the updateImagesTextsAndSketches method. Calls the updateReport method.
     */
    private void editText(TextsAndImagesOnReport textOrImage) {
        AddTextFieldController addTextFieldController = new AddTextFieldController();
        addTextFieldController.setCurrentReport(currentReport);
        addTextFieldController.setNextAvailablePosition(nextPosition);
        addTextFieldController.setCurrentText(textOrImage);
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/GUI/View/AddTextFieldView.fxml"));
        loader.setController(addTextFieldController);
        util.openNewWindow(stage, loader, "Could not open Add Image Window");
        updateImagesTextsAndSketches();
        updateReport();
    }

    /**
     * Handles the submission of the report based on the state of the btnSubmitReportForReview button.
     * If the button's text is "Submit Report", it calls the submitForReview method to submit the report for review.
     * If the button's text is "Close Report", it calls the closeReport method to close the report.
     * If the button's text is "Generate PDF", it calls the generatePDF method with the specified path to generate a PDF version of the report.
     */
    public void handleSubmitReport(ActionEvent event) throws FileNotFoundException {
        if (btnSubmitReportForReview.getText().equals("Submit Report")) {
            submitForReview();
        } else if (btnSubmitReportForReview.getText().equals("Close Report")) {
            closeReport();
        } else if (btnSubmitReportForReview.getText().equals("Generate PDF")) {
            generatePDF(getPath());
        }
    }

    /**
     * Generates a PDF version of the report using the specified path.
     * It creates a PDFGenerator instance and calls the generateReport method to generate the PDF.
     * The method also opens the generated PDF file using the default system PDF viewer.
     */
    private void generatePDF(String path) throws FileNotFoundException {
        PDFGenerator pdfGenerator = new PDFGenerator();

        pdfGenerator.generateReport(currentReport, currentCase, currentCustomer, textsAndImagesOnReportList(), loginDetailsList(), path);
        File file = new File(path +"\\"+ currentReport.getReportName()+".pdf");
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open pdf");
            alert.showAndWait();
        }

    }

    /**
     * Opens a directory chooser dialog to select a folder path.
     * It sets the title of the directory chooser dialog and retrieves the initial directory.
     * If the "Generate PDF" button is clicked, it gets the current window's stage and shows the directory chooser dialog.
     * If a folder is selected, it returns the path of the selected folder, returns the path.
     */
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
        return path;
    }

    /**
     * Closes the report and submits it for review.
     * It displays a confirmation alert to confirm the user wants to proceed.
     * If the user presses yes, the method updates the report and case status, disables editing,
     * changes the text of the submit button to "Generate PDF", and updates the report status label.
     */
    private void closeReport() {
        Alert areYouSureAlert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to submit your report for review?", ButtonType.YES, ButtonType.NO);
        areYouSureAlert.showAndWait();
        if (areYouSureAlert.getResult() == ButtonType.YES) {
            try {
                model.closeReport(currentReport.getReportID());
                model.closeCase(currentCase);
                disableEditing();
                btnSubmitReportForReview.setText("Generate PDF");
                currentReport.setIsActive("Closed");
                lblReportStatus.setText(currentReport.getIsActive());
                checkForReportStatus();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Could not submit report for review");
                alert.showAndWait();
            }
        }
    }

    /**
     * Submits the report for review.
     * It checks if there are any login details added to the report.
     * If there are no login details, it displays an information alert and prompts the user to add login details.
     * After adding login details, the method returns.
     * If there are login details or if the user confirms the submission in the warning alert,
     * the method proceeds to submit the report for review.
     * It updates the report status, disables editing, and updates the report status label.
     */
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
                currentReport.setIsActive("Submitted For Review");
                lblReportStatus.setText(currentReport.getIsActive());
                checkForReportStatus();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Could not submit report for review");
                alert.showAndWait();
            }
        }
    }

    /**
     * Handles the event when the "Add Login Details" button is clicked.
     * It creates an instance of the AddLoginDetailsController and sets the current report.
     * Calls the openLogInDetailsView method to open the login details view.
     */
    public void handleAddLoginDetails(ActionEvent actionEvent) {
        AddLoginDetailsController addLoginDetailsController = new AddLoginDetailsController();
        addLoginDetailsController.setCurrentReport(currentReport);
        openLogInDetailsView(addLoginDetailsController);
    }
}
