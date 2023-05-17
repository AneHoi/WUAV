package GUI.Controller;

import BE.LoginDetails;
import BE.Report;
import GUI.Model.Model;
import javafx.event.ActionEvent;
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

    private void updateTextFields() {
        txtComponent.setText(currentLoginDetails.getComponent());
        txtUsername.setText(currentLoginDetails.getUsername());
        txtPassword.setText(currentLoginDetails.getPassword());
        txtAdditionalInfo.setText(currentLoginDetails.getAdditionalInfo());
    }

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

    public void handleSaveLoginDetails(ActionEvent actionEvent) {
        if(checkBoxNoLogin.isSelected() && currentLoginDetails != null) {
            updateWithNoLogin();
        } else if (!checkBoxNoLogin.isSelected() && currentLoginDetails != null) {
            updateLoginDetails();
        } else if (checkBoxNoLogin.isSelected() && currentLoginDetails == null) {
            noLogin();
        } else if (!checkBoxNoLogin.isSelected() && currentLoginDetails == null) {
            saveLoginDetails();
        }
        Stage stage = (Stage) btnSaveLoginDetails.getScene().getWindow();
        stage.close();
    }

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
            alert.showAndWait();
        }

    }

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
            alert.showAndWait();
        }

    }

    private void updateWithNoLogin() {
        LocalDate createdDate = LocalDate.now();
        LocalTime createdTime = LocalTime.now();
        int userID = controllerAssistant.getLoggedInUser().getUserID();
        try {
            model.updateToNoLogin(currentLoginDetails.getLoginDetailsID(), createdDate, createdTime, userID);
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not save info to database", ButtonType.OK);
            alert.showAndWait();
        }
    }


    private void noLogin() {
        LocalDate createdDate = LocalDate.now();
        LocalTime createdTime = LocalTime.now();
        int userID = controllerAssistant.getLoggedInUser().getUserID();
        try {
            model.noLoginInfoForThisReport(currentReport.getReportID(), createdDate, createdTime, userID);
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not save info to report", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void setCurrentReport(Report currentReport) {
        this.currentReport = currentReport;
    }

    public void setCurrentLoginDetails(LoginDetails ld) {
        this.currentLoginDetails = ld;
    }
}
