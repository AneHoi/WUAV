package DAL.Interfaces;

import BE.Case;
import BE.Technician;

import java.sql.SQLException;
import java.util.List;

public interface ICaseDAO {
    List<Case> getCasesForThisCustomer(int customerID) throws SQLException;

    void createNewCase(String caseName, String caseContact, String caseDescription, int customerID) throws SQLException;

    void addTechnicianToCase(int caseID, int technicianID) throws SQLException;
}
