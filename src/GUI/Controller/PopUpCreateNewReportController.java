package GUI.Controller;

import BE.Case;
import BE.Report;
import GUI.Controller.Util.ControllerAssistant;
import GUI.Controller.Util.Util;
import GUI.Model.Model;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PopUpCreateNewReportController implements Initializable {
    @FXML
    private TextField txtReportName;
    @FXML
    private TextArea txtReportDescription;
    @FXML
    private Button btnCreateNewReport;
    @FXML
    private Label lblTitle, lblReportName;
    private ControllerAssistant controllerAssistant;
    private Model model;
    private Case currentCase;
    private Report currentReport;
    private Util util = new Util();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        controllerAssistant = ControllerAssistant.getInstance();
        checkCreateOrUpdate();

    }

    /**
     * Checks if a current report exists and performs operations accordingly.
     * If a current report exists, it sets the report information, adds shadow effect to UI elements,
     * and associates listeners. Otherwise, it adds listeners, sets opacity and disables buttons, and adds shadow effect.
     */
    private void checkCreateOrUpdate() {
        if (currentReport != null) {
            setCurrentReportInfo();
            util.addShadow(txtReportDescription,txtReportName,btnCreateNewReport);

        } else {
            addListeners();
            lblReportName.setOpacity(0);
            btnCreateNewReport.setDisable(true);
            util.addShadow(txtReportDescription, txtReportName);
        }
    }

    /**
     * Sets the current report information to UI elements for updating.
     * Updates title, report name label, and button text.
     * Fills report name and description fields.
     */
    private void setCurrentReportInfo() {
        lblTitle.setText("Update Report");
        lblReportName.setText(currentReport.getReportName());
        btnCreateNewReport.setDisable(false);
        btnCreateNewReport.setText("Update Report");
        txtReportName.setText(currentReport.getReportName());
        txtReportDescription.setText(currentReport.getReportDescription());

    }

    /**
     * Setter for the current case.
     */
    public void setCurrentCase(Case currentCase) {
        this.currentCase = currentCase;
    }

    /**
     * Adds listeners to report name and report description text fields.
     */
    private void addListeners() {
        txtReportName.textProperty().addListener(createNewReportBtnListener);
        txtReportDescription.textProperty().addListener(createNewReportBtnListener);
    }

    /**
     * If report name and report description txt fields is empty btnCreateNewReport is disabled and shadows are removed.
     * Otherwise, btnCreateNewReport is enabled and shadows are added
     */
    ChangeListener<String> createNewReportBtnListener = (observable, oldValue, newValue) -> {
        if (txtReportName.getText().isEmpty() || txtReportDescription.getText().isEmpty()) {
            btnCreateNewReport.setDisable(true);
            util.removeShadow(btnCreateNewReport);
        } else {
            btnCreateNewReport.setDisable(false);
            util.addShadow(btnCreateNewReport);
        }
    };

    /**
     * If create new report button text is update report, updateReport method is called. Otherwise createNewReport method is called.
     */
    public void handleCreateNewReport() {
        if (btnCreateNewReport.getText().equals("Update Report")) {
            updateReport();
        } else {
            createNewReport();
        }

    }

    /**
     * Creates a new report using the provided report name, description, and current case ID.
     * The report is associated with the ID of the logged-in user.
     * If the report creation is successful, the current window is closed.
     */
    private void createNewReport() {
        String reportName = txtReportName.getText();
        String reportDescription = txtReportDescription.getText();
        int caseID = currentCase.getCaseID();
        try {
            model.createNewReport(reportName, reportDescription, caseID, controllerAssistant.getLoggedInUser().getUserID());
            Stage stage = (Stage) btnCreateNewReport.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not create a new report", ButtonType.CANCEL);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add("/GUI/View/css/Main.css");
            dialogPane.getStyleClass().add("dialog");
            alert.showAndWait();
        }
    }

     /**
     * Updates the current report with the provided report name and description.
     * The report ID is obtained from the current report.
     * The update operation is performed using the model, associating it with the ID of the logged-in user.
     * If the report update is successful, the current window is closed.
     */
    private void updateReport() {
        int reportID = currentReport.getReportID();
        String reportName = txtReportName.getText();
        String reportDescription = txtReportDescription.getText();
        try {
            model.updateReport(reportID, reportName, reportDescription, controllerAssistant.getLoggedInUser().getUserID());
            Stage stage = (Stage) btnCreateNewReport.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not update report in database", ButtonType.CANCEL);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add("/GUI/View/css/Main.css");
            dialogPane.getStyleClass().add("dialog");
            alert.showAndWait();
        }
    }

    /**
     * Setter for currentReport.
     */
    public void setCurrentReport(Report selectedItem) {
        currentReport = selectedItem;
    }
}
