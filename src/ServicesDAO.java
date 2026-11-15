import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Data Access Object for Services: all database operations (CRUD) live here
public class ServicesDAO {

    // CREATE: insert a new service. Returns true if a row was added.
    public boolean addService(Services s) throws SQLException {
        String sql = "INSERT INTO services (service_name, customer_name, customer_email, "
                   + "customer_phone, device_name, device_brand, issue_description, "
                   + "date_received, status, cost) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, s.getServiceName());
            ps.setString(2, s.getCustomerName());
            ps.setString(3, s.getCustomerEmail());
            ps.setString(4, s.getCustomerPhone());
            ps.setString(5, s.getDeviceName());
            ps.setString(6, s.getDeviceBrand());
            ps.setString(7, s.getIssueDescription());
            ps.setDate(8, Date.valueOf(s.getDateReceived())); // String "yyyy-MM-dd" -> DATE
            ps.setString(9, s.getStatus());
            ps.setDouble(10, s.getCost());

            return ps.executeUpdate() > 0;
        }
    }

    // READ: get every service, newest first
    public List<Services> getAllServices() throws SQLException {
        List<Services> list = new ArrayList<>();
        String sql = "SELECT * FROM services ORDER BY service_id DESC";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Services(
                    rs.getInt("service_id"),
                    rs.getString("service_name"),
                    rs.getString("customer_name"),
                    rs.getString("customer_email"),
                    rs.getString("customer_phone"),
                    rs.getString("device_name"),
                    rs.getString("device_brand"),
                    rs.getString("issue_description"),
                    rs.getDate("date_received").toString(), // DATE -> "yyyy-MM-dd"
                    rs.getString("status"),
                    rs.getDouble("cost")
                ));
            }
        }
        return list;
    }

    // UPDATE: change an existing service (found by its ID). Returns true if updated.
    public boolean updateService(Services s) throws SQLException {
        String sql = "UPDATE services SET service_name = ?, customer_name = ?, "
                   + "customer_email = ?, customer_phone = ?, device_name = ?, "
                   + "device_brand = ?, issue_description = ?, date_received = ?, "
                   + "status = ?, cost = ? WHERE service_id = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, s.getServiceName());
            ps.setString(2, s.getCustomerName());
            ps.setString(3, s.getCustomerEmail());
            ps.setString(4, s.getCustomerPhone());
            ps.setString(5, s.getDeviceName());
            ps.setString(6, s.getDeviceBrand());
            ps.setString(7, s.getIssueDescription());
            ps.setDate(8, Date.valueOf(s.getDateReceived()));
            ps.setString(9, s.getStatus());
            ps.setDouble(10, s.getCost());
            ps.setInt(11, s.getServiceId());

            return ps.executeUpdate() > 0;
        }
    }

    // DELETE: remove a service by ID. Returns true if a row was deleted.
    public boolean deleteService(int serviceId) throws SQLException {
        String sql = "DELETE FROM services WHERE service_id = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, serviceId);
            return ps.executeUpdate() > 0;
        }
    }

    
}