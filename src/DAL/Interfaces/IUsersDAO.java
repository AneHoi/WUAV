package DAL.Interfaces;

import BE.Technician;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.SQLException;
import java.util.List;

public interface IUsersDAO {
    List<Technician> getAllTechnicians() throws SQLException;
}
