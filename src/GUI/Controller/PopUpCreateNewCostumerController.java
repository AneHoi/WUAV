package GUI.Controller;

import BE.Customer;
import GUI.Controller.Util.Util;
import GUI.Model.Model;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PopUpCreateNewCostumerController implements Initializable {
    private Model model;
    @FXML
    private TextField txtCostumerName, txtAddress, txtTlfNumber, txtEmail, txtCVR;
    @FXML
    private Button btnCreateCostumer;
    @FXML
    private ComboBox<String> cbCustomerType;
    private Customer customerVar;

    private String search = "data/Images/search.png";
    private ObservableList<String> typesOfCustomers;
    private Util util = new Util();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        typesOfCustomers = FXCollections.observableArrayList();
        typesOfCustomers.addAll("Corporate", "Private", "Government");
        cbCustomerType.setItems(typesOfCustomers);
        model = Model.getInstance();
        util.addShadow(txtCostumerName, txtAddress, txtTlfNumber, txtEmail, txtCVR);
        addListeners();
        btnCreateCostumer.setDisable(true);
        checkCVRTextField();
        if (customerVar != null){
            editableCustomer();
            btnCreateCostumer.setText("Update Customer");
        }
    }

    public void editableCustomer() {
        txtCostumerName.setText(customerVar.getCustomerName());
        txtAddress.setText(customerVar.getAddress());
        txtTlfNumber.setText(customerVar.getPhoneNumber());
        txtEmail.setText(customerVar.getEmail());
        txtCVR.setText(String.valueOf(customerVar.getCVR()));
        cbCustomerType.getSelectionModel().select(customerVar.getCustomerType());
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
    }


    ChangeListener<String> createNewCustomerBtn = (observable, oldValue, newValue) -> {
        if (txtCostumerName.getText().isEmpty() || txtAddress.getText().isEmpty() || txtTlfNumber.getText().isEmpty() || txtEmail.getText().isEmpty() || txtCVR.getText().isEmpty()) {
            btnCreateCostumer.setDisable(true);
            util.removeShadow(btnCreateCostumer);
        } else {
            btnCreateCostumer.setDisable(false);
            util.addShadow(btnCreateCostumer);
        }
    };

    public void saveCostumer(javafx.event.ActionEvent event) {
        //If there already is a customer, we only need to update that customer
        int id;
        if (customerVar != null){
            customerVar.setCustomerName(txtCostumerName.getText());
            customerVar.setAddress(txtAddress.getText());
            customerVar.setEmail(txtEmail.getText());
            customerVar.setCVR(Integer.parseInt(txtCVR.getText()));
            customerVar.setPhoneNumber(txtTlfNumber.getText());
            customerVar.setCustomerType(cbCustomerType.getSelectionModel().getSelectedItem());
            try {
                model.updateCustomer(customerVar);
//                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Customer updated successfully", ButtonType.OK);
//                alert.showAndWait();
            } catch (SQLException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Could not update the customerVar", ButtonType.CANCEL);
                alert.showAndWait();
            }
        }else {
            //Generated a random id for the customerVar to be saved.
            id = (int) (Math.random() * (100 - 1 + 1) + 1);
            String name = txtCostumerName.getText();
            String address = txtAddress.getText();
            String email = txtEmail.getText();
            int cvr = Integer.parseInt(txtCVR.getText());
            String tlf = txtTlfNumber.getText();
            Customer customer = new Customer(id, name, address, tlf, email, cvr, "");
            model.saveCustomer(customer);
        }
        //This closes the window
        ((Node)(event.getSource())).getScene().getWindow().hide();


    }

    public void setCustomerVar(Customer customerVar) {
        this.customerVar = customerVar;

    }
}
