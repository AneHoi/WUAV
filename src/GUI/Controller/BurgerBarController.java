package GUI.Controller;

import GUI.Controller.Util.ControllerAssistant;
import GUI.Controller.Util.Util;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BurgerBarController implements Initializable {
    @FXML
    private ImageView imgHome, imgCustomers, imgCases, imgUsers;
    @FXML
    private VBox vboxBurgerMenu;
    @FXML
    private FlowPane flowHome, flowCustomers, flowCases, flowUsers;

    private Util util = new Util();

    private final String home = "data/Images/Home.png";
    private final String customers = "data/Images/Customers.png";
    private final String cases = "data/Images/Cases.png";
    private final String users = "data/Images/Users.png";
    private final String homeOrange = "data/Images/Home orange.png";
    private final String customersOrange = "data/Images/Customers orange.png";
    private final String casesOrange = "data/Images/Cases orange.png";
    private final String usersOrange = "data/Images/Users orange.png";

    private final String labelStyleWhite = "burgerBarMenuLabels";
    private final String labelStyleOrange = "burgerBarMenuLabelsOrange";


    private Image imgHomeNormal, imgHomeOrange, imgCustomerNormal, imgCustomerOrange, imgCasesNormal, imgCasesOrange, imgUsersNormal, imgUsersOrange;
    private ControllerAssistant controllerAssistant;
    private Label lHome, lCustomers, lCases, lUsers;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controllerAssistant = ControllerAssistant.getInstance();
        loadAllImages();
        loadIconsToBar();
        expandMenuBar();
        imgCases.setOnMouseClicked(event -> loadSearchForCases());
        lCases.setOnMouseClicked(event -> loadSearchForCases());

        imgHome.setOnMouseClicked(event -> loadUserHomePage());
        lHome.setOnMouseClicked(event -> loadUserHomePage());

        imgCustomers.setOnMouseClicked(event -> loadCustomerView());
        lCustomers.setOnMouseClicked(event -> loadCustomerView());

        imgUsers.setOnMouseClicked(event -> loadUserView());
        lUsers.setOnMouseClicked(event -> loadUserView());
    }

    private void changeLblStyleToOrange(Label label) {
        label.getStyleClass().remove(labelStyleWhite);
        label.getStyleClass().add(labelStyleOrange);
    }

    /**
     * loads all the different images into image-variables
     */
    private void loadAllImages() {
        imgHomeNormal = util.loadImages(home);
        imgHomeOrange = util.loadImages(homeOrange);
        imgCustomerNormal = util.loadImages(customers);
        imgCustomerOrange = util.loadImages(customersOrange);
        imgCasesNormal = util.loadImages(cases);
        imgCasesOrange = util.loadImages(casesOrange);
        imgUsersNormal = util.loadImages(users);
        imgUsersOrange = util.loadImages(usersOrange);
    }

    private void setImagesToWhite() {
        imgHome.setImage(imgHomeNormal);
        imgCustomers.setImage(imgCustomerNormal);
        imgCases.setImage(imgCasesNormal);
        imgUsers.setImage(imgUsersNormal);
    }

    private void changeLblStyleToWhite(Label...labels){
        for (Label label: labels){
            label.getStyleClass().remove("burgerBarMenuLabelsOrange");
            label.getStyleClass().add("burgerBarMenuLabels");
        }
    }

    private void loadUserView() {
        setImagesToWhite();
        changeLblStyleToWhite(lHome, lCustomers, lCases, lUsers);
        imgUsers.setImage(imgUsersOrange);
        changeLblStyleToOrange(lUsers);
        try {
            controllerAssistant.loadCenter("CreateUserView.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load Create New User Page", ButtonType.OK);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add("/GUI/View/css/Main.css");
            dialogPane.getStyleClass().add("dialog");
            alert.showAndWait();
        }
    }

    private void loadCustomerView() {
        setImagesToWhite();
        changeLblStyleToWhite(lHome, lCustomers, lCases, lUsers);
        imgCustomers.setImage(imgCustomerOrange);
        changeLblStyleToOrange(lCustomers);
        try {
            controllerAssistant.loadCenter("CustomerView.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load Create New Customer Page", ButtonType.OK);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add("/GUI/View/css/Main.css");
            dialogPane.getStyleClass().add("dialog");
            alert.showAndWait();

        }
    }

    private void loadSearchForCases() {
        setImagesToWhite();
        changeLblStyleToWhite(lHome, lCustomers, lCases, lUsers);
        imgCases.setImage(imgCasesOrange);
        changeLblStyleToOrange(lCases);
        try {
            controllerAssistant.loadCenter("SearchForCaseView.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load Search For Case Page", ButtonType.OK);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add("/GUI/View/css/Main.css");
            dialogPane.getStyleClass().add("dialog");
            alert.showAndWait();

        }
    }

    private void loadUserHomePage() {
        setImagesToWhite();
        changeLblStyleToWhite(lHome, lCustomers, lCases, lUsers);
        imgHome.setImage(imgHomeOrange);
        changeLblStyleToOrange(lHome);
        try {
            controllerAssistant.loadCenter("UserHomePageView.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load Home Page", ButtonType.OK);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add("/GUI/View/css/Main.css");
            dialogPane.getStyleClass().add("dialog");
            alert.showAndWait();

        }
    }

    private void expandMenuBar() {
        vboxBurgerMenu.setMinWidth(250);
        loadLabelsForIcons();
        //Border in orange for menu
        vboxBurgerMenu.getStyleClass().add("vboxBorderOrange");
    }

    private void loadLabelsForIcons() {
        lHome = new Label("Home");
        lCustomers = new Label("Customers");
        lCases = new Label("Reports");
        lUsers = new Label("Users");

        setStylingAndPaddingLabels(lHome, lCustomers, lCases, lUsers);
        //color orange because the user starts on home-page
        lHome.getStyleClass().remove("burgerBarMenuLabels");
        lHome.getStyleClass().add("burgerBarMenuLabelsOrange");

        flowHome.getChildren().add(lHome);
        flowCustomers.getChildren().add(lCustomers);
        flowCases.getChildren().add(lCases);
        flowUsers.getChildren().add(lUsers);

        setPosAndPaddingFlowPanes(flowHome, flowCustomers, flowCases, flowUsers);
    }

    private void setPosAndPaddingFlowPanes(FlowPane... flowPanes) {
        for (FlowPane flowpane: flowPanes) {
            flowpane.setAlignment(Pos.CENTER_LEFT);
            flowpane.setPadding(new Insets(20, 0, 0, 13));
        }
    }

    private void setStylingAndPaddingLabels(Label...labels) {
        for (Label label: labels) {
            label.getStyleClass().add("burgerBarMenuLabels");
            label.setPadding(new Insets(0, 0, 20, 0));
        }
    }


    /**
     * load the not-orange images into the burger bar
     */
    private void loadIconsToBar() {
        imgHome.setImage(imgHomeOrange);
        imgCustomers.setImage(imgCustomerNormal);
        imgCases.setImage(imgCasesNormal);
        imgUsers.setImage(imgUsersNormal);

    }
}
