package main.java.com.hospital.model;

import java.sql.Date;
import java.sql.Timestamp;
public class Maintenance{
    private int id;
    private int equipmentId;   // Référence vers l'équipement
    private int technicianId;  // Référence vers l'employé (Technicien)
    private Date scheduledDate;
    private Date completedDate;
    private String type;       // 'Préventive', 'Corrective'
    private String priority;   // 'Basse', 'Haute', 'Urgente'
    private String status;     // 'Planifiée', 'Terminée'
    private String description;
    private String notes;
    private double cost;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    public Maintenance(){}
    public Maintenance(int id, int equipmentId, int technicianId, Date scheduledDate, Date completedDate, String type,
            String priority, String status, String description, String notes, double cost, Timestamp createdAt,
            Timestamp updatedAt) {
        this.id = id;
        this.equipmentId = equipmentId;
        this.technicianId = technicianId;
        this.scheduledDate = scheduledDate;
        this.completedDate = completedDate;
        this.type = type;
        this.priority = priority;
        this.status = status;
        this.description = description;
        this.notes = notes;
        this.cost = cost;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getEquipmentId() {
        return equipmentId;
    }
    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
    }
    public int getTechnicianId() {
        return technicianId;
    }
    public void setTechnicianId(int technicianId) {
        this.technicianId = technicianId;
    }
    public Date getScheduledDate() {
        return scheduledDate;
    }
    public void setScheduledDate(Date scheduledDate) {
        this.scheduledDate = scheduledDate;
    }
    public Date getCompletedDate() {
        return completedDate;
    }
    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getPriority() {
        return priority;
    }
    public void setPriority(String priority) {
        this.priority = priority;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public double getCost() {
        return cost;
    }
    public void setCost(double cost) {
        this.cost = cost;
    }
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    };
    
}