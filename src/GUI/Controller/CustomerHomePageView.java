package GUI.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerHomePageView implements Initializable {
    @FXML
    private ComboBox cbTechnician;
    @FXML
    private TableColumn colCaseName, colCaseID, colTechnicians, colCreatedDate;
    @FXML
    private TableView tblViewExistingCases;
    @FXML
    private ImageView imgSearch;
    @FXML
    private Button btnCreateNewCase, btnAddTechnician;
    @FXML
    private TextField txtCaseName, txtCaseID, txtContactPerson, txtSearchBar;
    @FXML
    private Label lblCustomerName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void handleCreateNewCase(ActionEvent actionEvent) {
    }

    public void search(MouseEvent mouseEvent) {
    }

    public void handleAddTechnician(ActionEvent actionEvent) {
    }

    public void searchKey(KeyEvent keyEvent) {
    }
}
