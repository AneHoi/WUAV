package GUI.Controller;

import BE.Report;
import GUI.Model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class AddTextFieldView implements Initializable {

    @FXML
    private Button btnSave;
    @FXML
    private TextArea txtAddText;
    private Report currentReport;
    private ControllerAssistant controllerAssistant;
    private Model model;
    private int nextPosition;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        controllerAssistant = ControllerAssistant.getInstance();
    }

    public void handleSave(ActionEvent actionEvent) {
        int position = nextPosition;
        int reportID = currentReport.getReportID();
        String txt = txtAddText.getText();
        int userID = controllerAssistant.getLoggedInUser().getUserID();
        LocalDate createdDate = LocalDate.now();
        LocalTime createdTime = LocalTime.now();

        try {
            model.SaveTextToReport(position, reportID, txt, userID, createdDate, createdTime);
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not save text in the Database", ButtonType.CANCEL);
            alert.showAndWait();
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Created case successfully", ButtonType.OK);
        alert.showAndWait();
        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }

    public void setCurrentReport(Report currentReport) {
        this.currentReport = currentReport;
    }

    public void setNextAvailablePosition(int nextPosition) {
        this.nextPosition = nextPosition;
    }
}
