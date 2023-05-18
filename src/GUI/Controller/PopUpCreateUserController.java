package GUI.Controller;

import GUI.Controller.Util.ControllerAssistant;
import GUI.Model.Model;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PopUpCreateUserController implements Initializable {
    @FXML
    private Button btnCreateNewUser;
    @FXML
    private ComboBox cbUserTypeCreate;
    @FXML
    private TextField txtFullNameCreate, txtUserNameCreate, txtTelephoneCreate, txtEmailCreate;

    private ObservableList<String> userTypes;
    private DropShadow shadow = new DropShadow(0, 4, 4, Color.color(0, 0, 0, 0.25));
    private Model model;

    private ControllerAssistant controllerAssistant;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        controllerAssistant = ControllerAssistant.getInstance();
        addListeners();
        userTypes = FXCollections.observableArrayList();
        addShadow(txtFullNameCreate, txtUserNameCreate, txtTelephoneCreate, txtEmailCreate, cbUserTypeCreate);
        btnCreateNewUser.setDisable(true);
        cbUserTypeCreate.setItems(userTypes);
        checkLoggedInUser();
    }

    private void addListeners() {
        txtFullNameCreate.textProperty().addListener(createNewCustomerBtn);
        txtUserNameCreate.textProperty().addListener(createNewCustomerBtn);
        txtTelephoneCreate.textProperty().addListener(createNewCustomerBtn);
        txtEmailCreate.textProperty().addListener(createNewCustomerBtn);
        cbUserTypeCreate.getSelectionModel().selectedItemProperty().addListener(createNewCustomerBtn);

    }

    ChangeListener<String> createNewCustomerBtn = (observable, oldValue, newValue) -> {
        if (txtFullNameCreate.getText().isEmpty() || txtUserNameCreate.getText().isEmpty() || txtTelephoneCreate.getText().isEmpty() || txtEmailCreate.getText().isEmpty() || cbUserTypeCreate.getSelectionModel().getSelectedItem() == null) {
            btnCreateNewUser.setDisable(true);
            removeShadow(btnCreateNewUser);
        } else {
            btnCreateNewUser.setDisable(false);
            addShadow(btnCreateNewUser);
        }
    };

    private void checkLoggedInUser() {
        String admin = "Admin";
        String projectManager = "ProjectManager";
        String technician = "Technician";
        String salesRepresentative = "SalesRepresentative";
        int userType = controllerAssistant.getLoggedInUser().getUserType();
        userTypes.clear();
        if (userType == 1) {
            userTypes.addAll(admin, projectManager, technician, salesRepresentative);
        } else if (userType == 2) {
            userTypes.addAll(technician);
        }
    }


    public void handleCreateNewUser(ActionEvent actionEvent) {
        String fullName = txtFullNameCreate.getText();
        String userName = txtUserNameCreate.getText();
        String userTlf = txtTelephoneCreate.getText();
        String userEmail = txtEmailCreate.getText();
        int userType = 0;
        switch ((String) cbUserTypeCreate.getSelectionModel().getSelectedItem()) {
            case "Admin":
                userType = 1;
                break;
            case "ProjectManager":
                userType = 2;
                break;
            case "Technician":
                userType = 3;
                break;
            case "SalesRepresentative":
                userType = 4;
        }
        try {
            model.createNewUser(fullName, userName, userTlf, userEmail, userType);
        } catch (
                SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not create User", ButtonType.CANCEL);
            alert.showAndWait();
        }
        //This closes the window
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
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
}
