package GUI.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class UserHomePageView implements Initializable {
    @FXML
    private Label lblWelcomeUser;
    @FXML
    private TableView tblViewActiveCases, tblViewViewedCustomers;
    @FXML
    private TableColumn colCustomerActiveCases, colAddressActiveCases, colTechnicians, colCaseDate, colCustomerViewed, colAddressViewed, colActiveCases, colCVR;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
