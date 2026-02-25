package main.java.com.hospital.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Leave {
    private int id;
    private int employeeId;
    private Date startDate;
    private Date endDate;
    private String type; // Ex: Maladie, Annuel, Maternité, Sans solde
    private String status; // Ex: En attente, Approuvée, Refusée
    private String reason;
    private Timestamp createdAt;

    public Leave() {}

    public Leave(int id, int employeeId, Date startDate, Date endDate, String type, String status, String reason) {
        this.id = id;
        this.employeeId = employeeId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.status = status;
        this.reason = reason;
    }

    public int getId() { return id; }
    public int getEmployeeId() { return employeeId; }
    public Date getStartDate() { return startDate; }
    public Date getEndDate() { return endDate; }
    public String getType() { return type; }
    public String getStatus() { return status; }
    public String getReason() { return reason; }
    public Timestamp getCreatedAt() { return createdAt; }

    public void setId(int id) { this.id = id; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public void setType(String type) { this.type = type; }
    public void setStatus(String status) { this.status = status; }
    public void setReason(String reason) { this.reason = reason; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}