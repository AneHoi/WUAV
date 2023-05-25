package GUI.Controller;

import BE.DrawingIcon;
import BE.Report;
import GUI.Controller.Util.DraggableMaker;
import GUI.Controller.Util.Util;
import GUI.Model.Model;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class DrawSketchController implements Initializable {
    @FXML
    private Button btnSnapshot;
    @FXML
    private ImageView imgView;
    @FXML
    private AnchorPane drawingPane;
    @FXML
    private VBox vboxIcons;
    @FXML
    private ScrollPane scrollPaneIcons;
    private Util util = new Util();
    private Model model;
    private Report currentReport;
    private int nextPosition;


    DraggableMaker draggableMaker = new DraggableMaker();
    private final String homeOrange = "data/Images/Home orange.png";
    private ArrayList<String> colors = new ArrayList<>(
            Arrays.asList("green", "yellow", "red", "blue", "purple", "black", "white"));

    private List<DrawingIcon> imageIcons = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = new Model();
        draggableMaker.setStartVal(scrollPaneIcons.getPrefWidth());
        getAllIcons();
        testButtons();
    }

    private void getAllIcons() {
        vboxIcons.getChildren().clear();
        try {
            imageIcons = model.getAllDrawingIcons();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not get the icons", ButtonType.CANCEL);
            alert.showAndWait();
        }
    }

    private void testButtons() {
        getAllIcons();
        for (DrawingIcon drawingIcon: imageIcons) {
            Label lblIcon = new Label();
            Label lblIconName = new Label(drawingIcon.getImageComment());
            lblIconName.getStyleClass().add("infoLabel");
            lblIconName.setStyle("-fx-alignment: center");
            // Creating a graphic (image) on the label
            Image img = drawingIcon.getImage();
            ImageView view = new ImageView(img);
            view.setFitHeight(80);
            view.setPreserveRatio(true);
            lblIcon.setGraphic(view);
            lblIcon.setPrefSize(80,80);
            lblIconName.setPrefWidth(80);
            lblIcon.setOnMouseClicked(event -> spawnNewlbl(lblIcon, img));
            lblIconName.setOnMouseClicked(event -> SpawnLabelName(lblIconName));
            vboxIcons.getChildren().add(lblIcon);
            vboxIcons.getChildren().add(lblIconName);
        }
    }

    private void SpawnLabelName(Label lbl) {
        Label newLabel = new Label();
        newLabel.setPrefSize(lbl.getWidth(), lbl.getHeight());
        newLabel.setText(lbl.getText());
        newLabel.getStyleClass().add("infoLabel");

        newLabel.setLayoutX(100);
        newLabel.setLayoutY(100);
        draggableMaker.makeDraggable(newLabel);
        drawingPane.getChildren().add(newLabel);
    }

    private void spawnNewlbl(Label lbl, Image graphic) {
        Label newLabel = new Label();
        newLabel.setPrefSize(lbl.getPrefWidth(), lbl.getPrefHeight());
        newLabel.setText(lbl.getText());

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
            saveImgController.setCurrentReport(currentReport);
            saveImgController.setNextPosition(nextPosition);
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

    /**
     * Opens a new window for adding an icon to the drawing program
     */
    public void addIcon(){
        AddIconController addIconController = new AddIconController();
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/GUI/View/AddIconView.fxml"));
        loader.setController(addIconController);
        util.openNewWindow(stage, loader, "Could not open Add Image Window");
        testButtons();
    }

    public Report getCurrentReport() {
        return currentReport;
    }

    public void setCurrentReport(Report currentReport) {
        this.currentReport = currentReport;
    }

    public int getNextPosition() {
        return nextPosition;
    }

    public void setNextPosition(int nextPosition) {
        this.nextPosition = nextPosition;
    }
}
