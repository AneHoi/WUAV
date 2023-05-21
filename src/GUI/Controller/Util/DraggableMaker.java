package GUI.Controller.Util;

import javafx.scene.Node;

public class DraggableMaker {
    private double mouseX, mouseY, startVal;

    public void makeDraggable(Node... node) {
        for (Node nodes : node) {
            nodes.setOnMousePressed(mouseEvent -> {
                //This is the place on the node the cursor grabs
                mouseX = mouseEvent.getX();
                mouseY = mouseEvent.getY();
            });
            nodes.setOnMouseDragged(mouseEvent -> {
                //Updates everytime the mouse drags the node
                //SetLayoutX and Y is the layout of the AnchorPane, where the .getScene is the wholeWindow
                //Needs to be the start value partWith
                nodes.setLayoutX(mouseEvent.getSceneX() - mouseX - startVal);
                nodes.setLayoutY(mouseEvent.getSceneY() - mouseY);
            });
        }
    }
    public void setStartVal(double startVal) {
        this.startVal = startVal;
    }
}
