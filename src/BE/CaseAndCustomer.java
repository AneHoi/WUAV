package BE;

import java.time.LocalDate;

public class CaseAndCustomer {
    private String customerName;
    private String customerAddress;
    private String caseName;
    private String technicianName;
    private LocalDate createdDate;

    public CaseAndCustomer(Case caseObj, Customer customerObj) {
        this.customerName = customerObj.getCustomerName();
        this.customerAddress = customerObj.getAddress();
        this.caseName = caseObj.getCaseName();
        this.technicianName = caseObj.getAssignedTechnician();
        this.createdDate = caseObj.getCreatedDate();
    }

    // Getters for all properties
    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public String getCaseName() {
        return caseName;
    }

    public String getTechnicianName() {
        return technicianName;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }
}