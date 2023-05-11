package BE;

import javafx.scene.image.Image;

import java.time.LocalDate;
import java.time.LocalTime;

public class ImageOnReport {
    private Image image;
    private String imageComment;
    private int positionOnReport;
    private User addedByTech;
    private LocalDate createdDate;
    private LocalTime createdTime;

    public ImageOnReport(Image image, String imageComment, int positionOnReport, User addedByTech, LocalDate createdDate, LocalTime createdTime) {
        this.image = image;
        this.imageComment = imageComment;
        this.positionOnReport = positionOnReport;
        this.addedByTech = addedByTech;
        this.createdDate = createdDate;
        this.createdTime = createdTime;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
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
}
