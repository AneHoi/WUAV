package DAL.Interfaces;

import BE.Addendum;
import BE.Report;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface IReportDAO {

    void createNewReport(String reportName, String reportDescription, int caseID, int userID) throws SQLException;


    List<Report> getReports(int caseID) throws SQLException;

    void createNewAddendum(String addendumName, String addendumDescription, int caseID, int reportID, int userID) throws SQLException;

    List<Addendum> getAddendums(int caseID, int reportID) throws SQLException;

    void SaveTextToReport(int position, int reportID, String txt, int userID, LocalDate createdDate, LocalTime createdTime) throws SQLException;

    void SaveImageToReport(int position, int reportID, byte[] dataImage, String comment, int userID, LocalDate createdDate, LocalTime createdTime) throws SQLException;
}
