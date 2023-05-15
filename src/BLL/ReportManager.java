package BLL;

import BE.*;
import DAL.ReportDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ReportManager {
    private ReportDAO reportDAO;

    public ReportManager() {
        reportDAO = new ReportDAO();
    }

    public void createNewReport(String reportName, String reportDescription, int caseID, int userID) throws SQLException {
        reportDAO.createNewReport(reportName, reportDescription, caseID, userID);
    }


    public List<Report> getReports(int caseID) throws SQLException {
        return reportDAO.getReports(caseID);
    }
    public Report getChosenReport(int reportID) throws SQLException {
        return reportDAO.getChosenReport(reportID);
    }

    public List<ReportCaseAndCustomer> getAllReports() throws SQLException {
        return reportDAO.getAllReports();
    }


    public void SaveTextToReport(int position, int reportID, String txt, int userID, LocalDate createdDate, LocalTime createdTime) throws SQLException {
        reportDAO.SaveTextToReport(position, reportID, txt, userID, createdDate, createdTime);
    }


    public void SaveImageToReport(int position, int reportID, byte[] dataImage, String comment, int userID, LocalDate createdDate, LocalTime createdTime) throws SQLException {
        reportDAO.SaveImageToReport(position, reportID, dataImage, comment, userID, createdDate, createdTime);
    }

    public List<TextsAndImagesOnReport> getImagesAndTextsForReport(int currentReportID) throws SQLException {
        return reportDAO.getImagesAndTextsForReport(currentReportID);
    }

    public void updateImageInReport(int imageID, byte[] dataImage, String comment, int userID, LocalDate createdDate, LocalTime createdTime) throws SQLException {
        reportDAO.updateImageInReport(imageID, dataImage, comment, userID, createdDate, createdTime);
    }

    public void deletePartOfReport(TextsAndImagesOnReport textOrImage) throws SQLException {
        reportDAO.deletePartOfReport(textOrImage);
    }

    public void updateTextInReport(int textID, String txt, int userID, LocalDate createdDate, LocalTime createdTime) throws SQLException {
        reportDAO.updateTextInReport(textID, txt, userID, createdDate, createdTime);
    }

    public void moveItemUp(int textOrImageID, int positionOnReport) throws SQLException, IllegalStateException {
        reportDAO.moveItemUp(textOrImageID, positionOnReport);
    }

    public void moveItemDown(int textOrImageID, int positionOnReport) throws SQLException, IllegalStateException {
        reportDAO.moveItemDown(textOrImageID, positionOnReport);
    }
}
