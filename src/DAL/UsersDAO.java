package DAL;

import BE.*;
import DAL.Interfaces.IUsersDAO;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import javafx.scene.image.Image;

import javax.swing.plaf.nimbus.State;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
            String sql = "SELECT * FROM User_;";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int userID = rs.getInt("User_ID");
                String userFullName = rs.getString("User_Full_Name");
                String userName = rs.getString("User_Name");
                int userType = rs.getInt("User_Type");
                String userEmail = rs.getString("User_Email");
                String userTlf = rs.getString("User_tlf");
                boolean userActive = rs.getBoolean("User_Active");

                if (userType == 1) {
                    String userStringType = "";
                    User user = new Admin(userID, userFullName, userName, userStringType, userTlf, userEmail, userActive);
                    users.add(user);
                } else if (userType == 2) {
                    String userStringType = "";
                    User user = new ProjectManager(userID, userFullName, userName, userStringType, userTlf, userEmail, userActive);
                    users.add(user);
                } else if (userType == 3) {
                    String userStringType = "";
                    User user = new Technician(userID, userFullName, userName, userStringType, userTlf, userEmail, userActive);
                    users.add(user);
                } else {
                    String userStringType = "";
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

    @Override
    public String getUserSalt(String userName) throws Exception {
        String salt = "";
        try (Connection conn = db.getConnection()) {

            String sql = "SELECT Users_Salt FROM User_Passwords WHERE (Users_Username = ?)";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userName);


            stmt.executeQuery();

            //Execute the update to the DB
            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {
                salt = rs.getString("Users_Salt");
            }

        }

        return salt;
    }

    @Override
    public void setPassword(String userName, String password, String salt) throws Exception {
        try (Connection conn = db.getConnection()) {
            String sql = "INSERT INTO User_Passwords VALUES((SELECT DISTINCT User_ID FROM User_  WHERE User_Name = ?),?,?,?)";

            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, userName);
            stmt.setString(2, userName);
            stmt.setString(3, password);
            stmt.setString(4, salt);

            stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new Exception("Could not set password\n" + ex);
        }
    }

    @Override
    public User doesLogInExist(String username, String password) throws Exception {
        User user = null;
        String userStringType = "";
        try (Connection conn = db.getConnection()) {

            String sql = "SELECT * FROM User_ WHERE User_ID = (SELECT Users_User_ID FROM User_Passwords WHERE (Users_Username = ?) AND (Users_Password = ?))";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            stmt.executeQuery();

            //Execute the update to the DB
            ResultSet rs = stmt.getResultSet();

            while (rs.next()) {

                int id = rs.getInt("User_ID");
                String fullName = rs.getString("User_Full_Name");
                String userName = rs.getString("User_Name");
                String tlfNumber = rs.getString("User_tlf");
                String userEmail = rs.getString("User_Email");
                int userType = rs.getInt("User_Type");
                boolean isActive = rs.getBoolean("User_Active");
                byte[] profilePicture = rs.getBytes("User_Img");
                Image image = null;
                if (profilePicture != null) {
                    image = new javafx.scene.image.Image(new ByteArrayInputStream(profilePicture));
                }
                if (userType == 1) {
                    user = new Admin(id, fullName, userName, password, tlfNumber, userEmail,isActive, image);
                }
                if (userType == 2) {
                    user = new ProjectManager(id, fullName, userName, password, tlfNumber, userEmail, isActive, image);
                }
                if (userType == 3) {
                    user = new Technician(id, fullName, userName, password, tlfNumber, userEmail, isActive, image);
                }
                if (userType == 4) {
                    user = new SalesRepresentative(id, fullName, userName,password, tlfNumber, userEmail, isActive, image);
                }
            }
        }


        return user;
    }

    @Override
    public void createNewUserWithImage(String fullName, String userName, String userTlf, String userEmail, int userType, byte[] profilePicture) throws SQLException {
        try (Connection conn = db.getConnection()) {
            String sql = "INSERT INTO User_(User_Full_Name, User_Name, User_Type, User_tlf, User_Email, User_Img, User_Active) VALUES(?,?,?,?,?,?,?) ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, fullName);
            ps.setString(2, userName);
            ps.setInt(3, userType);
            ps.setString(4, userTlf);
            ps.setString(5, userEmail);
            ps.setBytes(6, profilePicture);
            ps.setBoolean(7, true);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Could not create the User");
        }

    }
}
