package BLL;

import BE.*;
import DAL.CaseDAO;
import DAL.DAO;
import DAL.ReportDAO;
import DAL.UsersDAO;

import java.sql.SQLException;
import java.util.List;

public class Manager {
    private UsersDAO usersDAO;
    private CaseDAO caseDAO;
    private ReportDAO reportDAO;
    private DAO dao;

    public Manager() {
        dao = new DAO();
        reportDAO = new ReportDAO();
        caseDAO = new CaseDAO();
        usersDAO = new UsersDAO();
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

    public List<Case> getCasesForThisCustomer(int customerID) throws SQLException {
        return caseDAO.getCasesForThisCustomer(customerID);
    }

    public List<Technician> getAllTechnicians() throws SQLException {
        return usersDAO.getAllTechnicians();
    }
}
