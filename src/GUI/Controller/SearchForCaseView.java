package GUI.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SearchForCaseView implements Initializable {
    @FXML
    private TextField txtCustomer, txtCustomerAddress, txtCaseName, txtTechnician;
    @FXML
    private DatePicker dpDate;
    @FXML
    private Button btnFilter;
    @FXML
    private TableView tblViewFilteredCases;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void handleFilter(ActionEvent actionEvent) {
    }
}
