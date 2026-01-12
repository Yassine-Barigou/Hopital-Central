-- =============================================
-- DROP & CREATE DATABASE
-- =============================================
DROP DATABASE IF EXISTS hopital;
CREATE DATABASE hospital_management;
use hospital_management ;-- connect to the new database

-- =============================================
-- HOSPITAL MANAGEMENT DATABASE SCHEMA
-- =============================================

-- Table: employees (utilisateurs du système)
CREATE TABLE employees (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    role VARCHAR(50) NOT NULL CHECK (role IN ('RH', 'Médecin', 'Infirmier', 'Technicien')),
    department VARCHAR(100),
    phone VARCHAR(20),
    hire_date DATE DEFAULT (CURRENT_DATE),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: patients
CREATE TABLE patients (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    date_of_birth DATE NOT NULL,
    gender VARCHAR(20) CHECK (gender IN ('Homme', 'Femme', 'Autre')),
    phone VARCHAR(20),
    email VARCHAR(255),
    address TEXT,
    blood_type VARCHAR(5),
    allergies TEXT,
    medical_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: consultations
CREATE TABLE consultations (
    id SERIAL PRIMARY KEY,
    patient_id INTEGER NOT NULL REFERENCES patients(id) ON DELETE CASCADE,
    doctor_id INTEGER REFERENCES employees(id) ON DELETE SET NULL,
    date TIMESTAMP NOT NULL,
    reason VARCHAR(255) NOT NULL,
    diagnosis TEXT,
    prescription TEXT,
    notes TEXT,
    status VARCHAR(50) DEFAULT 'Planifiée' CHECK (status IN ('Planifiée', 'En cours', 'Terminée', 'Annulée')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: appointments (rendez-vous)
CREATE TABLE appointments (
    id SERIAL PRIMARY KEY,
    patient_id INTEGER NOT NULL REFERENCES patients(id) ON DELETE CASCADE,
    doctor_id INTEGER REFERENCES employees(id) ON DELETE SET NULL,
    date TIMESTAMP NOT NULL,
    duration_minutes INTEGER DEFAULT 30,
    type VARCHAR(100) NOT NULL,
    status VARCHAR(50) DEFAULT 'En attente' CHECK (status IN ('En attente', 'Confirmé', 'Annulé', 'Terminé')),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: equipment (équipements médicaux)
CREATE TABLE equipment (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(100) NOT NULL,
    serial_number VARCHAR(100) UNIQUE,
    location VARCHAR(255),
    status VARCHAR(50) DEFAULT 'Fonctionnel' CHECK (status IN ('Fonctionnel', 'En maintenance', 'Hors service')),
    purchase_date DATE,
    last_maintenance_date DATE,
    next_maintenance_date DATE,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: maintenance (historique de maintenance)
CREATE TABLE maintenance (
    id SERIAL PRIMARY KEY,
    equipment_id INTEGER NOT NULL REFERENCES equipment(id) ON DELETE CASCADE,
    technician_id INTEGER REFERENCES employees(id) ON DELETE SET NULL,
    scheduled_date DATE NOT NULL,
    completed_date DATE,
    type VARCHAR(100) NOT NULL,
    priority VARCHAR(50) DEFAULT 'Normale' CHECK (priority IN ('Basse', 'Normale', 'Haute', 'Urgente')),
    status VARCHAR(50) DEFAULT 'Planifiée' CHECK (status IN ('Planifiée', 'En cours', 'Terminée', 'Reportée')),
    description TEXT,
    notes TEXT,
    cost DECIMAL(10, 2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =============================================
-- INDEXES pour améliorer les performances
-- =============================================

CREATE INDEX idx_employees_role ON employees(role);
CREATE INDEX idx_employees_email ON employees(email);
CREATE INDEX idx_patients_name ON patients(last_name, first_name);
CREATE INDEX idx_consultations_patient ON consultations(patient_id);
CREATE INDEX idx_consultations_doctor ON consultations(doctor_id);
CREATE INDEX idx_consultations_date ON consultations(date);
CREATE INDEX idx_appointments_patient ON appointments(patient_id);
CREATE INDEX idx_appointments_doctor ON appointments(doctor_id);
CREATE INDEX idx_appointments_date ON appointments(date);
CREATE INDEX idx_equipment_status ON equipment(status);
CREATE INDEX idx_maintenance_equipment ON maintenance(equipment_id);
CREATE INDEX idx_maintenance_technician ON maintenance(technician_id);
CREATE INDEX idx_maintenance_status ON maintenance(status);