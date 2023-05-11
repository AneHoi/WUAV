package BE;

import java.time.LocalDate;

public class ReportCaseAndCustomer {
    private String reportName;
    private String customerName;
    private String customerAddress;
    private String caseName;
    private String technicianName;
    private LocalDate createdDate;

    public ReportCaseAndCustomer(Report reportObj, Case caseObj, Customer customerObj) {
        this.reportName = reportObj.getReportName();
        this.createdDate = reportObj.getCreatedDate();
        this.customerName = customerObj.getCustomerName();
        this.customerAddress = customerObj.getAddress();
        this.caseName = caseObj.getCaseName();
        this.technicianName = caseObj.getAssignedTechnician();
    }

    // Getters for all properties
    public String getReportName(){return reportName;}

    public LocalDate getCreatedDate() {
        return createdDate;
    }

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

}

