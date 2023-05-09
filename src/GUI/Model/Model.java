package GUI.Model;


import BE.*;

import BLL.Manager;

import java.sql.SQLException;
import java.util.List;

public class Model {

    private static Model model;
    private Customer currentCustomer;
    private Case currentCase;
    private Report currentReport;
    private Addendum currentAddendum;
    private Section currentSection;
    private List<Customer> customers;
    private Manager manager;

    public Model() {
        manager = new Manager();
    }

    public static Model getInstance() {
        if (model == null) model = new Model();
        return model;
    }

    public List<Customer> getAllCustomers() throws SQLException {
        customers = manager.getAllCustomers();
        return customers;
    }

    public void saveCustomer(Customer customer) {
        manager.saveCustomer(customer);
    }

    public void createNewReport(String reportName, String reportDescription, int caseID, int userID) throws SQLException {
        manager.createNewReport(reportName, reportDescription, caseID, userID);
    }

    public List<Report> getReports(int caseID) throws SQLException {
        return manager.getReports(caseID);
    }

    public void setCurrentReport(Report selectedItem) {
        currentReport = selectedItem;
    }

    public Report getCurrentReport() {
        return currentReport;
    }

    public void setCurrentCase(Case currentCase) {
        this.currentCase = currentCase;
    }

    public void setCurrentCustomer(Customer currentCustomer) {
        this.currentCustomer = currentCustomer;
    }

    public Case getCurrentCase() {
        return currentCase;
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public List<Case> getCasesForThisCustomer(int customerID) throws SQLException {
        return manager.getCasesForThisCustomer(customerID);
    }

    public List<Technician> getAllTechnicians() throws SQLException {
        return manager.getAllTechnicians();
    }

    public void createNewCase(String caseName, String caseContact, String caseDescription, int customerID) throws SQLException {
        manager.createNewCase(caseName, caseContact, caseDescription, customerID);
    }

    public void addTechnicianToCase(int caseID, int technicianID) throws SQLException {
        manager.addTechnicianToCase(caseID, technicianID);
    }

    public List<Case> getAllCases() throws SQLException {
        return manager.getAllCases();
    }

    public List<User> getAllUsers() throws SQLException {
        return manager.getAllUsers();
    }

    public void updateUser(int userID, String fullName, String userName, String userTlf, String userEmail, boolean userActive) throws SQLException {
        manager.updateUser(userID, fullName, userName, userTlf, userEmail, userActive);
    }

    public void createNewUser(String fullName, String userName, String userTlf, String userEmail, int userType) throws SQLException {
        manager.createNewUser(fullName, userName, userTlf, userEmail, userType);
    }

    public void createNewAddendum(String addendumName, String addendumDescription, int caseID, int reportID, int userID) throws SQLException {
        manager.createNewAddendum(addendumName, addendumDescription, caseID, reportID, userID);
    }

    public List<Addendum> getAddendums(int caseID, int reportID) throws SQLException {
        return manager.getAddendums(caseID, reportID);
    }

    public Addendum getCurrentAddendum() {
        return currentAddendum;
    }

    public void setCurrentSection(Section section) {
        currentSection = section;
    }

    public List<Section> getAllSections(int currentReportID) throws SQLException {
        return manager.getAllSections(currentReportID);
    }

    public void updateCurrentSection(Section currentSection) throws SQLException {
        manager.updateCurrentSection(currentSection);
    }

    public void createSectionForReport(Section section) throws SQLException {
        manager.createSectionForReport(section);
    }

    public void createSectionForAddendum(Section section) throws SQLException {
        manager.createSectionForAddendum(section);
    }

    public void deleteSection(int sectionID) throws SQLException {
        manager.deleteSection(sectionID);

    }

    public void updateCase(int caseID, String caseName, String contactPerson, String caseDescription) throws SQLException {
        manager.updateCase(caseID, caseName, contactPerson, caseDescription);
    }
}
