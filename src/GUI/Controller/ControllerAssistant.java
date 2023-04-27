package GUI.Controller;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class ControllerAssistant {
    @FXML
    private static BorderPane borderPane;
    private static ControllerAssistant controllerAssistant;

    /**
     * Controller constructor, only used once in get instance
     */
    private ControllerAssistant() {

    }

    /**
     * Singleton, returns the instance of the Assistant
     * @return
     */
    public static ControllerAssistant getInstance(){
        if (controllerAssistant == null) controllerAssistant = new ControllerAssistant();

        return controllerAssistant;
    }

    /**
     * Takes a borderpane for and saves for general use.
     * should be implemented the first time the class is called.
     * @param borderPane
     */
    public void setBorderPane(BorderPane borderPane){
        this.borderPane = borderPane;
    }
}
