package BE;

import java.time.LocalDate;

public class Case {
    private int caseID;
    private String caseName;
    private String caseDescription;
    private String contactPerson;
    private int customerID;
    private String assignedTechnician;
    private LocalDate createdDate;

    public Case(int caseID, String caseName, String caseDescription, String contactPerson, int customerID, String assignedTechnician, LocalDate createdDate) {
        this.caseID = caseID;
        this.caseName = caseName;
        this.caseDescription = caseDescription;
        this.contactPerson = contactPerson;
        this.customerID = customerID;
        this.assignedTechnician = assignedTechnician;
        this.createdDate = createdDate;
    }

    public int getCaseID() {
        return caseID;
    }

    public void setCaseID(int caseID) {
        this.caseID = caseID;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getCaseDescription() {
        return caseDescription;
    }

    public void setCaseDescription(String caseDescription) {
        this.caseDescription = caseDescription;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getAssignedTechnician() {
        return assignedTechnician;
    }

    public void setAssignedTechnician(String assignedTechnician) {
        this.assignedTechnician = assignedTechnician;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
}
