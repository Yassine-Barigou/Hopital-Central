package main.java.com.hospital.model;

import java.sql.Timestamp; 
import java.text.SimpleDateFormat;

public class Consultation {
    private int id;
    private int patientId;
    private int doctorId;
    private Timestamp date; 
    private String reason;
    private String diagnosis;
    private String prescription;
    private String notes;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Consultation() {}

    public Consultation(int id, int patientId, int doctorId, Timestamp date, String reason, 
                        String diagnosis, String prescription, String notes, String status, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.reason = reason;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
        this.notes = notes;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public int getDoctorId() { return doctorId; }
    public Timestamp getDate() { return date; }
    public String getReason() { return reason; }
    public String getDiagnosis() { return diagnosis; }
    public String getPrescription() { return prescription; }
    public String getNotes() { return notes; }
    public String getStatus() { return status; }
    public Timestamp getCreatedAt() { return createdAt; }
    public Timestamp getUpdatedAt() { return updatedAt; }

    public void setId(int id) { this.id = id; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }
    public void setDate(Timestamp date) { this.date = date; } // ✅ T9addat hna
    public void setReason(String reason) { this.reason = reason; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }
    public void setPrescription(String prescription) { this.prescription = prescription; }
    public void setNotes(String notes) { this.notes = notes; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public String getFormattedDate() {
        if (date == null) return "";
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(date);
    }

    public boolean hasPrescription() {
        return prescription != null && !prescription.trim().isEmpty();
    }

    public String getSummary() {
        if (diagnosis == null) return "Aucun diag";
        return "Diag: " + (diagnosis.length() > 20 ? diagnosis.substring(0, 20) + "..." : diagnosis);
    }

    public String getPrintableFormat() {
        return "ORDONNANCE MÉDICALE\n" +
               "Date : " + getFormattedDate() + "\n" +
               "Médicaments :\n" + (prescription != null ? prescription : "Aucun");
    }

    @Override
    public String toString() {
        return "Consultation #" + id + " [Patient=" + patientId + " | " + getFormattedDate() + "]";
    }
}