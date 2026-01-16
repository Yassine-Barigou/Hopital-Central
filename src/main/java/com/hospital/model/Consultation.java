package main.java.com.hospital.model;

import java.security.Timestamp;
import java.text.SimpleDateFormat;

public class Consultation{
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
                        String diagnosis, String prescription, String notes, String status,Timestamp createdAt,Timestamp updatedAt) {
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
    //getters

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
    }

    public int getId() {
        return id;
    }

    public int getPatientId() {
        return patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public Timestamp getDate() {
        return date;
    }

    public String getReason() {
        return reason;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getPrescription() {
        return prescription;
    }

    public String getNotes() {
        return notes;
    }

    public String getStatus() {
        return status;
    }
    //setters

    public void setId(int id) {
        this.id = id;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    //methode
    public String getFormattedDate() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(date);
    }
    public boolean hasPrescription() {
        return prescription != null && !prescription.trim().isEmpty();
    }
    public String getSummary() {
        return "Diag: " + (diagnosis.length() > 20 ? diagnosis.substring(0, 20) + "..." : diagnosis);
    }
    // 2. Formatting for the printer (What the patient sees) had l3yba i m not sure wach nzidoha ola la
public String getPrintableFormat() {
    return "ORDONNANCE MEDICALE\n" +
           "Date : " + getFormattedDate() + "\n" +
           "Médicaments :\n" + prescription;
}
    public String toString() {
        return "Consultation #" + id + " [Patient=" + patientId + " | " + getFormattedDate() + "]";
    }
}