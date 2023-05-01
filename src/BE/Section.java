package BE;

import javafx.scene.image.Image;

import java.sql.Time;
import java.time.LocalDate;

public class Section {
    private int sectionID;
    private String sectionTitle;
    private Image sketch;
    private byte[] sketchBytes;
    private String sketchComment;
    private Image image;
    private byte[] imageBytes;
    private String imageComment;
    private String description;
    private String madeByTechnician;
    private int reportID;
    private int addendumID;
    private LocalDate createdDate;
    private Time time;

    public Section(int sectionID, String sectionTitle, Image sketch, String sketchComment, Image image, String imageComment, String description, String madeByTechnician, int reportID, int addendumID, LocalDate createdDate, Time time) {
        this.sectionID = sectionID;
        this.sectionTitle = sectionTitle;
        this.sketch = sketch;
        this.sketchComment = sketchComment;
        this.image = image;
        this.imageComment = imageComment;
        this.description = description;
        this.madeByTechnician = madeByTechnician;
        this.reportID = reportID;
        this.addendumID = addendumID;
        this.createdDate = createdDate;
        this.time = time;
    }

    public int getSectionID() {
        return sectionID;
    }

    public void setSectionID(int sectionID) {
        this.sectionID = sectionID;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public void setSectionTitle(String sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    public Image getSketch() {
        return sketch;
    }

    public void setSketch(Image sketch) {
        this.sketch = sketch;
    }

    public String getSketchComment() {
        return sketchComment;
    }

    public void setSketchComment(String sketchComment) {
        this.sketchComment = sketchComment;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMadeByTechnician() {
        return madeByTechnician;
    }

    public void setMadeByTechnician(String madeByTechnician) {
        this.madeByTechnician = madeByTechnician;
    }

    public int getReportID() {
        return reportID;
    }

    public void setReportID(int reportID) {
        this.reportID = reportID;
    }

    public int getAddendumID() {
        return addendumID;
    }

    public void setAddendumID(int addendumID) {
        this.addendumID = addendumID;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }
}
