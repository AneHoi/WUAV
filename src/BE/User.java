package BE;

import javafx.scene.image.Image;

import java.util.Arrays;

public abstract class User {

    private int userID;
    private String fullName;
    private String userName;
    private String password;
    private String telephone;
    private String email;
    private int userType;
    private String userStringType;
    private Image profilePicture;
    private int[] recentlyViewedCases;
    private int[] activeCases;

    public User(int userID, String fullName, String userName, String password, String telephone, String email, int userType, String userStringType, Image profilePicture, int[] recentlyViewedCases, int[] activeCases) {
        this.userID = userID;
        this.fullName = fullName;
        this.userName = userName;
        this.password = password;
        this.telephone = telephone;
        this.email = email;
        this.userType = userType;
        this.userStringType = userStringType;
        this.profilePicture = profilePicture;
        this.recentlyViewedCases = recentlyViewedCases;
        this.activeCases = activeCases;
    }

    public User(int techID, String techName) {
        this.userID = techID;
        this.fullName = techName;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getUserStringType() {
        return userStringType;
    }

    public void setUserStringType(String userStringType) {
        this.userStringType = userStringType;
    }

    public Image getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Image profilePicture) {
        this.profilePicture = profilePicture;
    }

    public int[] getRecentlyViewedCases() {
        return recentlyViewedCases;
    }

    public void setRecentlyViewedCases(int[] recentlyViewedCases) {
        this.recentlyViewedCases = recentlyViewedCases;
    }

    public int[] getActiveCases() {
        return activeCases;
    }

    public void setActiveCases(int[] activeCases) {
        this.activeCases = activeCases;
    }

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", fullName='" + fullName + '\'' +
                ", userName='" + userName + '\'' +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                ", userStringType='" + userStringType + '\'' +
                ", profilePicture=" + profilePicture +
                ", recentlyViewedCases=" + Arrays.toString(recentlyViewedCases) +
                ", activeCases=" + Arrays.toString(activeCases) +
                '}';
    }
}
