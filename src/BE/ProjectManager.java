package BE;

import javafx.scene.image.Image;

public class ProjectManager extends User{

    public ProjectManager(int userID, String fullName, String userName, String password, String telephone, String email, int userType, String userStringType, boolean isActive, Image profilePicture, int[] recentlyViewedCases, int[] activeCases) {
        super(userID,fullName,userName,password,telephone,email, 2, "Project Manager", isActive, profilePicture,recentlyViewedCases,activeCases);
    }

    public ProjectManager(int userID, String fullName, String userName, String userStringType, String telephone, String email, boolean userActive) {
        super(userID, fullName, userName, userStringType, telephone, email, userActive);
    }
}
