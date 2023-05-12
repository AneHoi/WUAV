package BLL;

import BE.Technician;
import BE.User;
import DAL.UsersDAO;

import java.sql.SQLException;
import java.util.List;

public class UserManager {
    private UsersDAO usersDAO;
    public UserManager() {
        usersDAO = new UsersDAO();
    }

    public List<Technician> getAllTechnicians() throws SQLException {
        return usersDAO.getAllTechnicians();
    }

    public List<User> getAllUsers() throws SQLException {
        return usersDAO.getAllUsers();
    }

    public void updateUser(int userID, String fullName, String userName, String userTlf, String userEmail, boolean userActive) throws SQLException {
        usersDAO.updateUser(userID, fullName, userName, userTlf, userEmail, userActive);
    }

    public void createNewUser(String fullName, String userName, String userTlf, String userEmail, int userType) throws SQLException {
        usersDAO.createNewUser(fullName,userName,userTlf,userEmail,userType);
    }

}
