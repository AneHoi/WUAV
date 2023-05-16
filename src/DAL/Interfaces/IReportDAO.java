package DAL.Interfaces;

import BE.Report;
import BE.ReportCaseAndCustomer;
import BE.TextsAndImagesOnReport;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface IReportDAO {

    void createNewReport(String reportName, String reportDescription, int caseID, int userID) throws SQLException;

    Report getChosenReport(int reportID) throws SQLException;

    List<Report> getReports(int caseID) throws SQLException;

    List<ReportCaseAndCustomer> getAllReports() throws SQLException;

    void SaveTextToReport(int position, int reportID, String txt, int userID, LocalDate createdDate, LocalTime createdTime) throws SQLException;

    void SaveImageToReport(int position, int reportID, byte[] dataImage, String comment, int userID, LocalDate createdDate, LocalTime createdTime) throws SQLException;

    List<TextsAndImagesOnReport> getImagesAndTextsForReport(int currentReportID) throws SQLException;

    void updateImageInReport(int reportID, byte[] dataImage, String comment, int userID, LocalDate createdDate, LocalTime createdTime) throws SQLException;

    void deletePartOfReport(TextsAndImagesOnReport textOrImage) throws SQLException;

    void updateTextInReport(int textID, String txt, int userID, LocalDate createdDate, LocalTime createdTime) throws SQLException;

    void moveItemUp(int textOrImageID, int positionOnReport) throws SQLException, IllegalStateException;

    void moveItemDown(int textOrImageID, int positionOnReport) throws SQLException, IllegalStateException;

    void submitReportForReview(int reportID) throws SQLException;

    void closeReport(int reportID) throws SQLException;

    void updateReport(int reportID, String reportName, String reportDescription, int userID) throws SQLException;

    void deleteReport(int reportID) throws SQLException;
}
