package GUI.Controller.Util;

import BE.Admin;
import BE.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ControllerAssistant {
    @FXML
    private static BorderPane borderPane;

    private static ControllerAssistant controllerAssistant;
    private static User loggedInUser;

    /**
     * Controller constructor, only used once in get instance
     */
    public ControllerAssistant() {

    }

    /**
     * Singleton, returns the instance of the Assistant
     *
     * @return
     */
    public static ControllerAssistant getInstance() {
        if (controllerAssistant == null) controllerAssistant = new ControllerAssistant();
        return controllerAssistant;
    }

    /**
     * Takes a borderpane and saves for general use.
     * should be implemented the first time the class is called.
     *
     * @param borderPane
     */
    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }

    /**
     * Sets the center of the borderPane, the "main" window of the application
     * @param file
     * @throws IOException
     */
    public void loadCenter(String file) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/GUI/View/" + file));
        Pane newScene = loader.load();

        borderPane.setCenter(newScene);
    }

    /**
     * Loads the top bar of the application
     * @param file
     * @throws IOException
     */
    public void loadTop(String file) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/GUI/View/" + file));
        HBox newScene = loader.load();

        borderPane.setTop(newScene);
    }

    /**
     * Loads the burgerbar of the application
     * @param file
     * @throws IOException
     */
    public void loadLeft(String file) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/GUI/View/" + file));
        VBox newScene = loader.load();

        borderPane.setLeft(newScene);
    }

    /**
     * Sets the logged-in user
     * @param loggedInUser
     */
    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    /**
     * Returns logged in user
     *
     * @return
     */
    public User getLoggedInUser() {
        return this.loggedInUser;
    }


}
