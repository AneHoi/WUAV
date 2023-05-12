package GUI.Controller;

import BE.Case;
import BE.Report;
import GUI.Model.Model;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

import java.io.IOException;
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

    private DropShadow shadow = new DropShadow(0, 4, 4, Color.color(0, 0, 0, 0.25));
    private ControllerAssistant controllerAssistant;
    private Model model;
    private Case currentCase;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        controllerAssistant = ControllerAssistant.getInstance();
        addListeners();
        btnCreateNewReport.setDisable(true);
        addShadow(txtReportDescription, txtReportName);
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
        String reportName = txtReportName.getText();
        String reportDescription = txtReportDescription.getText();
        int caseID = currentCase.getCaseID();
        try {
            model.createNewReport(reportName, reportDescription, caseID, controllerAssistant.getLoggedInUser().getUserID()); //TODO UserID might not be right here, we need to fix this.
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not create a new report", ButtonType.CANCEL);
            alert.showAndWait();
        }
    }
}
