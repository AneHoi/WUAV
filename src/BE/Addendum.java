package BE;

import java.time.LocalDate;

public class Addendum extends Report {

    private int connectedReportID;

    public Addendum(int addendumID, String addendumName, String addendumDescription, int reportID, String assignedTechnician, LocalDate createdDate, int logID, boolean isActive, int connectedReportID) {
        super(addendumID, addendumName, addendumDescription, reportID, assignedTechnician, createdDate, logID, isActive);
        this.connectedReportID = connectedReportID;

    }

    public int getConnectedReportID() {
        return connectedReportID;
    }

    public void setConnectedReportID(int connectedReportID) {
        this.connectedReportID = connectedReportID;
    }
}
