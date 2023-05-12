package BLL;

import BE.Case;
import BE.Technician;
import DAL.CaseDAO;

import java.sql.SQLException;
import java.util.List;

public class CaseManager {
    private CaseDAO caseDAO;

    public CaseManager(){
        caseDAO = new CaseDAO();
    }
    public List<Case> getCasesForThisCustomer(int customerID) throws SQLException {
        return caseDAO.getCasesForThisCustomer(customerID);
    }

    public void createNewCase(String caseName, String caseContact, String caseDescription, int customerID) throws SQLException {
        caseDAO.createNewCase(caseName, caseContact, caseDescription, customerID);
    }

    public void addTechnicianToCase(int caseID, List<Technician> chosenTechnicians) throws SQLException {
        caseDAO.addTechnicianToCase(caseID, chosenTechnicians);
    }

    public List<Case> getAllCases() throws SQLException {
        return caseDAO.getAllCases();
    }

    public void updateCase(int caseID, String caseName, String contactPerson, String caseDescription) throws SQLException {
        caseDAO.updateCase(caseID, caseName,contactPerson,caseDescription);
    }

    public List<Technician> getAssignedTechnicians(int caseID) throws SQLException {
        return caseDAO.getAssignedTechnicians(caseID);
    }
}