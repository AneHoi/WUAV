package BE;

import javafx.scene.image.Image;

public class Admin extends User {

    public Admin(int userID, String fullName, String userName, String password, String telephone, String email, boolean isActive, Image profilePicture) {
        super(userID, fullName, userName, password, telephone, email, 1, "Admin", isActive, profilePicture);
    }

    public Admin(int userID, String fullName, String userName, String userStringType, String telephone, String email, boolean userActive) {
        super(userID, fullName, userName, userStringType, telephone, email, userActive);
    }
    public Admin(int userID, int userType, String fullName, String userName, String telephone, String email) {
        super(userID, userType, fullName, userName, telephone, email);
    }

}
