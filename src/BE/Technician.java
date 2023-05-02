package BE;

import javafx.scene.image.Image;

public class Technician extends User {
    public Technician(int userID, String fullName, String userName, String password, String telephone, String email, int userType, String userStringType, Image profilePicture, int[] recentlyViewedCases, int[] activeCases) {
        super(userID,fullName,userName,password,telephone,email, 3, "Technician", profilePicture,recentlyViewedCases,activeCases);
    }

    public Technician(int techID, String techName) {
        super(techID,techName);
    }
}
