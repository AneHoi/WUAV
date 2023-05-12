package GUI.Controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class BurgerBarController implements Initializable {
    @FXML
    private ImageView imgHome, imgCustomers, imgCases, imgUsers;
    @FXML
    private VBox vboxBurgerMenu;
    @FXML
    private FlowPane flowHome, flowCustomers, flowCases, flowUsers;

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
        imgHome.setOnMouseClicked(event -> loadUserHomePage());
        imgCustomers.setOnMouseClicked(event -> loadCustomerView());
        imgUsers.setOnMouseClicked(event -> loadUserView());
        listenerForClickedImages();
    }

    private void listenerForClickedImages() {
        EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
                    ImageView clickedImage = (ImageView) e.getSource();
                    setImagesToWhite();
                    changeLblStyleToWhite(lHome, lCustomers, lCases, lUsers);

                    if (clickedImage.equals(imgHome)) {
                        imgHome.setImage(imgHomeOrange);
                        changeLblStyleToOrange(lHome);

                    } else if (clickedImage.equals(imgCustomers)) {
                        imgCustomers.setImage(imgCustomerOrange);
                        changeLblStyleToOrange(lCustomers);

                    } else if (clickedImage.equals(imgCases)) {
                        imgCases.setImage(imgCasesOrange);
                        changeLblStyleToOrange(lCases);

                    } else if (clickedImage.equals(imgUsers)) {
                        imgUsers.setImage(imgUsersOrange);
                        changeLblStyleToOrange(lUsers);
                    }
                }
            }
        };
        imgHome.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
        imgCustomers.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
        imgCases.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
        imgUsers.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
    }

    private void changeLblStyleToOrange(Label label) {
        label.getStyleClass().remove(labelStyleWhite);
        label.getStyleClass().add(labelStyleOrange);
    }

    /**
     * loads all the different images into image-variables
     */
    private void loadAllImages() {
        String home = "data/Images/Home.png";
        imgHomeNormal = loadImages(home);
        imgHomeOrange = loadImages(homeOrange);
        imgCustomerNormal = loadImages(customers);
        imgCustomerOrange = loadImages(customersOrange);
        imgCasesNormal = loadImages(cases);
        imgCasesOrange = loadImages(casesOrange);
        imgUsersNormal = loadImages(users);
        imgUsersOrange = loadImages(usersOrange);
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
        try {
            controllerAssistant.loadCenter("CreateUserView.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load Create New User Page", ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void loadCustomerView() {
        try {
            controllerAssistant.loadCenter("CustomerView.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load Create New Customer Page", ButtonType.OK);
            alert.showAndWait();

        }
    }

    private void loadSearchForCases() {
        try {
            controllerAssistant.loadCenter("SearchForCaseView.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load Search For Case Page", ButtonType.OK);
            alert.showAndWait();

        }
    }

    private void loadUserHomePage() {
        try {
            controllerAssistant.loadCenter("UserHomePageView.fxml");
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load Home Page", ButtonType.OK);
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
        lHome = new Label("Homeg");
        lCustomers = new Label("Customers");
        lCases = new Label("Cases");
        lUsers = new Label("Users");

        setStylingAndPaddingLabels(lHome, lCustomers, lCases, lUsers);
        //color orange because the user starts on home-page
        //lHome.getStyleClass().clear();
        lHome.getStyleClass().remove("burgerBarMenuLabels");
        lHome.getStyleClass().add("burgerBarMenuLabelsOrange");

        flowHome.getChildren().add(lHome);
        flowCustomers.getChildren().add(lCustomers);
        flowCases.getChildren().add(lCases);
        flowUsers.getChildren().add(lUsers);

        setPosAndPaddingFlowPanes(flowHome, flowCustomers, flowCases, flowUsers);
    }

    private void setPosAndPaddingFlowPanes(FlowPane... flowpanes) {
        for (FlowPane flowpane: flowpanes) {
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
