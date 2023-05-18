package GUI.Controller;

import GUI.Controller.Util.ControllerAssistant;
import GUI.Model.Model;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class UserHomePageController implements Initializable {
    @FXML
    private Label lblWelcomeUser;
    @FXML
    private TableView tblViewActiveCases, tblViewViewedCustomers;
    @FXML
    private TableColumn colCustomerActiveCases, colAddressActiveCases, colTechnicians, colCaseDate, colCustomerViewed, colAddressViewed, colActiveCases, colCVR;
    private DropShadow shadow = new DropShadow(0, 4, 4, Color.color(0, 0, 0, 0.25));
    private Model model;
    private ControllerAssistant controllerAssistant;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = Model.getInstance();
        controllerAssistant = ControllerAssistant.getInstance();
        addShadow();
    }

    private void addShadow(Node... node) {
        for (Node nodes : node) {
            nodes.setEffect(shadow);
        }
    }
}
