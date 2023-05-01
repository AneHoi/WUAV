package GUI.Controller;

import BE.Customer;
import GUI.Model.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class DisplayCutomersController implements Initializable {
    public TableView tblView;
    @FXML
    private TableColumn clmID, clmName, clmAddress, clmNumber, clmMail, clmCVR, clmType;

    Model model;
    private ObservableList<Customer> customerObservableList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = new Model();

        customerObservableList = FXCollections.observableArrayList();
        customerObservableList.addAll(model.getAllCostumers());


        clmID.setCellFactory(new PropertyValueFactory<>("customerID"));
        clmName.setCellFactory(new PropertyValueFactory<>("customerName"));
        clmAddress.setCellFactory(new PropertyValueFactory<>("address"));
        clmNumber.setCellFactory(new PropertyValueFactory<>("phoneNumber"));
        clmMail.setCellFactory(new PropertyValueFactory<>("email"));
        clmCVR.setCellFactory(new PropertyValueFactory<>("CVR"));
        clmType.setCellFactory(new PropertyValueFactory<>("customerType"));


        //TODO fix me
        //tblView.setItems(customerObservableList);
    }
}
