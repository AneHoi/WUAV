package BE;

import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;

public class DrawingIcon {

    private byte[] imageData;
    private String imageComment;
    private Image image;

    public DrawingIcon(byte[] imageData, String imageComment) {
        this.imageData = imageData;
        this.imageComment = imageComment;
    }

    public void setImageWithBytes(byte[] byteImage){
        Image img = new Image(new ByteArrayInputStream(byteImage));
        image = img;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageWithData(byte[] imageData) {
        this.imageData = imageData;
    }

    public String getImageComment() {
        return imageComment;
    }

    public void setImageComment(String imageComment) {
        this.imageComment = imageComment;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
