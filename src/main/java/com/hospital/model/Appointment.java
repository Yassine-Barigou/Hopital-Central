package main.java.com.hospital.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Appointment {
    private int id;
    private int patientId;
    private int doctorId;
    private Timestamp date;
    private int durationMinutes;
    private String type;
    private String status;
    private String notes;

    public Appointment() {}

    public Appointment(int id, int patientId, int doctorId, Timestamp date, int durationMinutes, String type, String status, String notes) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.durationMinutes = durationMinutes;
        this.type = type;
        this.status = status;
        this.notes = notes;
    }

    // Getters
    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public int getDoctorId() { return doctorId; }
    public Timestamp getDate() { return date; }
    public int getDurationMinutes() { return durationMinutes; }
    public String getType() { return type; }
    public String getStatus() { return status; }
    public String getNotes() { return notes; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
    public void setDate(Timestamp date) { this.date = date; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }
    public void setType(String type) { this.type = type; }
    public void setStatus(String status) { this.status = status; }
    public void setNotes(String notes) { this.notes = notes; }

    // Utilitaires
    public String getFormattedDate() {
        if (date == null) return "";
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(date);
    }
}