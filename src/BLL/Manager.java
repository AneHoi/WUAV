package BLL;
import BE.*;
import DAL.*;

import java.sql.SQLException;
import java.util.List;

public class Manager {
    private UsersDAO usersDAO;
    private CaseDAO caseDAO;
    private ReportDAO reportDAO;
    private SectionDAO sectionDAO;
    private DAO dao;

    public Manager() {
        dao = new DAO();
        reportDAO = new ReportDAO();
        caseDAO = new CaseDAO();
        usersDAO = new UsersDAO();
        sectionDAO = new SectionDAO();
    }

    public List<Customer> getAllCustomers() throws SQLException {
        return dao.getAllCostumers();
    }

    public void saveCustomer(Customer customer) {
        dao.saveCustomer(customer);
    }
    public int createSection(Section section) throws Exception {
        return dao.createSection(section);
    }
    public void createNewReport(String reportName, String reportDescription, int caseID, int userID) throws SQLException {
        reportDAO.createNewReport(reportName,reportDescription, caseID, userID);
    }

    public List<Report> getReports(int caseID) throws SQLException {
        return reportDAO.getReports(caseID);
    }
    public List<Section> getSections(int reportID) throws SQLException {
        return sectionDAO.getSections(reportID);
    }

    public List<Case> getCasesForThisCustomer(int customerID) throws SQLException {
        return caseDAO.getCasesForThisCustomer(customerID);
    }

    public List<Technician> getAllTechnicians() throws SQLException {
        return usersDAO.getAllTechnicians();
    }

    public void createNewCase(String caseName, String caseContact, String caseDescription, int customerID) throws SQLException {
        caseDAO.createNewCase(caseName, caseContact, caseDescription, customerID);
    }

    public void addTechnicianToCase(int caseID, int technicianID) throws SQLException {
        caseDAO.addTechnicianToCase(caseID, technicianID);
    }

    public List<Case> getAllCases() throws SQLException {
        return caseDAO.getAllCases();
    }

    public List<User> getAllUsers() throws SQLException {
        return usersDAO.getAllUsers();
    }

    public void updateUser(int userID, String fullName, String userName, String userTlf, String userEmail, boolean userActive) throws SQLException {
        usersDAO.updateUser(userID, fullName, userName, userTlf, userEmail, userActive);
    }

    public void createNewUser(String fullName, String userName, String userTlf, String userEmail, int userType) throws SQLException {
        usersDAO.createNewUser(fullName,userName,userTlf,userEmail,userType);
    }

    public void createNewAddendum(String addendumName, String addendumDescription, int caseID, int reportID, int userID) throws SQLException {
        reportDAO.createNewAddendum(addendumName, addendumDescription, caseID, reportID, userID);

    }

    public List<Addendum> getAddendums(int caseID, int reportID) throws SQLException {
        return reportDAO.getAddendums(caseID, reportID);
    }

    public List<Section> getAllSections(int currentReportID) throws SQLException {
        return dao.getAllSections(currentReportID);
    }

    public void updateCurrentSection(Section currentSection) throws SQLException {
        dao.updateCurrentSection(currentSection);
    }

    public void createSectionForReport(Section section) throws SQLException {
        dao.createSectionForReport(section);
    }

    public void createSectionForAddendum(Section section) throws SQLException {
        dao.createSectionForAddendum(section);
    }

    public void deleteSection(int sectionID) throws SQLException {
        dao.deleteSection(sectionID);
    }

    public void updateCase(int caseID, String caseName, String contactPerson, String caseDescription) throws SQLException {
        caseDAO.updateCase(caseID, caseName,contactPerson,caseDescription);
    }
}
