package GUI.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class CaseHomePageView implements Initializable {
    @FXML
    private Label lblCaseName;
    @FXML
    private TextField txtReportName, txtSearchField, txtAddendumName;
    @FXML
    private TextArea txtReportDescription, txtAddendumDescription;
    @FXML
    private Button btnCreateNewReport, btnCreateNewAddendum;
    @FXML
    private TableView tblViewExistingReports;
    @FXML
    private TableColumn colReportName, colTechnician, colCreatedDate, colStatus;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}

