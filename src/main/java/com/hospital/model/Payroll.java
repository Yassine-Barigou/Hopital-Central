package main.java.com.hospital.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Payroll {
    private int id;
    private int employeeId;
    private String month;
    private int year;
    private double baseSalary;
    private double bonus;
    private double deductions;
    private double netSalary;
    private Date paymentDate;
    private Timestamp createdAt;

    public Payroll() {}

    public Payroll(int id, int employeeId, String month, int year, double baseSalary, double bonus, double deductions, double netSalary, Date paymentDate) {
        this.id = id;
        this.employeeId = employeeId;
        this.month = month;
        this.year = year;
        this.baseSalary = baseSalary;
        this.bonus = bonus;
        this.deductions = deductions;
        this.netSalary = netSalary;
        this.paymentDate = paymentDate;
    }

    public int getId() { return id; }
    public int getEmployeeId() { return employeeId; }
    public String getMonth() { return month; }
    public int getYear() { return year; }
    public double getBaseSalary() { return baseSalary; }
    public double getBonus() { return bonus; }
    public double getDeductions() { return deductions; }
    public double getNetSalary() { return netSalary; }
    public Date getPaymentDate() { return paymentDate; }
    public Timestamp getCreatedAt() { return createdAt; }

    public void setId(int id) { this.id = id; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
    public void setMonth(String month) { this.month = month; }
    public void setYear(int year) { this.year = year; }
    public void setBaseSalary(double baseSalary) { this.baseSalary = baseSalary; }
    public void setBonus(double bonus) { this.bonus = bonus; }
    public void setDeductions(double deductions) { this.deductions = deductions; }
    public void setNetSalary(double netSalary) { this.netSalary = netSalary; }
    public void setPaymentDate(Date paymentDate) { this.paymentDate = paymentDate; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public void calculateNetSalary() {
        this.netSalary = this.baseSalary + this.bonus - this.deductions;
    }
}