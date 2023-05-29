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

    /**
     * Fills the input fields with the information of an existing customer.
     * Sets the text values of customer name, address, phone number, email, CVR,
     * and selects the appropriate customer type in the combo box.
     */
    public void editableCustomer() {
        txtCostumerName.setText(customerVar.getCustomerName());
        txtAddress.setText(customerVar.getAddress());
        txtTlfNumber.setText(customerVar.getPhoneNumber());
        txtEmail.setText(customerVar.getEmail());
        txtCVR.setText(String.valueOf(customerVar.getCVR()));
        cbCustomerType.getSelectionModel().select(customerVar.getCustomerType());
    }

    /**
     * Adds a listener to the CVR text field to restrict input to numbers only.
     * If the entered value contains anything other than a number, the text field reverts to the previous value
     * and an information alert is displayed to inform the user to use only numbers.
     */
    private void checkCVRTextField() {
        txtCVR.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                txtCVR.setText(oldValue);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Invalid Character");
                alert.setHeaderText(null);
                alert.setContentText("Please use only numbers");
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add("/GUI/View/css/Main.css");
                dialogPane.getStyleClass().add("dialog");
                alert.showAndWait();
            }
        });
    }

    /**
     * Adds listeners to the text properties of input fields.
     * The listeners are attached to enable or disable the "create new customer" button based on the input values.
     */
    private void addListeners() {
        txtCostumerName.textProperty().addListener(createNewCustomerBtn);
        txtAddress.textProperty().addListener(createNewCustomerBtn);
        txtEmail.textProperty().addListener(createNewCustomerBtn);
        txtTlfNumber.textProperty().addListener(createNewCustomerBtn);
        txtCVR.textProperty().addListener(createNewCustomerBtn);
    }

    /**
     * Listener for the input fields to enable or disable the "create new customer" button.
     * If any of the required fields (customer name, address, phone number, email, or CVR) are empty,
     * the button is disabled and the shadow is removed.
     * Otherwise, the button is enabled and a shadow is added.
     */
    ChangeListener<String> createNewCustomerBtn = (observable, oldValue, newValue) -> {
        if (txtCostumerName.getText().isEmpty() || txtAddress.getText().isEmpty() || txtTlfNumber.getText().isEmpty() || txtEmail.getText().isEmpty() || txtCVR.getText().isEmpty()) {
            btnCreateCostumer.setDisable(true);
            util.removeShadow(btnCreateCostumer);
        } else {
            btnCreateCostumer.setDisable(false);
            util.addShadow(btnCreateCostumer);
        }
    };

    /**
     * Saves or updates a customer based on the input fields.
     * If a customer already exists, it updates the existing customer with the new information.
     * If there is no existing customer, it generates a random ID and creates a new customer object.
     * The customer object is then either updated or saved using the model.
     * After the operation is done, the window is closed.
     */
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
            } catch (SQLException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Could not update the customerVar", ButtonType.CANCEL);
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add("/GUI/View/css/Main.css");
                dialogPane.getStyleClass().add("dialog");
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

    /**
     * Setter for customerVar
     */
    public void setCustomerVar(Customer customerVar) {
        this.customerVar = customerVar;

    }
}
