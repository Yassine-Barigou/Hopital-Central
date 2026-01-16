package main.java.com.hospital.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Patient{
    private int id;
    private String firstName;
    private String lastName;
    private Date dateBirth;
    private String gende;
    private int phone;
    private String email;
    private String adress ;
    private String bloodType;
    private String allergies;
    private String medicalNotes;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Patient(){}

    public Patient(int id, String firstName, String lastName, Date dateBirth, String gende, int phone, String email,
            String adress, String bloodType, String allergies, String medicalNotes, Timestamp createdAt,
            Timestamp updatedAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateBirth = dateBirth;
        this.gende = gende;
        this.phone = phone;
        this.email = email;
        this.adress = adress;
        this.bloodType = bloodType;
        this.allergies = allergies;
        this.medicalNotes = medicalNotes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(Date dateBirth) {
        this.dateBirth = dateBirth;
    }

    public String getGende() {
        return gende;
    }

    public void setGende(String gende) {
        this.gende = gende;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getMedicalNotes() {
        return medicalNotes;
    }

    public void setMedicalNotes(String medicalNotes) {
        this.medicalNotes = medicalNotes;
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
    public String toString(){
        return firstName+" "+lastName;
    }





}