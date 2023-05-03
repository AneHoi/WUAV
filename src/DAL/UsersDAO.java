package DAL;

import BE.Technician;
import DAL.Interfaces.IUsersDAO;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UsersDAO implements IUsersDAO {
    private DBConnector db;

    public UsersDAO() {
        db = DBConnector.getInstance();
    }

    @Override
    public List<Technician> getAllTechnicians() throws SQLException {
        List<Technician> technicians = new ArrayList<>();
        try (Connection conn = db.getConnection()) {
            String sql = "SELECT * FROM User_ WHERE User_Type = 3;";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int techID = rs.getInt("User_ID");
                String techName = rs.getString("User_Full_Name");

                Technician t = new Technician(techID,techName);
                technicians.add(t);
            }

        } catch (SQLException e) {
            throw new SQLException("Could not get Technicians from Database");
        }

        return technicians;
    }
}
