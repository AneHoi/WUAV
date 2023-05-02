package GUI.Controller;

import BE.Customer;
import GUI.Model.Model;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CreateNewCustomerView implements Initializable {
    private Model model;
    @FXML
    private TableColumn clmCustomerName, clmAddress, clmCVR, clmCustomerType;
    @FXML
    private TableView tblViewCustomers;
    @FXML
    private TextField txtCostumerName, txtAddress, txtTlfNumber, txtEmail, txtCVR, txtSearchBar;
    @FXML
    private Button btnCreateCostumer;
    @FXML
    private ImageView imgSearch;

    private ObservableList<Customer> customerObservableList;

    private DropShadow shadow = new DropShadow(0, 4, 4, Color.color(0, 0, 0, 0.25));
    private String search = "data/Images/search.png";

    private ControllerAssistant controllerAssistant;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controllerAssistant = ControllerAssistant.getInstance();
        model = Model.getInstance();
        customerObservableList = FXCollections.observableArrayList();
        imgSearch.setImage(loadImages(search));
        addShadow(txtCostumerName, txtAddress, txtTlfNumber, txtEmail, txtCVR);
        addListeners();
        updateCostumerView();
        btnCreateCostumer.setDisable(true);
        checkCVRTextField();
        searchBarFilter();


    }

    private void searchBarFilter() {  //TODO understand this...
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
                    return customer.getCustomerName().toLowerCase().contains(lowerCaseFilter)
                            || customer.getAddress().toLowerCase().contains(lowerCaseFilter)
                            || customer.getCustomerType().toLowerCase().contains(lowerCaseFilter)
                            || cvrString.contains(lowerCaseFilter);
                });
                tblViewCustomers.setItems(filteredList);
            }
        });
    }

    private void checkCVRTextField() {
        txtCVR.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtCVR.setText(oldValue);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Invalid Character");
                alert.setHeaderText(null);
                alert.setContentText("Please use only numbers");
                alert.showAndWait();
            }
        });
    }

    private void addListeners() {
        txtCostumerName.textProperty().addListener(createNewCustomerBtn);
        txtAddress.textProperty().addListener(createNewCustomerBtn);
        txtEmail.textProperty().addListener(createNewCustomerBtn);
        txtTlfNumber.textProperty().addListener(createNewCustomerBtn);
        txtCVR.textProperty().addListener(createNewCustomerBtn);

        tblViewCustomers.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tblViewCustomers.getSelectionModel().getSelectedItem() != null) {
                Customer selectedItem = (Customer) tblViewCustomers.getSelectionModel().getSelectedItem();
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

    }



    ChangeListener<String> createNewCustomerBtn = (observable, oldValue, newValue) -> {
        if (txtCostumerName.getText().isEmpty() || txtAddress.getText().isEmpty() || txtTlfNumber.getText().isEmpty() || txtEmail.getText().isEmpty() || txtCVR.getText().isEmpty()) {
            btnCreateCostumer.setDisable(true);
            removeShadow(btnCreateCostumer);
        } else {
            btnCreateCostumer.setDisable(false);
            addShadow(btnCreateCostumer);
        }
    };

    private void removeShadow(Node... node) {
        for (Node nodes : node) {
            nodes.setEffect(null);
        }
    }

    private void updateCostumerView() {
        clmCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        clmAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        clmCVR.setCellValueFactory(new PropertyValueFactory<>("CVR"));
        clmCustomerType.setCellValueFactory(new PropertyValueFactory<>("customerType"));
        customerObservableList.clear();
        try {
            customerObservableList.addAll(model.getAllCostumers());
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not get customers from database", ButtonType.CANCEL);
            alert.showAndWait();
        }
        tblViewCustomers.setItems(customerObservableList);
    }

    private void addShadow(Node... node) {
        for (Node nodes : node) {
            nodes.setEffect(shadow);
        }
    }


    private Image loadImages(String url) {
        Image image = null;
        try {
            InputStream img = new FileInputStream(url);
            image = new Image(img);
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load an image, following error occurred:\n" + e, ButtonType.CANCEL);
            alert.showAndWait();
        }
        return image;

    }

    public void saveCostumer() {
        //Generated a random id for the customer to be saved.
        int id = (int) (Math.random() * (100 - 1 + 1) + 1);
        String name = txtCostumerName.getText();
        String address = txtAddress.getText();
        String email = txtEmail.getText();
        int cvr = Integer.parseInt(txtCVR.getText());
        String tlf = txtTlfNumber.getText();
        Customer customer = new Customer(id, name, address, tlf, email, cvr, "");
        model.saveCustomer(customer);
        updateCostumerView();
    }
}

