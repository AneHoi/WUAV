package GUI.Controller;

import GUI.Controller.Util.DraggableMaker;
import GUI.Controller.Util.Util;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class DrawSketchController implements Initializable {
    @FXML
    private Button btnSnapshot;
    @FXML
    private ImageView imgView;
    @FXML
    private AnchorPane drawingPane;
    @FXML
    private VBox vbox;
    @FXML
    private ScrollPane scrollPane;
    private Util util = new Util();

    DraggableMaker draggableMaker = new DraggableMaker();
    private final String homeOrange = "data/Images/Home orange.png";
    private ArrayList<String> colors = new ArrayList<>(
            Arrays.asList("green", "yellow", "red", "blue", "purple", "black", "white"));


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        draggableMaker.setStartVal(scrollPane.getPrefWidth());
        getAllImages();

    }

    private void getAllImages() {
        //TODO get all the different images the company wants

        testButtons();
    }

    private void testButtons() {
        for (String color: colors) {
            Label lbl = new Label();
            // Creating a graphic (image) on the label
            Image img = util.loadImages(homeOrange);
            ImageView view = new ImageView(img);
            view.setFitHeight(80);
            view.setPreserveRatio(true);
            lbl.setGraphic(view);
            lbl.setStyle("-fx-background-color: " + color);
            lbl.setPrefSize(80,80);
            lbl.setOnMouseClicked(event -> spawnNewlbl(lbl, img, color));
            vbox.getChildren().add(lbl);
        }
    }

    private void spawnNewlbl(Label lbl, Image graphic, String color) {
        Label newLabel = new Label();
        newLabel.setPrefSize(lbl.getPrefWidth(), lbl.getPrefHeight());
        newLabel.setText(lbl.getText());
        newLabel.setStyle("-fx-background-color: " + color);

        ImageView view = new ImageView(graphic);
        view.setFitHeight(80);
        view.setPreserveRatio(true);
        newLabel.setGraphic(view);
        newLabel.setLayoutX(100);
        newLabel.setLayoutY(100);
        draggableMaker.makeDraggable(newLabel);
        drawingPane.getChildren().add(newLabel);
    }

    public void snapshot() {
        WritableImage snapshot = drawingPane.snapshot(new SnapshotParameters(), null);

        try {
            SaveImgController saveImgController = new SaveImgController();
            saveImgController.setImgView(snapshot);
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/GUI/View/SaveImg.fxml"));
            loader.setController(saveImgController);
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not open the SaveImage page", ButtonType.CANCEL);
            alert.showAndWait();
        }
    }

    public void clear() {
        drawingPane.getChildren().clear();
    }
}
