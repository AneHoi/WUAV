package BLL;

import BE.Customer;
import BE.Report;
import DAL.DAO;
import DAL.ReportDAO;

import java.sql.SQLException;
import java.util.List;

public class Manager {

    private ReportDAO reportDAO;
    private DAO dao;

    public Manager() {
        dao = new DAO();
        reportDAO = new ReportDAO();
    }

    public List<Customer> getAllCustomers() throws SQLException {
        return dao.getAllCostumers();
    }

    public void saveCustomer(Customer customer) {
        dao.saveCustomer(customer);
    }

    public void createNewReport(String reportName, String reportDescription, int caseID, int userID) throws SQLException {
        reportDAO.createNewReport(reportName,reportDescription, caseID, userID);
    }

    public List<Report> getReports(int caseID) throws SQLException {
        return reportDAO.getReports(caseID);
    }
}
