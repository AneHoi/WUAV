package GUI.Controller;

import BE.Report;
import BE.TextsAndImagesOnReport;
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

public class AddTextFieldController implements Initializable {

    @FXML
    private Button btnSave;
    @FXML
    private TextArea txtAddText;
    @FXML
    private Label lblTitle;
    private Report currentReport;
    private ControllerAssistant controllerAssistant;
    private Model model;
    private int nextPosition;
    private TextsAndImagesOnReport textOrImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (textOrImage != null) {
            lblTitle.setText("Edit Text:");
            txtAddText.setPromptText("Edit Text...");
            txtAddText.setText(textOrImage.getText());
            btnSave.setText("Save Changes");
        }
        model = Model.getInstance();
        controllerAssistant = ControllerAssistant.getInstance();
    }

    /**
     * Checks the text of the button to see whether to update old text, or save new text
     */
    public void handleSave() {
        if (btnSave.getText().equals("Save Changes")) {
            saveChanges();
        } else {
            saveText();
        }
    }

    /**
     * Saves new text to the database
     */
    private void saveText() {
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
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add("/GUI/View/css/Main.css");
            dialogPane.getStyleClass().add("dialog");
            alert.showAndWait();
        }
        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }

    /**
     * Updates the old text to users input
     */
    private void saveChanges() {
        int textID = textOrImage.getTextOrImageID();
        String txt = txtAddText.getText();
        int userID = controllerAssistant.getLoggedInUser().getUserID();
        LocalDate createdDate = LocalDate.now();
        LocalTime createdTime = LocalTime.now();
        try {
            model.updateTextInReport(textID, txt, userID, createdDate, createdTime);
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not update text in the Database", ButtonType.CANCEL);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add("/GUI/View/css/Main.css");
            dialogPane.getStyleClass().add("dialog");
            alert.showAndWait();
        }
        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }

    /**
     * Sets the current report the user is working with
     * @param currentReport
     */
    public void setCurrentReport(Report currentReport) {
        this.currentReport = currentReport;
    }

    /**
     * Sets the position the text is saved to, to the next available position in the report
     * @param nextPosition
     */
    public void setNextAvailablePosition(int nextPosition) {
        this.nextPosition = nextPosition;
    }

    /**
     * If the edit button was pressedd the textOrImage is passed along to be edited.
     * @param textOrImage
     */
    public void setCurrentText(TextsAndImagesOnReport textOrImage) {
        this.textOrImage = textOrImage;
    }
}
