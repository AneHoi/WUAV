package BE;

import javafx.scene.image.Image;

import java.time.LocalDate;
import java.time.LocalTime;

public class ImageOnReport {
    private byte[] imageData;
    private String imageComment;
    private int positionOnReport;
    private User addedByTech;
    private LocalDate createdDate;
    private LocalTime createdTime;

    public ImageOnReport(byte[] imageData, String imageComment, int positionOnReport, User addedByTech, LocalDate createdDate, LocalTime createdTime) {
        this.imageData = imageData;
        this.imageComment = imageComment;
        this.positionOnReport = positionOnReport;
        this.addedByTech = addedByTech;
        this.createdDate = createdDate;
        this.createdTime = createdTime;
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
}
