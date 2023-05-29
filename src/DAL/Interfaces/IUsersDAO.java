package DAL.Interfaces;

import BE.Technician;
import BE.User;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.SQLException;
import java.util.List;

public interface IUsersDAO {
    List<Technician> getAllTechnicians() throws SQLException;

    List<User> getAllUsers() throws SQLException;

    void updateUser(int userID, String fullName, String userName, String userTlf, String userEmail, boolean userActive) throws SQLException;

    void createNewUser(String fullName, String userName, String userTlf, String userEmail, int userType) throws SQLException;
    String getUserSalt(String userName) throws Exception;
    void setPassword(String userName, String password, String salt) throws Exception;
    User doesLogInExist (String username, String password) throws Exception;

    void createNewUserWithImage(String fullName, String userName, String userTlf, String userEmail, int userType, byte[] profilePicture) throws SQLException;
}
