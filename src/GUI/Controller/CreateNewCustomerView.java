package GUI.Controller;

import BE.Customer;
import GUI.Model.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class CreateNewCustomerView implements Initializable {
    private Model model;
    @FXML
    private TableColumn clmCostumerName, clmAddress, clmCVR, clmCreatedDate;
    @FXML
    private TableView tableviewCostumers;
    @FXML
    private TextField txtCostumerName, txtAddress, txtTlfNumber, txtEmail, txtCVR;
    @FXML
    private Button btnCreateCostumer;
    @FXML
    private ImageView imgSearch;

    private ObservableList<Customer> customerObservableList;

    private DropShadow shadow = new DropShadow(0, 4, 4, Color.color(0, 0, 0, 0.25));
    private String search = "data/Images/saerch.png";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = new Model();
        customerObservableList = FXCollections.observableArrayList();

        imgSearch.setImage(loadImages(search));
        setEffect(txtCostumerName, txtAddress, txtTlfNumber, txtEmail, txtCVR, btnCreateCostumer);
        updateCostumerView();

    }

    private void updateCostumerView() {

        clmCostumerName.setCellFactory(new PropertyValueFactory<>("customerName"));
        clmAddress.setCellFactory(new PropertyValueFactory<>("address"));
        clmCVR.setCellFactory(new PropertyValueFactory<>("CVR"));
        clmCreatedDate.setCellFactory(new PropertyValueFactory<>("customerType"));

        customerObservableList.addAll(model.getAllCostumers());
        //tableviewCostumers.setItems(customerObservableList);
    }

    private void setEffect(Node... node) {
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

    public void search(KeyEvent keyEvent) {
        //TODO FIXME
    }


    public void saveCostumer() {

        int id = (int) (Math.random() * (100 - 1 + 1) + 1);
        String name = txtCostumerName.getText();
        String address = txtAddress.getText();
        String email = txtEmail.getText();
        int cvr = Integer.parseInt(txtCVR.getText());
        String tlf = txtTlfNumber.getText();
        Customer customer = new Customer(id, name, address, tlf, email, cvr, "");
        model.saveCustomer(customer);
        updateCostumerView();
        System.out.println("Saved: " + name);
    }
}

