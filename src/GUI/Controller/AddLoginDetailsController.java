package GUI.Controller;

import BE.LoginDetails;
import BE.Report;
import GUI.Controller.Util.ControllerAssistant;
import GUI.Model.Model;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class AddLoginDetailsController implements Initializable {

    @FXML
    private TextField txtComponent, txtUsername, txtPassword;
    @FXML
    private TextArea txtAdditionalInfo;
    @FXML
    private CheckBox checkBoxNoLogin;
    @FXML
    private Button btnSaveLoginDetails;
    private Report currentReport;
    private LoginDetails currentLoginDetails;
    private Model model;
    private ControllerAssistant controllerAssistant;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controllerAssistant = ControllerAssistant.getInstance();
        model = Model.getInstance();
        btnSaveLoginDetails.setDisable(true);
        txtAdditionalInfo.setDisable(true);
        txtPassword.setDisable(true);
        txtUsername.setDisable(true);
        if (currentLoginDetails != null) {
            if (currentLoginDetails.isNoLoginDetails()) {
                checkBoxNoLogin.setSelected(true);
                btnSaveLoginDetails.setText("Save Changes");
            } else {
                updateTextFields();
                txtUsername.setDisable(false);
                txtPassword.setDisable(false);
                txtAdditionalInfo.setDisable(false);
                btnSaveLoginDetails.setText("Save Changes");
            }

        }
        setupListeners();
    }

    /**
     * If the edit button was clicked, the textfields are populated with the original text of the loginDetails to be changed.
     */
    private void updateTextFields() {
        txtComponent.setText(currentLoginDetails.getComponent());
        txtUsername.setText(currentLoginDetails.getUsername());
        txtPassword.setText(currentLoginDetails.getPassword());
        txtAdditionalInfo.setText(currentLoginDetails.getAdditionalInfo());
    }

    /**
     * Sets up listeners for the component, username, password and the checkbox.
     */
    private void setupListeners() {
        txtComponent.textProperty().addListener((observable, oldValue, newValue) -> {
            updateFieldsState();
            updateSaveButtonState();
        });

        txtUsername.textProperty().addListener((observable, oldValue, newValue) -> {
            updateSaveButtonState();
        });

        txtPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            updateSaveButtonState();
        });

        checkBoxNoLogin.selectedProperty().addListener((observable, oldValue, newValue) -> {
            updateFieldsState();
            updateSaveButtonState();
        });
    }

    /**
     * If the checkbox is ticked, disable all textfields
     */
    private void updateFieldsState() {
        boolean isNoLoginSelected = checkBoxNoLogin.isSelected();
        txtComponent.setDisable(isNoLoginSelected);
        if (!isNoLoginSelected) {
            boolean hasComponent = !txtComponent.getText().isEmpty();
            txtUsername.setDisable(!hasComponent);
            txtPassword.setDisable(!hasComponent);
            txtAdditionalInfo.setDisable(!hasComponent);
        } else {
            txtUsername.setDisable(true);
            txtPassword.setDisable(true);
            txtAdditionalInfo.setDisable(true);
        }
    }

    /**
     * Update the save button to be either disabled or enabled depending on the listeners
     */
    private void updateSaveButtonState() {
        boolean isNoLoginSelected = checkBoxNoLogin.isSelected();
        boolean hasUsername = !txtUsername.getText().isEmpty();
        boolean hasPassword = !txtPassword.getText().isEmpty();

        if (isNoLoginSelected || (hasUsername && hasPassword)) {
            btnSaveLoginDetails.setDisable(false);
        } else {
            btnSaveLoginDetails.setDisable(true);
        }
    }

    /**
     * Save the login details
     */
    public void handleSaveLoginDetails() {
        if(checkBoxNoLogin.isSelected() && currentLoginDetails != null) {
            updateWithNoLogin(); // Check Box is selected, and old details exist, update to NO login details
        } else if (!checkBoxNoLogin.isSelected() && currentLoginDetails != null) {
            updateLoginDetails(); //Check box is not selected, and old details exist, update to NEW login details
        } else if (checkBoxNoLogin.isSelected() && currentLoginDetails == null) {
            noLogin(); //Check box is selected, and old details do not exist, save as NO login details
        } else if (!checkBoxNoLogin.isSelected() && currentLoginDetails == null) {
            saveLoginDetails(); //Check box is not selected and no old details exist, save NEW login details
        }
        Stage stage = (Stage) btnSaveLoginDetails.getScene().getWindow();
        stage.close();
    }

    /**
     * Save new login details
     */
    private void saveLoginDetails() {
        int reportID = currentReport.getReportID();
        String component = txtComponent.getText();
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String additionalInfo = txtAdditionalInfo.getText();
        LocalDate createdDate = LocalDate.now();
        LocalTime createdTime = LocalTime.now();
        int userID = controllerAssistant.getLoggedInUser().getUserID();
        try {
            model.saveLoginDetails(reportID, component, username, password, additionalInfo, createdDate, createdTime, userID);
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not save info to report", ButtonType.OK);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add("/GUI/View/css/Main.css");
            dialogPane.getStyleClass().add("dialog");
            alert.showAndWait();
        }

    }

    /**
     * Update old login details
     */
    private void updateLoginDetails() {
        int loginDetailsID = currentLoginDetails.getLoginDetailsID();
        String component = txtComponent.getText();
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String additionalInfo = txtAdditionalInfo.getText();
        LocalDate createdDate = LocalDate.now();
        LocalTime createdTime = LocalTime.now();
        int userID = controllerAssistant.getLoggedInUser().getUserID();
        try {
            model.updateLoginDetails(loginDetailsID, component, username, password, additionalInfo, createdDate, createdTime, userID);
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not update info in database", ButtonType.OK);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add("/GUI/View/css/Main.css");
            dialogPane.getStyleClass().add("dialog");
            alert.showAndWait();
        }

    }

    /**
     * Update from old login details to no login details
     */
    private void updateWithNoLogin() {
        LocalDate createdDate = LocalDate.now();
        LocalTime createdTime = LocalTime.now();
        int userID = controllerAssistant.getLoggedInUser().getUserID();
        try {
            model.updateToNoLogin(currentLoginDetails.getLoginDetailsID(), createdDate, createdTime, userID);
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not save info to database", ButtonType.OK);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add("/GUI/View/css/Main.css");
            dialogPane.getStyleClass().add("dialog");
            alert.showAndWait();
        }
    }

    /**
     * Save as no login details
     */
    private void noLogin() {
        LocalDate createdDate = LocalDate.now();
        LocalTime createdTime = LocalTime.now();
        int userID = controllerAssistant.getLoggedInUser().getUserID();
        try {
            model.noLoginInfoForThisReport(currentReport.getReportID(), createdDate, createdTime, userID);
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not save info to report", ButtonType.OK);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add("/GUI/View/css/Main.css");
            dialogPane.getStyleClass().add("dialog");
            alert.showAndWait();
        }
    }

    /**
     * Sets the current report the user is working with
     * @param currentReport
     */
    public void setCurrentReport(Report currentReport) {
        this.currentReport = currentReport;
    }

    /**
     * Sets the current Login Details to be edited
     * @param ld
     */
    public void setCurrentLoginDetails(LoginDetails ld) {
        this.currentLoginDetails = ld;
    }
}
