package BE;

import java.time.LocalDate;

public class Report {
    private int reportID;
    private String reportName;
    private String reportDescription;
    private int caseID;
    private int assignedTechnician;
    private LocalDate createdDate;
    private int logID;
    private boolean isActive;

    public Report(int reportID, String reportName, String reportDescription, int caseID, int assignedTechnician, LocalDate createdDate, int logID, boolean isActive) {
        this.reportID = reportID;
        this.reportName = reportName;
        this.reportDescription = reportDescription;
        this.caseID = caseID;
        this.assignedTechnician = assignedTechnician;
        this.createdDate = createdDate;
        this.logID = logID;
        this.isActive = isActive;
    }

    public int getReportID() {
        return reportID;
    }

    public void setReportID(int reportID) {
        this.reportID = reportID;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportDescription() {
        return reportDescription;
    }

    public void setReportDescription(String reportDescription) {
        this.reportDescription = reportDescription;
    }

    public int getCaseID() {
        return caseID;
    }

    public void setCaseID(int caseID) {
        this.caseID = caseID;
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
