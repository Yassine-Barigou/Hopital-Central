package main.java.com.hospital.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Employees {
    private int id;
    private String email;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private String role;
    private String department;
    private String phone;
    private Date hireDate;  // java.sql.Date
    
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // constructeurs
    public Employees() {}

    public Employees(int id, String email, String passwordHash, String firstName, String lastName,
                     String role, String department, String phone, Date hireDate, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.department = department;
        this.phone = phone;
        this.hireDate = hireDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // setters
    public void setId(int id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setRole(String role) { this.role = role; }
    public void setDepartment(String department) { this.department = department; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setHireDate(Date hireDate) { this.hireDate = hireDate; }  // ✅ maintenant cohérent
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    // getters
    public int getId() { return id; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getRole() { return role; }
    public String getDepartment() { return department; }
    public String getPhone() { return phone; }
    public  Date getHireDate() { return hireDate; }
    public Timestamp getCreatedAt() { return createdAt; }
    public Timestamp getUpdatedAt() { return updatedAt; }

    // méthodes
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean isDoctor() { return "Médecin".equalsIgnoreCase(role); }
    public boolean isRH() { return "RH".equalsIgnoreCase(role); }
    public boolean isNurse() { return "Infirmier".equalsIgnoreCase(role); }
    public boolean isTechnician() { return "Technicien".equalsIgnoreCase(role); }

    @Override
    public String toString() {
        return getFullName() + " [" + role + "]";
    }

    public void setHireDate(Object hireDate2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setHireDate'");
    }
}
