package DAL.Interfaces;

import BE.Case;
import BE.Technician;

import java.sql.SQLException;
import java.util.List;

public interface ICaseDAO {
    List<Case> getCasesForThisCustomer(int customerID) throws SQLException;

    void createNewCase(String caseName, String caseContact, String caseDescription, int customerID) throws SQLException;

    void addTechnicianToCase(int caseID, List<Technician> technicianID) throws SQLException;
    Case getChosenCase(int chosenCase) throws SQLException;

    List<Case> getAllCases() throws SQLException;

    void updateCase(int caseID, String caseName, String contactPerson, String caseDescription) throws SQLException;

    List<Technician> getAssignedTechnicians(int caseID) throws SQLException;

    void storeUserCaseLink(int userID, int caseID) throws SQLException;

    List<Case> getUsersActiveCases(int userID) throws SQLException;
}
