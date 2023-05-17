package BE;

import java.time.LocalDate;
import java.time.LocalTime;

public class LoginDetails {
    private int loginDetailsID;
    private boolean noLoginDetails;
    private String component;
    private String username;
    private String password;
    private String additionalInfo;
    private User addedBy;
    private LocalDate createdDate;
    private LocalTime createdTime;

    public LoginDetails(int loginDetailsID, boolean noLoginDetails, String component, String username, String password, String additionalInfo, User addedBy, LocalDate createdDate, LocalTime createdTime) {
        this.loginDetailsID = loginDetailsID;
        this.noLoginDetails = noLoginDetails;
        this.component = component;
        this.username = username;
        this.password = password;
        this.additionalInfo = additionalInfo;
        this.addedBy = addedBy;
        this.createdDate = createdDate;
        this.createdTime = createdTime;
    }

    public LoginDetails(int id, boolean noLoginDetails, Technician t, LocalDate date, LocalTime time) {
        this.loginDetailsID = id;
        this.noLoginDetails = noLoginDetails;
        this.addedBy = t;
        this.createdDate = date;
        this.createdTime = time;
    }

    public int getLoginDetailsID() {
        return loginDetailsID;
    }

    public void setLoginDetailsID(int loginDetailsID) {
        this.loginDetailsID = loginDetailsID;
    }

    public boolean isNoLoginDetails() {
        return noLoginDetails;
    }

    public void setNoLoginDetails(boolean noLoginDetails) {
        this.noLoginDetails = noLoginDetails;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public User getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(User addedBy) {
        this.addedBy = addedBy;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalTime createdTime) {
        this.createdTime = createdTime;
    }

}
