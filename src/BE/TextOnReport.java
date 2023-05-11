package BE;

import java.time.LocalDate;
import java.time.LocalTime;

public class TextOnReport {
    private String text;
    private int positionOnReport;
    private User addedByTech;
    private LocalDate createdDate;
    private LocalTime createdTime;

    public TextOnReport(String text, int positionOnReport, User addedByTech, LocalDate createdDate, LocalTime createdTime) {
        this.text = text;
        this.positionOnReport = positionOnReport;
        this.addedByTech = addedByTech;
        this.createdDate = createdDate;
        this.createdTime = createdTime;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

