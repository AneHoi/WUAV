package GUI.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class BurgerBarView implements Initializable {
    @FXML
    private ImageView imgBurger, imgHome, imgCustomers, imgCases, imgUsers;
    @FXML
    private VBox vboxBurgerMenu;
    @FXML
    private FlowPane flowBurger, flowHome, flowCustomers, flowCases, flowUsers;

    private String burger = "data/Images/Burger menu.png";
    private String home = "data/Images/Home.png";
    private String customers = "data/Images/Customers.png";
    private String cases = "data/Images/Cases.png";
    private String users = "data/Images/Users.png";

    private String burgerOrange = "data/Images/Burger orange.png";
    private String homeOrange = "data/Images/Home orange.png";
    private String customersOrange = "data/Images/Customers orange.png";
    private String casesOrange = "data/Images/Cases orange.png";
    private String usersOrange = "data/Images/Users orange.png";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imgBurger.setImage(loadImages(burger));
        imgHome.setImage(loadImages(home));
        imgCustomers.setImage(loadImages(customers));
        imgCases.setImage(loadImages(cases));
        imgUsers.setImage(loadImages(users));

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

    /* USE This to make a border around the BurgerBarMenu when the burger is clicked on.

        PseudoClass burgerBarMenuBorder = PseudoClass.getPseudoClass("border");


           vboxBurgerMenu.getStyleClass().add("image-view-wrapper");

        BooleanProperty imageViewBorderActive = new SimpleBooleanProperty() {
            @Override
            protected void invalidated() {
                imgBurger.pseudoClassStateChanged(burgerBarMenuBorder, get());
            }
        };

        imgBurger.setOnMouseClicked(ev -> imageViewBorderActive
                .set(!imageViewBorderActive.get()));


    }
     */
}
