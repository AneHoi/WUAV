package BLL;

import BE.Technician;
import BE.User;
import DAL.UsersDAO;
import BLL.util.BCrypt;

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
    public void handlePassword(String userName, String password) throws Exception {
        String salt =BCrypt.gensalt(16);

        String hashedPassword = BCrypt.hashpw(password, salt);

        usersDAO.setPassword(userName, hashedPassword, salt);
    }
    public User checkLoggedInUser(String userName, String password) throws Exception {
        String salt = usersDAO.getUserSalt(userName);

        String hashedPassword =BCrypt.hashpw(password, salt);

        return usersDAO.doesLogInExist(userName, hashedPassword);
    }
    public boolean checkPassword(String hashedPW, String password) throws Exception {
        String hashedPassword = hashedPW;
        String checkPassword = password;
        boolean hashed = (BCrypt.checkpw(checkPassword,hashedPassword));
        return hashed;
        }

    public void createNewUserWithImage(String fullName, String userName, String userTlf, String userEmail, int userType, byte[] profilePicture) throws SQLException {
        usersDAO.createNewUserWithImage(fullName, userName, userTlf, userEmail, userType, profilePicture);
    }
}

