package GUI.Controller;

import BE.Case;
import BE.Report;
import GUI.Controller.Util.ControllerAssistant;
import GUI.Model.Model;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
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
    private DropShadow shadow = new DropShadow(0, 4, 4, Color.color(0, 0, 0, 0.25));
    private ControllerAssistant controllerAssistant;
    private Model model;
    private Case currentCase;
    private Report currentReport;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        controllerAssistant = ControllerAssistant.getInstance();
        checkCreateOrUpdate();

    }

    private void checkCreateOrUpdate() {
        if (currentReport != null) {
            setCurrentReportInfo();
            addShadow(txtReportDescription,txtReportName,btnCreateNewReport);

        } else {
            addListeners();
            lblReportName.setOpacity(0);
            btnCreateNewReport.setDisable(true);
            addShadow(txtReportDescription, txtReportName);
        }
    }

    private void setCurrentReportInfo() {
        lblTitle.setText("Update Report");
        lblReportName.setText(currentReport.getReportName());
        btnCreateNewReport.setDisable(false);
        btnCreateNewReport.setText("Update Report");
        txtReportName.setText(currentReport.getReportName());
        txtReportDescription.setText(currentReport.getReportDescription());

    }

    public void setCurrentCase(Case currentCase) {
        this.currentCase = currentCase;
    }

    private void addListeners() {
        txtReportName.textProperty().addListener(createNewReportBtnListener);
        txtReportDescription.textProperty().addListener(createNewReportBtnListener);
    }

    ChangeListener<String> createNewReportBtnListener = (observable, oldValue, newValue) -> {
        if (txtReportName.getText().isEmpty() || txtReportDescription.getText().isEmpty()) {
            btnCreateNewReport.setDisable(true);
            removeShadow(btnCreateNewReport);
        } else {
            btnCreateNewReport.setDisable(false);
            addShadow(btnCreateNewReport);
        }
    };


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

    public void handleCreateNewReport(ActionEvent actionEvent) {
        if (btnCreateNewReport.getText().equals("Update Report")) {
            updateReport();
        } else {
            createNewReport();
        }

    }

    private void createNewReport() {
        String reportName = txtReportName.getText();
        String reportDescription = txtReportDescription.getText();
        int caseID = currentCase.getCaseID();
        try {
            model.createNewReport(reportName, reportDescription, caseID, controllerAssistant.getLoggedInUser().getUserID());
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Report successfully created", ButtonType.OK);
            alert.showAndWait();
            Stage stage = (Stage) btnCreateNewReport.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not create a new report", ButtonType.CANCEL);
            alert.showAndWait();
        }
    }

    private void updateReport() {
        int reportID = currentReport.getReportID();
        String reportName = txtReportName.getText();
        String reportDescription = txtReportDescription.getText();
        try {
            model.updateReport(reportID, reportName, reportDescription, controllerAssistant.getLoggedInUser().getUserID());
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Report updated successfully", ButtonType.OK);
            alert.showAndWait();
            Stage stage = (Stage) btnCreateNewReport.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not update report in database", ButtonType.CANCEL);
            alert.showAndWait();
        }
    }

    public void setCurrentReport(Report selectedItem) {
        currentReport = selectedItem;
    }
}
