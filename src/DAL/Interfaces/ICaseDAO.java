package DAL.Interfaces;

import BE.Case;

import java.sql.SQLException;
import java.util.List;

public interface ICaseDAO {
    List<Case> getCasesForThisCustomer(int customerID) throws SQLException;
}
