package BE;

import java.time.LocalDate;

public class Addendum {
    private int addendumID;
    private String addendumName;
    private String addendumDescription;
    private int reportID;
    private int assignedTechnician;
    private LocalDate createdDate;
    private int logID;
    private boolean isActive;

    public Addendum(int addendumID, String addendumName, String addendumDescription, int reportID, int assignedTechnician, LocalDate createdDate, int logID, boolean isActive) {
        this.addendumID = addendumID;
        this.addendumName = addendumName;
        this.addendumDescription = addendumDescription;
        this.reportID = reportID;
        this.assignedTechnician = assignedTechnician;
        this.createdDate = createdDate;
        this.logID = logID;
        this.isActive = isActive;
    }

    public int getAddendumID() {
        return addendumID;
    }

    public void setAddendumID(int addendumID) {
        this.addendumID = addendumID;
    }

    public String getAddendumName() {
        return addendumName;
    }

    public void setAddendumName(String addendumName) {
        this.addendumName = addendumName;
    }

    public String getAddendumDescription() {
        return addendumDescription;
    }

    public void setAddendumDescription(String addendumDescription) {
        this.addendumDescription = addendumDescription;
    }

    public int getReportID() {
        return reportID;
    }

    public void setReportID(int reportID) {
        this.reportID = reportID;
    }

    public int getAssignedTechnician() {
        return assignedTechnician;
    }

    public void setAssignedTechnician(int assignedTechnician) {
        this.assignedTechnician = assignedTechnician;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public int getLogID() {
        return logID;
    }

    public void setLogID(int logID) {
        this.logID = logID;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
