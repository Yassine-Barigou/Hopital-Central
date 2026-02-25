package main.java.com.hospital.model;

import java.sql.Timestamp;

public class Triage {
    private int id;
    private int patientId;
    private int nurseId;
    private String priority;
    private String symptoms;
    private String status;
    private Timestamp createdAt;

    public Triage() {}

    public Triage(int id, int patientId, int nurseId, String priority, String symptoms, String status) {
        this.id = id;
        this.patientId = patientId;
        this.nurseId = nurseId;
        this.priority = priority;
        this.symptoms = symptoms;
        this.status = status;
    }

    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public int getNurseId() { return nurseId; }
    public String getPriority() { return priority; }
    public String getSymptoms() { return symptoms; }
    public String getStatus() { return status; }
    public Timestamp getCreatedAt() { return createdAt; }

    public void setId(int id) { this.id = id; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    public void setNurseId(int nurseId) { this.nurseId = nurseId; }
    public void setPriority(String priority) { this.priority = priority; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}