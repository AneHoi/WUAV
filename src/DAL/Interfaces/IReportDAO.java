package DAL.Interfaces;

import BE.Report;

import java.sql.SQLException;
import java.util.List;

public interface IReportDAO {

    void createNewReport(String reportName, String reportDescription, int caseID) throws SQLException;


    List<Report> getReports(int caseID) throws SQLException;
}
