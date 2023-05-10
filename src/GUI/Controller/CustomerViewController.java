package GUI.Controller;

import BE.Customer;
import GUI.Model.Model;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class CustomerViewController implements Initializable {
    private Model model;
    @FXML
    private TableColumn clmCustomerName, clmAddress, clmCVR, clmCustomerType;
    @FXML
    private TableView tblViewCustomers;
    @FXML
    private TextField txtSearchBar;
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
        addListeners();
        controllerAssistant = ControllerAssistant.getInstance();
        model = Model.getInstance();
        customerObservableList = FXCollections.observableArrayList();
        imgSearch.setImage(loadImages(search));
        updateCostumerView();
        searchBarFilter();


    }


    private void addListeners() {
        tblViewCustomers.focusedProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
            {
                if (!newPropertyValue)
                {
                    btnCreateCostumer.setText("Create new customer");
                }
            }
        });

        tblViewCustomers.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && tblViewCustomers.getSelectionModel().getSelectedItem() != null){
                btnCreateCostumer.setText("Edit customer");
            }else {
                btnCreateCostumer.setText("Create new customer");
            }
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


    public void openNewCustomerPopUp(ActionEvent event) {
        PopUpCreateNewCostumerController popUpCreateNewCostumerController = new PopUpCreateNewCostumerController();
        if(tblViewCustomers.getSelectionModel().getSelectedItem() != null){
            Customer customer = (Customer) tblViewCustomers.getSelectionModel().getSelectedItem();
            popUpCreateNewCostumerController.setCustomerVar(customer);
        }
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setController(popUpCreateNewCostumerController);
        loader.setLocation(getClass().getResource("/GUI/View/PopUpCreateNewCostumer.fxml"));

        try {
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open Add Section Window", ButtonType.CANCEL);
            alert.showAndWait();
        }
        updateCostumerView();
    }
}
