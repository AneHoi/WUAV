package GUI.Controller;

import BE.Case;
import BE.Customer;
import GUI.Controller.Util.ControllerAssistant;
import GUI.Controller.Util.Util;
import GUI.Model.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserHomePageController implements Initializable {
    @FXML
    private Label lblWelcomeUser;
    @FXML
    private TableView tblViewActiveCases, tblViewViewedCustomers;
    @FXML
    private TableColumn colCaseName, colContactPerson, colTechnician, colCaseCreated, colCustomer, colAddress, colCustomerType, colCVR;
    private Model model;
    private ControllerAssistant controllerAssistant;
    private Util util = new Util();

    private ObservableList<Case> usersActiveCases;
    private ObservableList<Customer> recentlyViewedCustomers;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        controllerAssistant = ControllerAssistant.getInstance();
        util.addShadow();
        updateTableViews();
        addListeners();
        lblWelcomeUser.setText("Welcome " + controllerAssistant.getLoggedInUser().getFullName());
    }

    private void addListeners() {
        tblViewViewedCustomers.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tblViewViewedCustomers.getSelectionModel().getSelectedItem() != null) {
                Customer selectedItem = (Customer) tblViewViewedCustomers.getSelectionModel().getSelectedItem();
                try {
                    model.setCurrentCustomer(selectedItem);
                    controllerAssistant.loadCenter("CustomerHomePageView.fxml");
                } catch (IOException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open Customer Home Page", ButtonType.CANCEL);
                    alert.showAndWait();
                }
            }
        });

        tblViewActiveCases.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tblViewActiveCases.getSelectionModel().getSelectedItem() != null) {
                Case selectedItem = (Case) tblViewActiveCases.getSelectionModel().getSelectedItem();
                try {
                    model.setCurrentCase(selectedItem);
                    model.setCurrentCustomer(model.getChosenCustomer(selectedItem.getCustomerID()));
                    controllerAssistant.loadCenter("CaseHomePageView.fxml");
                } catch (IOException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open Case Home Page", ButtonType.CANCEL);
                    alert.showAndWait();
                } catch (SQLException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Could not set the customer for this Case", ButtonType.CANCEL);
                    alert.showAndWait();
                }

            }
        });
    }

    private void updateTableViews() {
        usersActiveCases = FXCollections.observableArrayList();
        recentlyViewedCustomers = FXCollections.observableArrayList();

        try {
            usersActiveCases.addAll(model.getUsersActiveCases(controllerAssistant.getLoggedInUser().getUserID()));
            recentlyViewedCustomers.addAll(model.getRecentlyViewedCustomers(controllerAssistant.getLoggedInUser().getUserID()));
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not get data from database", ButtonType.CANCEL);
            alert.showAndWait();
        }
        colCaseName.setCellValueFactory(new PropertyValueFactory<>("caseName"));
        colContactPerson.setCellValueFactory(new PropertyValueFactory<>("contactPerson"));
        colTechnician.setCellValueFactory(new PropertyValueFactory<>("assignedTechnician"));
        colCaseCreated.setCellValueFactory(new PropertyValueFactory<>("createdDate"));

        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCustomerType.setCellValueFactory(new PropertyValueFactory<>("customerType"));
        colCVR.setCellValueFactory(new PropertyValueFactory<>("CVR"));

        tblViewActiveCases.setItems(usersActiveCases);
        tblViewViewedCustomers.setItems(recentlyViewedCustomers);
    }


}
