// Service class: the OOP foundation of the project (model for one row of the services table)
public class Services {

    // Private attributes (match the columns in the services table)
    private int serviceId;
    private String serviceName;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private String deviceName;
    private String deviceBrand;
    private String issueDescription;
    private String dateReceived; // format: yyyy-MM-dd, e.g. 2026-11-15
    private String status;
    private double cost;

    // Empty constructor
    public Services() {
    }

    // Constructor for NEW records (no ID yet, the database generates it)
    public Services(String serviceName, String customerName, String customerEmail,
                    String customerPhone, String deviceName, String deviceBrand,
                    String issueDescription, String dateReceived, String status,
                    double cost) {
        this.serviceName = serviceName;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.deviceName = deviceName;
        this.deviceBrand = deviceBrand;
        this.issueDescription = issueDescription;
        this.dateReceived = dateReceived;
        this.status = status;
        this.cost = cost;
    }

    // Constructor for records READ from the database (includes the ID)
    public Services(int serviceId, String serviceName, String customerName,
                    String customerEmail, String customerPhone, String deviceName,
                    String deviceBrand, String issueDescription, String dateReceived,
                    String status, double cost) {
        this(serviceName, customerName, customerEmail, customerPhone, deviceName,
             deviceBrand, issueDescription, dateReceived, status, cost);
        this.serviceId = serviceId;
    }

    // Getters
    public int getServiceId() { return serviceId; }
    public String getServiceName() { return serviceName; }
    public String getCustomerName() { return customerName; }
    public String getCustomerEmail() { return customerEmail; }
    public String getCustomerPhone() { return customerPhone; }
    public String getDeviceName() { return deviceName; }
    public String getDeviceBrand() { return deviceBrand; }
    public String getIssueDescription() { return issueDescription; }
    public String getDateReceived() { return dateReceived; }
    public String getStatus() { return status; }
    public double getCost() { return cost; }

    // Setters
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }
    public void setDeviceBrand(String deviceBrand) { this.deviceBrand = deviceBrand; }
    public void setIssueDescription(String issueDescription) { this.issueDescription = issueDescription; }
    public void setDateReceived(String dateReceived) { this.dateReceived = dateReceived; }
    public void setStatus(String status) { this.status = status; }
    public void setCost(double cost) { this.cost = cost; }

    // Handy for debugging
    @Override
    public String toString() {
        return "Services [id=" + serviceId + ", service=" + serviceName
             + ", customer=" + customerName + ", device=" + deviceName
             + ", status=" + status + ", cost=" + cost + "]";
    }
}