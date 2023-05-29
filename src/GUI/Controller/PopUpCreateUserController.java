package GUI.Controller;

import GUI.Controller.Util.ControllerAssistant;
import GUI.Controller.Util.Util;
import GUI.Model.Model;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PopUpCreateUserController implements Initializable {
    @FXML
    private Button btnCreateNewUser, btnChooseImage;
    @FXML
    private ComboBox cbUserTypeCreate;
    @FXML
    private TextField txtFullNameCreate, txtUserNameCreate, txtTelephoneCreate, txtEmailCreate;

    @FXML
    private ImageView imgViewChooseImage;

    private byte[] dataImage;

    private ObservableList<String> userTypes;
    private Model model;
    private Util util = new Util();

    private ControllerAssistant controllerAssistant;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        controllerAssistant = ControllerAssistant.getInstance();
        addListeners();
        userTypes = FXCollections.observableArrayList();
        util.addShadow(txtFullNameCreate, txtUserNameCreate, txtTelephoneCreate, txtEmailCreate, cbUserTypeCreate);
        btnCreateNewUser.setDisable(true);
        cbUserTypeCreate.setItems(userTypes);
        userTypes = util.checkLoggedInUser(userTypes);
    }

    /**
     * Adds listeners to the input fields and the user type selection in the create new customer form.
     * The listeners are triggered when there are changes in the text or selection, and they call the createNewCustomerBtn method.
     */
    private void addListeners() {
        txtFullNameCreate.textProperty().addListener(createNewCustomerBtn);
        txtUserNameCreate.textProperty().addListener(createNewCustomerBtn);
        txtTelephoneCreate.textProperty().addListener(createNewCustomerBtn);
        txtEmailCreate.textProperty().addListener(createNewCustomerBtn);
        cbUserTypeCreate.getSelectionModel().selectedItemProperty().addListener(createNewCustomerBtn);
    }

    /**
     * Checks if the text fields are empty if they are btnCreateNewUser is disabled otherwise btnCreateNewUser is enabled
     */
    ChangeListener<String> createNewCustomerBtn = (observable, oldValue, newValue) -> {
        if (txtFullNameCreate.getText().isEmpty() || txtUserNameCreate.getText().isEmpty() || txtTelephoneCreate.getText().isEmpty() || txtEmailCreate.getText().isEmpty() || cbUserTypeCreate.getSelectionModel().getSelectedItem() == null) {
            btnCreateNewUser.setDisable(true);
            util.removeShadow(btnCreateNewUser);
        } else {
            btnCreateNewUser.setDisable(false);
            util.addShadow(btnCreateNewUser);
        }
    };

    /**
     * Gets values from input fields, checks usertype creates new user with or without picture. Closes the window.
     */
    public void handleCreateNewUser(ActionEvent actionEvent) {
        String fullName = txtFullNameCreate.getText();
        String userName = txtUserNameCreate.getText();
        String userTlf = txtTelephoneCreate.getText();
        String userEmail = txtEmailCreate.getText();
        String password = "WUAV1234";
        byte[] profilePicture = dataImage;
        int userType = 0;
        switch ((String) cbUserTypeCreate.getSelectionModel().getSelectedItem()) {
            case "Admin":
                userType = 1;
                break;
            case "ProjectManager":
                userType = 2;
                break;
            case "Technician":
                userType = 3;
                break;
            case "SalesRepresentative":
                userType = 4;
        }
        try {
            if (profilePicture != null) {
                model.createNewUserWithImage(fullName, userName, userTlf, userEmail, userType, profilePicture);
            } else {
                model.createNewUser(fullName, userName, userTlf, userEmail, userType);
            }

            model.setPassword(userName, password);
        } catch (
                SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not create User", ButtonType.CANCEL);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add("/GUI/View/css/Main.css");
            dialogPane.getStyleClass().add("dialog");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not create password", ButtonType.CANCEL);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add("/GUI/View/css/Main.css");
            dialogPane.getStyleClass().add("dialog");
            alert.showAndWait();
        }
        //This closes the window
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }

    /**
     * Opens file chooser for choosing an image, when image is chosen closes the window. Sets Image in imgViewChooseImage image view.
     * Reads the chosen file into  a byte array
     */
    public void handleChooseImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        Stage stage = (Stage) btnChooseImage.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null && selectedFile.getName().endsWith(".png") || selectedFile != null && selectedFile.getName().endsWith(".jpg") || selectedFile != null && selectedFile.getName().endsWith(".gif")) {
            Image image = new Image(selectedFile.toURI().toString());
            imgViewChooseImage.setImage(image);
            btnChooseImage.setText("Change Image");
        }
        try {
            if (selectedFile != null) {
                dataImage = Files.readAllBytes(selectedFile.getAbsoluteFile().toPath());
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load image", ButtonType.OK);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add("/GUI/View/css/Main.css");
            dialogPane.getStyleClass().add("dialog");
            alert.showAndWait();
        }
    }
}
