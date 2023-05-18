package GUI.Controller;

import BE.User;
import GUI.Controller.Util.ControllerAssistant;
import GUI.Model.Model;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CreateUserController implements Initializable {
    @FXML
    private TableColumn colName, colUserName, colUserType, colStatus;
    @FXML
    private TableView tblViewExistingUsers;
    @FXML
    private Button btnCreateNewUser, btnUpdateUser;
    @FXML
    private ComboBox cbUserActive;
    @FXML
    private TextField txtFullNameUpdate, txtUserNameUpdate, txtTelephoneUpdate, txtEmailUpdate;
    private DropShadow shadow = new DropShadow(0, 4, 4, Color.color(0, 0, 0, 0.25));
    private Model model;
    private ControllerAssistant controllerAssistant;
    private ObservableList<String> userTypes;
    private ObservableList<User> allUsers;

    private ObservableList<String> activeOrInactive;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        controllerAssistant = ControllerAssistant.getInstance();
        disableButtons(btnUpdateUser, txtFullNameUpdate, txtUserNameUpdate, txtTelephoneUpdate, txtEmailUpdate, cbUserActive);
        updateTableView();
        addListeners();
        userTypes = FXCollections.observableArrayList();
        activeOrInactive = FXCollections.observableArrayList();
        checkLoggedInUser();
        addShadow(btnCreateNewUser);

        activeOrInactive.add("Active");
        activeOrInactive.add("Inactive");
    }

    private void disableButtons(Node... node) {
        for (Node nodes : node) {
            nodes.setDisable(true);
        }
    }

    private void enableButtons(Node... node) {
        for (Node nodes : node) {
            nodes.setDisable(false);
        }
    }

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

    private void addListeners() {
        tblViewExistingUsers.getSelectionModel().selectedItemProperty().addListener(selectedUserListener);
        txtFullNameUpdate.textProperty().addListener(updateUserBtn);
        txtUserNameUpdate.textProperty().addListener(updateUserBtn);
        txtTelephoneUpdate.textProperty().addListener(updateUserBtn);
        txtEmailUpdate.textProperty().addListener(updateUserBtn);
        cbUserActive.valueProperty().addListener(updateUserBtn); // Add listener to ComboBox value property
    }

    ChangeListener<String> updateUserBtn = (observable, oldValue, newValue) -> {
        updateUpdateButtonState(); // Extracted method to update the button state
    };

    ChangeListener<User> selectedUserListener = (observable, oldValue, newValue) -> {
        if (newValue != null) {
            enableButtons(btnUpdateUser, txtFullNameUpdate, txtUserNameUpdate, txtTelephoneUpdate, txtEmailUpdate, cbUserActive);
            addShadow(btnUpdateUser, txtFullNameUpdate, txtUserNameUpdate, txtTelephoneUpdate, txtEmailUpdate, cbUserActive);
            txtFullNameUpdate.setText(newValue.getFullName());
            txtUserNameUpdate.setText(newValue.getUserName());
            txtTelephoneUpdate.setText(newValue.getTelephone());
            txtEmailUpdate.setText(newValue.getEmail());
            cbUserActive.setItems(activeOrInactive);
            cbUserActive.getSelectionModel().select(newValue.getIsActive());

            updateUpdateButtonState(); // Update the button state when a user is selected
        }
    };

    // Method to update the state of the "Update User" button
    private void updateUpdateButtonState() {
        if (txtFullNameUpdate.getText().isEmpty() || txtUserNameUpdate.getText().isEmpty() || txtTelephoneUpdate.getText().isEmpty() || txtEmailUpdate.getText().isEmpty() || cbUserActive.getSelectionModel().getSelectedItem() == null) {
            btnUpdateUser.setDisable(true);
            removeShadow(btnUpdateUser);
        } else {
            btnUpdateUser.setDisable(false);
            addShadow(btnUpdateUser);
        }
    }


    private void updateTableView() {
        allUsers = FXCollections.observableArrayList();
        colName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colUserType.setCellValueFactory(new PropertyValueFactory<>("userStringType"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("isActive"));
        tblViewExistingUsers.getColumns().addAll();
        try {
            allUsers.addAll(model.getAllUsers());
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not get users from database", ButtonType.CANCEL);
            alert.showAndWait();
        }
        tblViewExistingUsers.setItems(allUsers);
        tblViewExistingUsers.getSortOrder().add(colStatus);
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

    public void handleCreateNewUser(ActionEvent actionEvent) {
        PopUpCreateUserController popUpCreateUserController = new PopUpCreateUserController();
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setController(popUpCreateUserController);

        loader.setLocation(getClass().getResource("/GUI/View/PopUpCreateUserView.fxml"));
        stage.setTitle("Create new user");
        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open Add Section Window", ButtonType.CANCEL);
            alert.showAndWait();
        }

        updateTableView();
    }

    public void handleUpdateUser(ActionEvent actionEvent) {
        String fullName = txtFullNameUpdate.getText();
        String userName = txtUserNameUpdate.getText();
        String userTlf = txtTelephoneUpdate.getText();
        String userEmail = txtEmailUpdate.getText();
        boolean userActive = cbUserActive.getSelectionModel().getSelectedItem().equals("Active");
        User user = (User) tblViewExistingUsers.getSelectionModel().getSelectedItem();
        int userID = user.getUserID();

            if(cbUserActive.getSelectionModel().getSelectedItem().equals("Inactive")){
                Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to make: " + user.getFullName() + " Inactive", ButtonType.YES, ButtonType.NO);
                alert.getDialogPane().getStylesheets().add("/gui/view/Main.css");
                alert.getDialogPane().getStyleClass().add("alertPane");
                alert.showAndWait();
                if (alert.getResult() == ButtonType.YES) {
                    try {
                        model.updateUser(userID, fullName, userName, userTlf, userEmail, userActive);
                        Alert success = new Alert(Alert.AlertType.INFORMATION, "User: " + user.getFullName() + " was made inactive", ButtonType.OK );
                        success.showAndWait();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Alert alert1 = new Alert(Alert.AlertType.ERROR, "Could not update User", ButtonType.CANCEL);
                        alert1.showAndWait();
                    }
                }
            }
            else {
                try {
                    model.updateUser(userID, fullName, userName, userTlf, userEmail, userActive);
                    Alert success = new Alert(Alert.AlertType.INFORMATION, "User: " + user.getFullName() + " was made active again", ButtonType.OK );
                    success.showAndWait();
                } catch (SQLException e) {
                    e.printStackTrace();
                    Alert alert1 = new Alert(Alert.AlertType.ERROR, "Could not update User", ButtonType.CANCEL);
                    alert1.showAndWait();
                }
            }

        updateTableView();
    }
}
