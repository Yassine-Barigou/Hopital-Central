package main.java.com.hospital.model;

import java.sql.Date;
import java.sql.Timestamp;
public class Equipment{
    private int id;
    private String name;
    private String type;
    private String serial_number;
    private String location;
    private String status;
    private Date purchase_date;
    private Date last_maintenance_date ;
    private Date next_maintenance_date ;
    private String notes;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Equipment(){}

    public Equipment(int id, String name, String type, String serial_number, String location, String status,
            Date purchase_date, Date last_maintenance_date, Date next_maintenance_date, String notes,
            Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.serial_number = serial_number;
        this.location = location;
        this.status = status;
        this.purchase_date = purchase_date;
        this.last_maintenance_date = last_maintenance_date;
        this.next_maintenance_date = next_maintenance_date;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getPurchase_date() {
        return purchase_date;
    }

    public void setPurchase_date(Date purchase_date) {
        this.purchase_date = purchase_date;
    }

    public Date getLast_maintenance_date() {
        return last_maintenance_date;
    }

    public void setLast_maintenance_date(Date last_maintenance_date) {
        this.last_maintenance_date = last_maintenance_date;
    }

    public Date getNext_maintenance_date() {
        return next_maintenance_date;
    }

    public void setNext_maintenance_date(Date next_maintenance_date) {
        this.next_maintenance_date = next_maintenance_date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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