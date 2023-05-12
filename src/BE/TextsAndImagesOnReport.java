package BE;

import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalTime;

public class TextsAndImagesOnReport {
    private int textOrImageID;
    private String text;
    private Image image;
    private byte[] imageData;
    private String imageComment;
    private String textOrImage;
    private int positionOnReport;
    private User addedByTech;
    private LocalDate createdDate;
    private LocalTime createdTime;

    public TextsAndImagesOnReport(int textOrImageID, byte[] imageData, String imageComment, String textOrImage, int positionOnReport, User addedByTech, LocalDate createdDate, LocalTime createdTime) {
        this.textOrImageID = textOrImageID;
        this.imageData = imageData;
        this.imageComment = imageComment;
        this.textOrImage = textOrImage;
        this.positionOnReport = positionOnReport;
        this.addedByTech = addedByTech;
        this.createdDate = createdDate;
        this.createdTime = createdTime;
    }

    public TextsAndImagesOnReport(int textOrImageID, String text, String textOrImage, int positionOnReport, User addedByTech, LocalDate createdDate, LocalTime createdTime) {
        this.textOrImageID = textOrImageID;
        this.text = text;
        this.textOrImage = textOrImage;
        this.positionOnReport = positionOnReport;
        this.addedByTech = addedByTech;
        this.createdDate = createdDate;
        this.createdTime = createdTime;
    }

    public int getTextOrImageID() {
        return textOrImageID;
    }

    public void setTextOrImageID(int textOrImageID) {
        this.textOrImageID = textOrImageID;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public String getImageComment() {
        return imageComment;
    }

    public void setImageComment(String imageComment) {
        this.imageComment = imageComment;
    }

    public int getPositionOnReport() {
        return positionOnReport;
    }

    public void setPositionOnReport(int positionOnReport) {
        this.positionOnReport = positionOnReport;
    }

    public User getAddedByTech() {
        return addedByTech;
    }

    public void setAddedByTech(User addedByTech) {
        this.addedByTech = addedByTech;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalTime createdTime) {
        this.createdTime = createdTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTextOrImage() {
        return textOrImage;
    }

    public void setTextOrImage(String textOrImage) {
        this.textOrImage = textOrImage;
    }

    public void setImageWithBytes(byte[] byteImage) throws Exception {
        Image img = new Image(new ByteArrayInputStream(byteImage));
        image = img;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
