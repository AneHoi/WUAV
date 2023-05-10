package DAL;

import BE.*;
import DAL.Interfaces.IUsersDAO;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
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
                boolean isActive = rs.getBoolean("User_Active");
                if (isActive) {
                    Technician t = new Technician(techID, techName);
                    technicians.add(t);
                }

            }

        } catch (SQLException e) {
            throw new SQLException("Could not get Technicians from Database");
        }

        return technicians;
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection conn = db.getConnection()) {
            String sql = "SELECT * FROM User_ LEFT JOIN User_Type ON User_.User_Type = User_Type_ID;";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int userID = rs.getInt("User_ID");
                String userFullName = rs.getString("User_Full_Name");
                String userName = rs.getString("User_Name");
                int userType = rs.getInt("User_Type");
                String userStringType = rs.getNString("USER_TYPE_TYPE");
                String userEmail = rs.getString("User_Email");
                String userTlf = rs.getString("User_tlf");
                boolean userActive = rs.getBoolean("User_Active");

                if (userType == 1) {
                    User user = new Admin(userID, userFullName, userName, userStringType, userTlf, userEmail, userActive);
                    users.add(user);
                } else if (userType == 2) {
                    User user = new ProjectManager(userID, userFullName, userName, userStringType, userTlf, userEmail, userActive);
                    users.add(user);
                } else if (userType == 3) {
                    User user = new Technician(userID, userFullName, userName, userStringType, userTlf, userEmail, userActive);
                    users.add(user);
                } else {
                    User user = new SalesRepresentative(userID, userFullName, userName, userStringType, userTlf, userEmail, userActive);
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Could not get Users from Database");
        }

        return users;
    }

    @Override
    public void updateUser(int userID, String fullName, String userName, String userTlf, String userEmail, boolean userActive) throws SQLException {
        try (Connection conn = db.getConnection()) {
            String sql = "UPDATE User_ SET User_Full_Name = (?), User_Name = (?), User_tlf = (?), User_Email = (?), User_Active = (?) WHERE User_ID = " + userID + ";";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, fullName);
            ps.setString(2, userName);
            ps.setString(3, userTlf);
            ps.setString(4, userEmail);
            ps.setBoolean(5, userActive);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Could not update the User");
        }
    }

    @Override
    public void createNewUser(String fullName, String userName, String userTlf, String userEmail, int userType) throws SQLException {
        try (Connection conn = db.getConnection()) {
            String sql = "INSERT INTO User_(User_Full_Name, User_Name, User_Type, User_tlf, User_Email, User_Active) VALUES(?,?,?,?,?,?) ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, fullName);
            ps.setString(2, userName);
            ps.setInt(3, userType);
            ps.setString(4, userTlf);
            ps.setString(5, userEmail);
            ps.setBoolean(6, true);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Could not create the User");
        }

    }
}
