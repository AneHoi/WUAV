package GUI.Controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

    private String burgerOrange = "data/Images/Burger menu orange.png";
    private String homeOrange = "data/Images/Home orange.png";
    private String customersOrange = "data/Images/Customers orange.png";
    private String casesOrange = "data/Images/Cases orange.png";
    private String usersOrange = "data/Images/Users orange.png";

    private Image imgBurgerOrange;
    private Image imgBurgerNormal;

    private ControllerAssistant controllerAssistant;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controllerAssistant = ControllerAssistant.getInstance();
        imgBurgerOrange = loadImages(burgerOrange);
        imgBurgerNormal = loadImages(burger);
        loadIconsToBar();
        imgBurger.setOnMouseClicked(event -> expandMenuBar());
        imgCases.setOnMouseClicked(event -> loadCasesView());
        imgHome.setOnMouseClicked(event -> loadReportHomePage());
    }

    private void loadReportHomePage() {
        try {
            controllerAssistant.loadCenter("ReportHomePageView.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load Cases Home Page", ButtonType.OK);
            alert.showAndWait();

        }
    }

    private void loadCasesView() {
        try {
            controllerAssistant.loadCenter("CaseHomePageView.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load Cases Home Page", ButtonType.OK);
            alert.showAndWait();

        }
    }

    private void expandMenuBar() {
        if(imgBurger.getImage().equals(imgBurgerNormal)) {
            imgBurger.setImage(imgBurgerOrange);
            vboxBurgerMenu.setMinWidth(250);
            loadLabelsForIcons();
            vboxBurgerMenu.getStyleClass().add("vboxBorderOrange");
        } else {
            imgBurger.setImage(imgBurgerNormal);
            vboxBurgerMenu.setMinWidth(125);
            removeLabelsFromIcons();
            vboxBurgerMenu.getStyleClass().remove(1);
        }

    }

    private void removeLabelsFromIcons() {
        flowHome.getChildren().remove(1);
        flowCustomers.getChildren().remove(1);
        flowCases.getChildren().remove(1);
        flowUsers.getChildren().remove(1);
        flowBurger.setAlignment(Pos.CENTER);
        flowHome.setAlignment(Pos.CENTER);
        flowCustomers.setAlignment(Pos.CENTER);
        flowCases.setAlignment(Pos.CENTER);
        flowUsers.setAlignment(Pos.CENTER);
        flowBurger.setPadding(new Insets(20, 0, 0, 0));
        flowHome.setPadding(new Insets(20, 0, 0, 0));
        flowCustomers.setPadding(new Insets(20, 0, 0, 0));
        flowCases.setPadding(new Insets(20, 0, 0, 0));
        flowUsers.setPadding(new Insets(20, 0, 0, 0));
    }

    private void loadLabelsForIcons() {
        Label home = new Label("Home");
        home.getStyleClass().add("burgerBarMenuLabels");
        home.setPadding(new Insets(0,0,20,0));
        Label customers = new Label("Customers");
        customers.getStyleClass().add("burgerBarMenuLabels");
        customers.setPadding(new Insets(0,0,20,0));
        Label cases = new Label("Cases");
        cases.getStyleClass().add("burgerBarMenuLabels");
        cases.setPadding(new Insets(0,0,20,0));
        Label users = new Label("Users");
        users.getStyleClass().add("burgerBarMenuLabels");
        users.setPadding(new Insets(0,0,20,0));
        flowHome.getChildren().add(home);
        flowCustomers.getChildren().add(customers);
        flowCases.getChildren().add(cases);
        flowUsers.getChildren().add(users);
        flowBurger.setAlignment(Pos.CENTER_LEFT);
        flowHome.setAlignment(Pos.CENTER_LEFT);
        flowCustomers.setAlignment(Pos.CENTER_LEFT);
        flowCases.setAlignment(Pos.CENTER_LEFT);
        flowUsers.setAlignment(Pos.CENTER_LEFT);
        flowBurger.setPadding(new Insets(20, 0, 0, 13));
        flowHome.setPadding(new Insets(20, 0, 0, 13));
        flowCustomers.setPadding(new Insets(20, 0, 0, 13));
        flowCases.setPadding(new Insets(20, 0, 0, 13));
        flowUsers.setPadding(new Insets(20, 0, 0, 13));
    }

    private void loadIconsToBar() {
        imgBurger.setImage(imgBurgerNormal);
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

}
