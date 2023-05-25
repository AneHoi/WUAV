package BE;

import javafx.scene.image.Image;

public class SalesRepresentative extends User {
    public SalesRepresentative(int userID, String fullName, String userName, String password, String telephone, String email, boolean isActive, Image profilePicture) {
        super(userID,fullName,userName,password,telephone,email, 4, "SalesRepresentative", isActive, profilePicture);
    }

    public SalesRepresentative(int userID, String fullName, String userName, String userStringType, String telephone, String email, boolean userActive) {
        super(userID, fullName, userName, "SalesRepresentative", telephone, email, userActive);
    }
}
