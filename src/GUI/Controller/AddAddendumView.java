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
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddAddendumView implements Initializable {


    @FXML
    private Label lblReportName;
    @FXML
    private TextField txtAddendumName;
    @FXML
    private TextArea txtAddendumDescription;
    @FXML
    private Button btnCreateNewAddendum;
    private Model model;
    private ControllerAssistant controllerAssistant;
    private Case currentCase;
    private Report currentReport;
    private DropShadow shadow = new DropShadow(0, 4, 4, Color.color(0, 0, 0, 0.25));

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controllerAssistant = ControllerAssistant.getInstance();
        model = Model.getInstance();
        lblReportName.setText(currentReport.getReportName());
        btnCreateNewAddendum.setDisable(true);
        txtAddendumName.textProperty().addListener(createNewAddendumBtnListener);
        txtAddendumDescription.textProperty().addListener(createNewAddendumBtnListener);
    }

    public void setCaseAndReport(Case currentCase, Report selectedReport) {
        this.currentCase = currentCase;
        this.currentReport = selectedReport;
    }

    public void handleCreateNewAddendum(ActionEvent actionEvent) {
        String addendumName = txtAddendumName.getText();
        String addendumDescription = txtAddendumDescription.getText();
        int caseID = currentCase.getCaseID();
        int reportID = currentReport.getReportID();
        try {
            model.createNewAddendum(addendumName, addendumDescription, caseID, reportID, controllerAssistant.getLoggedInUser().getUserID());
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not create a new Addendum", ButtonType.CANCEL);
            alert.showAndWait();
        }

        // Create a custom dialog with a "Close" button
        Dialog dialog = new Dialog<>();
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.setTitle("Addendum created successfully");
        dialog.setContentText("Addendum created successfully");

        // Override the onCloseRequest method to close the dialog and the window
        dialog.setOnCloseRequest(event -> {
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        });

        dialog.showAndWait();
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
    ChangeListener<String> createNewAddendumBtnListener = (observable, oldValue, newValue) -> {
        if (txtAddendumName.getText().isEmpty() || txtAddendumDescription.getText().isEmpty()) {
            btnCreateNewAddendum.setDisable(true);
            removeShadow(btnCreateNewAddendum);
        } else {
            btnCreateNewAddendum.setDisable(false);
            addShadow(btnCreateNewAddendum);
        }
    };
}

