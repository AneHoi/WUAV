package BE;

import java.time.LocalDate;

public class ReportCaseAndCustomer {
    private int reportId;
    private String reportName;
    private String reportStatus;
    private String customerName;
    private String customerAddress;
    private String caseName;
    private String technicianName;
    private LocalDate createdDate;
    private int caseId;
    private int customerId;

    public ReportCaseAndCustomer(Report reportObj, Case caseObj, Customer customerObj) {
        this.reportId = reportObj.getReportID();
        this.reportName = reportObj.getReportName();
        this.reportStatus = reportObj.getIsActive();
        this.createdDate = reportObj.getCreatedDate();
        this.customerName = customerObj.getCustomerName();
        this.customerAddress = customerObj.getAddress();
        this.caseName = caseObj.getCaseName();
        this.technicianName = caseObj.getAssignedTechnician();
        this.caseId = caseObj.getCaseID();
        this.customerId = customerObj.getCustomerID();
    }

    // Getters for all properties
    public int getReportId(){return reportId;}
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
    public int getCaseId(){return caseId;}
    public int getCustomerId(){return customerId;}

    public String getReportStatus() {
        return reportStatus;
    }
}

