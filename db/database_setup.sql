-- 1. Création de la base de données
CREATE DATABASE gestion_hopital;

-- 2. Sélection de la base pour travailler dessus
USE gestion_hopital;

-- ============================================================
-- TABLE 1 : SERVICES (Ex: Cardiologie, IT, RH...)
-- ============================================================
CREATE TABLE service (
    id_service INT AUTO_INCREMENT PRIMARY KEY,
    nom_service VARCHAR(50) NOT NULL,
    etage INT NOT NULL
);

-- ============================================================
-- TABLE 2 : EMPLOYES (Table unique pour RH, Médecin, Infirmier...)
-- ============================================================
CREATE TABLE employe (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL, 
    password VARCHAR(255) NOT NULL,
    salaire FLOAT, 
    
    -- Le rôle (Trés important pour le Login en Java)
    role ENUM('RH', 'MEDECIN', 'INFIRMIER', 'TECHNICIEN') NOT NULL,
    
    -- Informations spécifiques (Optionnelles selon le rôle)
    specialite VARCHAR(50),  -- Pour les Médecins
    grade VARCHAR(50),       -- Pour les Infirmiers
    domaine VARCHAR(50),     -- Pour les Techniciens (ex: Réseau, Biomédical)
    
    -- Lien avec le Service
    id_service INT,
    FOREIGN KEY (id_service) REFERENCES service(id_service)
);

-- ============================================================
-- TABLE 3 : PATIENTS (Les malades)
-- ============================================================
CREATE TABLE patient (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    prenom VARCHAR(50) NOT NULL,
    telephone VARCHAR(20),
    sexe ENUM('M', 'F'),
    date_naissance DATE
);

-- ============================================================
-- TABLE 4 : RENDEZ-VOUS (Lien Patient <-> Médecin)
-- ============================================================
CREATE TABLE rendez_vous (
    id INT AUTO_INCREMENT PRIMARY KEY,
    date_heure DATETIME NOT NULL,
    statut ENUM('EN_ATTENTE', 'CONFIRME', 'ANNULE', 'TERMINE') DEFAULT 'EN_ATTENTE',
    
    id_patient INT NOT NULL,
    id_medecin INT NOT NULL,
    
    FOREIGN KEY (id_patient) REFERENCES patient(id),
    FOREIGN KEY (id_medecin) REFERENCES employe(id)
);

-- ============================================================
-- TABLE 5 : CONSULTATIONS (Le dossier médical après RDV)
-- ============================================================
CREATE TABLE consultation (
    id INT AUTO_INCREMENT PRIMARY KEY,
    diagnostic TEXT,
    traitement TEXT,
    
    -- Une consultation appartient à un seul RDV
    id_rendez_vous INT UNIQUE NOT NULL,
    FOREIGN KEY (id_rendez_vous) REFERENCES rendez_vous(id)
);

-- ============================================================
-- TABLE 6 : MACHINES (Pour les Techniciens)
-- ============================================================
CREATE TABLE machine (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(50) NOT NULL,
    etat ENUM('FONCTIONNEL', 'EN_PANNE', 'HORS_SERVICE') DEFAULT 'FONCTIONNEL',
    
    -- Quel technicien s'occupe de cette machine ?
    id_technicien INT,
    FOREIGN KEY (id_technicien) REFERENCES employe(id)
);

-- ============================================================
-- TABLE 7 : CONGÉS (Pour les RH)
-- ============================================================
CREATE TABLE conge (
    id INT AUTO_INCREMENT PRIMARY KEY,
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    statut ENUM('EN_ATTENTE', 'VALIDE', 'REFUSE') DEFAULT 'EN_ATTENTE',
    
    id_employe INT NOT NULL, -- Celui qui demande le congé
    FOREIGN KEY (id_employe) REFERENCES employe(id)
);

-- ============================================================
-- JEU DE DONNÉES DE TEST (Données initiales)
-- ============================================================

-- Ajouter des Services
INSERT INTO service (nom_service, etage) VALUES 
('Administration', 1),
('Cardiologie', 2),
('Informatique', 0);

-- Ajouter des Employés (Mot de passe '1234' pour tous)
INSERT INTO employe (nom, prenom, email, password, role, id_service, specialite, domaine) VALUES 
('Benani', 'Ahmed', 'rh@hopital.com', '1234', 'RH', 1, NULL, NULL),
('Dr. Alami', 'Sara', 'sara@hopital.com', '1234', 'MEDECIN', 2, 'Cardiologue', NULL),
('Tazi', 'Karim', 'tech@hopital.com', '1234', 'TECHNICIEN', 3, NULL, 'Maintenance');

-- Ajouter un Patient
INSERT INTO patient (nom, prenom, telephone, sexe, date_naissance) VALUES 
('Mansouri', 'Yassine', '0661123456', 'M', '1995-05-20');