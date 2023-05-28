package GUI.Controller;

import BE.DrawingIcon;
import BE.Report;
import GUI.Controller.Util.DraggableMaker;
import GUI.Controller.Util.Util;
import GUI.Model.Model;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DrawSketchController implements Initializable {
    @FXML
    private BorderPane borderPane;
    @FXML
    private VBox vboxCables, vboxIcons;
    @FXML
    private ScrollPane scrollPaneIcons;
    @FXML
    private StackPane stackPane;
    @FXML
    private AnchorPane iconPane;
    @FXML
    private Canvas canvasForCables;
    private Util util = new Util();
    private Model model;
    private Report currentReport;
    private int nextPosition;
    boolean iconsInFront;
    private ContextMenu contextMenu = new ContextMenu();

    DraggableMaker draggableMaker = new DraggableMaker();

    private List<DrawingIcon> imageIcons = new ArrayList<>();
    private Color color;
    private Node chosenNode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = new Model();
        iconPane.setStyle("-fx-border-color: rgba(23,38,58,1); -fx-border-width: 10;");
        iconsInFront = true;
        stackPane.getChildren().clear();
        stackPane.getChildren().addAll(canvasForCables, iconPane);
        //stackPane.getChildren().addAll(canvasForCables);
        borderPane.setCenter(stackPane);
        draggableMaker.setStartVal(scrollPaneIcons.getPrefWidth());
        createContectmenu();
        getAllIcons();
        getAllCables();
        testButtons();
        drawing();
    }

    private void createContectmenu() {
        //Creating a context menu
        //Creating the menu Items for the context menu
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(e -> {
            iconPane.getChildren().remove(chosenNode);
        });
        contextMenu.getItems().addAll(deleteItem);
    }

    private void drawing() {
        GraphicsContext g = canvasForCables.getGraphicsContext2D();
        canvasForCables.setOnMouseDragged(e -> {
            double size = Double.parseDouble(String.valueOf(30));
            double x = e.getX() - size / 2;
            double y = e.getY() - size / 2;

            if (color != null) {
                g.setFill(color);
                g.fillRect(x, y, size, size);
            }else {
            }
        });
    }

    private void getAllCables() {
        Button drawRed = new Button("Red");
        drawRed.getStyleClass().clear();
        drawRed.setStyle("-fx-background-color: red");
        drawRed.setPrefSize(80, 80);
        drawRed.setOnMouseClicked(e ->{
            beginToMoveIcons(false);
            color = Color.valueOf("red");
        });
        vboxCables.getChildren().add(drawRed);
    }

    private void beginToMoveIcons(boolean bool) {
        if (bool == false) {
            stackPane.getChildren().clear();
            stackPane.getChildren().addAll(iconPane, canvasForCables);
        }else {
            stackPane.getChildren().clear();
            stackPane.getChildren().addAll(canvasForCables, iconPane);
        }
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
            //Set graphics for lblIcon
            graphicsForLabelIcon(lblIcon, drawingIcon);
            lblIconName.setPrefWidth(80);
            lblIconName.setOnMouseClicked(event -> {
                SpawnLabelName(lblIconName);
                beginToMoveIcons(true);
            });
            vboxIcons.getChildren().add(lblIcon);
            vboxIcons.getChildren().add(lblIconName);
        }
    }

    private void graphicsForLabelIcon(Label lblIcon, DrawingIcon drawingIcon) {

        // Creating a graphic (image) on the label
        Image img = drawingIcon.getImage();
        ImageView view = new ImageView(img);
        view.setFitHeight(80);
        view.setPreserveRatio(true);
        lblIcon.setGraphic(view);
        lblIcon.setPrefSize(80,80);

        lblIcon.setOnMouseClicked(event -> {
            spawnNewlbl(lblIcon, img);
            beginToMoveIcons(true);
        });

    }

    private void SpawnLabelName(Label lbl) {
        Label newLabel = new Label();
        newLabel.setPrefSize(lbl.getWidth(), lbl.getHeight());
        newLabel.setText(lbl.getText());
        newLabel.getStyleClass().add("infoLabel");

        newLabel.setLayoutX(100);
        newLabel.setLayoutY(100);
        draggableMaker.makeDraggable(newLabel);
        newLabel.setContextMenu(contextMenu);
        newLabel.setOnMouseClicked(e -> {
            chosenNode = newLabel;
        });
        iconPane.getChildren().add(newLabel);
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
        newLabel.setContextMenu(contextMenu);
        newLabel.setOnMouseClicked(e -> {
            chosenNode = newLabel;
        });
        draggableMaker.makeDraggable(newLabel);
        iconPane.getChildren().add(newLabel);
    }

    public void snapshot() {
        WritableImage snapshot = borderPane.getCenter().snapshot(new SnapshotParameters(), null);

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
        iconPane.getChildren().clear();
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
