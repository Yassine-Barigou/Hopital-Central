package main.java.com.hospital.model;

import java.sql.Timestamp;

public class VitalSign {
    private int id;
    private int patientId;
    private int nurseId;
    private double temperature;
    private String bloodPressure;
    private int heartRate;
    private double weight;
    private String notes;
    private Timestamp createdAt;

    public VitalSign() {}

    public VitalSign(int id, int patientId, int nurseId, double temperature, String bloodPressure, int heartRate, double weight, String notes) {
        this.id = id;
        this.patientId = patientId;
        this.nurseId = nurseId;
        this.temperature = temperature;
        this.bloodPressure = bloodPressure;
        this.heartRate = heartRate;
        this.weight = weight;
        this.notes = notes;
    }

    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public int getNurseId() { return nurseId; }
    public double getTemperature() { return temperature; }
    public String getBloodPressure() { return bloodPressure; }
    public int getHeartRate() { return heartRate; }
    public double getWeight() { return weight; }
    public String getNotes() { return notes; }
    public Timestamp getCreatedAt() { return createdAt; }

    public void setId(int id) { this.id = id; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    public void setNurseId(int nurseId) { this.nurseId = nurseId; }
    public void setTemperature(double temperature) { this.temperature = temperature; }
    public void setBloodPressure(String bloodPressure) { this.bloodPressure = bloodPressure; }
    public void setHeartRate(int heartRate) { this.heartRate = heartRate; }
    public void setWeight(double weight) { this.weight = weight; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}