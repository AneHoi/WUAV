package GUI.Controller;

import BE.Customer;
import BE.User;
import GUI.Controller.Util.ControllerAssistant;
import GUI.Controller.Util.Util;
import GUI.Model.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerViewController implements Initializable {
    @FXML
    private Button btnCreateCustomer, btnDeleteCustomer;
    @FXML
    private HBox hBoxButtonBar;
    private Model model;
    @FXML
    private TableColumn clmCustomerName, clmAddress, clmCVR, clmCustomerType;
    @FXML
    private TableView tblViewCustomers;
    @FXML
    private TextField txtSearchBar;
    private ObservableList<Customer> customerObservableList;
    private ControllerAssistant controllerAssistant;
    private Util util = new Util();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addListeners();
        controllerAssistant = ControllerAssistant.getInstance();
        model = Model.getInstance();
        customerObservableList = FXCollections.observableArrayList();
        updateCostumerView();
        searchBarFilter();
        btnDeleteCustomer.setVisible(false);
        btnDeleteCustomer.setDisable(true);
        util.addShadow(btnCreateCustomer, btnDeleteCustomer, txtSearchBar);
    }


    private void addListeners() {
        tblViewCustomers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                hBoxButtonBar.getChildren().removeIf(node -> node instanceof Button && ((Button) node).getText().equals("Edit Customer"));
                Button btnEditCustomer = new Button("Edit Customer");
                btnEditCustomer.setPrefWidth(200);
                util.addShadow(btnEditCustomer);
                btnEditCustomer.setOnAction(event -> handleEditCustomer(event));
                hBoxButtonBar.getChildren().add(btnEditCustomer);
                btnDeleteCustomer.setVisible(true);
                btnDeleteCustomer.setDisable(false);
                btnCreateCustomer.setText("Create New Customer");
            } else {
                hBoxButtonBar.getChildren().removeIf(node -> node instanceof Button && ((Button) node).getText().equals("Edit Customer"));
                btnCreateCustomer.setText("Create New Customer");
                btnDeleteCustomer.setVisible(false);
                btnDeleteCustomer.setDisable(true);
            }
        });

        tblViewCustomers.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tblViewCustomers.getSelectionModel().getSelectedItem() != null) {
                Customer selectedItem = (Customer) tblViewCustomers.getSelectionModel().getSelectedItem();
                try {
                    model.setCurrentCustomer(selectedItem);
                    controllerAssistant.loadCenter("CustomerHomePageView.fxml");
                    Thread thread = new Thread(() -> {
                    storeUserCustomerLink(selectedItem);
                    });
                    thread.start();
                } catch (IOException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open Customer Home Page", ButtonType.CANCEL);
                    alert.showAndWait();
                }
            }
        });
    }

    private void storeUserCustomerLink(Customer customer) {
        User user = controllerAssistant.getLoggedInUser();
        try {
            model.storeUserCustomerLink(user.getUserID(), customer.getCustomerID());
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could store link between customer and user in database", ButtonType.CANCEL);
            alert.showAndWait();
        }
    }

    private void searchBarFilter() {
        // Create a list to hold the original unfiltered items in the tblViewCustomers TableView
        ObservableList<Customer> originalList = FXCollections.observableArrayList(tblViewCustomers.getItems());

        // Add a listener to the txtSearchBar TextField to filter the tblViewCustomers TableView based on the user's input
        txtSearchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            // Create a filtered list that contains all items from the tblViewCustomers TableView
            FilteredList<Customer> filteredList = new FilteredList<>(originalList);

            // Set a predicate to filter the items based on the user's input
            if (newValue == null || newValue.isEmpty()) {
                // If the user has not entered any input, display all items
                tblViewCustomers.setItems(originalList);
            } else {
                // Otherwise, filter the items based on the user's input
                String lowerCaseFilter = newValue.toLowerCase();
                filteredList.setPredicate(customer -> {
                    String cvrString = String.valueOf(customer.getCVR());
                    String customerType = Objects.toString(customer.getCustomerType(), "");
                    customerType = customerType.toLowerCase();
                    return customer.getCustomerName().toLowerCase().contains(lowerCaseFilter)
                            || customer.getAddress().toLowerCase().contains(lowerCaseFilter)
                            || customerType.contains(lowerCaseFilter)
                            || cvrString.contains(lowerCaseFilter);
                });
                tblViewCustomers.setItems(filteredList);
            }
        });
    }

    private void updateCostumerView() {
        clmCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        clmAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        clmCVR.setCellValueFactory(new PropertyValueFactory<>("CVR"));
        clmCustomerType.setCellValueFactory(new PropertyValueFactory<>("customerType"));
        customerObservableList.clear();
        try {
            customerObservableList.addAll(model.getAllCustomers());
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not get customers from database", ButtonType.CANCEL);
            alert.showAndWait();
        }
        tblViewCustomers.setItems(customerObservableList);
    }

    public void handleCreateCustomer(ActionEvent event) {
        PopUpCreateNewCostumerController popUpCreateNewCostumerController = new PopUpCreateNewCostumerController();
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setController(popUpCreateNewCostumerController);
        loader.setLocation(getClass().getResource("/GUI/View/PopUpCreateNewCostumer.fxml"));
        stage.setTitle("Create New Customer");
        util.openNewWindow(stage, loader, "Could not create open create customer window");
        updateCostumerView();
        btnDeleteCustomer.setVisible(false);
        btnDeleteCustomer.setDisable(true);
    }

    public void handleEditCustomer (ActionEvent event) {
        PopUpCreateNewCostumerController popUpCreateNewCostumerController = new PopUpCreateNewCostumerController();
        if (tblViewCustomers.getSelectionModel().getSelectedItem() != null) {
            Customer customer = (Customer) tblViewCustomers.getSelectionModel().getSelectedItem();
            popUpCreateNewCostumerController.setCustomerVar(customer);
        }
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setController(popUpCreateNewCostumerController);
        loader.setLocation(getClass().getResource("/GUI/View/PopUpCreateNewCostumer.fxml"));
        stage.setTitle("Edit or create new customer");
        util.openNewWindow(stage, loader, "could not open edit customer window");
        updateCostumerView();
        btnDeleteCustomer.setVisible(false);
        btnDeleteCustomer.setDisable(true);
    }

    public void deleteCustomer(ActionEvent event) {
        Customer customer = (Customer) tblViewCustomers.getSelectionModel().getSelectedItem();
        Alert alertAreYouSure = new Alert(Alert.AlertType.CONFIRMATION);
        alertAreYouSure.setTitle("Deleting a customer");
        alertAreYouSure.setHeaderText("Are you sure you want to delete this customer:\n" + customer.getCustomerName());

        ButtonType deleteCustomer = new ButtonType("Delete customer");
        ButtonType cancel = new ButtonType("Cancel");

        alertAreYouSure.getButtonTypes().clear();
        alertAreYouSure.getButtonTypes().addAll(deleteCustomer, cancel);
        Optional<ButtonType> option = alertAreYouSure.showAndWait();
        if (option.get() == deleteCustomer) {
            try {
                model.deleteCustomer(customer);
            } catch (SQLException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Could not delete a customer from the program", ButtonType.CANCEL);
                alert.showAndWait();
            }
        }
        updateCostumerView();
        btnDeleteCustomer.setVisible(false);
        btnDeleteCustomer.setDisable(true);
    }
}
