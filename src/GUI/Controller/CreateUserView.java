package GUI.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateUserView implements Initializable {
    @FXML
    private TableColumn colName, colUserName, colUserType, colStatus;
    @FXML
    private TableView tblViewExistingUsers;
    @FXML
    private Button btnCreateNewUser, btnUpdateUser;
    @FXML
    private ComboBox cbUserTypeCreate, cbUserTypeUpdate;
    @FXML
    private TextField txtFullNameCreate, txtUserNameCreate, txtTelephoneCreate, txtEmailCreate, txtFullNameUpdate, txtUserNameUpdate, txtTelephoneUpdate, txtEmailUpdate;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void handleCreateNewUser(ActionEvent actionEvent) {
    }

    public void handleUpdateUser(ActionEvent actionEvent) {
    }
}
